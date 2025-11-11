import { useCallback, useEffect, useState } from "react";
import ChamadosService from "../services/chamadosService";
import type {
  ChamadoResponse,
  CreateChamadoPayload,
  UpdateChamadoPayload,
} from "../types/chamados";
import { extractErrorMessage } from "../utils/error";

export interface UseChamadosResult {
  chamados: ChamadoResponse[];
  isLoading: boolean;
  error: string | null;
  refetch: () => Promise<void>;
  createChamado: (payload: CreateChamadoPayload) => Promise<ChamadoResponse>;
  updateChamado: (id: number, payload: UpdateChamadoPayload) => Promise<ChamadoResponse>;
}

export function useChamados(autoFetch: boolean = true): UseChamadosResult {
  const [chamados, setChamados] = useState<ChamadoResponse[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  const loadChamados = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await ChamadosService.listar();
      setChamados(data);
    } catch (err) {
      setError(extractErrorMessage(err));
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    if (autoFetch) {
      void loadChamados();
    }
  }, [autoFetch, loadChamados]);

  const createChamado = useCallback(
    async (payload: CreateChamadoPayload) => {
      setIsLoading(true);
      setError(null);
      try {
        const response = await ChamadosService.criar(payload);
        await loadChamados();
        return response;
      } catch (err) {
        setError(extractErrorMessage(err));
        throw err;
      } finally {
        setIsLoading(false);
      }
    },
    [loadChamados],
  );

  const updateChamado = useCallback(
    async (id: number, payload: UpdateChamadoPayload) => {
      setIsLoading(true);
      setError(null);
      try {
        const response = await ChamadosService.atualizar(id, payload);
        await loadChamados();
        return response;
      } catch (err) {
        setError(extractErrorMessage(err));
        throw err;
      } finally {
        setIsLoading(false);
      }
    },
    [loadChamados],
  );

  return {
    chamados,
    isLoading,
    error,
    refetch: loadChamados,
    createChamado,
    updateChamado,
  };
}

export default useChamados;
