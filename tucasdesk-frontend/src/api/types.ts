import type { AxiosRequestConfig, InternalAxiosRequestConfig } from "axios";

interface ApiRequestFlags {
  skipAuth?: boolean;
  skipSuccessToast?: boolean;
  skipErrorToast?: boolean;
  successMessage?: string;
  successTitle?: string;
  errorMessage?: string;
  errorTitle?: string;
  eventName?: string;
  _retry?: boolean;
}

export type ApiRequestConfig<D = unknown> = AxiosRequestConfig<D> & ApiRequestFlags;

export type InternalApiRequestConfig<D = unknown> = InternalAxiosRequestConfig<D> & ApiRequestFlags;
