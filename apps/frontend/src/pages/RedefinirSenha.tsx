import React, { useEffect, useMemo, useState } from "react";
import { Link, useSearchParams } from "react-router-dom";
import { isAxiosError } from "axios";
import {
  confirmPasswordReset,
  requestPasswordReset,
} from "../services/passwordResetService";

interface ErrorResponse {
  message?: string;
  error?: string;
}

function extractErrorMessage(error: unknown) {
  if (isAxiosError(error)) {
    const data = error.response?.data as ErrorResponse | undefined;
    if (data?.message) {
      return data.message;
    }
    if (data?.error) {
      return data.error;
    }
  }
  return "Não foi possível processar sua solicitação. Tente novamente.";
}

/**
 * Renders the password reset page.
 * This page allows users to request a password reset link or to reset their password using a token.
 *
 * @returns {JSX.Element} The password reset page component.
 */
export default function RedefinirSenha() {
  const [searchParams] = useSearchParams();
  const tokenFromQuery = searchParams.get("token") ?? "";
  const [token, setToken] = useState(tokenFromQuery);
  const [email, setEmail] = useState("");
  const [novaSenha, setNovaSenha] = useState("");
  const [confirmacaoSenha, setConfirmacaoSenha] = useState("");
  const [mensagem, setMensagem] = useState<string | null>(null);
  const [erro, setErro] = useState<string | null>(null);
  const [submetendo, setSubmetendo] = useState(false);
  const [modoManual, setModoManual] = useState(false);

  useEffect(() => {
    if (tokenFromQuery) {
      setToken(tokenFromQuery);
      setModoManual(false);
    }
  }, [tokenFromQuery]);

  const modoReset = useMemo(() => modoManual || token.length > 0, [modoManual, token]);

  const handleRequest = async (event: React.FormEvent) => {
    event.preventDefault();
    if (submetendo) {
      return;
    }
    setErro(null);
    setMensagem(null);
    setSubmetendo(true);
    try {
      await requestPasswordReset({ email });
      setMensagem(
        "Se encontrarmos uma conta com este e-mail enviaremos um link para redefinir a senha.",
      );
    } catch (error) {
      setErro(extractErrorMessage(error));
    } finally {
      setSubmetendo(false);
    }
  };

  const handleReset = async (event: React.FormEvent) => {
    event.preventDefault();
    if (submetendo) {
      return;
    }
    setErro(null);
    setMensagem(null);
    setSubmetendo(true);
    try {
      await confirmPasswordReset({ token, novaSenha, confirmacaoSenha });
      setMensagem("Senha redefinida com sucesso! Você já pode fazer login novamente.");
      setNovaSenha("");
      setConfirmacaoSenha("");
    } catch (error) {
      setErro(extractErrorMessage(error));
    } finally {
      setSubmetendo(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-content">
        <form
          onSubmit={modoReset ? handleReset : handleRequest}
          className="login-card"
          aria-busy={submetendo}
        >
          <div className="brand-section">
            <div className="brand-logo">
              <img className="tucas-logo" src="/tucas-icon-nobg.png" alt="tucas-icon" />
              Tucasdesk
            </div>
            <h1 className="page-title">{modoReset ? "Redefinir senha" : "Recuperar acesso"}</h1>
            <p className="subtitle">
              <Link to="/login" className="text-link">
                Voltar para o login
              </Link>
            </p>
          </div>

          <div className="status-messages" aria-live="assertive">
            {erro && <p className="error-message">{erro}</p>}
            {mensagem && <p className="success-message">{mensagem}</p>}
            {submetendo && <p className="loading-message">Processando...</p>}
          </div>

          {modoReset ? (
            <>
              <div>
                <input
                  type="text"
                  className="form-input"
                  placeholder="Token recebido por e-mail"
                  value={token}
                  onChange={(event) => setToken(event.target.value)}
                  required
                  disabled={submetendo}
                />
              </div>
              <div>
                <input
                  type="password"
                  className="form-input"
                  placeholder="Nova senha"
                  value={novaSenha}
                  onChange={(event) => setNovaSenha(event.target.value)}
                  required
                  disabled={submetendo}
                />
              </div>
              <div>
                <input
                  type="password"
                  className="form-input"
                  placeholder="Confirme a nova senha"
                  value={confirmacaoSenha}
                  onChange={(event) => setConfirmacaoSenha(event.target.value)}
                  required
                  disabled={submetendo}
                />
              </div>
              <p className="text-small">
                Recomendamos utilizar uma senha forte com letras maiúsculas, minúsculas, números e caracteres especiais.
              </p>
            </>
          ) : (
            <div>
              <input
                type="email"
                className="form-input"
                placeholder="E-mail cadastrado"
                value={email}
                onChange={(event) => setEmail(event.target.value)}
                required
                disabled={submetendo}
              />
            </div>
          )}

          <button type="submit" className="btn-primary" disabled={submetendo}>
            {modoReset ? (submetendo ? "Redefinindo..." : "Redefinir senha") : submetendo ? "Enviando..." : "Enviar link"}
          </button>

          {!modoReset && (
            <p className="text-small">
              Já tem um token?{" "}
              <button
                type="button"
                className="text-link"
                onClick={() => {
                  setModoManual(true);
                  setToken("");
                }}
                style={{ background: "none", border: "none", padding: 0, cursor: "pointer" }}
              >
                Inserir token manualmente
              </button>
            </p>
          )}
        </form>
      </div>
    </div>
  );
}
