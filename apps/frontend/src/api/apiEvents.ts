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

/**
 * Emits an API event to all subscribed listeners.
 *
 * @param {ApiEventType} type - The type of the event to emit.
 * @param {ApiEventPayload} payload - The payload to send with the event.
 */
export function emitApiEvent(type: ApiEventType, payload: ApiEventPayload) {
  listeners[type].forEach((listener) => {
    try {
      listener(payload);
    } catch (error) {
      console.error("Unhandled error while processing API event listener", error);
    }
  });
}

/**
 * Subscribes a listener to a specific type of API event.
 *
 * @param {ApiEventType} type - The type of the event to subscribe to.
 * @param {ApiEventListener} listener - The callback function to execute when the event is emitted.
 * @returns {() => void} A function that unsubscribes the listener when called.
 */
export function subscribeToApiEvents(type: ApiEventType, listener: ApiEventListener) {
  listeners[type].add(listener);
  return () => {
    listeners[type].delete(listener);
  };
}
