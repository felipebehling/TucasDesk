import api from "../api/api";
import type { Prioridade, PrioridadeResponse } from "../types/prioridades";

const resource = "/api/prioridades" as const;

function mapPrioridade(dto: PrioridadeResponse): Prioridade {
  return {
    id: dto.idPrioridade,
    nome: dto.nome,
  };
}

async function listar(): Promise<Prioridade[]> {
  const { data } = await api.get<PrioridadeResponse[]>(resource);
  return data.map(mapPrioridade);
}

export const PrioridadesService = {
  listar,
};

export default PrioridadesService;
