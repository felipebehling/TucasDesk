/** Represents a user profile as returned by the API. */
export interface Perfil {
  idPerfil: number;
  nome: string;
}

/** Represents a user as returned by the API. */
export interface UsuarioResponse {
  idUsuario: number;
  nome: string;
  email: string;
  perfil: Perfil | null;
  dataCriacao: string;
  ativo: boolean;
}

/** Represents a user within the application. */
export interface Usuario {
  id: number;
  nome: string;
  email: string;
  perfil: string;
  ativo: boolean;
}
