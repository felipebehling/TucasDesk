import { useCallback, useContext, useMemo, useRef, useState } from "react";
import type { PropsWithChildren } from "react";
import "./toast.css";

import { ToastContext, type Toast, type ToastOptions } from "./toastContext";

const DEFAULT_DURATION = 4000;

function ToastItem({ toast, onDismiss }: { toast: Toast; onDismiss: (id: number) => void }) {
  const { id, title, description, tone } = toast;
  return (
    <div className={`toast toast--${tone}`} role="status">
      <div className="toast-content">
        {title ? <strong className="toast-title">{title}</strong> : null}
        <p className="toast-description">{description}</p>
      </div>
      <button type="button" className="toast-dismiss" aria-label="Fechar alerta" onClick={() => onDismiss(id)}>
        ×
      </button>
    </div>
  );
}

export function ToastProvider({ children }: PropsWithChildren) {
  const [toasts, setToasts] = useState<Toast[]>([]);
  const idRef = useRef(0);

  const dismiss = useCallback((id: number) => {
    setToasts((prev) => prev.filter((toast) => toast.id !== id));
  }, []);

  const showToast = useCallback(({ description, duration, title, tone }: ToastOptions) => {
    idRef.current += 1;
    const id = idRef.current;
    const toast: Toast = {
      id,
      description,
      duration: duration ?? DEFAULT_DURATION,
      title: title ?? "",
      tone: tone ?? "info",
    };
    setToasts((prev) => [...prev, toast]);
    if (typeof window !== "undefined") {
      window.setTimeout(() => {
        dismiss(id);
      }, toast.duration);
    }
  }, [dismiss]);

  const contextValue = useMemo(() => ({ showToast }), [showToast]);

  return (
    <ToastContext.Provider value={contextValue}>
      {children}
      <div className="toast-container" aria-live="polite" aria-atomic="false">
        {toasts.map((toast) => (
          <ToastItem key={toast.id} toast={toast} onDismiss={dismiss} />
        ))}
      </div>
    </ToastContext.Provider>
  );
}

/* eslint-disable-next-line react-refresh/only-export-components */
export function useToast() {
  const context = useContext(ToastContext);
  if (!context) {
    throw new Error("useToast must be used within a ToastProvider");
  }
  return context;
}
