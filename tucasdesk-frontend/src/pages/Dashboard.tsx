import { useState } from "react";

/**
 * Defines the shape of a support ticket object.
 */
interface Chamado {
  /** The unique identifier for the ticket. */
  id: number;
  /** The title of the ticket. */
  titulo: string;
  /** The current status of the ticket (e.g., "Aberto", "Em Andamento"). */
  status: string;
  /** The priority level of the ticket (e.g., "Alta", "Média"). */
  prioridade: string;
}

/**
 * Renders the main dashboard page.
 * This page displays a summary of recent tickets and quick statistics.
 *
 * @returns {JSX.Element} The dashboard page component.
 */
export default function DashboardPage() {
  // TODO: Replace with actual data fetching from the API.
  const [chamados] = useState<Chamado[]>([
    { id: 1, titulo: "Erro no sistema de login", status: "Aberto", prioridade: "Alta" },
    { id: 2, titulo: "Instalação de software", status: "Em Andamento", prioridade: "Média" },
  ]);

  return (
    <>
      <div className="content-header">
        <h2>Dashboard</h2>
      </div>
      <div className="grid-2-col">
        <div className="card">
          <div className="card-header">
            <h3>Meus Chamados Recentes</h3>
          </div>
          <ul className="card-content">
            {chamados.map(c => (
              <li key={c.id}>
                <a href="#" className="table-link" onClick={() => console.log(`Ir para Chamado ${c.id}`)}>
                  {c.titulo} - {c.status}
                </a>
              </li>
            ))}
          </ul>
        </div>
        <div className="card">
          <div className="card-header">
            <h3>Estatísticas Rápidas</h3>
          </div>
          <div className="card-content">
            <p>Total de chamados abertos: {chamados.filter(c => c.status === "Aberto").length}</p>
            <p>Total de chamados em andamento: {chamados.filter(c => c.status === "Em Andamento").length}</p>
          </div>
        </div>
      </div>
    </>
  );
}
