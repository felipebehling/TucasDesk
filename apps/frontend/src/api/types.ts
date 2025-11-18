import type { AxiosRequestConfig, InternalAxiosRequestConfig } from "axios";

/**
 * Defines a set of flags that can be added to an Axios request configuration
 * to control custom behaviors like authentication, notifications, and retries.
 */
interface ApiRequestFlags {
  /** If true, the authentication token will not be added to the request. */
  skipAuth?: boolean;
  /** If true, success notifications will be skipped for this request. */
  skipSuccessToast?: boolean;
  /** If true, error notifications will be skipped for this request. */
  skipErrorToast?: boolean;
  /** Custom message for success notifications. */
  successMessage?: string;
  /** Custom title for success notifications. */
  successTitle?: string;
  /** Custom message for error notifications. */
  errorMessage?: string;
  /** Custom title for error notifications. */
  errorTitle?: string;
  /** A name for the event to be emitted. */
  eventName?: string;
  /** Internal flag to prevent infinite retry loops. */
  _retry?: boolean;
}

/**
 * Custom Axios request configuration that includes API-specific flags.
 * @template D - The type of the request data.
 */
export type ApiRequestConfig<D = unknown> = AxiosRequestConfig<D> & ApiRequestFlags;

/**
 * Custom internal Axios request configuration that includes API-specific flags.
 * @template D - The type of the request data.
 */
export type InternalApiRequestConfig<D = unknown> = InternalAxiosRequestConfig<D> & ApiRequestFlags;
