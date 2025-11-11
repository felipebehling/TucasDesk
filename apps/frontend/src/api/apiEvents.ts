import type { AxiosError, AxiosResponse } from "axios";
import type { InternalApiRequestConfig } from "./types";

export type ApiEventType = "request:start" | "request:success" | "request:error";

export interface ApiEventPayload {
  config: InternalApiRequestConfig;
  response?: AxiosResponse;
  error?: AxiosError | unknown;
}

type ApiEventListener = (payload: ApiEventPayload) => void;

const listeners: Record<ApiEventType, Set<ApiEventListener>> = {
  "request:start": new Set(),
  "request:success": new Set(),
  "request:error": new Set(),
};

export function emitApiEvent(type: ApiEventType, payload: ApiEventPayload) {
  listeners[type].forEach((listener) => {
    try {
      listener(payload);
    } catch (error) {
      // eslint-disable-next-line no-console
      console.error("Unhandled error while processing API event listener", error);
    }
  });
}

export function subscribeToApiEvents(type: ApiEventType, listener: ApiEventListener) {
  listeners[type].add(listener);
  return () => {
    listeners[type].delete(listener);
  };
}
