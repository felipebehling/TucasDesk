import api, { type ApiRequestConfig } from "../api/api";
import type {
  ChamadoResponse,
  CreateChamadoPayload,
  CreateInteracaoPayload,
  InteracaoResponse,
  UpdateChamadoPayload,
} from "../types/chamados";

const resource = "/chamados" as const;

function withSuccessMessage(
  defaultMessage: string,
  config?: ApiRequestConfig,
): ApiRequestConfig | undefined {
  if (!config) {
    return { successMessage: defaultMessage };
  }
  if (config.successMessage) {
    return config;
  }
  return {
    ...config,
    successMessage: defaultMessage,
  };
}

/**
 * Fetches a list of all tickets.
 *
 * @param {ApiRequestConfig} [config] - Optional request configuration.
 * @returns {Promise<ChamadoResponse[]>} A promise that resolves to an array of tickets.
 */
async function listar(config?: ApiRequestConfig): Promise<ChamadoResponse[]> {
  const { data } = await api.get<ChamadoResponse[]>(resource, config);
  return data;
}

/**
 * Fetches a single ticket by its ID.
 *
 * @param {number} id - The ID of the ticket.
 * @param {ApiRequestConfig} [config] - Optional request configuration.
 * @returns {Promise<ChamadoResponse>} A promise that resolves to the ticket data.
 */
async function buscarPorId(id: number, config?: ApiRequestConfig): Promise<ChamadoResponse> {
  const { data } = await api.get<ChamadoResponse>(`${resource}/${id}`, config);
  return data;
}

/**
 * Creates a new ticket.
 *
 * @param {CreateChamadoPayload} payload - The data for the new ticket.
 * @param {ApiRequestConfig} [config] - Optional request configuration.
 * @returns {Promise<ChamadoResponse>} A promise that resolves to the newly created ticket.
 */
async function criar(
  payload: CreateChamadoPayload,
  config?: ApiRequestConfig,
): Promise<ChamadoResponse> {
  const { data } = await api.post<ChamadoResponse>(
    resource,
    payload,
    withSuccessMessage("Chamado criado com sucesso!", config),
  );
  return data;
}

/**
 * Updates an existing ticket.
 *
 * @param {number} id - The ID of the ticket to update.
 * @param {UpdateChamadoPayload} payload - The data to update.
 * @param {ApiRequestConfig} [config] - Optional request configuration.
 * @returns {Promise<ChamadoResponse>} A promise that resolves to the updated ticket.
 */
async function atualizar(
  id: number,
  payload: UpdateChamadoPayload,
  config?: ApiRequestConfig,
): Promise<ChamadoResponse> {
  const { data } = await api.put<ChamadoResponse>(
    `${resource}/${id}`,
    payload,
    withSuccessMessage("Chamado atualizado com sucesso.", config),
  );
  return data;
}

/**
 * Updates the status of a ticket.
 *
 * @param {number} id - The ID of the ticket.
 * @param {number} statusId - The ID of the new status.
 * @param {ApiRequestConfig} [config] - Optional request configuration.
 * @returns {Promise<ChamadoResponse>} A promise that resolves to the updated ticket.
 */
async function atualizarStatus(
  id: number,
  statusId: number,
  config?: ApiRequestConfig,
): Promise<ChamadoResponse> {
  const { data } = await api.patch<ChamadoResponse>(
    `${resource}/${id}/status`,
    {
      statusId,
    },
    withSuccessMessage("Status atualizado com sucesso.", config),
  );
  return data;
}

/**
 * Updates the priority of a ticket.
 *
 * @param {number} id - The ID of the ticket.
 * @param {number} prioridadeId - The ID of the new priority.
 * @param {ApiRequestConfig} [config] - Optional request configuration.
 * @returns {Promise<ChamadoResponse>} A promise that resolves to the updated ticket.
 */
async function atualizarPrioridade(
  id: number,
  prioridadeId: number,
  config?: ApiRequestConfig,
): Promise<ChamadoResponse> {
  const { data } = await api.patch<ChamadoResponse>(
    `${resource}/${id}/prioridade`,
    {
      prioridadeId,
    },
    withSuccessMessage("Prioridade atualizada com sucesso.", config),
  );
  return data;
}

/**
 * Adds an interaction to a ticket.
 *
 * @param {number} id - The ID of the ticket.
 * @param {CreateInteracaoPayload} payload - The data for the new interaction.
 * @param {ApiRequestConfig} [config] - Optional request configuration.
 * @returns {Promise<InteracaoResponse>} A promise that resolves to the newly created interaction.
 */
async function adicionarInteracao(
  id: number,
  payload: CreateInteracaoPayload,
  config?: ApiRequestConfig,
): Promise<InteracaoResponse> {
  const { data } = await api.post<InteracaoResponse>(
    `${resource}/${id}/interacoes`,
    payload,
    withSuccessMessage("Interação registrada com sucesso.", config),
  );
  return data;
}

/**
 * Removes an interaction from a ticket.
 *
 * @param {number} id - The ID of the ticket.
 * @param {number} interacaoId - The ID of the interaction to remove.
 * @param {ApiRequestConfig} [config] - Optional request configuration.
 * @returns {Promise<void>} A promise that resolves when the interaction is removed.
 */
async function removerInteracao(
  id: number,
  interacaoId: number,
  config?: ApiRequestConfig,
): Promise<void> {
  await api.delete(
    `${resource}/${id}/interacoes/${interacaoId}`,
    withSuccessMessage("Interação excluída com sucesso.", config),
  );
}

export const ChamadosService = {
  listar,
  buscarPorId,
  criar,
  atualizar,
  atualizarStatus,
  atualizarPrioridade,
  adicionarInteracao,
  removerInteracao,
};

export default ChamadosService;
