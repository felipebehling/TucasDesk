export interface Perfil {
  idPerfil: number;
  nome: string;
}

export interface UsuarioResponse {
  idUsuario: number;
  nome: string;
  email: string;
  perfil: Perfil | null;
  dataCriacao: string;
  ativo: boolean;
}

export interface Usuario {
  id: number;
  nome: string;
  email: string;
  perfil: string;
  ativo: boolean;
}
