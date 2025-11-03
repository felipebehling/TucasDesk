import { createContext, useCallback, useEffect, useState, type ReactNode } from "react";
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

interface LoginOptions {
  remember?: boolean;
}

/**
 * Defines the shape of the authentication context, including user state and actions.
 */
interface AuthContextType {
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
    const stored = getStoredAuth();

    if (!stored.token) {
      setUsuario(null);
      setCarregando(false);
      return;
    }

    const storedUsuario = selectUsuarioFromStorage(stored);
    if (storedUsuario) {
      setUsuario(storedUsuario);
    }

    try {
      const { data } = await api.get<AuthenticatedUser>("/auth/me");
      setUsuario(data);
      persistUsuario(data, stored.storageType);
    } catch (error) {
      console.error("Failed to restore session", error);
      clearStoredAuth();
      setUsuario(null);
    } finally {
      setCarregando(false);
    }
  }, []);

  useEffect(() => {
    restoreSession();
  }, [restoreSession]);

  useEffect(() => {
    const unsubscribe = subscribeToAuthChanges(({ data, type }) => {
      if (type === "clear" || !data.usuario) {
        setUsuario(null);
        return;
      }
      setUsuario(data.usuario);
    });
    return unsubscribe;
  }, []);

  /**
   * Logs the user in by sending credentials to the backend, storing the received
   * token, and updating the user state.
   *
   * @param {LoginRequest} data - The user's login credentials.
   * @param {LoginOptions} options - Additional options such as persistence strategy.
   */
  const login = useCallback(
    async (data: LoginRequest, options?: LoginOptions) => {
      const remember = options?.remember ?? false;
      try {
        const response = await api.post<LoginResponse>("/auth/login", data, {
          skipAuth: true,
        });

        const storageType = persistAuthState({
          token: response.data.token,
          refreshToken: response.data.refreshToken ?? null,
          usuario: response.data.usuario,
          remember,
        });
        setUsuario(response.data.usuario);

        try {
          const { data: me } = await api.get<AuthenticatedUser>("/auth/me");
          setUsuario(me);
          persistUsuario(me, storageType);
        } catch (meError) {
          console.warn("Failed to fetch /auth/me after login", meError);
          persistUsuario(response.data.usuario, storageType);
        }
      } catch (error) {
        clearStoredAuth();
        setUsuario(null);
        throw error;
      }
    },
    [],
  );

  /**
   * Logs the user out by removing the token from local storage and clearing the user state.
   */
  const logout = useCallback(() => {
    clearStoredAuth();
    setUsuario(null);
  }, []);

  const refreshSession = useCallback(async () => {
    const stored = getStoredAuth();

    if (!stored.token) {
      setUsuario(null);
      return;
    }

    try {
      const { data } = await api.get<AuthenticatedUser>("/auth/me");
      setUsuario(data);
      persistUsuario(data, stored.storageType);
    } catch (error) {
      console.error("Failed to refresh authenticated user", error);
      clearStoredAuth();
      setUsuario(null);
      throw error;
    }
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
