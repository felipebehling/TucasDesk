import { useEffect } from 'react';
import { useAuth } from 'react-oidc-context';
import { useNavigate } from 'react-router-dom';
import LoadingOverlay from '../components/common/LoadingOverlay';

export default function Callback() {
  const auth = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    // O AuthProvider está a tratar do retorno de chamada de redirecionamento.
    // Apenas precisamos de esperar que o estado de autenticação seja resolvido.
    if (!auth.isLoading) {
      if (auth.isAuthenticated) {
        // Se autenticado, redireciona para a página inicial
        navigate('/');
      } else {
        // Se não estiver autenticado, pode ser um erro ou o utilizador cancelou.
        // Redireciona para iniciar o login novamente.
        auth.signinRedirect();
      }
    }
  }, [auth, navigate]);

  return <LoadingOverlay fullscreen message="Autenticando..." />;
}
