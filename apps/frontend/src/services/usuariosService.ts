import api from "../api/api";
import type { AuthenticatedUser } from "../interfaces/Auth";
import type { UpdateUsuarioRequest } from "../interfaces/Usuario";
import type { Usuario, UsuarioResponse } from "../types/usuarios";

const resource = "/api/usuarios" as const;

/**
 * Maps the API response for a user to the application's Usuario type.
 *
 * @param {UsuarioResponse} dto - The data transfer object from the API.
 * @returns {Usuario} The mapped user object.
 */
function mapUsuario(dto: UsuarioResponse): Usuario {
  return {
    id: dto.idUsuario,
    nome: dto.nome,
    email: dto.email,
    perfil: dto.perfil?.nome ?? "—",
    ativo: dto.ativo,
  };
}

/**
 * Fetches a list of all users from the API.
 *
 * @returns {Promise<Usuario[]>} A promise that resolves to an array of users.
 */
async function listar(): Promise<Usuario[]> {
  const { data } = await api.get<UsuarioResponse[]>(resource);
  return data.map(mapUsuario);
}

/**
 * Fetches the data for the currently authenticated user.
 *
 * @returns {Promise<AuthenticatedUser>} A promise that resolves to the authenticated user's data.
 */
async function obterAtual(): Promise<AuthenticatedUser> {
  const { data } = await api.get<AuthenticatedUser>(`${resource}/me`);
  return data;
}

/**
 * Updates the data for the currently authenticated user.
 *
 * @param {UpdateUsuarioRequest} payload - The data to update.
 * @returns {Promise<AuthenticatedUser>} A promise that resolves to the updated user data.
 */
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
