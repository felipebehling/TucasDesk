import { useEffect, useState } from "react";
import api from "../../api/api";

interface Chamado {
  id_chamado: number;
  titulo: string;
  descricao: string;
  categoria_nome: string;
  status_nome: string;
  prioridade_nome: string;
  usuario_nome: string;
  tecnico_nome?: string;
  data_abertura: string;
  data_fechamento?: string;
}

const ChamadosPage: React.FC = () => {
  const [chamados, setChamados] = useState<Chamado[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  // Carregar chamados ao montar o componente
  useEffect(() => {
    fetchChamados();
  }, []);

  const fetchChamados = async () => {
    try {
      const response = await api.get<Chamado[]>("/chamados");
      setChamados(response.data);
    } catch (error) {
      console.error("Erro ao carregar chamados:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-4">
      <h3>Chamados</h3>

      {loading ? (
        <p>Carregando chamados...</p>
      ) : (
        <table className="table table-striped">
          <thead>
            <tr>
              <th>ID</th>
              <th>Título</th>
              <th>Descrição</th>
              <th>Categoria</th>
              <th>Status</th>
              <th>Prioridade</th>
              <th>Usuário</th>
              <th>Técnico</th>
              <th>Abertura</th>
              <th>Fechamento</th>
            </tr>
          </thead>
          <tbody>
            {chamados.map((c) => (
              <tr key={c.id_chamado}>
                <td>{c.id_chamado}</td>
                <td>{c.titulo}</td>
                <td>{c.descricao}</td>
                <td>{c.categoria_nome}</td>
                <td>{c.status_nome}</td>
                <td>{c.prioridade_nome}</td>
                <td>{c.usuario_nome}</td>
                <td>{c.tecnico_nome || "-"}</td>
                <td>{new Date(c.data_abertura).toLocaleString()}</td>
                <td>{c.data_fechamento ? new Date(c.data_fechamento).toLocaleString() : "-"}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default ChamadosPage;
