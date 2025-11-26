import { useEffect } from 'react';
import { useAuth } from 'react-oidc-context';
import { useNavigate } from 'react-router-dom';
import LoadingOverlay from '../components/common/LoadingOverlay';

export default function Callback() {
  const auth = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    // Verifica se não estamos já no processo de redirecionamento
    if (!auth.isLoading && !auth.isAuthenticated) {
      // Tenta completar o login
      auth.signinRedirectCallback().then(() => {
        navigate('/');
      }).catch(() => {
        // Se falhar (ex: usuário voltou), inicia o login
        auth.signinRedirect();
      });
    } else if (auth.isAuthenticated) {
        navigate('/');
    } else {
        auth.signinRedirect();
    }
  }, [auth, navigate]);

  return <LoadingOverlay fullscreen message="Autenticando..." />;
}
