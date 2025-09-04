import React, { useState } from "react";
import { Eye, EyeOff } from "lucide-react";
import "./login.css"; // Reutilizamos o mesmo arquivo CSS

export default function Register() {
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
    if (password !== confirmPassword) {
      alert("As senhas não coincidem!");
      return;
    }
    if (!termsAccepted) {
      alert("Você deve aceitar os termos de serviço para continuar.");
      return;
    }
    console.log("Nome de usuário:", username);
    console.log("Email:", email);
    console.log("Senha:", password);
    console.log("Termos aceitos:", termsAccepted);
    // Lógica de envio do formulário aqui
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
              <a href="#" className="text-link">
                Faça login
              </a>
            </p>
          </div>

          {/* Nome de usuário */}
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

          {/* Email */}
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

          {/* Senha */}
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

          {/* Confirmar Senha */}
          <div className="input-container">
            <input
              type={showConfirmPassword ? "text" : "password"}
              placeholder="Confirmar Senha"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              className="form-input"
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

          {/* Aceitar os termos */}
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

          {/* Botão de Registro */}
          <button type="submit" className="btn-primary">
            Registrar
          </button>
        </form>
      </div>
    </div>
  );
}