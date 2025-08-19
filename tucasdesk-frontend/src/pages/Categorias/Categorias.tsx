import { useEffect, useState } from "react";
import api from "../../api/api";

interface Categoria {
  id_categoria: number;
  nome: string;
}

const CategoriasPage: React.FC = () => {
  const [categorias, setCategorias] = useState<Categoria[]>([]);
  const [novoNome, setNovoNome] = useState<string>("");
  const [loading, setLoading] = useState<boolean>(true);

  // Carregar categorias ao montar o componente
  useEffect(() => {
    fetchCategorias();
  }, []);

  const fetchCategorias = async () => {
    try {
      const response = await api.get<Categoria[]>("/categorias");
      setCategorias(response.data);
    } catch (error) {
      console.error("Erro ao carregar categorias:", error);
    } finally {
      setLoading(false);
    }
  };

  const adicionarCategoria = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!novoNome) return;

    try {
      await api.post("/categorias", { nome: novoNome });
      setNovoNome("");
      fetchCategorias(); // Atualiza a lista
    } catch (error) {
      console.error("Erro ao adicionar categoria:", error);
    }
  };

  return (
    <div className="container mt-4">
      <h3>Categorias</h3>

      <form onSubmit={adicionarCategoria} className="mb-3">
        <div className="input-group">
          <input
            type="text"
            className="form-control"
            placeholder="Nova categoria"
            value={novoNome}
            onChange={(e) => setNovoNome(e.target.value)}
          />
          <button className="btn btn-success" type="submit">
            Adicionar
          </button>
        </div>
      </form>

      {loading ? (
        <p>Carregando categorias...</p>
      ) : (
        <table className="table table-striped">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nome</th>
            </tr>
          </thead>
          <tbody>
            {categorias.map((categoria) => (
              <tr key={categoria.id_categoria}>
                <td>{categoria.id_categoria}</td>
                <td>{categoria.nome}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
};

export default CategoriasPage;
