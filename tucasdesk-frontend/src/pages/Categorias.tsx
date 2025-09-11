import React, { useState } from "react";

/**
 * Defines the shape of a category object.
 */
interface Categoria {
  /** The unique identifier for the category. */
  id: number;
  /** The name of the category. */
  nome: string;
}

/**
 * Renders the page for managing ticket categories.
 * It displays a list of existing categories and a form to add new ones.
 *
 * @returns {JSX.Element} The category management page component.
 */
export default function CategoriasPage() {
  // TODO: Replace with actual data fetching from the API.
  const [categorias] = useState<Categoria[]>([
    { id: 1, nome: "Infraestrutura" },
    { id: 2, nome: "Sistema" },
    { id: 3, nome: "Rede" },
  ]);
  const [novaCategoria, setNovaCategoria] = useState<string>("");

  /**
   * Handles the form submission for adding a new category.
   *
   * @param {React.FormEvent} e - The form submission event.
   */
  const adicionarCategoria = (e: React.FormEvent) => {
    e.preventDefault();
    if (!novaCategoria) return;
    // TODO: Implement the API call to add the new category.
    console.log("Adicionar categoria:", novaCategoria);
    setNovaCategoria("");
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
            />
            <button className="btn-primary" type="submit">
              Adicionar
            </button>
          </div>
        </form>
        <table className="table-list">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nome</th>
            </tr>
          </thead>
          <tbody>
            {categorias.map(cat => (
              <tr key={cat.id}>
                <td>{cat.id}</td>
                <td>{cat.nome}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
}
