import { useEffect, useState } from "react";
import api from "../../api/api";

interface Usuario {
  id_usuario: number;
  nome: string;
  email: string;
  perfil_id: number;
  ativo: boolean;
  data_criacao: string;
}

const UsuarioPage: React.FC = () => {
  const [usuarios, setUsuarios] = useState<Usuario[]>([]);
  const [loading, setLoading] = useState<boolean>(true);

  // Carregar usuários ao montar o componente
  useEffect(() => {
    fetchUsuarios();
  }, []);

  const fetchUsuarios = async () => {
    try {
      const response = await api.get<Usuario[]>("/usuarios");
      setUsuarios(response.data);
    } catch (error) {
      console.error("Erro ao carregar usuários:", error);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="container mt-4">
      <h3>Usuários</h3>
      {loading ? (
        <p>Carregando...</p>
      ) : (
        <table className="table table-striped mt-3">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nome</th>
              <th>Email</th>
              <th>Perfil</th>
              <th>Ativo</th>
              <th>Data de Criação</th>
            </tr>
          </thead>
          <tbody>
            {usuarios.map((usuario) => (
              <tr key={usuario.id_usuario}>
                <td>{usuario.id_usuario}</td>
                <td>{usuario.nome}</td>
                <td>{usuario.email}</td>
                <td>{usuario.perfil_id}</td>
                <td>{usuario.ativo ? "Sim" : "Não"}</td>
                <td>{new Date(usuario.data_criacao).toLocaleString()}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default UsuarioPage;
