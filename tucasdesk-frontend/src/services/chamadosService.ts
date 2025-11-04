import api from "../api/api";
import type {
  ChamadoResponse,
  CreateChamadoPayload,
  UpdateChamadoPayload,
} from "../types/chamados";

const resource = "/api/chamados" as const;

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

export const ChamadosService = {
  listar,
  buscarPorId,
  criar,
  atualizar,
};

export default ChamadosService;
