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
 * Defines the shape of the response from a successful login attempt.
 */
export interface LoginResponse {
  /**
   * The authentication token (JWT).
   */
  token: string;
  /**
   * Information about the authenticated user.
   */
  usuario: {
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
  };
}
