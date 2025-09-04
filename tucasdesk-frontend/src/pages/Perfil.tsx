//import React from "react";
export default function PerfilPage() {
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
