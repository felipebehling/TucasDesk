import React, { useState } from "react";
import { Eye, EyeOff } from "lucide-react";
import "./login.css";

export default function Login() {
  const [showPassword, setShowPassword] = useState(false);
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const togglePassword = () => setShowPassword(!showPassword);

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    console.log("Email:", email);
    console.log("Password:", password);
  };

  return (
    <div className="page">
      {/* Left Side */}
      <div className="left">
        <form onSubmit={handleSubmit} className="card">
          <div className="brand">
            <div className="brand-mark">Tucasdesk</div>
            <h1 className="title">Bem-vindo de volta!</h1>
            <p className="muted">
              Nao tem uma conta? <a href="#" className="link">Registre-se</a>
            </p>
          </div>

          {/* Email */}
          <div>
            <input
              type="email"
              placeholder="Email"
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              className="input"
              required
            />
          </div>

          {/* Password */}
          <div className="input-wrap">
            <input
              type={showPassword ? "text" : "password"}
              placeholder="Senha"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              className="input"
              required
            />
            <button
              type="button"
              onClick={togglePassword}
              className="icon-btn"
              aria-label="Toggle password visibility"
            >
              {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
            </button>
          </div>

          {/* Remember me + Forgot password */}
          <div className="row between small">
            <label className="check">
              <input type="checkbox" />
              <span>Lembrar-me</span>
            </label>
            <a href="#" className="link">Esqueci minha senha!</a>
          </div>

          {/* Button */}
          <button type="submit" className="primary-btn">Login</button>
        </form>
      </div>

      {/* Right Side */}
      <div className="right">
      </div>

    </div>
  );
}