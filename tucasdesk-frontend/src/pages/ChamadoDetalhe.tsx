// src/pages/ChamadoDetalhe.tsx

import { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import ChamadosService from "../services/chamadosService";
import type { ChamadoResponse } from "../types/chamados";
import { extractErrorMessage } from "../utils/error";

/**
 * Renders the detail page for a single support ticket.
 * It fetches the ticket ID from the URL and displays its details and interactions.
 *
 * @returns {JSX.Element} The ticket detail page component.
 */
export default function ChamadoDetalhePage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [chamado, setChamado] = useState<ChamadoResponse | null>(null);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  useEffect(() => {
    if (!id) {
      return;
    }

    const chamadoId = Number(id);
    if (Number.isNaN(chamadoId)) {
      setError("Identificador de chamado inválido.");
      return;
    }

    const fetchChamado = async () => {
      setIsLoading(true);
      setError(null);
      try {
        const response = await ChamadosService.buscarPorId(chamadoId);
        setChamado(response);
      } catch (err) {
        setError(extractErrorMessage(err));
      } finally {
        setIsLoading(false);
      }
    };

    void fetchChamado();
  }, [id]);

  return (
    <>
      <div className="content-header">
        <h2>Chamado #{id}</h2> {/* Usa o ID da URL */}
      </div>
      <div className="card">
        {/* O botão de voltar agora usa o navigate */}
        <a href="#" className="text-link" onClick={() => navigate("/chamados")}>← Voltar para a lista</a>
        <div className="card-content">
          {isLoading ? (
            <p>Carregando detalhes do chamado...</p>
          ) : error ? (
            <p>Não foi possível carregar o chamado: {error}</p>
          ) : chamado ? (
            <>
              <h3 style={{ marginTop: "1rem" }}>{chamado.titulo}</h3>
              <p>
                <strong>Descrição:</strong> {chamado.descricao}
              </p>
              <p>
                <strong>Status:</strong> {chamado.status?.nome ?? "—"}
              </p>
              <p>
                <strong>Prioridade:</strong> {chamado.prioridade?.nome ?? "—"}
              </p>
            </>
          ) : (
            <p>Chamado não encontrado.</p>
          )}
        </div>
      </div>
      {/* O resto do componente permanece o mesmo */}
    </>
  );
}