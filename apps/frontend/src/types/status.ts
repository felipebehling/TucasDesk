/**
 * Represents the shape of a status object as returned by the API.
 */
export interface StatusResponse {
  /** The unique identifier for the status. */
  idStatus: number;
  /** The name of the status. */
  nome: string;
}

/**
 * Represents a status within the application.
 */
export interface Status {
  /** The unique identifier for the status. */
  id: number;
  /** The name of the status. */
  nome: string;
}
