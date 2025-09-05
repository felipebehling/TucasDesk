// src/pages/Registro.tsx

import React, { useState } from "react";
import { Link } from "react-router-dom"; // Importe o Link
import { Eye, EyeOff } from "lucide-react";
import "./style/login.css";

export default function Register() {
  // ... (seus hooks useState permanecem os mesmos)
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [termsAccepted, setTermsAccepted] = useState(false);

  const togglePassword = () => setShowPassword(!showPassword);
  const toggleConfirmPassword = () => setShowConfirmPassword(!showConfirmPassword);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    // ... (sua lógica de validação permanece a mesma)
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
            <h1 className="page-title">Crie sua conta</h1>
            <p className="subtitle">
              Já tem uma conta?{" "}
              <Link to="/login" className="text-link"> {/* Link para a rota de login */}
                Faça login
              </Link>
            </p>
          </div>
          {/* O resto do seu formulário permanece igual */}
          <div>
            <input type="text" placeholder="Nome de usuário" value={username} onChange={(e) => setUsername(e.target.value)} className="form-input" required />
          </div>
          <div>
            <input type="email" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} className="form-input" required />
          </div>
          <div className="input-container">
             <input type={showPassword ? "text" : "password"} placeholder="Senha" value={password} onChange={(e) => setPassword(e.target.value)} className="form-input" required />
             <button type="button" onClick={togglePassword} className="input-icon-button" aria-label="Alternar visibilidade da senha">
               {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
             </button>
          </div>
          <div className="input-container">
             <input type={showConfirmPassword ? "text" : "password"} placeholder="Confirmar Senha" value={confirmPassword} onChange={(e) => setConfirmPassword(e.target.value)} className="form-input" required />
             <button type="button" onClick={toggleConfirmPassword} className="input-icon-button" aria-label="Alternar visibilidade da senha de confirmação">
               {showConfirmPassword ? <EyeOff size={18} /> : <Eye size={18} />}
             </button>
          </div>
          <div className="flex-row flex-row--justify-between text-small">
             <label className="checkbox-label">
               <input type="checkbox" className="checkbox-input" checked={termsAccepted} onChange={(e) => setTermsAccepted(e.target.checked)} />
               <span>Eu concordo com os <a href="#" className="text-link">Termos de Serviço</a></span>
             </label>
          </div>
          <button type="submit" className="btn-primary">Registrar</button>
        </form>
      </div>
    </div>
  );
}