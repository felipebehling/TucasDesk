import api from "../api/api";

export interface PasswordResetRequestPayload {
  email: string;
}

export interface PasswordResetConfirmPayload {
  token: string;
  novaSenha: string;
  confirmacaoSenha: string;
}

/**
 * Sends a request to the backend to initiate the password reset process.
 *
 * @param {PasswordResetRequestPayload} payload - The payload containing the user's email.
 * @returns {Promise<any>} A promise that resolves when the request is complete.
 */
export function requestPasswordReset(payload: PasswordResetRequestPayload) {
  return api.post("/auth/password/reset-request", payload, { skipAuth: true });
}

/**
 * Sends a request to the backend to confirm and finalize the password reset.
 *
 * @param {PasswordResetConfirmPayload} payload - The payload containing the reset token and new password.
 * @returns {Promise<any>} A promise that resolves when the password has been reset.
 */
export function confirmPasswordReset(payload: PasswordResetConfirmPayload) {
  return api.post("/auth/password/reset", payload, { skipAuth: true });
}
