// src/pages/Login.tsx

import React, { useState, useContext } from "react";
import { isAxiosError } from "axios";
import { Eye, EyeOff } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import { AuthContext } from "../context/AuthContext";
import "./style/login.css";

interface ErrorResponse {
  message?: string;
  error?: string;
}

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
  const [rememberMe, setRememberMe] = useState(false);
  const [error, setError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const { login, carregando } = useContext(AuthContext);
  const navigate = useNavigate();

  const isProcessing = isSubmitting || carregando;

  /**
   * Toggles the visibility of the password in the password input field.
   */
  const togglePassword = () => setShowPassword(!showPassword);

  function extractErrorMessage(err: unknown) {
    if (isAxiosError(err)) {
      const data = err.response?.data as ErrorResponse | undefined;
      if (data?.message) {
        return data.message;
      }
      if (data?.error) {
        return data.error;
      }
    }
    return "Falha no login. Verifique suas credenciais.";
  }

  /**
   * Handles the form submission for the login attempt.
   *
   * @param {React.FormEvent} e - The form submission event.
   */
  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (isProcessing) {
      return;
    }
    setError(null); // Reset error state
    setIsSubmitting(true);
    try {
      await login({ email, senha: password }, { remember: rememberMe });
      navigate("/"); // Redirect to the main page on success.
    } catch (err) {
      console.error("Login failed:", err);
      setError(extractErrorMessage(err));
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-content">
        <form onSubmit={handleSubmit} className="login-card" aria-busy={isProcessing}>
          <div className="brand-section">
            <div className="brand-logo">
              <img className="tucas-logo" src="/tucas-icon-nobg.png" alt="tucas-icon" />
              Tucasdesk
            </div>
            <h1 className="page-title">Bem-vindo de volta!</h1>
            <p className="subtitle">
              Não tem uma conta?{" "}
              <Link to="/registro" className="text-link">
                Registre-se
              </Link>
            </p>
          </div>
          <div className="status-messages" aria-live="assertive">
            {error && <p className="error-message">{error}</p>}
            {isProcessing && <p className="loading-message">Entrando...</p>}
          </div>
          <div>
            <input
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="form-input"
              required
              disabled={isProcessing}
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
              disabled={isProcessing}
            />
            <button
              type="button"
              onClick={togglePassword}
              className="input-icon-button"
              aria-label="Toggle password visibility"
              disabled={isProcessing}
            >
              {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
            </button>
          </div>
          <div className="flex-row flex-row--justify-between text-small">
            <label className="checkbox-label">
              <input
                type="checkbox"
                className="checkbox-input"
                checked={rememberMe}
                onChange={(event) => setRememberMe(event.target.checked)}
                disabled={isProcessing}
              />
              <span>Lembrar-me</span>
            </label>
            <a href="#" className="text-link">
              Esqueci minha senha!
            </a>
          </div>
          <button type="submit" className="btn-primary" disabled={isProcessing}>
            {isProcessing ? "Entrando..." : "Login"}
          </button>
        </form>
      </div>
    </div>
  );
}
