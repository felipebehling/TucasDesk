import { createContext } from "react";

/** Defines the possible tones for a toast notification. */
export type ToastTone = "success" | "error" | "info" | "warning";

/**
 * Defines the options for creating a toast notification.
 */
export interface ToastOptions {
  /** The title of the toast. */
  title?: string;
  /** The main description/message of the toast. */
  description: string;
  /** The tone of the toast, controlling its color and icon. */
  tone?: ToastTone;
  /** The duration in milliseconds for which the toast should be visible. */
  duration?: number;
}

/**
 * Represents a toast notification with all its properties.
 */
export interface Toast extends Required<ToastOptions> {
  /** A unique identifier for the toast. */
  id: number;
}

/**
 * Defines the value provided by the ToastContext.
 */
export interface ToastContextValue {
  /** Function to display a new toast notification. */
  showToast: (options: ToastOptions) => void;
}

/**
 * React context for the toast notification system.
 */
export const ToastContext = createContext<ToastContextValue | undefined>(undefined);
