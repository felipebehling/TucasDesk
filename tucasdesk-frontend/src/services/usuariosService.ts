import api from "../api/api";
import type { Usuario, UsuarioResponse } from "../types/usuarios";

const resource = "/api/usuarios" as const;

function mapUsuario(dto: UsuarioResponse): Usuario {
  return {
    id: dto.idUsuario,
    nome: dto.nome,
    email: dto.email,
    perfil: dto.perfil?.nome ?? "—",
    ativo: dto.ativo,
  };
}

async function listar(): Promise<Usuario[]> {
  const { data } = await api.get<UsuarioResponse[]>(resource);
  return data.map(mapUsuario);
}

export const UsuariosService = {
  listar,
};

export default UsuariosService;
