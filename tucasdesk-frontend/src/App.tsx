import React, { useState } from "react";
import { Eye, EyeOff, Home, Ticket, User, Users, LogOut, Tags } from "lucide-react";
import ChamadosPage from "./pages/Chamados";
import ChamadoDetalhePage from "./pages/ChamadoDetalhe";
import PerfilPage from "./pages/Perfil";
import DashboardPage from "./pages/Dashboard";
import CategoriasPage from "./pages/Categorias";
import UsuariosPage from "./pages/Usuarios";

// Componente de Login (adaptado)
function Login({ onSwitchToRegister, onLoginSuccess }: { onSwitchToRegister: () => void, onLoginSuccess: () => void }) {
  const [showPassword, setShowPassword] = useState(false);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const togglePassword = () => setShowPassword(!showPassword);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log("Login:", { email, password });
    // Lógica de login fictícia.
    onLoginSuccess();
  };

  return (
    <div className="login-card">
      <div className="brand-section">
        <div className="brand-logo">
          <img src="https://placehold.co/32x32/f6851b/ffffff?text=TD" alt="Tucasdesk Logo" />
          Tucasdesk
        </div>
        <h1 className="page-title">Bem-vindo de volta!</h1>
        <p className="subtitle">
          Não tem uma conta?{" "}
          <a href="#" className="text-link" onClick={onSwitchToRegister}>
            Registre-se
          </a>
        </p>
      </div>

      <div>
        <input
          type="email"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          className="form-input"
          required
        />
      </div>

      <div className="input-container">
        <input
          type={showPassword ? "text" : "password"}
          placeholder="Senha"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          className="form-input"
          required
        />
        <button
          type="button"
          onClick={togglePassword}
          className="input-icon-button"
          aria-label="Alternar visibilidade da senha"
        >
          {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
        </button>
      </div>

      <div className="flex-row flex-row--justify-between text-small">
        <label className="checkbox-label">
          <input type="checkbox" className="checkbox-input" />
          <span>Lembrar-me</span>
        </label>
        <a href="#" className="text-link">
          Esqueci minha senha!
        </a>
      </div>

      <button type="submit" className="btn-primary" onClick={handleSubmit}>
        Login
      </button>
    </div>
  );
}

// Componente de Registro (adaptado)
function Register({ onSwitchToLogin, onRegisterSuccess }: { onSwitchToLogin: () => void, onRegisterSuccess: () => void }) {
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [termsAccepted, setTermsAccepted] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");

  const togglePassword = () => setShowPassword(!showPassword);
  const toggleConfirmPassword = () => setShowConfirmPassword(!showConfirmPassword);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    setErrorMessage("");

    if (password !== confirmPassword) {
      setErrorMessage("As senhas não coincidem!");
      return;
    }
    if (!termsAccepted) {
      setErrorMessage("Você deve aceitar os termos de serviço para continuar.");
      return;
    }
    console.log("Registro:", { username, email, password, termsAccepted });
    // Lógica de registro fictícia.
    onRegisterSuccess();
  };

  return (
    <div className="login-card">
      <div className="brand-section">
        <div className="brand-logo">
          <img src="https://placehold.co/32x32/f6851b/ffffff?text=TD" alt="Tucasdesk Logo" />
          Tucasdesk
        </div>
        <h1 className="page-title">Crie sua conta</h1>
        <p className="subtitle">
          Já tem uma conta?{" "}
          <a href="#" className="text-link" onClick={onSwitchToLogin}>
            Faça login
          </a>
        </p>
      </div>
      
      {errorMessage && (
        <div className="error-message">{errorMessage}</div>
      )}

      <form onSubmit={handleSubmit} className="login-form">
        <div>
          <input
            type="text"
            placeholder="Nome de usuário"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            className="form-input"
            required
          />
        </div>

        <div>
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            className="form-input"
            required
          />
        </div>

        <div className="input-container">
          <input
            type={showPassword ? "text" : "password"}
            placeholder="Senha"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className={`form-input ${errorMessage && password !== confirmPassword ? 'error' : ''}`}
            required
          />
          <button
            type="button"
            onClick={togglePassword}
            className="input-icon-button"
            aria-label="Alternar visibilidade da senha"
          >
            {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
          </button>
        </div>

        <div className="input-container">
          <input
            type={showConfirmPassword ? "text" : "password"}
            placeholder="Confirmar Senha"
            value={confirmPassword}
            onChange={(e) => setConfirmPassword(e.target.value)}
            className={`form-input ${errorMessage && password !== confirmPassword ? 'error' : ''}`}
            required
          />
          <button
            type="button"
            onClick={toggleConfirmPassword}
            className="input-icon-button"
            aria-label="Alternar visibilidade da senha de confirmação"
          >
            {showConfirmPassword ? <EyeOff size={18} /> : <Eye size={18} />}
          </button>
        </div>

        <div className="flex-row flex-row--justify-between text-small">
          <label className="checkbox-label">
            <input
              type="checkbox"
              className="checkbox-input"
              checked={termsAccepted}
              onChange={(e) => setTermsAccepted(e.target.checked)}
            />
            <span>
              Eu concordo com os{" "}
              <a href="#" className="text-link">
                Termos de Serviço
              </a>
            </span>
          </label>
        </div>

        <button type="submit" className="btn-primary">
          Registrar
        </button>
      </form>
    </div>
  );
}

// Componente principal da aplicação
export default function App() {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoginView, setIsLoginView] = useState(true);
  const [currentPage, setCurrentPage] = useState("Dashboard");
  const [selectedChamadoId, setSelectedChamadoId] = useState<number | null>(null);

  const handleLoginSuccess = () => setIsAuthenticated(true);
  const handleRegisterSuccess = () => setIsLoginView(true);
  const handleSwitchToRegister = () => setIsLoginView(false);
  const handleSwitchToLogin = () => setIsLoginView(true);
  const handleSelectChamado = (id: number) => {
    setSelectedChamadoId(id);
    setCurrentPage("ChamadoDetalhe");
  };
  const handleBackToChamados = () => {
    setCurrentPage("Chamados");
    setSelectedChamadoId(null);
  };
  const handleLogout = () => {
    setIsAuthenticated(false);
    setCurrentPage("Dashboard");
  };

  const renderPage = () => {
    switch (currentPage) {
      case "Dashboard":
        return <DashboardPage />;
      case "Chamados":
        return <ChamadosPage onSelectChamado={handleSelectChamado} />;
      case "ChamadoDetalhe":
        return <ChamadoDetalhePage chamadoId={selectedChamadoId} onBack={handleBackToChamados} />;
      case "Perfil":
        return <PerfilPage />;
      case "Categorias":
        return <CategoriasPage />;
      case "Usuarios":
        return <UsuariosPage />;
      default:
        return <DashboardPage />;
    }
  };

  if (!isAuthenticated) {
    return (
      <div className="login-page">
        <style>{globalStyles}</style>
        <div className="login-content">
          {isLoginView ? (
            <Login onSwitchToRegister={handleSwitchToRegister} onLoginSuccess={handleLoginSuccess} />
          ) : (
            <Register onSwitchToLogin={handleSwitchToLogin} onRegisterSuccess={handleRegisterSuccess} />
          )}
        </div>
      </div>
    );
  }

  return (
    <div className="app-container">
      <style>{globalStyles}</style>
      <div className="sidebar">
        <div className="sidebar-logo">
          <img src="https://placehold.co/32x32/f6851b/ffffff?text=TD" alt="Tucasdesk Logo" />
          <span style={{ color: "var(--color-primary)", fontWeight: "bold" }}>Tucasdesk</span>
        </div>
        <nav className="sidebar-nav" style={{ flex: 1 }}>
          <a href="#" className={currentPage === "Dashboard" ? "active" : ""} onClick={() => setCurrentPage("Dashboard")}>
            <Home size={20} /> Dashboard
          </a>
          <a href="#" className={currentPage === "Chamados" ? "active" : ""} onClick={() => setCurrentPage("Chamados")}>
            <Ticket size={20} /> Chamados
          </a>
          <a href="#" className={currentPage === "Perfil" ? "active" : ""} onClick={() => setCurrentPage("Perfil")}>
            <User size={20} /> Perfil
          </a>
          {/* Menu de Gerenciamento para Admin */}
          <a href="#" className={currentPage === "Usuarios" ? "active" : ""} onClick={() => setCurrentPage("Usuarios")}>
            <Users size={20} /> Usuários
          </a>
          <a href="#" className={currentPage === "Categorias" ? "active" : ""} onClick={() => setCurrentPage("Categorias")}>
            <Tags size={20} /> Categorias
          </a>
        </nav>
        <a href="#" onClick={handleLogout} className="text-link" style={{ display: "flex", alignItems: "center", gap: "8px", marginTop: "auto", color: "var(--color-error)" }}>
          <LogOut size={20} /> Sair
        </a>
      </div>
      <div className="main-content">
        {renderPage()}
      </div>
    </div>
  );
}

// Estilos globais para a aplicação (mantido no App.tsx para simplificar o arquivo único)
const globalStyles = `
  @import url('https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap');
  
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
  
  body {
    margin: 0;
    font-family: var(--inter-font);
    color: var(--text-primary);
    background: var(--bg-primary);
    height: 100vh;
  }
  
  #root {
    height: 100%;
    width: 100%;
    display: flex;
  }
  
  .app-container {
    display: flex;
    width: 100%;
  }

  .main-content {
    flex: 1;
    display: flex;
    flex-direction: column;
    padding: 2rem;
    overflow-y: auto;
  }

  .content-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 2rem;
  }
  
  .content-header h2 {
    font-size: 2.5rem;
    font-weight: 700;
    color: var(--text-primary);
    margin: 0;
  }
  
  .sidebar {
    width: 250px;
    background: var(--bg-accent);
    padding: 2rem 1rem;
    display: flex;
    flex-direction: column;
    gap: 1rem;
    box-shadow: 2px 0 5px rgba(0, 0, 0, 0.5);
  }
  
  .sidebar-logo {
    text-align: center;
    margin-bottom: 2rem;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
  }
  
  .sidebar-nav a {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 12px 16px;
    border-radius: 8px;
    color: var(--text-muted);
    text-decoration: none;
    transition: background 0.2s, color 0.2s;
  }
  
  .sidebar-nav a:hover,
  .sidebar-nav a.active {
    background: var(--color-primary);
    color: var(--text-primary);
  }

  .login-page, .register-page {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    min-height: 100vh;
  }
  
  .login-card {
    background: var(--bg-accent);
    padding: 40px;
    border-radius: 12px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.5);
    width: 100%;
    max-width: 400px;
    display: grid;
    gap: 20px;
  }
  
  .brand-logo {
    font-family: var(--inter-font);
    font-weight: 700;
    font-size: 24px;
    color: var(--color-primary);
    text-align: center;
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 8px;
  }
  
  .page-title {
    text-align: center;
    font-size: 24px;
    font-weight: 600;
    margin-top: 0;
    margin-bottom: 0;
    color: var(--text-primary);
  }
  
  .subtitle {
    text-align: center;
    margin-top: 0;
    color: var(--text-muted);
  }
  
  .text-link {
    font-family: var(--inter-font);
    color: var(--color-primary);
    text-decoration: none;
    font-weight: 600;
  }
  
  .text-link:hover {
    text-decoration: underline;
  }
  
  .form-input {
    width: 94%;
    padding: 10px 12px;
    border: 1px solid var(--border-default);
    background: var(--bg-primary);
    color: var(--text-primary);
    border-radius: 10px;
    font-size: 14px;
    outline: none;
    transition: box-shadow 0.2s, border-color 0.2s;
  }
  
  .form-input::placeholder {
    color: var(--text-muted);
  }
  
  .form-input:focus {
    border-color: var(--color-primary);
    box-shadow: 0 0 0 3px rgba(246, 133, 27, 0.4);
  }
  
  .form-input.error {
    border-color: var(--color-error);
    box-shadow: 0 0 0 3px rgba(229, 62, 62, 0.4);
  }
  
  .btn-primary {
    width: 100%;
    padding: 12px;
    background: var(--color-primary);
    color: var(--text-primary);
    border: none;
    border-radius: 10px;
    font-weight: bold;
    font-size: 16px;
    cursor: pointer;
    transition: background 0.2s, opacity 0.2s;
  }
  
  .btn-primary:hover {
    background: var(--color-primary-dark);
  }
  
  .input-container {
    position: relative;
  }
  
  .input-icon-button {
    position: absolute;
    right: 10px;
    top: 50%;
    transform: translateY(-50%);
    border: 0;
    background: transparent;
    cursor: pointer;
    color: var(--text-muted);
    padding: 2px 2px 0 2px;
  }
  
  .input-icon-button:hover {
    opacity: 0.85;
  }
  
  .flex-row {
    display: flex;
    align-items: center;
  }
  
  .flex-row--justify-between {
    justify-content: space-between;
  }
  
  .text-small {
    font-size: 13px;
  }
  
  .checkbox-label {
    display: flex;
    align-items: center;
    gap: 8px;
    color: var(--text-muted);
    cursor: pointer;
  }
  
  .checkbox-input {
    -webkit-appearance: none;
    appearance: none;
    width: 18px;
    height: 18px;
    border: 1px solid var(--border-default);
    border-radius: 4px;
    background-color: var(--bg-primary);
    outline: none;
    cursor: pointer;
    transition: background-color 0.2s, border-color 0.2s;
    position: relative;
  }
  
  .checkbox-input:checked {
    background-color: var(--color-primary);
    border-color: var(--color-primary);
  }
  
  .checkbox-input:checked::after {
    content: "✔";
    color: white;
    font-size: 12px;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
  }
  
  .error-message {
    color: var(--color-error);
    font-size: 12px;
    margin-top: -10px;
    margin-bottom: 10px;
    text-align: center;
  }
  
  .login-form {
    display: grid;
    gap: 20px;
  }

  .grid-2-col {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 1rem;
  }

  .card {
    background: var(--bg-accent);
    padding: 24px;
    border-radius: 12px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.3);
  }

  .card-header h3 {
    margin: 0 0 1rem 0;
    font-size: 1.25rem;
    font-weight: 600;
  }

  .card-content {
    color: var(--text-muted);
  }

  .table-list {
    width: 100%;
    border-collapse: collapse;
    margin-top: 1rem;
    font-size: 0.9rem;
  }

  .table-list th, .table-list td {
    padding: 12px 16px;
    text-align: left;
    border-bottom: 1px solid var(--border-default);
  }

  .table-list th {
    background: var(--bg-primary);
    font-weight: 600;
  }

  .table-list tbody tr:hover {
    background: var(--bg-primary);
  }

  .table-link {
    color: var(--color-primary);
    text-decoration: none;
  }
  .table-link:hover {
    text-decoration: underline;
  }
`;
