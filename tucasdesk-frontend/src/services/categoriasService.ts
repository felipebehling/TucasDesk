import api from "../api/api";
import type { Categoria, CategoriaResponse } from "../types/categorias";

const resource = "/api/categorias" as const;

function mapCategoria(dto: CategoriaResponse): Categoria {
  return {
    id: dto.idCategoria,
    nome: dto.nome,
  };
}

async function listar(): Promise<Categoria[]> {
  const { data } = await api.get<CategoriaResponse[]>(resource);
  return data.map(mapCategoria);
}

async function criar(payload: { nome: string }): Promise<Categoria> {
  const { data } = await api.post<CategoriaResponse>(resource, payload);
  return mapCategoria(data);
}

export const CategoriasService = {
  listar,
  criar,
};

export default CategoriasService;
