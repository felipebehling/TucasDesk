import { isAxiosError } from "axios";

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
