export interface UpdateUsuarioRequest {
  nome: string;
  email: string;
  senhaAtual?: string;
  novaSenha?: string;
  confirmacaoSenha?: string;
}
