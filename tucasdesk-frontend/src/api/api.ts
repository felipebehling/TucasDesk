import axios, { type AxiosError, type AxiosResponse } from "axios";
import type { LoginResponse } from "../interfaces/Auth";
import {
  clearStoredAuth,
  getStoredAuth,
  updateStoredTokens,
} from "./tokenStorage";
import type { ApiRequestConfig, InternalApiRequestConfig } from "./types";
import { emitApiEvent } from "./apiEvents";

interface QueueItem {
  resolve: (value: AxiosResponse) => void;
  reject: (error: unknown) => void;
  config: InternalApiRequestConfig;
}

let isRefreshing = false;
let failedQueue: QueueItem[] = [];

function processQueue(error: unknown | null, token: string | null) {
  failedQueue.forEach(({ resolve, reject, config }) => {
    if (error) {
      reject(error);
      return;
    }
    if (token) {
      config.headers = config.headers ?? {};
      config.headers.Authorization = `Bearer ${token}`;
    }
    api(config).then(resolve).catch(reject);
  });
  failedQueue = [];
}

/**
 * An Axios instance configured for making requests to the TucasDesk backend API.
 * The base URL is read from the Vite environment variable to keep it aligned
 * with the configured backend service (defaults to http://localhost:8080).
 */
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL ?? "http://localhost:8080",
  headers: {
    "Content-Type": "application/json",
  },
});

/**
 * Axios request interceptor to automatically add the JWT to the Authorization header.
 * It retrieves the token from persistent storage and adds it as a Bearer token.
 */
api.interceptors.request.use((config) => {
  const requestConfig = config as InternalApiRequestConfig;
  if (requestConfig.skipAuth) {
    emitApiEvent("request:start", { config: requestConfig });
    return config;
  }

  const { token } = getStoredAuth();
  if (token) {
    config.headers = config.headers ?? {};
    config.headers.Authorization = `Bearer ${token}`;
  }
  emitApiEvent("request:start", { config: requestConfig });
  return config;
});

api.interceptors.response.use(
  (response) => {
    emitApiEvent("request:success", {
      config: response.config as InternalApiRequestConfig,
      response,
    });
    return response;
  },
  async (error: AxiosError) => {
    const { response, config } = error;
    const originalRequest = config as InternalApiRequestConfig | undefined;

    if (response?.status === 401 && originalRequest?.skipAuth) {
      emitApiEvent("request:error", {
        config: originalRequest,
        error,
        response,
      });
      clearStoredAuth();
      return Promise.reject(error);
    }

    if (response?.status === 401 && originalRequest && !originalRequest._retry) {
      const { refreshToken, storageType } = getStoredAuth();

      if (refreshToken) {
        if (isRefreshing) {
          return new Promise((resolve, reject) => {
            failedQueue.push({ resolve, reject, config: originalRequest });
          });
        }

        originalRequest._retry = true;
        isRefreshing = true;

        try {
          const refreshConfig: ApiRequestConfig = {
            skipAuth: true,
            skipErrorToast: true,
          };

          const refreshResponse = await api.post<LoginResponse>(
            "/auth/refresh",
            { refreshToken },
            refreshConfig,
          );

          const { token: newToken, refreshToken: newRefreshToken } = refreshResponse.data;
          updateStoredTokens(newToken, newRefreshToken ?? null, storageType);
          processQueue(null, newToken);
          return api(originalRequest);
        } catch (refreshError) {
          processQueue(refreshError, null);
          clearStoredAuth();
          emitApiEvent("request:error", {
            config: originalRequest,
            error: refreshError,
          });
          return Promise.reject(refreshError);
        } finally {
          isRefreshing = false;
        }
      }

      clearStoredAuth();
    }

    if (originalRequest) {
      emitApiEvent("request:error", {
        config: originalRequest,
        error,
        response,
      });
    }

    return Promise.reject(error);
  },
);

export default api;
export type { ApiRequestConfig, InternalApiRequestConfig } from "./types";
