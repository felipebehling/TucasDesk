// src/pages/Chamados.tsx

import { useState } from "react";
import { useNavigate } from "react-router-dom";

/**
 * Defines the shape of a support ticket object.
 */
interface Chamado {
  /** The unique identifier for the ticket. */
  id: number;
  /** The title of the ticket. */
  titulo: string;
  /** The current status of the ticket. */
  status: string;
  /** The priority level of the ticket. */
  prioridade: string;
}

/**
 * Renders the page that lists all support tickets.
 * It displays the tickets in a table and allows navigation to the detail view of each ticket.
 *
 * @returns {JSX.Element} The tickets list page component.
 */
export default function ChamadosPage() {
  const navigate = useNavigate();
  // TODO: Replace with actual data fetching from the API.
  const [chamados] = useState<Chamado[]>([
    { id: 1, titulo: "Erro no sistema de login", status: "Aberto", prioridade: "Alta" },
    { id: 2, titulo: "Instalação de software", status: "Em Andamento", prioridade: "Média" },
    { id: 3, titulo: "Troca de monitor", status: "Pendente", prioridade: "Baixa" },
  ]);

  return (
    <>
      <div className="content-header"><h2>Chamados</h2></div>
      <div className="card">
        <div className="card-header"><h3>Lista de Chamados</h3></div>
        <table className="table-list">
          <thead>
            <tr>
              <th>ID</th>
              <th>Título</th>
              <th>Status</th>
              <th>Prioridade</th>
            </tr>
          </thead>
          <tbody>
            {chamados.map(c => (
              <tr key={c.id}>
                <td>{c.id}</td>
                <td>
                  {/* Ao clicar, navega para a rota de detalhes do chamado */}
                  <a href="#" className="table-link" onClick={() => navigate(`/chamados/${c.id}`)}>
                    {c.titulo}
                  </a>
                </td>
                <td>{c.status}</td>
                <td>{c.prioridade}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
}