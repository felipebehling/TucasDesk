import { useMemo } from "react";
import useChamados from "../hooks/useChamados";

function formatStatus(label: string | undefined | null): string {
  return label ?? "—";
}

/**
 * Renders the main dashboard page.
 * This page displays a summary of recent tickets and quick statistics.
 *
 * @returns {JSX.Element} The dashboard page component.
 */
export default function DashboardPage() {
  const { chamados, isLoading, error } = useChamados();
  const chamadosRecentes = useMemo(() => chamados.slice(0, 5), [chamados]);
  const totalAbertos = useMemo(
    () => chamados.filter(c => c.status?.nome?.toLowerCase() === "aberto").length,
    [chamados],
  );
  const totalEmAndamento = useMemo(
    () => chamados.filter(c => c.status?.nome?.toLowerCase() === "em andamento").length,
    [chamados],
  );

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
          <div className="card-content">
            {isLoading ? (
              <p>Carregando chamados recentes...</p>
            ) : error ? (
              <p>Não foi possível carregar os chamados: {error}</p>
            ) : chamadosRecentes.length > 0 ? (
              <ul>
                {chamadosRecentes.map(c => (
                  <li key={c.id}>
                    <span>{c.titulo}</span> - <span>{formatStatus(c.status?.nome)}</span>
                  </li>
                ))}
              </ul>
            ) : (
              <p>Nenhum chamado recente encontrado.</p>
            )}
          </div>
        </div>
        <div className="card">
          <div className="card-header">
            <h3>Estatísticas Rápidas</h3>
          </div>
          <div className="card-content">
            {isLoading ? (
              <p>Carregando estatísticas...</p>
            ) : error ? (
              <p>Não foi possível carregar as estatísticas.</p>
            ) : (
              <>
                <p>Total de chamados abertos: {totalAbertos}</p>
                <p>Total de chamados em andamento: {totalEmAndamento}</p>
              </>
            )}
          </div>
        </div>
      </div>
    </>
  );
}
