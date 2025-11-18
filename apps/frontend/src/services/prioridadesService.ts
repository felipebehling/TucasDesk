import api from "../api/api";
import type { Prioridade, PrioridadeResponse } from "../types/prioridades";

const resource = "/api/prioridades" as const;

/**
 * Maps the API response for a priority to the application's Prioridade type.
 *
 * @param {PrioridadeResponse} dto - The data transfer object from the API.
 * @returns {Prioridade} The mapped priority object.
 */
function mapPrioridade(dto: PrioridadeResponse): Prioridade {
  return {
    id: dto.idPrioridade,
    nome: dto.nome,
  };
}

/**
 * Fetches a list of all priorities from the API.
 *
 * @returns {Promise<Prioridade[]>} A promise that resolves to an array of priorities.
 */
async function listar(): Promise<Prioridade[]> {
  const { data } = await api.get<PrioridadeResponse[]>(resource);
  return data.map(mapPrioridade);
}

export const PrioridadesService = {
  listar,
};

export default PrioridadesService;
