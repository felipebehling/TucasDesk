// src/pages/ChamadoDetalhe.tsx

import { useState } from "react";
import { useParams, useNavigate } from "react-router-dom"; // Importe os hooks

interface Interacao { id: number; usuario: string; mensagem: string; }
interface Chamado { id: number; titulo: string; descricao: string; status: string; prioridade: string; interacoes: Interacao[]; }

export default function ChamadoDetalhePage() {
  const { id } = useParams(); // Pega o ID da URL, ex: /chamados/1 -> id = "1"
  const navigate = useNavigate(); // Hook para navegação

  const [chamado] = useState<Chamado>({ /* Seus dados mockados */
    id: 1,
    titulo: "Erro no sistema de login",
    descricao: "Não consigo acessar minha conta, aparece erro 500.",
    status: "Aberto",
    prioridade: "Alta",
    interacoes: [
      { id: 1, usuario: "Técnico Ana", mensagem: "Verifiquei os logs e o problema está no servidor de autenticação." },
      { id: 2, usuario: "Maria Oliveira", mensagem: "Obrigado pelo retorno, aguardo solução." },
    ]
  });

  if (!id) {
    return <div>Chamado não encontrado.</div>;
  }

  return (
    <>
      <div className="content-header">
        <h2>Chamado #{id}</h2> {/* Usa o ID da URL */}
      </div>
      <div className="card">
        {/* O botão de voltar agora usa o navigate */}
        <a href="#" className="text-link" onClick={() => navigate("/chamados")}>← Voltar para a lista</a>
        <div className="card-content">
          <h3 style={{ marginTop: "1rem" }}>{chamado.titulo}</h3>
          <p><strong>Descrição:</strong> {chamado.descricao}</p>
          <p><strong>Status:</strong> {chamado.status}</p>
          <p><strong>Prioridade:</strong> {chamado.prioridade}</p>
        </div>
      </div>
      {/* O resto do componente permanece o mesmo */}
    </>
  );
}