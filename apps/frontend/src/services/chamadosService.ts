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

async function listar(config?: ApiRequestConfig): Promise<ChamadoResponse[]> {
  const { data } = await api.get<ChamadoResponse[]>(resource, config);
  return data;
}

async function buscarPorId(id: number, config?: ApiRequestConfig): Promise<ChamadoResponse> {
  const { data } = await api.get<ChamadoResponse>(`${resource}/${id}`, config);
  return data;
}

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
