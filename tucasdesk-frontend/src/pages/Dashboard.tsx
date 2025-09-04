import { useState } from "react";

// Definição da interface para o objeto de chamado.
interface Chamado {
  id: number;
  titulo: string;
  status: string;
  prioridade: string;
}

export default function DashboardPage() {
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
