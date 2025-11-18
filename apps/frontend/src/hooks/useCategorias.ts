import { useCallback, useEffect, useState } from "react";
import CategoriasService from "../services/categoriasService";
import type { Categoria } from "../types/categorias";
import { extractErrorMessage } from "../utils/error";

export interface UseCategoriasResult {
  categorias: Categoria[];
  isLoading: boolean;
  error: string | null;
  refetch: () => Promise<void>;
  createCategoria: (nome: string) => Promise<Categoria>;
}

/**
 * Custom hook for managing categories.
 *
 * @param {boolean} [autoFetch=true] - Whether to fetch categories automatically on mount.
 * @returns {UseCategoriasResult} The state and functions for managing categories.
 */
export function useCategorias(autoFetch: boolean = true): UseCategoriasResult {
  const [categorias, setCategorias] = useState<Categoria[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  const loadCategorias = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await CategoriasService.listar();
      setCategorias(data);
    } catch (err) {
      setError(extractErrorMessage(err));
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    if (autoFetch) {
      void loadCategorias();
    }
  }, [autoFetch, loadCategorias]);

  const createCategoria = useCallback(
    async (nome: string) => {
      setIsLoading(true);
      setError(null);
      try {
        const novaCategoria = await CategoriasService.criar({ nome });
        await loadCategorias();
        return novaCategoria;
      } catch (err) {
        setError(extractErrorMessage(err));
        throw err;
      } finally {
        setIsLoading(false);
      }
    },
    [loadCategorias],
  );

  return {
    categorias,
    isLoading,
    error,
    refetch: loadCategorias,
    createCategoria,
  };
}

export default useCategorias;
