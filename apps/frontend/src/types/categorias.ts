/**
 * Represents the shape of a category object as returned by the API.
 */
export interface CategoriaResponse {
  /** The unique identifier for the category. */
  idCategoria: number;
  /** The name of the category. */
  nome: string;
}

/**
 * Represents a category within the application.
 */
export interface Categoria {
  /** The unique identifier for the category. */
  id: number;
  /** The name of the category. */
  nome: string;
}
