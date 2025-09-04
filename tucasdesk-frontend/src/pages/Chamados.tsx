import { useState } from "react";

// Definição da interface para o objeto de chamado.
interface Chamado {
  id: number;
  titulo: string;
  status: string;
  prioridade: string;
}

// Interface para as propriedades do componente.
interface ChamadosPageProps {
  onSelectChamado: (id: number) => void;
}

// Componente para a página de chamados.
export default function ChamadosPage({ onSelectChamado }: ChamadosPageProps) {
  // Estado para armazenar a lista de chamados.
  const [chamados] = useState<Chamado[]>([
    { id: 1, titulo: "Erro no sistema de login", status: "Aberto", prioridade: "Alta" },
    { id: 2, titulo: "Instalação de software", status: "Em Andamento", prioridade: "Média" },
    { id: 3, titulo: "Troca de monitor", status: "Pendente", prioridade: "Baixa" },
  ]);

  return (
    <>
      <div className="content-header">
        <h2>Chamados</h2>
      </div>
      <div className="card">
        <div className="card-header">
          <h3>Lista de Chamados</h3>
        </div>
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
                  <a href="#" className="table-link" onClick={() => onSelectChamado(c.id)}>
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
