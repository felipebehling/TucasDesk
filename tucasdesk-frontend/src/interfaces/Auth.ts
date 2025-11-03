/**
 * Defines the shape of the request body for a login attempt.
 */
export interface LoginRequest {
  /**
   * The user's email address.
   */
  email: string;
  /**
   * The user's password.
   */
  senha: string;
}

/**
 * Defines the shape of the request body for a registration attempt.
 */
export interface RegisterRequest {
  /**
   * The user's full name.
   */
  nome: string;
  /**
   * The user's email address.
   */
  email: string;
  /**
   * The chosen password.
   */
  senha: string;
  /**
   * The confirmation of the chosen password.
   */
  confirmacaoSenha: string;
}

/**
 * Represents the data describing an authenticated user.
 */
export interface AuthenticatedUser {
  /**
   * The unique identifier of the user.
   */
  id: number;
  /**
   * The name of the user.
   */
  nome: string;
  /**
   * The email address of the user.
   */
  email: string;
  /**
   * The role of the user (e.g., 'Admin', 'User').
   */
  role: string;
}

/**
 * Defines the shape of the response from a successful login attempt.
 */
export interface LoginResponse {
  /**
   * The authentication token (JWT).
   */
  token: string;
  /**
   * Optional refresh token for renewing the session.
   */
  refreshToken?: string | null;
  /**
   * Information about the authenticated user.
   */
  usuario: AuthenticatedUser;
}
