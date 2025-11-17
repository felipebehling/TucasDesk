import api from "../api/api";

export interface PasswordResetRequestPayload {
  email: string;
}

export interface PasswordResetConfirmPayload {
  token: string;
  novaSenha: string;
  confirmacaoSenha: string;
}

export function requestPasswordReset(payload: PasswordResetRequestPayload) {
  return api.post("/auth/password/reset-request", payload, { skipAuth: true });
}

export function confirmPasswordReset(payload: PasswordResetConfirmPayload) {
  return api.post("/auth/password/reset", payload, { skipAuth: true });
}
