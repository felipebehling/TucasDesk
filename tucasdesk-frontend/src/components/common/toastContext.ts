import { createContext, useContext } from "react";

export type ToastTone = "success" | "error" | "info" | "warning";

export interface ToastOptions {
  title?: string;
  description: string;
  tone?: ToastTone;
  duration?: number;
}

export interface Toast extends Required<ToastOptions> {
  id: number;
}

export interface ToastContextValue {
  showToast: (options: ToastOptions) => void;
}

export const ToastContext = createContext<ToastContextValue | undefined>(undefined);

export function useToast() {
  const context = useContext(ToastContext);
  if (!context) {
    throw new Error("useToast must be used within a ToastProvider");
  }
  return context;
}
