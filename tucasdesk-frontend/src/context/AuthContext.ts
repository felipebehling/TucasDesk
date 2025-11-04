import { createContext } from "react";
import type { AuthenticatedUser, LoginRequest } from "../interfaces/Auth";

export interface LoginOptions {
  remember?: boolean;
}

/**
 * Defines the shape of the authentication context, including user state and actions.
 */
export interface AuthContextType {
  /** The currently authenticated user's data, or null if not authenticated. */
  usuario: AuthenticatedUser | null;
  /** Function to perform user login. */
  login: (data: LoginRequest, options?: LoginOptions) => Promise<void>;
  /** Function to perform user logout. */
  logout: () => void;
  /** A boolean flag indicating if the user is currently authenticated. */
  autenticado: boolean;
  /** Indicates whether the provider is checking/restoring the session. */
  carregando: boolean;
  /** Explicitly refreshes the authenticated user's data from the backend. */
  refreshSession: () => Promise<void>;
}

/**
 * React context for managing authentication state throughout the application.
 */
export const AuthContext = createContext<AuthContextType>({} as AuthContextType);
