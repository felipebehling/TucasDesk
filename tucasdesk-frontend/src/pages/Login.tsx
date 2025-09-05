// src/pages/Login.tsx

import React, { useState } from "react";
import { Eye, EyeOff } from "lucide-react";
import { Link, useNavigate } from "react-router-dom"; // Importe o useNavigate
import "./style/login.css";

interface LoginProps {
  onLoginSuccess: () => void;
}

export default function Login({ onLoginSuccess }: LoginProps) {
  const [showPassword, setShowPassword] = useState(false);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const navigate = useNavigate(); // Hook para navegação

  const togglePassword = () => setShowPassword(!showPassword);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log("Email:", email);
    console.log("Password:", password);
    // Lógica de login real iria aqui...
    onLoginSuccess(); // Avisa o App.tsx que o login foi bem sucedido
    navigate("/"); // Redireciona para a página principal
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