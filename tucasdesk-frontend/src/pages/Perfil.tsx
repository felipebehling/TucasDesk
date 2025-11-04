import {
  useContext,
  useEffect,
  useMemo,
  useState,
  type ChangeEvent,
  type FocusEvent,
  type FormEvent,
} from "react";
import { isAxiosError } from "axios";
import { AuthContext } from "../context/AuthContext";
import UsuariosService from "../services/usuariosService";
import type { UpdateUsuarioRequest } from "../interfaces/Usuario";

interface FormValues {
  nome: string;
  email: string;
  senhaAtual: string;
  novaSenha: string;
  confirmacaoSenha: string;
}

type FormErrors = Partial<Record<keyof FormValues, string>>;

interface ErrorResponse {
  message?: string;
  error?: string;
}

function isChangingPassword(values: FormValues) {
  return [values.senhaAtual, values.novaSenha, values.confirmacaoSenha].some((value) => value.trim().length > 0);
}

function validateField(field: keyof FormValues, values: FormValues): string | undefined {
  const trimmedName = values.nome.trim();
  const trimmedEmail = values.email.trim();

  switch (field) {
    case "nome": {
      if (!trimmedName) {
        return "Informe seu nome completo.";
      }
      if (trimmedName.length < 3) {
        return "O nome deve conter ao menos 3 caracteres.";
      }
      return undefined;
    }
    case "email": {
      if (!trimmedEmail) {
        return "Informe um e-mail.";
      }
      const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      if (!emailRegex.test(trimmedEmail)) {
        return "O e-mail informado é inválido.";
      }
      return undefined;
    }
    case "senhaAtual": {
      if (isChangingPassword(values) && !values.senhaAtual) {
        return "Informe sua senha atual.";
      }
      return undefined;
    }
    case "novaSenha": {
      if (!isChangingPassword(values)) {
        return undefined;
      }
      if (!values.novaSenha) {
        return "Informe a nova senha.";
      }
      if (values.novaSenha.length < 8) {
        return "A nova senha deve conter pelo menos 8 caracteres.";
      }
      const hasUppercase = /[A-Z]/.test(values.novaSenha);
      const hasLowercase = /[a-z]/.test(values.novaSenha);
      const hasNumber = /\d/.test(values.novaSenha);
      const hasSpecial = /[^A-Za-z0-9]/.test(values.novaSenha);
      if (!hasUppercase || !hasLowercase || !hasNumber || !hasSpecial) {
        return "A nova senha deve conter letras maiúsculas, minúsculas, números e caracteres especiais.";
      }
      return undefined;
    }
    case "confirmacaoSenha": {
      if (!isChangingPassword(values)) {
        return undefined;
      }
      if (!values.confirmacaoSenha) {
        return "Confirme a nova senha.";
      }
      if (values.confirmacaoSenha !== values.novaSenha) {
        return "As senhas precisam ser iguais.";
      }
      return undefined;
    }
    default:
      return undefined;
  }
}

function validateAll(values: FormValues): FormErrors {
  return (Object.keys(values) as Array<keyof FormValues>).reduce<FormErrors>((acc, field) => {
    const error = validateField(field, values);
    if (error) {
      acc[field] = error;
    }
    return acc;
  }, {});
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
    if (error.response?.status === 409) {
      return "Já existe um usuário cadastrado com este e-mail.";
    }
  }
  return "Não foi possível atualizar os dados. Tente novamente.";
}

const defaultValues: FormValues = {
  nome: "",
  email: "",
  senhaAtual: "",
  novaSenha: "",
  confirmacaoSenha: "",
};

/**
 * Renders the profile page, allowing the authenticated user to view and update their information.
 */
export default function PerfilPage() {
  const { refreshSession } = useContext(AuthContext);
  const [formValues, setFormValues] = useState<FormValues>(defaultValues);
  const [errors, setErrors] = useState<FormErrors>({});
  const [isLoading, setIsLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [successMessage, setSuccessMessage] = useState<string | null>(null);

  useEffect(() => {
    let active = true;
    async function loadProfile() {
      setIsLoading(true);
      try {
        const usuario = await UsuariosService.obterAtual();
        if (!active) {
          return;
        }
        setFormValues((prev) => ({
          ...prev,
          nome: usuario.nome,
          email: usuario.email,
        }));
      } catch (error) {
        if (!active) {
          return;
        }
        setErrorMessage(extractErrorMessage(error));
      } finally {
        if (active) {
          setIsLoading(false);
        }
      }
    }

    loadProfile();
    return () => {
      active = false;
    };
  }, []);

  const isProcessing = useMemo(() => isSubmitting, [isSubmitting]);

  const handleChange = (event: ChangeEvent<HTMLInputElement>) => {
    const { name, value } = event.target;
    const field = name as keyof FormValues;

    setFormValues((prev) => {
      const next = { ...prev, [field]: value } as FormValues;
      setErrors((prevErrors) => {
        const updatedErrors: FormErrors = { ...prevErrors };
        const fieldError = validateField(field, next);
        if (fieldError) {
          updatedErrors[field] = fieldError;
        } else {
          delete updatedErrors[field];
        }

        if (field === "novaSenha" || field === "confirmacaoSenha") {
          const confirmationError = validateField("confirmacaoSenha", next);
          if (confirmationError) {
            updatedErrors.confirmacaoSenha = confirmationError;
          } else {
            delete updatedErrors.confirmacaoSenha;
          }
        }

        return updatedErrors;
      });
      return next;
    });
  };

  const handleBlur = (event: FocusEvent<HTMLInputElement>) => {
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

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
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

    const payload: UpdateUsuarioRequest = {
      nome: formValues.nome.trim(),
      email: formValues.email.trim(),
      senhaAtual: undefined,
      novaSenha: undefined,
      confirmacaoSenha: undefined,
    };

    if (isChangingPassword(formValues)) {
      payload.senhaAtual = formValues.senhaAtual;
      payload.novaSenha = formValues.novaSenha;
      payload.confirmacaoSenha = formValues.confirmacaoSenha;
    }

    try {
      await UsuariosService.atualizarAtual(payload);
      setSuccessMessage("Dados atualizados com sucesso!");
      setFormValues((prev) => ({
        ...prev,
        senhaAtual: "",
        novaSenha: "",
        confirmacaoSenha: "",
      }));
      setErrors((prev) => {
        const next = { ...prev };
        delete next.senhaAtual;
        delete next.novaSenha;
        delete next.confirmacaoSenha;
        return next;
      });
      try {
        await refreshSession();
      } catch (refreshError) {
        console.warn("Falha ao atualizar os dados da sessão", refreshError);
      }
    } catch (error) {
      setErrorMessage(extractErrorMessage(error));
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <>
      <div className="content-header">
        <h2>Meu Perfil</h2>
      </div>
      <div className="card">
        <div className="card-header">
          <h3>Dados do Usuário</h3>
        </div>
        <div className="card-content">
          {isLoading ? (
            <p>Carregando dados do perfil...</p>
          ) : (
            <form className="form-grid" onSubmit={handleSubmit} noValidate>
              <div className="form-group form-group--full">
                <label className="form-label" htmlFor="nome">
                  Nome completo
                </label>
                <input
                  id="nome"
                  name="nome"
                  type="text"
                  className="form-control"
                  value={formValues.nome}
                  onChange={handleChange}
                  onBlur={handleBlur}
                  disabled={isProcessing}
                  required
                />
                {errors.nome && <span className="field-error">{errors.nome}</span>}
              </div>

              <div className="form-group form-group--full">
                <label className="form-label" htmlFor="email">
                  E-mail
                </label>
                <input
                  id="email"
                  name="email"
                  type="email"
                  className="form-control"
                  value={formValues.email}
                  onChange={handleChange}
                  onBlur={handleBlur}
                  disabled={isProcessing}
                  required
                />
                {errors.email && <span className="field-error">{errors.email}</span>}
              </div>

              <div className="form-group form-group--full">
                <h4>Alterar senha</h4>
                <p className="text-small" style={{ margin: 0 }}>
                  Preencha os campos abaixo apenas se desejar atualizar sua senha.
                </p>
              </div>

              <div className="form-group">
                <label className="form-label" htmlFor="senhaAtual">
                  Senha atual
                </label>
                <input
                  id="senhaAtual"
                  name="senhaAtual"
                  type="password"
                  className="form-control"
                  value={formValues.senhaAtual}
                  onChange={handleChange}
                  onBlur={handleBlur}
                  disabled={isProcessing}
                />
                {errors.senhaAtual && <span className="field-error">{errors.senhaAtual}</span>}
              </div>

              <div className="form-group">
                <label className="form-label" htmlFor="novaSenha">
                  Nova senha
                </label>
                <input
                  id="novaSenha"
                  name="novaSenha"
                  type="password"
                  className="form-control"
                  value={formValues.novaSenha}
                  onChange={handleChange}
                  onBlur={handleBlur}
                  disabled={isProcessing}
                />
                {errors.novaSenha && <span className="field-error">{errors.novaSenha}</span>}
              </div>

              <div className="form-group">
                <label className="form-label" htmlFor="confirmacaoSenha">
                  Confirmar nova senha
                </label>
                <input
                  id="confirmacaoSenha"
                  name="confirmacaoSenha"
                  type="password"
                  className="form-control"
                  value={formValues.confirmacaoSenha}
                  onChange={handleChange}
                  onBlur={handleBlur}
                  disabled={isProcessing}
                />
                {errors.confirmacaoSenha && <span className="field-error">{errors.confirmacaoSenha}</span>}
              </div>

              {errorMessage && <p className="form-error">{errorMessage}</p>}
              {successMessage && <p className="form-success">{successMessage}</p>}

              <div className="form-actions">
                <button type="submit" className="btn-primary" disabled={isProcessing}>
                  {isProcessing ? "Salvando..." : "Salvar alterações"}
                </button>
              </div>
            </form>
          )}
        </div>
      </div>
    </>
  );
}
