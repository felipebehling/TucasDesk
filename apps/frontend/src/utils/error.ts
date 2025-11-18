import { isAxiosError } from "axios";

/**
 * Extracts a user-friendly error message from an unknown error type.
 *
 * @param {unknown} error - The error object, which can be an Axios error, a standard Error, or any other type.
 * @param {string} [fallbackMessage="Ocorreu um erro inesperado."] - A fallback message to use if no specific message can be extracted.
 * @returns {string} The extracted or fallback error message.
 */
export function extractErrorMessage(error: unknown, fallbackMessage = "Ocorreu um erro inesperado."): string {
  if (isAxiosError(error)) {
    return (
      (typeof error.response?.data === "string" && error.response.data) ||
      (error.response?.data as { message?: string } | undefined)?.message ||
      error.message ||
      fallbackMessage
    );
  }
  if (error instanceof Error) {
    return error.message;
  }
  return fallbackMessage;
}
