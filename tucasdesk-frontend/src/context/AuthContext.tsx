import { createContext, useState, useEffect, type ReactNode } from "react";
import api from "../api/api";
import type { LoginRequest, LoginResponse } from "../interfaces/Auth";

/**
 * Defines the shape of the authentication context, including user state and actions.
 */
interface AuthContextType {
  /** The currently authenticated user's data, or null if not authenticated. */
  usuario: LoginResponse["usuario"] | null;
  /** Function to perform user login. */
  login: (data: LoginRequest) => Promise<void>;
  /** Function to perform user logout. */
  logout: () => void;
  /** A boolean flag indicating if the user is currently authenticated. */
  autenticado: boolean;
}

/**
 * React context for managing authentication state throughout the application.
 */
export const AuthContext = createContext<AuthContextType>({} as AuthContextType);

/**
 * Provides the authentication context to its children components.
 * It manages the user's authentication state, including login, logout,
 * and session persistence.
 *
 * @param {object} props - The component props.
 * @param {ReactNode} props.children - The child components to be wrapped by the provider.
 * @returns {JSX.Element} The authentication provider component.
 */
export function AuthProvider({ children }: { children: ReactNode }) {
  const [usuario, setUsuario] = useState<LoginResponse["usuario"] | null>(null);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      // TODO: Implement a /auth/me endpoint in the backend to validate the token
      // and retrieve user data.
      api.get("/auth/me")
        .then((res) => setUsuario(res.data))
        .catch(() => logout());
    }
  }, []);

  /**
   * Logs the user in by sending credentials to the backend, storing the received
   * token, and updating the user state.
   *
   * @param {LoginRequest} data - The user's login credentials.
   */
  const login = async (data: LoginRequest) => {
    const response = await api.post<LoginResponse>("/auth/login", data);
    localStorage.setItem("token", response.data.token);
    setUsuario(response.data.usuario);
  };

  /**
   * Logs the user out by removing the token from local storage and clearing the user state.
   */
  const logout = () => {
    localStorage.removeItem("token");
    setUsuario(null);
  };

  return (
    <AuthContext.Provider value={{ usuario, login, logout, autenticado: !!usuario }}>
      {children}
    </AuthContext.Provider>
  );
}
