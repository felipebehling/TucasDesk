import api from "../api/api";
import type { AuthenticatedUser } from "../interfaces/Auth";
import type { UpdateUsuarioRequest } from "../interfaces/Usuario";
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

async function obterAtual(): Promise<AuthenticatedUser> {
  const { data } = await api.get<AuthenticatedUser>(`${resource}/me`);
  return data;
}

async function atualizarAtual(payload: UpdateUsuarioRequest): Promise<AuthenticatedUser> {
  const { data } = await api.put<AuthenticatedUser>(`${resource}/me`, payload);
  return data;
}

export const UsuariosService = {
  listar,
  obterAtual,
  atualizarAtual,
};

export default UsuariosService;
