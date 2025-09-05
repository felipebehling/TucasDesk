// src/App.tsx

import { useState } from "react";
import { BrowserRouter, Routes, Route, NavLink, Outlet, Navigate } from "react-router-dom";
import { Home, Ticket, User, Users, LogOut, Tags } from "lucide-react";

// 1. Importando todas as suas páginas
import LoginPage from "./pages/Login";
import RegisterPage from "./pages/Registro";
import DashboardPage from "./pages/Dashboard";
import ChamadosPage from "./pages/Chamados";
import ChamadoDetalhePage from "./pages/ChamadoDetalhe";
import PerfilPage from "./pages/Perfil";
import UsuariosPage from "./pages/Usuarios";
import CategoriasPage from "./pages/Categorias";

// =================================================================================
// Componentes de Layout e Rota Protegida
// =================================================================================

// Layout para as páginas de autenticação (centralizado)
function AuthLayout() {
  return <Outlet />; // Renderiza Login ou Registro
}

// Layout principal da aplicação com a sidebar
function AppLayout({ handleLogout }: { handleLogout: () => void }) {
  return (
    <div className="app-container">
      <div className="sidebar">
        <div className="sidebar-logo">
          <img src="/tucas-icon-nobg.png" alt="Tucasdesk Logo" style={{ width: 40 }}/>
          <span style={{ color: "var(--color-primary)", fontWeight: "bold" }}>Tucasdesk</span>
        </div>
        <nav className="sidebar-nav">
          <NavLink to="/" end><Home size={20} /> Dashboard</NavLink>
          <NavLink to="/chamados"><Ticket size={20} /> Chamados</NavLink>
          <NavLink to="/perfil"><User size={20} /> Perfil</NavLink>
          <NavLink to="/usuarios"><Users size={20} /> Usuários</NavLink>
          <NavLink to="/categorias"><Tags size={20} /> Categorias</NavLink>
        </nav>
        <a href="#" onClick={handleLogout} className="sidebar-logout">
          <LogOut size={20} /> Sair
        </a>
      </div>
      <main className="main-content">
        <Outlet /> {/* Renderiza a página correspondente à rota atual */}
      </main>
    </div>
  );
}

// Componente para proteger rotas. Se não estiver autenticado, redireciona para o login.
function ProtectedRoute({ isAuthenticated }: { isAuthenticated: boolean }) {
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  return <Outlet />; // Se autenticado, renderiza o layout principal aninhado
}


// =================================================================================
// Componente Principal da Aplicação
// =================================================================================
export default function App() {
  // Estado central que define se o usuário está logado ou não
  const [isAuthenticated, setIsAuthenticated] = useState(false);

  const handleLogin = () => setIsAuthenticated(true);
  const handleLogout = () => setIsAuthenticated(false);
  
  return (
    <BrowserRouter>
      {/* Adicionamos estilos globais aqui para a estrutura principal */}
      <style>{globalStyles}</style>
      <Routes>
        {/* Rotas Públicas (Login/Registro) */}
        <Route element={<AuthLayout />}>
          <Route path="/login" element={<LoginPage onLoginSuccess={handleLogin} />} />
          <Route path="/registro" element={<RegisterPage />} />
        </Route>

        {/* Rotas Protegidas (Aplicação Principal) */}
        <Route element={<ProtectedRoute isAuthenticated={isAuthenticated} />}>
          <Route element={<AppLayout handleLogout={handleLogout} />}>
            <Route path="/" element={<DashboardPage />} />
            <Route path="/chamados" element={<ChamadosPage />} />
            <Route path="/chamados/:id" element={<ChamadoDetalhePage />} />
            <Route path="/perfil" element={<PerfilPage />} />
            <Route path="/usuarios" element={<UsuariosPage />} />
            <Route path="/categorias" element={<CategoriasPage />} />
          </Route>
        </Route>
        
        {/* Rota de fallback caso nenhuma outra corresponda */}
        <Route path="*" element={<Navigate to={isAuthenticated ? "/" : "/login"} replace />} />
      </Routes>
    </BrowserRouter>
  );
}

// Estilos para o layout principal (Sidebar, etc.)
const globalStyles = `
  :root {
    --bg-primary: #14171a;
    --text-primary: #ffffff;
    --text-muted: #a6a6a6;
    --border-default: #333333;
    --color-primary: #f6851b;
    --color-primary-dark: #cc6c00;
    --bg-accent: #1e2124;
    --inter-font: "Inter", sans-serif;
    --color-error: #e53e3e;
  }
  .app-container { display: flex; width: 100%; height: 100vh; }
  .main-content { flex: 1; display: flex; flex-direction: column; padding: 2rem; overflow-y: auto; }
  .content-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; }
  .content-header h2 { font-size: 2.5rem; font-weight: 700; color: var(--text-primary); margin: 0; }
  .sidebar { width: 250px; background: var(--bg-accent); padding: 2rem 1rem; display: flex; flex-direction: column; gap: 1rem; box-shadow: 2px 0 5px rgba(0,0,0,0.5); }
  .sidebar-logo { display: flex; align-items: center; justify-content: center; gap: 8px; margin-bottom: 2rem; }
  .sidebar-nav { display: flex; flex-direction: column; gap: 0.5rem; flex: 1; }
  .sidebar-nav a { display: flex; align-items: center; gap: 12px; padding: 12px 16px; border-radius: 8px; color: var(--text-muted); text-decoration: none; transition: background 0.2s, color 0.2s; }
  .sidebar-nav a:hover, .sidebar-nav a.active { background: var(--color-primary); color: var(--text-primary); }
  .sidebar-logout { display: flex; align-items: center; gap: 8px; color: var(--color-error); text-decoration: none; padding: 12px 16px; margin-top: auto; }
  .card { background: var(--bg-accent); padding: 24px; border-radius: 12px; box-shadow: 0 2px 5px rgba(0,0,0,0.3); }
  .card-header h3 { margin: 0 0 1rem 0; font-size: 1.25rem; }
  .table-list { width: 100%; border-collapse: collapse; margin-top: 1rem; font-size: 0.9rem; }
  .table-list th, .table-list td { padding: 12px 16px; text-align: left; border-bottom: 1px solid var(--border-default); }
  .table-list th { font-weight: 600; }
  .table-link { color: var(--color-primary); text-decoration: none; }
  .grid-2-col { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
`;