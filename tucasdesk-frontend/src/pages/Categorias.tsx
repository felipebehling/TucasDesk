import React, { useState } from "react";

// Definição da interface para o objeto de categoria.
interface Categoria {
  id: number;
  nome: string;
}

export default function CategoriasPage() {
  const [categorias] = useState<Categoria[]>([
    { id: 1, nome: "Infraestrutura" },
    { id: 2, nome: "Sistema" },
    { id: 3, nome: "Rede" },
  ]);
  const [novaCategoria, setNovaCategoria] = useState<string>("");

  const adicionarCategoria = (e: React.FormEvent) => {
    e.preventDefault();
    if (!novaCategoria) return;
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
