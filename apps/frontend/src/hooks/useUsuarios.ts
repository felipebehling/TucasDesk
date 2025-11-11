import { useCallback, useEffect, useState } from "react";
import UsuariosService from "../services/usuariosService";
import type { Usuario } from "../types/usuarios";
import { extractErrorMessage } from "../utils/error";

export interface UseUsuariosResult {
  usuarios: Usuario[];
  isLoading: boolean;
  error: string | null;
  refetch: () => Promise<void>;
}

export function useUsuarios(autoFetch: boolean = true): UseUsuariosResult {
  const [usuarios, setUsuarios] = useState<Usuario[]>([]);
  const [isLoading, setIsLoading] = useState<boolean>(false);
  const [error, setError] = useState<string | null>(null);

  const loadUsuarios = useCallback(async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await UsuariosService.listar();
      setUsuarios(data);
    } catch (err) {
      setError(extractErrorMessage(err));
    } finally {
      setIsLoading(false);
    }
  }, []);

  useEffect(() => {
    if (autoFetch) {
      void loadUsuarios();
    }
  }, [autoFetch, loadUsuarios]);

  return {
    usuarios,
    isLoading,
    error,
    refetch: loadUsuarios,
  };
}

export default useUsuarios;
