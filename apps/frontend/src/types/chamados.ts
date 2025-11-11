export interface Lookup {
  id: number;
  nome: string;
}

export interface UsuarioResumo {
  id: number;
  nome: string;
  email: string;
}

export interface InteracaoResponse {
  id: number;
  mensagem: string;
  anexoUrl: string | null;
  dataInteracao: string;
  usuario: UsuarioResumo | null;
}

export interface CreateInteracaoPayload {
  usuarioId: number;
  mensagem: string;
  anexoUrl?: string | null;
}

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

export interface CreateChamadoPayload {
  titulo: string;
  descricao?: string;
  categoriaId?: number;
  prioridadeId?: number;
  statusId: number;
  usuarioId: number;
  tecnicoId?: number;
}

export interface UpdateChamadoPayload {
  titulo?: string;
  descricao?: string;
  categoriaId?: number;
  prioridadeId?: number;
  statusId?: number;
  tecnicoId?: number;
}
