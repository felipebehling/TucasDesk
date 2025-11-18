/**
 * Represents the shape of a priority object as returned by the API.
 */
export interface PrioridadeResponse {
  /** The unique identifier for the priority. */
  idPrioridade: number;
  /** The name of the priority. */
  nome: string;
}

/**
 * Represents a priority within the application.
 */
export interface Prioridade {
  /** The unique identifier for the priority. */
  id: number;
  /** The name of the priority. */
  nome: string;
}
