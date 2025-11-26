import { useEffect } from 'react';
import { useAuth } from 'react-oidc-context';
import { useNavigate } from 'react-router-dom';
import LoadingOverlay from '../components/common/LoadingOverlay';

export default function Callback() {
  const auth = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!auth.isLoading && auth.isAuthenticated) {
      navigate('/');
    }
    // Optional: handle cases where the user lands here without being authenticated
    // a signinRedirect could be initiated, or a redirect to an error page.
    // For now, we'll rely on the AuthProvider to handle the redirect.
  }, [auth.isLoading, auth.isAuthenticated, navigate]);

  return <LoadingOverlay fullscreen message="Autenticando..." />;
}
