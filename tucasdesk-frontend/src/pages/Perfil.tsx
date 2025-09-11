/**
 * Renders the user's profile page.
 * This page displays the current user's information and provides an option to update it.
 *
 * @returns {JSX.Element} The user profile page component.
 */
export default function PerfilPage() {
  // TODO: Fetch and display the actual data for the logged-in user.
  return (
    <>
      <div className="content-header">
        <h2>Meu Perfil</h2>
      </div>
      <div className="card">
        <div className="card-header">
          <h3>Dados do Usuário</h3>
        </div>
        <div className="card-content">
          <p><strong>Nome:</strong> Usuário de Exemplo</p>
          <p><strong>Email:</strong> exemplo@tucasdesk.com</p>
          <button className="btn-primary" style={{marginTop: "1rem"}}>Atualizar Dados</button>
        </div>
      </div>
    </>
  );
}
