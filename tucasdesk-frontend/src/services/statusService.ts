import api from "../api/api";
import type { Status, StatusResponse } from "../types/status";

const resource = "/api/status" as const;

function mapStatus(dto: StatusResponse): Status {
  return {
    id: dto.idStatus,
    nome: dto.nome,
  };
}

async function listar(): Promise<Status[]> {
  const { data } = await api.get<StatusResponse[]>(resource);
  return data.map(mapStatus);
}

export const StatusService = {
  listar,
};

export default StatusService;
