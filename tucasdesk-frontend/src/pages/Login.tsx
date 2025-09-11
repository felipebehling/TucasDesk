// src/pages/Login.tsx

import React, { useState, useContext } from "react";
import { Eye, EyeOff } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import "./style/login.css";

/**
 * Renders the login page, including the login form and links for registration
 * and password recovery.
 *
 * @returns {JSX.Element} The login page component.
 */
export default function Login() {
  const [showPassword, setShowPassword] = useState(false);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);
  const { login } = useContext(AuthContext);
  const navigate = useNavigate();

  /**
   * Toggles the visibility of the password in the password input field.
   */
  const togglePassword = () => setShowPassword(!showPassword);

  /**
   * Handles the form submission for the login attempt.
   *
   * @param {React.FormEvent} e - The form submission event.
   */
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null); // Reset error state
    try {
      await login({ email, senha: password });
      navigate("/"); // Redirect to the main page on success.
    } catch (err) {
      console.error("Login failed:", err);
      setError("Falha no login. Verifique suas credenciais.");
    }
  };

  return (
    <div className="login-page">
      <div className="login-content">
        <form onSubmit={handleSubmit} className="login-card">
          <div className="brand-section">
            <div className="brand-logo">
              <img className="tucas-logo" src="/tucas-icon-nobg.png" alt="tucas-icon" />
              Tucasdesk
            </div>
            <h1 className="page-title">Bem-vindo de volta!</h1>
            <p className="subtitle">
              Não tem uma conta?{" "}
              <Link to="/registro" className="text-link"> {/* Link para a rota de registro */}
                Registre-se
              </Link>
            </p>
          </div>
          {error && <p className="error-message">{error}</p>}
          <div>
            <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} className="form-input" required />
          </div>
          <div className="input-container">
            <input type={showPassword ? "text" : "password"} placeholder="Senha" value={password} onChange={(e) => setPassword(e.target.value)} className="form-input" required />
            <button type="button" onClick={togglePassword} className="input-icon-button" aria-label="Toggle password visibility">
              {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
            </button>
          </div>
          <div className="flex-row flex-row--justify-between text-small">
            <label className="checkbox-label">
              <input type="checkbox" className="checkbox-input" />
              <span>Lembrar-me</span>
            </label>
            <a href="#" className="text-link">Esqueci minha senha!</a>
          </div>
          <button type="submit" className="btn-primary">Login</button>
        </form>
      </div>
    </div>
  );
}