import "./loading.css";

interface LoadingOverlayProps {
  /** Optional text to display below the spinner. */
  message?: string;
  /** Render the overlay taking the entire viewport. */
  fullscreen?: boolean;
  /** Render the loading indicator inline without the dark background. */
  inline?: boolean;
}

/**
 * A reusable loading indicator that can be displayed inline or as an overlay.
 */
export function LoadingOverlay({ message, fullscreen = false, inline = false }: LoadingOverlayProps) {
  const classes = ["loading-overlay"];
  if (fullscreen) {
    classes.push("loading-overlay--fullscreen");
  }
  if (inline) {
    classes.push("loading-overlay--inline");
  }

  return (
    <div className={classes.join(" ")}>
      <div className="loading-spinner" aria-hidden="true" />
      {message ? <p className="loading-message">{message}</p> : null}
    </div>
  );
}

/**
 * Convenience component to render a centered spinner without the overlay backdrop.
 */
export function LoadingSpinner({ message }: { message?: string }) {
  return <LoadingOverlay message={message} inline />;
}

export default LoadingOverlay;
