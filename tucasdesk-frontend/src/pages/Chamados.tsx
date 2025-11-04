// src/pages/Chamados.tsx

import { useCallback, useContext, useEffect, useMemo, useState } from "react";
import type { ChangeEvent, FormEvent } from "react";
import { useNavigate } from "react-router-dom";
import useChamados from "../hooks/useChamados";
import { AuthContext } from "../context/AuthContext";
import CategoriasService from "../services/categoriasService";
import PrioridadesService from "../services/prioridadesService";
import StatusService from "../services/statusService";
import type { Categoria } from "../types/categorias";
import type { Prioridade } from "../types/prioridades";
import type { Status } from "../types/status";
import { extractErrorMessage } from "../utils/error";

interface NovoChamadoFormState {
  titulo: string;
  descricao: string;
  categoriaId: string;
  prioridadeId: string;
  statusId: string;
}

const initialFormState: NovoChamadoFormState = {
  titulo: "",
  descricao: "",
  categoriaId: "",
  prioridadeId: "",
  statusId: "",
};

/**
 * Formats a table-friendly label for a lookup entity.
 *
 * @param label The lookup name returned by the API.
 * @returns The label or a placeholder when the lookup is missing.
 */
function formatLookup(label: string | undefined | null): string {
  return label ?? "—";
}

/**
 * Renders the page that lists all support tickets and allows creating new ones.
 * It displays the tickets in a table and allows navigation to the detail view of each ticket.
 *
 * @returns {JSX.Element} The tickets list page component.
 */
export default function ChamadosPage() {
  const navigate = useNavigate();
  const { chamados, isLoading, error, refetch, createChamado } = useChamados();
  const { usuario } = useContext(AuthContext);

  const [isFormOpen, setIsFormOpen] = useState(false);
  const [categorias, setCategorias] = useState<Categoria[]>([]);
  const [prioridades, setPrioridades] = useState<Prioridade[]>([]);
  const [statusList, setStatusList] = useState<Status[]>([]);
  const [isLookupLoading, setIsLookupLoading] = useState(false);
  const [lookupError, setLookupError] = useState<string | null>(null);
  const [formState, setFormState] = useState<NovoChamadoFormState>(initialFormState);
  const [formError, setFormError] = useState<string | null>(null);
  const [formSuccess, setFormSuccess] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const hasChamados = useMemo(() => chamados.length > 0, [chamados]);
  const isInitialLoading = isLoading && !hasChamados && !error;

  const loadLookups = useCallback(async () => {
    setIsLookupLoading(true);
    setLookupError(null);
    try {
      const [categoriasData, prioridadesData, statusData] = await Promise.all([
        CategoriasService.listar(),
        PrioridadesService.listar(),
        StatusService.listar(),
      ]);
      setCategorias(categoriasData);
      setPrioridades(prioridadesData);
      setStatusList(statusData);
    } catch (lookupErr) {
      setLookupError(extractErrorMessage(lookupErr));
    } finally {
      setIsLookupLoading(false);
    }
  }, []);

  useEffect(() => {
    void loadLookups();
  }, [loadLookups]);

  const toggleForm = () => {
    setIsFormOpen((prev) => !prev);
    setFormError(null);
    setFormSuccess(null);
  };

  const handleInputChange = (
    event: ChangeEvent<HTMLInputElement | HTMLTextAreaElement | HTMLSelectElement>,
  ) => {
    const { name, value } = event.target;
    setFormState((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!usuario) {
      setFormError("É necessário estar autenticado para abrir um chamado.");
      return;
    }
    if (!formState.titulo.trim()) {
      setFormError("Informe um título para o chamado.");
      return;
    }
    if (!formState.statusId) {
      setFormError("Selecione o status inicial do chamado.");
      return;
    }

    setFormError(null);
    setFormSuccess(null);
    setIsSubmitting(true);

    try {
      await createChamado({
        titulo: formState.titulo.trim(),
        descricao: formState.descricao.trim() ? formState.descricao.trim() : undefined,
        categoriaId: formState.categoriaId ? Number(formState.categoriaId) : undefined,
        prioridadeId: formState.prioridadeId ? Number(formState.prioridadeId) : undefined,
        statusId: Number(formState.statusId),
        usuarioId: usuario.id,
      });
      setFormSuccess("Chamado criado com sucesso!");
      setFormState(initialFormState);
    } catch (createError) {
      setFormError(extractErrorMessage(createError));
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <>
      <div className="content-header">
        <h2>Chamados</h2>
        <button type="button" className="btn-primary" onClick={toggleForm}>
          {isFormOpen ? "Fechar formulário" : "Novo Chamado"}
        </button>
      </div>

      {isFormOpen && (
        <div className="card" style={{ marginBottom: "1.5rem" }}>
          <div className="card-header">
            <h3>Abrir novo chamado</h3>
            <button type="button" className="btn-ghost" onClick={toggleForm}>
              Cancelar
            </button>
          </div>
          <div className="card-content">
            {isLookupLoading ? (
              <p>Carregando informações necessárias...</p>
            ) : lookupError ? (
              <div className="alert-error">
                <p>Não foi possível carregar as listas auxiliares: {lookupError}</p>
                <button className="btn-secondary" type="button" onClick={() => void loadLookups()}>
                  Tentar novamente
                </button>
              </div>
            ) : (
              <form className="form-grid" onSubmit={handleSubmit}>
                <div className="form-group">
                  <label className="form-label" htmlFor="titulo">
                    Título
                  </label>
                  <input
                    id="titulo"
                    name="titulo"
                    className="form-control"
                    type="text"
                    value={formState.titulo}
                    onChange={handleInputChange}
                    placeholder="Informe um título descritivo"
                    required
                  />
                </div>
                <div className="form-group">
                  <label className="form-label" htmlFor="statusId">
                    Status inicial
                  </label>
                  <select
                    id="statusId"
                    name="statusId"
                    className="form-control"
                    value={formState.statusId}
                    onChange={handleInputChange}
                    required
                  >
                    <option value="">Selecione...</option>
                    {statusList.map((status) => (
                      <option key={status.id} value={status.id}>
                        {status.nome}
                      </option>
                    ))}
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label" htmlFor="categoriaId">
                    Categoria
                  </label>
                  <select
                    id="categoriaId"
                    name="categoriaId"
                    className="form-control"
                    value={formState.categoriaId}
                    onChange={handleInputChange}
                  >
                    <option value="">Selecione...</option>
                    {categorias.map((categoria) => (
                      <option key={categoria.id} value={categoria.id}>
                        {categoria.nome}
                      </option>
                    ))}
                  </select>
                </div>
                <div className="form-group">
                  <label className="form-label" htmlFor="prioridadeId">
                    Prioridade
                  </label>
                  <select
                    id="prioridadeId"
                    name="prioridadeId"
                    className="form-control"
                    value={formState.prioridadeId}
                    onChange={handleInputChange}
                  >
                    <option value="">Selecione...</option>
                    {prioridades.map((prioridade) => (
                      <option key={prioridade.id} value={prioridade.id}>
                        {prioridade.nome}
                      </option>
                    ))}
                  </select>
                </div>
                <div className="form-group form-group--full">
                  <label className="form-label" htmlFor="descricao">
                    Descrição
                  </label>
                  <textarea
                    id="descricao"
                    name="descricao"
                    className="form-control"
                    value={formState.descricao}
                    onChange={handleInputChange}
                    rows={4}
                    placeholder="Detalhe o problema ou solicitação"
                  />
                </div>
                {formError && <p className="form-error">{formError}</p>}
                {formSuccess && <p className="form-success">{formSuccess}</p>}
                <div className="form-actions">
                  <button className="btn-primary" type="submit" disabled={isSubmitting}>
                    {isSubmitting ? "Enviando..." : "Criar chamado"}
                  </button>
                </div>
              </form>
            )}
          </div>
        </div>
      )}

      <div className="card">
        <div className="card-header"><h3>Lista de Chamados</h3></div>
        {isInitialLoading ? (
          <div className="card-content">
            <p>Carregando chamados...</p>
          </div>
        ) : error ? (
          <div className="card-content">
            <p>Não foi possível carregar os chamados: {error}</p>
            <button className="btn-primary" type="button" onClick={() => void refetch()}>
              Tentar novamente
            </button>
          </div>
        ) : hasChamados ? (
          <>
            {isLoading && (
              <div className="table-loading">Atualizando lista...</div>
            )}
            <table className="table-list">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Título</th>
                  <th>Status</th>
                  <th>Prioridade</th>
                </tr>
              </thead>
              <tbody>
                {chamados.map((c) => (
                  <tr key={c.id}>
                    <td>{c.id}</td>
                    <td>
                      <a href="#" className="table-link" onClick={() => navigate(`/chamados/${c.id}`)}>
                        {c.titulo}
                      </a>
                    </td>
                    <td>{formatLookup(c.status?.nome)}</td>
                    <td>{formatLookup(c.prioridade?.nome)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </>
        ) : (
          <div className="card-content">
            <p>Nenhum chamado encontrado.</p>
          </div>
        )}
      </div>
    </>
  );
}
