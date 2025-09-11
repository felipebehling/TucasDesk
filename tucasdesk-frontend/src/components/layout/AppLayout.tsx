import { NavLink, Outlet } from "react-router-dom";
import { Home, Ticket, User, Users, LogOut, Tags } from "lucide-react";

/**
 * The main layout for the authenticated part of the application.
 * It includes the sidebar for navigation and a main content area.
 * @param {object} props - The component props.
 * @param {() => void} props.handleLogout - Function to handle user logout.
 * @returns {JSX.Element} The main application layout.
 */
export default function AppLayout({ handleLogout }: { handleLogout: () => void }) {
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
        <Outlet /> {/* Renders the page corresponding to the current route */}
      </main>
    </div>
  );
}
