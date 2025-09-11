import { useState } from "react";

/**
 * Defines the shape of a user object.
 */
interface Usuario {
  /** The unique identifier for the user. */
  id: number;
  /** The name of the user. */
  nome: string;
  /** The email address of the user. */
  email: string;
  /** The profile or role of the user (e.g., "Administrador"). */
  perfil: string;
  /** A flag indicating if the user account is active. */
  ativo: boolean;
}

/**
 * Renders the page for managing users.
 * It displays a list of all users in a table.
 *
 * @returns {JSX.Element} The user management page component.
 */
export default function UsuariosPage() {
  // TODO: Replace with actual data fetching from the API.
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
