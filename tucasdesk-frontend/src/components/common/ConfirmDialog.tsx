import { useEffect } from "react";
import "./modal.css";

export interface ConfirmDialogProps {
  isOpen: boolean;
  title: string;
  description: string;
  confirmLabel?: string;
  cancelLabel?: string;
  tone?: "default" | "danger";
  isProcessing?: boolean;
  onConfirm: () => void;
  onCancel: () => void;
}

/**
 * Reusable modal dialog to confirm potentially destructive actions.
 */
export function ConfirmDialog({
  isOpen,
  title,
  description,
  confirmLabel = "Confirmar",
  cancelLabel = "Cancelar",
  tone = "default",
  isProcessing = false,
  onConfirm,
  onCancel,
}: ConfirmDialogProps) {
  useEffect(() => {
    function handleKeyDown(event: KeyboardEvent) {
      if (event.key === "Escape") {
        onCancel();
      }
    }
    if (isOpen) {
      window.addEventListener("keydown", handleKeyDown);
    }
    return () => {
      window.removeEventListener("keydown", handleKeyDown);
    };
  }, [isOpen, onCancel]);

  if (!isOpen) {
    return null;
  }

  return (
    <div className="modal-backdrop" role="dialog" aria-modal="true" aria-labelledby="confirm-dialog-title">
      <div className="modal-card">
        <header className="modal-card__header">
          <h4 id="confirm-dialog-title">{title}</h4>
          <button type="button" className="btn-ghost" onClick={onCancel} aria-label="Fechar">
            ×
          </button>
        </header>
        <div className="modal-card__body">
          <p>{description}</p>
        </div>
        <footer className="modal-card__footer">
          <button type="button" className="btn-ghost" onClick={onCancel} disabled={isProcessing}>
            {cancelLabel}
          </button>
          <button
            type="button"
            className={tone === "danger" ? "btn-danger" : "btn-primary"}
            onClick={onConfirm}
            disabled={isProcessing}
          >
            {isProcessing ? "Processando..." : confirmLabel}
          </button>
        </footer>
      </div>
    </div>
  );
}

export default ConfirmDialog;
