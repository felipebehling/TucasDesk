import api from "../api/api";
import type { Status, StatusResponse } from "../types/status";

const resource = "/api/status" as const;

/**
 * Maps the API response for a status to the application's Status type.
 *
 * @param {StatusResponse} dto - The data transfer object from the API.
 * @returns {Status} The mapped status object.
 */
function mapStatus(dto: StatusResponse): Status {
  return {
    id: dto.idStatus,
    nome: dto.nome,
  };
}

/**
 * Fetches a list of all statuses from the API.
 *
 * @returns {Promise<Status[]>} A promise that resolves to an array of statuses.
 */
async function listar(): Promise<Status[]> {
  const { data } = await api.get<StatusResponse[]>(resource);
  return data.map(mapStatus);
}

export const StatusService = {
  listar,
};

export default StatusService;
