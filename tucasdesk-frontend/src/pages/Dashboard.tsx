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
  const totalFechados = useMemo(
    () =>
      chamados.filter((c) => {
        const statusLower = c.status?.nome?.toLowerCase() ?? "";
        return statusLower.includes("fechado") || statusLower.includes("encerrado") || c.dataFechamento !== null;
      }).length,
    [chamados],
  );
  const totalChamados = chamados.length;

  const statusResumo = useMemo(() => {
    const mapa = new Map<string, number>();
    chamados.forEach((c) => {
      const label = formatStatus(c.status?.nome);
      mapa.set(label, (mapa.get(label) ?? 0) + 1);
    });
    return Array.from(mapa.entries())
      .map(([label, total]) => ({ label, total }))
      .sort((a, b) => b.total - a.total || a.label.localeCompare(b.label));
  }, [chamados]);

  const prioridadeResumo = useMemo(() => {
    const mapa = new Map<string, number>();
    chamados.forEach((c) => {
      const label = c.prioridade?.nome ?? "Não definida";
      mapa.set(label, (mapa.get(label) ?? 0) + 1);
    });
    return Array.from(mapa.entries())
      .map(([label, total]) => ({ label, total }))
      .sort((a, b) => b.total - a.total || a.label.localeCompare(b.label));
  }, [chamados]);

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
                <p>Total de chamados: {totalChamados}</p>
                <p>Total de chamados abertos: {totalAbertos}</p>
                <p>Total de chamados em andamento: {totalEmAndamento}</p>
                <p>Total de chamados encerrados: {totalFechados}</p>
              </>
            )}
          </div>
        </div>
      </div>
      <div className="grid-2-col" style={{ marginTop: "1rem" }}>
        <div className="card">
          <div className="card-header">
            <h3>Resumo por Status</h3>
          </div>
          <div className="card-content">
            {isLoading ? (
              <p>Carregando resumo por status...</p>
            ) : error ? (
              <p>Não foi possível carregar o resumo por status.</p>
            ) : statusResumo.length > 0 ? (
              <ul>
                {statusResumo.map((item) => (
                  <li key={item.label}>
                    {item.label}: {item.total}
                  </li>
                ))}
              </ul>
            ) : (
              <p>Nenhum dado de status disponível.</p>
            )}
          </div>
        </div>
        <div className="card">
          <div className="card-header">
            <h3>Resumo por Prioridade</h3>
          </div>
          <div className="card-content">
            {isLoading ? (
              <p>Carregando resumo por prioridade...</p>
            ) : error ? (
              <p>Não foi possível carregar o resumo por prioridade.</p>
            ) : prioridadeResumo.length > 0 ? (
              <ul>
                {prioridadeResumo.map((item) => (
                  <li key={item.label}>
                    {item.label}: {item.total}
                  </li>
                ))}
              </ul>
            ) : (
              <p>Nenhum dado de prioridade disponível.</p>
            )}
          </div>
        </div>
      </div>
    </>
  );
}
