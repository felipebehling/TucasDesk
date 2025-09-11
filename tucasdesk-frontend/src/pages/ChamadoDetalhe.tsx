// src/pages/ChamadoDetalhe.tsx

import { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";

/**
 * Defines the shape of an interaction object on a ticket.
 */
interface Interacao {
  /** The unique identifier for the interaction. */
  id: number;
  /** The name of the user who made the interaction. */
  usuario: string;
  /** The content of the interaction message. */
  mensagem: string;
}

/**
 * Defines the shape of a detailed support ticket object.
 */
interface Chamado {
  /** The unique identifier for the ticket. */
  id: number;
  /** The title of the ticket. */
  titulo: string;
  /** A detailed description of the issue. */
  descricao: string;
  /** The current status of the ticket. */
  status: string;
  /** The priority level of the ticket. */
  prioridade: string;
  /** A list of interactions associated with the ticket. */
  interacoes: Interacao[];
}

/**
 * Renders the detail page for a single support ticket.
 * It fetches the ticket ID from the URL and displays its details and interactions.
 *
 * @returns {JSX.Element} The ticket detail page component.
 */
export default function ChamadoDetalhePage() {
  const { id } = useParams();
  const navigate = useNavigate();

  // TODO: Replace with actual data fetching from the API using the `id` from `useParams`.
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

  if (!id) {
    return <div>Chamado não encontrado.</div>;
  }

  return (
    <>
      <div className="content-header">
        <h2>Chamado #{id}</h2> {/* Usa o ID da URL */}
      </div>
      <div className="card">
        {/* O botão de voltar agora usa o navigate */}
        <a href="#" className="text-link" onClick={() => navigate("/chamados")}>← Voltar para a lista</a>
        <div className="card-content">
          <h3 style={{ marginTop: "1rem" }}>{chamado.titulo}</h3>
          <p><strong>Descrição:</strong> {chamado.descricao}</p>
          <p><strong>Status:</strong> {chamado.status}</p>
          <p><strong>Prioridade:</strong> {chamado.prioridade}</p>
        </div>
      </div>
      {/* O resto do componente permanece o mesmo */}
    </>
  );
}