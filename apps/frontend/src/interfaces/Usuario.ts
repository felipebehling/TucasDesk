/**
 * Defines the shape of the request body for updating a user's profile.
 */
export interface UpdateUsuarioRequest {
  /** The user's full name. */
  nome: string;
  /** The user's email address. */
  email: string;
  /** The user's current password, required for password changes. */
  senhaAtual?: string;
  /** The new password. */
  novaSenha?: string;
  /** Confirmation of the new password. */
  confirmacaoSenha?: string;
}
