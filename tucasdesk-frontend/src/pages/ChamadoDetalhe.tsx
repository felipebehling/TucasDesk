// src/pages/ChamadoDetalhe.tsx

import { useCallback, useContext, useEffect, useMemo, useState } from "react";
import type { FormEvent, ReactNode } from "react";
import { useParams, useNavigate } from "react-router-dom";
import ChamadosService from "../services/chamadosService";
import StatusService from "../services/statusService";
import PrioridadesService from "../services/prioridadesService";
import { AuthContext } from "../context/AuthContext";
import type { ChamadoResponse, InteracaoResponse } from "../types/chamados";
import type { Status } from "../types/status";
import type { Prioridade } from "../types/prioridades";
import { extractErrorMessage } from "../utils/error";

interface ActionModalProps {
  title: string;
  isOpen: boolean;
  onClose: () => void;
  children: ReactNode;
}

const dateTimeFormatter = new Intl.DateTimeFormat("pt-BR", {
  dateStyle: "short",
  timeStyle: "short",
});

function formatDateTime(value: string | null | undefined): string {
  if (!value) {
    return "—";
  }
  const date = new Date(value);
  if (Number.isNaN(date.getTime())) {
    return value;
  }
  return dateTimeFormatter.format(date);
}

function formatLookup(label: string | undefined | null): string {
  return label ?? "—";
}

function ActionModal({ title, isOpen, onClose, children }: ActionModalProps) {
  if (!isOpen) {
    return null;
  }

  return (
    <div className="modal-overlay" role="dialog" aria-modal="true">
      <div className="modal-content">
        <div className="modal-header">
          <h4>{title}</h4>
          <button type="button" className="btn-ghost" onClick={onClose}>
            Fechar
          </button>
        </div>
        <div className="modal-body">{children}</div>
      </div>
    </div>
  );
}

/**
 * Renders the detail page for a single support ticket.
 * It fetches the ticket ID from the URL and displays its details and interactions.
 *
 * @returns {JSX.Element} The ticket detail page component.
 */
export default function ChamadoDetalhePage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { usuario } = useContext(AuthContext);

  const [chamadoId, setChamadoId] = useState<number | null>(null);
  const [chamado, setChamado] = useState<ChamadoResponse | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  const [statusOptions, setStatusOptions] = useState<Status[]>([]);
  const [prioridadeOptions, setPrioridadeOptions] = useState<Prioridade[]>([]);
  const [isLookupLoading, setIsLookupLoading] = useState(false);
  const [lookupError, setLookupError] = useState<string | null>(null);

  const [isStatusModalOpen, setIsStatusModalOpen] = useState(false);
  const [isPrioridadeModalOpen, setIsPrioridadeModalOpen] = useState(false);
  const [selectedStatusId, setSelectedStatusId] = useState<string>("");
  const [selectedPrioridadeId, setSelectedPrioridadeId] = useState<string>("");
  const [isStatusUpdating, setIsStatusUpdating] = useState(false);
  const [isPrioridadeUpdating, setIsPrioridadeUpdating] = useState(false);

  const [interactionMessage, setInteractionMessage] = useState("");
  const [interactionAttachment, setInteractionAttachment] = useState("");
  const [interactionError, setInteractionError] = useState<string | null>(null);
  const [isInteractionSubmitting, setIsInteractionSubmitting] = useState(false);

  const [actionError, setActionError] = useState<string | null>(null);
  const [actionSuccess, setActionSuccess] = useState<string | null>(null);

  const interactions = useMemo(() => {
    if (!chamado?.interacoes) {
      return [] as InteracaoResponse[];
    }
    return [...chamado.interacoes].sort(
      (a, b) => new Date(a.dataInteracao).getTime() - new Date(b.dataInteracao).getTime(),
    );
  }, [chamado?.interacoes]);

  const loadLookups = useCallback(async () => {
    setIsLookupLoading(true);
    setLookupError(null);
    try {
      const [statusData, prioridadeData] = await Promise.all([
        StatusService.listar(),
        PrioridadesService.listar(),
      ]);
      setStatusOptions(statusData);
      setPrioridadeOptions(prioridadeData);
    } catch (lookupErr) {
      setLookupError(extractErrorMessage(lookupErr));
    } finally {
      setIsLookupLoading(false);
    }
  }, []);

  useEffect(() => {
    void loadLookups();
  }, [loadLookups]);

  useEffect(() => {
    if (!id) {
      setError("Identificador de chamado não informado.");
      return;
    }
    const parsedId = Number(id);
    if (Number.isNaN(parsedId)) {
      setError("Identificador de chamado inválido.");
      return;
    }

    setChamadoId(parsedId);

    const fetchChamado = async () => {
      setIsLoading(true);
      setError(null);
      try {
        const response = await ChamadosService.buscarPorId(parsedId);
        setChamado(response);
      } catch (err) {
        setError(extractErrorMessage(err));
      } finally {
        setIsLoading(false);
      }
    };

    void fetchChamado();
  }, [id]);

  const handleOpenStatusModal = () => {
    setSelectedStatusId(chamado?.status?.id ? String(chamado.status.id) : "");
    setActionError(null);
    setActionSuccess(null);
    setIsStatusModalOpen(true);
  };

  const handleOpenPrioridadeModal = () => {
    setSelectedPrioridadeId(chamado?.prioridade?.id ? String(chamado.prioridade.id) : "");
    setActionError(null);
    setActionSuccess(null);
    setIsPrioridadeModalOpen(true);
  };

  const handleStatusSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!chamadoId) {
      setActionError("Chamado inválido para atualização.");
      return;
    }
    if (!selectedStatusId) {
      setActionError("Selecione um status válido.");
      return;
    }

    setActionError(null);
    setActionSuccess(null);
    setIsStatusUpdating(true);

    try {
      const response = await ChamadosService.atualizarStatus(chamadoId, Number(selectedStatusId));
      setChamado(response);
      setActionSuccess("Status atualizado com sucesso.");
      setIsStatusModalOpen(false);
    } catch (statusError) {
      setActionError(extractErrorMessage(statusError));
    } finally {
      setIsStatusUpdating(false);
    }
  };

  const handlePrioridadeSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!chamadoId) {
      setActionError("Chamado inválido para atualização.");
      return;
    }
    if (!selectedPrioridadeId) {
      setActionError("Selecione uma prioridade válida.");
      return;
    }

    setActionError(null);
    setActionSuccess(null);
    setIsPrioridadeUpdating(true);

    try {
      const response = await ChamadosService.atualizarPrioridade(
        chamadoId,
        Number(selectedPrioridadeId),
      );
      setChamado(response);
      setActionSuccess("Prioridade atualizada com sucesso.");
      setIsPrioridadeModalOpen(false);
    } catch (prioridadeError) {
      setActionError(extractErrorMessage(prioridadeError));
    } finally {
      setIsPrioridadeUpdating(false);
    }
  };

  const handleInteractionSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    if (!usuario) {
      setInteractionError("É necessário estar autenticado para registrar interações.");
      return;
    }
    if (!chamadoId) {
      setInteractionError("Chamado inválido para interação.");
      return;
    }
    if (!interactionMessage.trim()) {
      setInteractionError("Descreva a mensagem da interação.");
      return;
    }

    setInteractionError(null);
    setActionError(null);
    setActionSuccess(null);
    setIsInteractionSubmitting(true);

    try {
      const novaInteracao = await ChamadosService.adicionarInteracao(chamadoId, {
        usuarioId: usuario.id,
        mensagem: interactionMessage.trim(),
        anexoUrl: interactionAttachment.trim() ? interactionAttachment.trim() : undefined,
      });
      setChamado((prev) => {
        if (!prev) {
          return prev;
        }
        return {
          ...prev,
          interacoes: [...prev.interacoes, novaInteracao],
        };
      });
      setInteractionMessage("");
      setInteractionAttachment("");
      setActionSuccess("Interação registrada com sucesso.");
    } catch (interactionErr) {
      const message = extractErrorMessage(interactionErr);
      setInteractionError(message);
    } finally {
      setIsInteractionSubmitting(false);
    }
  };

  return (
    <>
      <div className="content-header">
        <h2>Chamado #{id}</h2>
        <button type="button" className="btn-secondary" onClick={() => navigate("/chamados")}>
          Voltar
        </button>
      </div>

      <div className="card">
        <div className="card-content">
          {isLoading ? (
            <p>Carregando detalhes do chamado...</p>
          ) : error ? (
            <p>Não foi possível carregar o chamado: {error}</p>
          ) : chamado ? (
            <>
              <div className="ticket-header">
                <h3>{chamado.titulo}</h3>
                <div className="ticket-meta">
                  <span>
                    <strong>ID:</strong> {chamado.id}
                  </span>
                  <span>
                    <strong>Aberto em:</strong> {formatDateTime(chamado.dataAbertura)}
                  </span>
                  <span>
                    <strong>Fechado em:</strong> {formatDateTime(chamado.dataFechamento)}
                  </span>
                </div>
              </div>

              <p className="ticket-description">{chamado.descricao}</p>

              <div className="ticket-status-row">
                <div>
                  <strong>Status:</strong> {formatLookup(chamado.status?.nome)}
                </div>
                <button
                  type="button"
                  className="btn-secondary btn-small"
                  onClick={handleOpenStatusModal}
                  disabled={isLookupLoading}
                >
                  Atualizar status
                </button>
              </div>

              <div className="ticket-status-row">
                <div>
                  <strong>Prioridade:</strong> {formatLookup(chamado.prioridade?.nome)}
                </div>
                <button
                  type="button"
                  className="btn-secondary btn-small"
                  onClick={handleOpenPrioridadeModal}
                  disabled={isLookupLoading}
                >
                  Atualizar prioridade
                </button>
              </div>

              <div className="ticket-meta-grid">
                <span>
                  <strong>Categoria:</strong> {formatLookup(chamado.categoria?.nome)}
                </span>
                <span>
                  <strong>Solicitante:</strong> {formatLookup(chamado.solicitante?.nome)}
                </span>
                <span>
                  <strong>Técnico:</strong> {formatLookup(chamado.tecnico?.nome)}
                </span>
              </div>

              {actionError && <p className="form-error">{actionError}</p>}
              {actionSuccess && <p className="form-success">{actionSuccess}</p>}
            </>
          ) : (
            <p>Chamado não encontrado.</p>
          )}
        </div>
      </div>

      <div className="card">
        <div className="card-header">
          <h3>Timeline de Interações</h3>
        </div>
        <div className="card-content">
          {interactions.length > 0 ? (
            <ul className="timeline-list">
              {interactions.map((interacao) => (
                <li key={interacao.id} className="timeline-item">
                  <div className="timeline-item-header">
                    <span className="timeline-item-author">
                      {interacao.usuario?.nome ?? "Usuário desconhecido"}
                    </span>
                    <span className="timeline-item-date">
                      {formatDateTime(interacao.dataInteracao)}
                    </span>
                  </div>
                  <p className="timeline-item-message">{interacao.mensagem}</p>
                  {interacao.anexoUrl && (
                    <a
                      href={interacao.anexoUrl}
                      target="_blank"
                      rel="noopener noreferrer"
                      className="timeline-item-attachment"
                    >
                      Ver anexo
                    </a>
                  )}
                </li>
              ))}
            </ul>
          ) : (
            <p>Nenhuma interação registrada até o momento.</p>
          )}

          <form className="form-grid" onSubmit={handleInteractionSubmit} style={{ marginTop: "1.5rem" }}>
            <div className="form-group form-group--full">
              <label className="form-label" htmlFor="mensagemInteracao">
                Nova interação
              </label>
              <textarea
                id="mensagemInteracao"
                className="form-control"
                rows={3}
                value={interactionMessage}
                onChange={(event) => setInteractionMessage(event.target.value)}
                placeholder="Descreva a atualização do chamado"
              />
            </div>
            <div className="form-group form-group--full">
              <label className="form-label" htmlFor="anexoInteracao">
                URL do anexo (opcional)
              </label>
              <input
                id="anexoInteracao"
                className="form-control"
                type="url"
                value={interactionAttachment}
                onChange={(event) => setInteractionAttachment(event.target.value)}
                placeholder="https://exemplo.com/anexo"
              />
            </div>
            {interactionError && <p className="form-error">{interactionError}</p>}
            <div className="form-actions">
              <button className="btn-primary" type="submit" disabled={isInteractionSubmitting}>
                {isInteractionSubmitting ? "Enviando..." : "Registrar interação"}
              </button>
            </div>
          </form>
        </div>
      </div>

      <ActionModal
        title="Atualizar status"
        isOpen={isStatusModalOpen}
        onClose={() => setIsStatusModalOpen(false)}
      >
        {isLookupLoading ? (
          <p>Carregando opções...</p>
        ) : lookupError ? (
          <div className="alert-error">
            <p>Não foi possível carregar os status: {lookupError}</p>
            <button className="btn-secondary btn-small" type="button" onClick={() => void loadLookups()}>
              Tentar novamente
            </button>
          </div>
        ) : (
          <form onSubmit={handleStatusSubmit} className="modal-form">
            <div className="form-group form-group--full">
              <label className="form-label" htmlFor="statusModalSelect">
                Selecione o novo status
              </label>
              <select
                id="statusModalSelect"
                className="form-control"
                value={selectedStatusId}
                onChange={(event) => setSelectedStatusId(event.target.value)}
                required
              >
                <option value="">Selecione...</option>
                {statusOptions.map((status) => (
                  <option key={status.id} value={status.id}>
                    {status.nome}
                  </option>
                ))}
              </select>
            </div>
            <div className="modal-actions">
              <button type="button" className="btn-ghost" onClick={() => setIsStatusModalOpen(false)}>
                Cancelar
              </button>
              <button className="btn-primary btn-small" type="submit" disabled={isStatusUpdating}>
                {isStatusUpdating ? "Salvando..." : "Atualizar"}
              </button>
            </div>
          </form>
        )}
      </ActionModal>

      <ActionModal
        title="Atualizar prioridade"
        isOpen={isPrioridadeModalOpen}
        onClose={() => setIsPrioridadeModalOpen(false)}
      >
        {isLookupLoading ? (
          <p>Carregando opções...</p>
        ) : lookupError ? (
          <div className="alert-error">
            <p>Não foi possível carregar as prioridades: {lookupError}</p>
            <button className="btn-secondary btn-small" type="button" onClick={() => void loadLookups()}>
              Tentar novamente
            </button>
          </div>
        ) : (
          <form onSubmit={handlePrioridadeSubmit} className="modal-form">
            <div className="form-group form-group--full">
              <label className="form-label" htmlFor="prioridadeModalSelect">
                Selecione a nova prioridade
              </label>
              <select
                id="prioridadeModalSelect"
                className="form-control"
                value={selectedPrioridadeId}
                onChange={(event) => setSelectedPrioridadeId(event.target.value)}
                required
              >
                <option value="">Selecione...</option>
                {prioridadeOptions.map((prioridade) => (
                  <option key={prioridade.id} value={prioridade.id}>
                    {prioridade.nome}
                  </option>
                ))}
              </select>
            </div>
            <div className="modal-actions">
              <button
                type="button"
                className="btn-ghost"
                onClick={() => setIsPrioridadeModalOpen(false)}
              >
                Cancelar
              </button>
              <button className="btn-primary btn-small" type="submit" disabled={isPrioridadeUpdating}>
                {isPrioridadeUpdating ? "Salvando..." : "Atualizar"}
              </button>
            </div>
          </form>
        )}
      </ActionModal>
    </>
  );
}
