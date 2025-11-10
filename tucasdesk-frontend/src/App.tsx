// src/App.tsx

import { lazy, Suspense, useContext } from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthContext } from "./context/AuthContext";
import { ThemeProvider } from "./context/ThemeProvider";

// 1. Lazy load all page components for code-splitting
const LoginPage = lazy(() => import("./pages/Login"));
const RegisterPage = lazy(() => import("./pages/Registro"));
const DashboardPage = lazy(() => import("./pages/Dashboard"));
const ChamadosPage = lazy(() => import("./pages/Chamados"));
const ChamadoDetalhePage = lazy(() => import("./pages/ChamadoDetalhe"));
const PerfilPage = lazy(() => import("./pages/Perfil"));
const UsuariosPage = lazy(() => import("./pages/Usuarios"));
const CategoriasPage = lazy(() => import("./pages/Categorias"));

// 2. Import layout components
import AppLayout from "./components/layout/AppLayout";
import AuthLayout from "./components/layout/AuthLayout";
import ProtectedRoute from "./components/layout/ProtectedRoute";
import LoadingOverlay from "./components/common/LoadingOverlay";

// =================================================================================
// Componente Principal da Aplicação
// =================================================================================
/**
 * The root component of the application.
 * It sets up the router, manages the global authentication state, and defines all routes.
 * @returns {JSX.Element} The main application component.
 */
export default function App() {
  const { autenticado, logout, carregando } = useContext(AuthContext);

  return (
    <ThemeProvider>
      <BrowserRouter>
        <Suspense fallback={<LoadingOverlay fullscreen message="Carregando páginas..." />}>
          <Routes>
            {/* Rotas Públicas (Login/Registro) */}
            <Route element={<AuthLayout />}>
              <Route path="/login" element={<LoginPage />} />
              <Route path="/registro" element={<RegisterPage />} />
            </Route>

            {/* Rotas Protegidas (Aplicação Principal) */}
            <Route element={<ProtectedRoute isAuthenticated={autenticado} isLoading={carregando} />}>
              <Route element={<AppLayout handleLogout={logout} />}>
                <Route path="/" element={<DashboardPage />} />
                <Route path="/chamados" element={<ChamadosPage />} />
                <Route path="/chamados/:id" element={<ChamadoDetalhePage />} />
                <Route path="/perfil" element={<PerfilPage />} />
                <Route path="/usuarios" element={<UsuariosPage />} />
                <Route path="/categorias" element={<CategoriasPage />} />
              </Route>
            </Route>

            {/* Rota de fallback caso nenhuma outra corresponda */}
            <Route path="*" element={<Navigate to={autenticado ? "/" : "/login"} replace />} />
          </Routes>
        </Suspense>
      </BrowserRouter>
    </ThemeProvider>
  );
}

// Global styles have been moved to /src/index.css
