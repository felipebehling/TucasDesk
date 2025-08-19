import { createContext, useState, useEffect, type ReactNode } from "react";
import api from "../api/api";
import type { LoginRequest, LoginResponse } from "../interfaces/Auth";

interface AuthContextType {
  usuario: LoginResponse["usuario"] | null;
  login: (data: LoginRequest) => Promise<void>;
  logout: () => void;
  autenticado: boolean;
}

export const AuthContext = createContext<AuthContextType>({} as AuthContextType);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [usuario, setUsuario] = useState<LoginResponse["usuario"] | null>(null);

  useEffect(() => {
    const token = localStorage.getItem("token");
    if (token) {
      api.get("/auth/me")
        .then((res) => setUsuario(res.data))
        .catch(() => logout());
    }
  }, []);

  const login = async (data: LoginRequest) => {
    const response = await api.post<LoginResponse>("/auth/login", data);
    localStorage.setItem("token", response.data.token);
    setUsuario(response.data.usuario);
  };

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
