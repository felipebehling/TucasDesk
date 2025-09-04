import { useState } from "react";

// Definição das interfaces para os objetos de chamado e interação.
interface Interacao {
  id: number;
  usuario: string;
  mensagem: string;
}

interface Chamado {
  id: number;
  titulo: string;
  descricao: string;
  status: string;
  prioridade: string;
  interacoes: Interacao[];
}

// Interface para as propriedades do componente.
interface ChamadoDetalhePageProps {
  chamadoId: number | null;
  onBack: () => void;
}

// Componente para a página de detalhes do chamado.
export default function ChamadoDetalhePage({ chamadoId, onBack }: ChamadoDetalhePageProps) {
  // Estado para armazenar os dados do chamado.
  const [chamado] = useState<Chamado>({
    id: 1,
    titulo: "Erro no sistema de login",
    descricao: "Não consigo acessar minha conta, aparece erro 500.",
    status: "Aberto",
    prioridade: "Alta",
    interacoes: [
      { id: 1, usuario: "Técnico Ana", mensagem: "Verifiquei os logs e o problema está no servidor de autenticação." },
      { id: 2, usuario: "Maria Oliveira", mensagem: "Obrigado pelo retorno, aguardo solução." },
    ]
  });

  if (!chamadoId) {
    return <div>Chamado não encontrado.</div>;
  }

  return (
    <>
      <div className="content-header">
        <h2>Chamado #{chamado.id}</h2>
      </div>
      <div className="card">
        <a href="#" className="text-link" onClick={onBack}>← Voltar para a lista</a>
        <div className="card-content">
          <h3 style={{ marginTop: "1rem" }}>{chamado.titulo}</h3>
          <p><strong>Descrição:</strong> {chamado.descricao}</p>
          <p><strong>Status:</strong> {chamado.status}</p>
          <p><strong>Prioridade:</strong> {chamado.prioridade}</p>
        </div>
      </div>
      <div className="card" style={{ marginTop: "20px" }}>
        <div className="card-header">
          <h3>Interações</h3>
        </div>
        <div className="card-content">
          {chamado.interacoes.map(i => (
            <div key={i.id} style={{ borderBottom: "1px solid var(--border-default)", paddingBottom: "10px", marginBottom: "10px" }}>
              <strong>{i.usuario}:</strong> {i.mensagem}
            </div>
          ))}
        </div>
      </div>
    </>
  );
}
