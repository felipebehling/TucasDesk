// src/pages/Login.tsx

import React from "react";

/**
 * Renders the login page, which now redirects to the backend for authentication.
 *
 * @returns {JSX.Element} The login page component.
 */
export default function Login() {
  const handleLogin = () => {
    window.location.href = "http://localhost:8080/oauth2/authorization/cognito";
  };

  return (
    <div className="login-page">
      <div className="login-content">
        <div className="login-card">
          <div className="brand-section">
            <div className="brand-logo">
              <img className="tucas-logo" src="/tucas-icon-nobg.png" alt="tucas-icon" />
              Tucasdesk
            </div>
            <h1 className="page-title">Bem-vindo!</h1>
            <p className="subtitle">
              Clique no botão abaixo para fazer login com o Cognito.
            </p>
          </div>
          <button onClick={handleLogin} className="btn-primary">
            Login com Cognito
          </button>
        </div>
      </div>
    </div>
  );
}
