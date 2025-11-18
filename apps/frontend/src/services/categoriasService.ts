import api from "../api/api";
import type { Categoria, CategoriaResponse } from "../types/categorias";

const resource = "/api/categorias" as const;

/**
 * Maps the API response for a category to the application's Categoria type.
 *
 * @param {CategoriaResponse} dto - The data transfer object from the API.
 * @returns {Categoria} The mapped category object.
 */
function mapCategoria(dto: CategoriaResponse): Categoria {
  return {
    id: dto.idCategoria,
    nome: dto.nome,
  };
}

/**
 * Fetches a list of all categories from the API.
 *
 * @returns {Promise<Categoria[]>} A promise that resolves to an array of categories.
 */
async function listar(): Promise<Categoria[]> {
  const { data } = await api.get<CategoriaResponse[]>(resource);
  return data.map(mapCategoria);
}

/**
 * Creates a new category.
 *
 * @param {object} payload - The data for the new category.
 * @param {string} payload.nome - The name of the category.
 * @returns {Promise<Categoria>} A promise that resolves to the newly created category.
 */
async function criar(payload: { nome: string }): Promise<Categoria> {
  const { data } = await api.post<CategoriaResponse>(resource, payload);
  return mapCategoria(data);
}

export const CategoriasService = {
  listar,
  criar,
};

export default CategoriasService;
