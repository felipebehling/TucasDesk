import useUsuarios from "../hooks/useUsuarios";

/**
 * Renders the page for managing users.
 * It displays a list of all users in a table.
 *
 * @returns {JSX.Element} The user management page component.
 */
export default function UsuariosPage() {
  const { usuarios, isLoading, error, refetch } = useUsuarios();

  return (
    <>
      <div className="content-header">
        <h2>Usuários</h2>
      </div>
      <div className="card">
        <div className="card-header">
          <h3>Lista de Usuários</h3>
        </div>
        {error ? (
          <div className="card-content">
            <p>Não foi possível carregar os usuários: {error}</p>
            <button className="btn-primary" type="button" onClick={() => void refetch()}>
              Tentar novamente
            </button>
          </div>
        ) : (
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
              {isLoading && usuarios.length === 0 ? (
                <tr>
                  <td colSpan={5}>Carregando usuários...</td>
                </tr>
              ) : usuarios.length > 0 ? (
                usuarios.map(u => (
                  <tr key={u.id}>
                    <td>{u.id}</td>
                    <td>{u.nome}</td>
                    <td>{u.email}</td>
                    <td>{u.perfil}</td>
                    <td>{u.ativo ? "Sim" : "Não"}</td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan={5}>Nenhum usuário encontrado.</td>
                </tr>
              )}
            </tbody>
          </table>
        )}
      </div>
    </>
  );
}
