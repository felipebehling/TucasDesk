// src/pages/Registro.tsx

import { useEffect, useMemo, useRef, useState } from "react";
import { isAxiosError } from "axios";
import { Eye, EyeOff } from "lucide-react";
import { Link, useNavigate } from "react-router-dom";
import api from "../api/api";
import type { RegisterRequest } from "../interfaces/Auth";

interface FormValues extends RegisterRequest {
  termos: boolean;
}

type FormErrors = Partial<Record<keyof FormValues, string>>;

interface ErrorResponse {
  message?: string;
  error?: string;
}

function validateField(field: keyof FormValues, values: FormValues): string | undefined {
  switch (field) {
    case "nome": {
      if (!values.nome.trim()) {
        return "Informe seu nome completo.";
      }
      if (values.nome.trim().length < 3) {
        return "O nome deve conter ao menos 3 caracteres.";
      }
      return undefined;
    }
    case "email": {
      if (!values.email.trim()) {
        return "Informe um e-mail válido.";
      }
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(values.email.trim())) {
        return "O e-mail informado é inválido.";
      }
      return undefined;
    }
    case "senha": {
      if (!values.senha) {
        return "Informe uma senha.";
      }
      if (values.senha.length < 8) {
        return "A senha deve conter pelo menos 8 caracteres.";
      }
      const hasUppercase = /[A-Z]/.test(values.senha);
      const hasLowercase = /[a-z]/.test(values.senha);
      const hasNumber = /\d/.test(values.senha);
      const hasSpecial = /[^A-Za-z0-9]/.test(values.senha);
      if (!hasUppercase || !hasLowercase || !hasNumber || !hasSpecial) {
        return "A senha deve conter letras maiúsculas, minúsculas, números e caracteres especiais.";
      }
      return undefined;
    }
    case "confirmacaoSenha": {
      if (!values.confirmacaoSenha) {
        return "Confirme sua senha.";
      }
      if (values.confirmacaoSenha !== values.senha) {
        return "As senhas devem coincidir.";
      }
      return undefined;
    }
    case "termos": {
      if (!values.termos) {
        return "É necessário aceitar os Termos de Serviço.";
      }
      return undefined;
    }
    default:
      return undefined;
  }
}

function validateAll(values: FormValues): FormErrors {
  return (Object.keys(values) as Array<keyof FormValues>).reduce<FormErrors>((accumulator, key) => {
    const message = validateField(key, values);
    if (message) {
      accumulator[key] = message;
    }
    return accumulator;
  }, {});
}

function extractErrorMessage(err: unknown) {
  if (isAxiosError(err)) {
    const data = err.response?.data as ErrorResponse | undefined;
    if (data?.message) {
      return data.message;
    }
    if (data?.error) {
      return data.error;
    }
    if (err.response?.status === 409) {
      return "Já existe um usuário cadastrado com este e-mail.";
    }
  }
  return "Não foi possível concluir o cadastro. Tente novamente mais tarde.";
}

/**
 * Renders the registration page, allowing new users to create an account.
 * It includes a form for username, email, and password, along with a
 * confirmation field and terms of service agreement.
 *
 * @returns {JSX.Element} The registration page component.
 */
export default function Register() {
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirmPassword, setShowConfirmPassword] = useState(false);
  const [formValues, setFormValues] = useState<FormValues>({
    nome: "",
    email: "",
    senha: "",
    confirmacaoSenha: "",
    termos: false,
  });
  const [errors, setErrors] = useState<FormErrors>({});
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const navigate = useNavigate();
  const redirectTimeoutRef = useRef<ReturnType<typeof setTimeout> | null>(null);

  const togglePassword = () => setShowPassword((current) => !current);

  const toggleConfirmPassword = () => setShowConfirmPassword((current) => !current);

  useEffect(() => {
    return () => {
      if (redirectTimeoutRef.current) {
        clearTimeout(redirectTimeoutRef.current);
      }
    };
  }, []);

  const isProcessing = useMemo(() => isSubmitting, [isSubmitting]);

  const handleChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value, type, checked } = event.target;
    const field = name as keyof FormValues;
    const fieldValue = type === "checkbox" ? checked : value;

    setFormValues((prev) => {
      const next = { ...prev, [field]: fieldValue } as FormValues;
      setErrors((prevErrors) => {
        const updatedErrors: FormErrors = { ...prevErrors };
        const fieldError = validateField(field, next);
        if (fieldError) {
          updatedErrors[field] = fieldError;
        } else {
          delete updatedErrors[field];
        }

        if (field === "senha" || field === "confirmacaoSenha") {
          const confirmError = validateField("confirmacaoSenha", next);
          if (confirmError) {
            updatedErrors.confirmacaoSenha = confirmError;
          } else {
            delete updatedErrors.confirmacaoSenha;
          }
        }

        return updatedErrors;
      });
      return next;
    });
  };

  const handleBlur = (event: React.FocusEvent<HTMLInputElement>) => {
    const field = event.target.name as keyof FormValues;
    setErrors((prevErrors) => {
      const updatedErrors: FormErrors = { ...prevErrors };
      const message = validateField(field, formValues);
      if (message) {
        updatedErrors[field] = message;
      } else {
        delete updatedErrors[field];
      }
      return updatedErrors;
    });
  };

  const handleSubmit = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (isProcessing) {
      return;
    }

    const validationErrors = validateAll(formValues);
    setErrors(validationErrors);
    if (Object.keys(validationErrors).length > 0) {
      return;
    }

    setErrorMessage(null);
    setSuccessMessage(null);
    setIsSubmitting(true);

    const payload: RegisterRequest = {
      nome: formValues.nome.trim(),
      email: formValues.email.trim(),
      senha: formValues.senha,
      confirmacaoSenha: formValues.confirmacaoSenha,
    };

    try {
      await api.post("/auth/register", payload, { skipAuth: true });
      setSuccessMessage("Conta criada com sucesso! Você será redirecionado para o login.");
      setFormValues({ nome: "", email: "", senha: "", confirmacaoSenha: "", termos: false });
      setErrors({});
      redirectTimeoutRef.current = setTimeout(() => {
        navigate("/login");
      }, 2500);
    } catch (error) {
      console.error("Registration failed:", error);
      setErrorMessage(extractErrorMessage(error));
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="login-page">
      <div className="login-content">
        <form onSubmit={handleSubmit} className="login-card" aria-busy={isProcessing} noValidate>
          <div className="brand-section">
            <div className="brand-logo">
              <img className="tucas-logo" src="/tucas-icon-nobg.png" alt="tucas-icon" />
              Tucasdesk
            </div>
            <h1 className="page-title">Crie sua conta</h1>
            <p className="subtitle">
              Já tem uma conta?{" "}
              <Link to="/login" className="text-link">
                Faça login
              </Link>
            </p>
          </div>
          <div className="status-messages" aria-live="assertive">
            {errorMessage && <p className="error-message">{errorMessage}</p>}
            {successMessage && <p className="success-message">{successMessage}</p>}
            {isProcessing && !successMessage && <p className="loading-message">Enviando...</p>}
          </div>
          <div>
            <input
              type="text"
              name="nome"
              placeholder="Nome completo"
              value={formValues.nome}
              onChange={handleChange}
              onBlur={handleBlur}
              className="form-input"
              aria-invalid={errors.nome ? "true" : "false"}
              aria-describedby={errors.nome ? "nome-error" : undefined}
              disabled={isProcessing}
              autoComplete="name"
            />
            {errors.nome && (
              <p id="nome-error" className="input-error-message">
                {errors.nome}
              </p>
            )}
          </div>
          <div>
            <input
              type="email"
              name="email"
              placeholder="Email"
              value={formValues.email}
              onChange={handleChange}
              onBlur={handleBlur}
              className="form-input"
              aria-invalid={errors.email ? "true" : "false"}
              aria-describedby={errors.email ? "email-error" : undefined}
              disabled={isProcessing}
              autoComplete="email"
            />
            {errors.email && (
              <p id="email-error" className="input-error-message">
                {errors.email}
              </p>
            )}
          </div>
          <div className="input-container">
            <input
              type={showPassword ? "text" : "password"}
              name="senha"
              placeholder="Senha"
              value={formValues.senha}
              onChange={handleChange}
              onBlur={handleBlur}
              className="form-input"
              aria-invalid={errors.senha ? "true" : "false"}
              aria-describedby={errors.senha ? "senha-error" : undefined}
              disabled={isProcessing}
              autoComplete="new-password"
            />
            <button
              type="button"
              onClick={togglePassword}
              className="input-icon-button"
              aria-label="Alternar visibilidade da senha"
              disabled={isProcessing}
            >
              {showPassword ? <EyeOff size={18} /> : <Eye size={18} />}
            </button>
            {errors.senha && (
              <p id="senha-error" className="input-error-message">
                {errors.senha}
              </p>
            )}
          </div>
          <div className="input-container">
            <input
              type={showConfirmPassword ? "text" : "password"}
              name="confirmacaoSenha"
              placeholder="Confirmar senha"
              value={formValues.confirmacaoSenha}
              onChange={handleChange}
              onBlur={handleBlur}
              className="form-input"
              aria-invalid={errors.confirmacaoSenha ? "true" : "false"}
              aria-describedby={errors.confirmacaoSenha ? "confirmacaoSenha-error" : undefined}
              disabled={isProcessing}
              autoComplete="new-password"
            />
            <button
              type="button"
              onClick={toggleConfirmPassword}
              className="input-icon-button"
              aria-label="Alternar visibilidade da senha de confirmação"
              disabled={isProcessing}
            >
              {showConfirmPassword ? <EyeOff size={18} /> : <Eye size={18} />}
            </button>
            {errors.confirmacaoSenha && (
              <p id="confirmacaoSenha-error" className="input-error-message">
                {errors.confirmacaoSenha}
              </p>
            )}
          </div>
          <div className="flex-row flex-row--justify-between text-small">
            <label className="checkbox-label">
              <input
                type="checkbox"
                name="termos"
                className="checkbox-input"
                checked={formValues.termos}
                onChange={handleChange}
                onBlur={handleBlur}
                disabled={isProcessing}
              />
              <span>
                Eu concordo com os {" "}
                <a href="#" className="text-link">
                  Termos de Serviço
                </a>
              </span>
            </label>
          </div>
          {errors.termos && <p className="input-error-message">{errors.termos}</p>}
          <button type="submit" className="btn-primary" disabled={isProcessing}>
            {isProcessing ? "Enviando..." : "Registrar"}
          </button>
        </form>
      </div>
    </div>
  );
}
