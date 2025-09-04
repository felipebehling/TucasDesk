import { useState } from "react";

// Definição da interface para o objeto de usuário.
interface Usuario {
  id: number;
  nome: string;
  email: string;
  perfil: string;
  ativo: boolean;
}

export default function UsuariosPage() {
  const [usuarios] = useState<Usuario[]>([
    { id: 1, nome: "Felipe Behling", email: "felipe@tucasdesk.com", perfil: "Administrador", ativo: true },
    { id: 2, nome: "Ana Souza", email: "ana@tucasdesk.com", perfil: "Técnico", ativo: true },
    { id: 3, nome: "Maria Oliveira", email: "maria@tucasdesk.com", perfil: "Usuário", ativo: false },
  ]);

  return (
    <>
      <div className="content-header">
        <h2>Usuários</h2>
      </div>
      <div className="card">
        <div className="card-header">
          <h3>Lista de Usuários</h3>
        </div>
        <table className="table-list">
          <thead>
            <tr>
              <th>ID</th>
              <th>Nome</th>
              <th>Email</th>
              <th>Perfil</th>
              <th>Ativo</th>
            </tr>
          </thead>
          <tbody>
            {usuarios.map(u => (
              <tr key={u.id}>
                <td>{u.id}</td>
                <td>{u.nome}</td>
                <td>{u.email}</td>
                <td>{u.perfil}</td>
                <td>{u.ativo ? "Sim" : "Não"}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
}
