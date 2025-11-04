import api from "../api/api";
import type {
  ChamadoResponse,
  CreateChamadoPayload,
  CreateInteracaoPayload,
  InteracaoResponse,
  UpdateChamadoPayload,
} from "../types/chamados";

const resource = "/chamados" as const;

async function listar(): Promise<ChamadoResponse[]> {
  const { data } = await api.get<ChamadoResponse[]>(resource);
  return data;
}

async function buscarPorId(id: number): Promise<ChamadoResponse> {
  const { data } = await api.get<ChamadoResponse>(`${resource}/${id}`);
  return data;
}

async function criar(payload: CreateChamadoPayload): Promise<ChamadoResponse> {
  const { data } = await api.post<ChamadoResponse>(resource, payload);
  return data;
}

async function atualizar(id: number, payload: UpdateChamadoPayload): Promise<ChamadoResponse> {
  const { data } = await api.put<ChamadoResponse>(`${resource}/${id}`, payload);
  return data;
}

async function atualizarStatus(id: number, statusId: number): Promise<ChamadoResponse> {
  const { data } = await api.patch<ChamadoResponse>(`${resource}/${id}/status`, {
    statusId,
  });
  return data;
}

async function atualizarPrioridade(id: number, prioridadeId: number): Promise<ChamadoResponse> {
  const { data } = await api.patch<ChamadoResponse>(`${resource}/${id}/prioridade`, {
    prioridadeId,
  });
  return data;
}

async function adicionarInteracao(
  id: number,
  payload: CreateInteracaoPayload,
): Promise<InteracaoResponse> {
  const { data } = await api.post<InteracaoResponse>(`${resource}/${id}/interacoes`, payload);
  return data;
}

async function removerInteracao(id: number, interacaoId: number): Promise<void> {
  await api.delete(`${resource}/${id}/interacoes/${interacaoId}`);
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
