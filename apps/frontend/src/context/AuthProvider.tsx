import { useCallback, useEffect, useState, type ReactNode } from "react";
import api from "../api/api";
import type { AuthenticatedUser, LoginRequest, LoginResponse } from "../interfaces/Auth";
import {
  clearStoredAuth,
  getStoredAuth,
  persistAuthState,
  persistUsuario,
  subscribeToAuthChanges,
  type StoredAuth,
} from "../api/tokenStorage";
import { AuthContext, type LoginOptions } from "./AuthContext";

function selectUsuarioFromStorage(stored: StoredAuth) {
  return stored.usuario ?? null;
}

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
  const [usuario, setUsuario] = useState<AuthenticatedUser | null>(null);
  const [carregando, setCarregando] = useState(true);

  const restoreSession = useCallback(async () => {
    setCarregando(true);
    try {
      const { data } = await api.get<AuthenticatedUser>("/api/usuarios/me");
      setUsuario(data);
    } catch (error) {
      console.error("Failed to restore session", error);
      setUsuario(null);
    } finally {
      setCarregando(false);
    }
  }, []);

  useEffect(() => {
    restoreSession();
  }, [restoreSession]);

  /**
   * Logs the user out by removing the token from local storage and clearing the user state.
   */
  const logout = useCallback(() => {
    window.location.href = "http://localhost:8080/logout";
  }, []);

  const login = useCallback(
    async (data: LoginRequest, options?: LoginOptions) => {
      // no-op
    },
    [],
  );

  const refreshSession = useCallback(async () => {
    // no-op
  }, []);

  return (
    <AuthContext.Provider
      value={{
        usuario,
        login,
        logout,
        autenticado: !!usuario,
        carregando,
        refreshSession,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}
