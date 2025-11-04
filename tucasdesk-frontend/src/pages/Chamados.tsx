// src/pages/Chamados.tsx

import { useMemo } from "react";
import { useNavigate } from "react-router-dom";
import useChamados from "../hooks/useChamados";

/**
 * Formats a table-friendly label for a lookup entity.
 *
 * @param label The lookup name returned by the API.
 * @returns The label or a placeholder when the lookup is missing.
 */
function formatLookup(label: string | undefined | null): string {
  return label ?? "—";
}

/**
 * Renders the page that lists all support tickets.
 * It displays the tickets in a table and allows navigation to the detail view of each ticket.
 *
 * @returns {JSX.Element} The tickets list page component.
 */
export default function ChamadosPage() {
  const navigate = useNavigate();
  const { chamados, isLoading, error, refetch } = useChamados();
  const hasChamados = useMemo(() => chamados.length > 0, [chamados]);

  return (
    <>
      <div className="content-header"><h2>Chamados</h2></div>
      <div className="card">
        <div className="card-header"><h3>Lista de Chamados</h3></div>
        {isLoading ? (
          <div className="card-content">
            <p>Carregando chamados...</p>
          </div>
        ) : error ? (
          <div className="card-content">
            <p>Não foi possível carregar os chamados: {error}</p>
            <button className="btn-primary" type="button" onClick={() => void refetch()}>
              Tentar novamente
            </button>
          </div>
        ) : hasChamados ? (
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
                    <a href="#" className="table-link" onClick={() => navigate(`/chamados/${c.id}`)}>
                      {c.titulo}
                    </a>
                  </td>
                  <td>{formatLookup(c.status?.nome)}</td>
                  <td>{formatLookup(c.prioridade?.nome)}</td>
                </tr>
              ))}
            </tbody>
          </table>
        ) : (
          <div className="card-content">
            <p>Nenhum chamado encontrado.</p>
          </div>
        )}
      </div>
    </>
  );
}