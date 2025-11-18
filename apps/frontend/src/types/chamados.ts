/** Represents a generic lookup entity with an ID and a name. */
export interface Lookup {
  id: number;
  nome: string;
}

/** Represents a summary of a user, with essential information. */
export interface UsuarioResumo {
  id: number;
  nome: string;
  email: string;
}

/** Represents an interaction on a ticket as returned by the API. */
export interface InteracaoResponse {
  id: number;
  mensagem: string;
  anexoUrl: string | null;
  dataInteracao: string;
  usuario: UsuarioResumo | null;
}

/** Defines the payload for creating a new interaction. */
export interface CreateInteracaoPayload {
  usuarioId: number;
  mensagem: string;
  anexoUrl?: string | null;
}

/** Represents a ticket as returned by the API. */
export interface ChamadoResponse {
  id: number;
  titulo: string;
  descricao: string;
  categoria: Lookup | null;
  status: Lookup | null;
  prioridade: Lookup | null;
  solicitante: UsuarioResumo | null;
  tecnico: UsuarioResumo | null;
  dataAbertura: string;
  dataFechamento: string | null;
  interacoes: InteracaoResponse[];
}

/** Defines the payload for creating a new ticket. */
export interface CreateChamadoPayload {
  titulo: string;
  descricao?: string;
  categoriaId?: number;
  prioridadeId?: number;
  statusId: number;
  usuarioId: number;
  tecnicoId?: number;
}

/** Defines the payload for updating an existing ticket. */
export interface UpdateChamadoPayload {
  titulo?: string;
  descricao?: string;
  categoriaId?: number;
  prioridadeId?: number;
  statusId?: number;
  tecnicoId?: number;
}
