import React, { useState } from "react";
import useCategorias from "../hooks/useCategorias";

/**
 * Renders the page for managing ticket categories.
 * It displays a list of existing categories and a form to add new ones.
 *
 * @returns {JSX.Element} The category management page component.
 */
export default function CategoriasPage() {
  const { categorias, isLoading, error, createCategoria, refetch } = useCategorias();
  const [novaCategoria, setNovaCategoria] = useState<string>("");
  const [feedback, setFeedback] = useState<string | null>(null);

  /**
   * Handles the form submission for adding a new category.
   *
   * @param {React.FormEvent} e - The form submission event.
   */
  const adicionarCategoria = async (e: React.FormEvent) => {
    e.preventDefault();
    setFeedback(null);
    if (!novaCategoria.trim()) {
      setFeedback("Informe um nome para a categoria.");
      return;
    }

    try {
      await createCategoria(novaCategoria.trim());
      setFeedback("Categoria criada com sucesso!");
      setNovaCategoria("");
    } catch {
      // O estado de erro global do hook já exibirá a mensagem apropriada.
    }
  };

  return (
    <>
      <div className="content-header">
        <h2>Categorias</h2>
      </div>
      <div className="card">
        <div className="card-header">
          <h3>Gerenciar Categorias</h3>
        </div>
        <form onSubmit={adicionarCategoria} style={{ marginBottom: "1rem" }}>
          <div style={{ display: "flex", gap: "10px" }}>
            <input
              type="text"
              className="form-input"
              placeholder="Nova categoria"
              value={novaCategoria}
              onChange={(e) => setNovaCategoria(e.target.value)}
              style={{ flex: 1 }}
              disabled={isLoading}
            />
            <button className="btn-primary" type="submit">
              {isLoading ? "Salvando..." : "Adicionar"}
            </button>
          </div>
        </form>
        {feedback && !error && (
          <div className="card-content">
            <p>{feedback}</p>
          </div>
        )}
        {error && (
          <div className="card-content">
            <p>Não foi possível carregar as categorias: {error}</p>
            <button className="btn-primary" type="button" onClick={() => void refetch()}>
              Tentar novamente
            </button>
          </div>
        )}
        {!error && (
          <table className="table-list">
            <thead>
              <tr>
                <th>ID</th>
                <th>Nome</th>
              </tr>
            </thead>
            <tbody>
              {isLoading && categorias.length === 0 ? (
                <tr>
                  <td colSpan={2}>Carregando categorias...</td>
                </tr>
              ) : categorias.length > 0 ? (
                categorias.map(cat => (
                  <tr key={cat.id}>
                    <td>{cat.id}</td>
                    <td>{cat.nome}</td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan={2}>Nenhuma categoria cadastrada.</td>
                </tr>
              )}
            </tbody>
          </table>
        )}
      </div>
    </>
  );
}
