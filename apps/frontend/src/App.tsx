import { lazy, Suspense } from 'react';
import { BrowserRouter, Routes, Route, useNavigate } from 'react-router-dom';
import { useAuth } from 'react-oidc-context';
import AppLayout from './components/layout/AppLayout';
import LoadingOverlay from './components/common/LoadingOverlay';

const DashboardPage = lazy(() => import('./pages/Dashboard'));
const ChamadosPage = lazy(() => import('./pages/Chamados'));
const ChamadoDetalhePage = lazy(() => import('./pages/ChamadoDetalhe'));
const PerfilPage = lazy(() => import('./pages/Perfil'));
const UsuariosPage = lazy(() => import('./pages/Usuarios'));
const CategoriasPage = lazy(() => import('./pages/Categorias'));
const CallbackPage = lazy(() => import('./pages/Callback'));

function AuthWrapper() {
  const auth = useAuth();
  const navigate = useNavigate();

  if (auth.isLoading) {
    return <LoadingOverlay fullscreen message="Carregando autenticação..." />;
  }

  if (auth.error) {
    return <div>Oops... {auth.error.message}</div>;
  }

  if (!auth.isAuthenticated) {
    // Redireciona para o callback que vai iniciar o login
     navigate('/callback');
     return null;
  }

  return (
    <Suspense fallback={<LoadingOverlay fullscreen message="Carregando página..." />}>
      <Routes>
        <Route element={<AppLayout handleLogout={() => auth.signoutRedirect()} />}>
          <Route path="/" element={<DashboardPage />} />
          <Route path="/chamados" element={<ChamadosPage />} />
          <Route path="/chamados/:id" element={<ChamadoDetalhePage />} />
          <Route path="/perfil" element={<PerfilPage />} />
          <Route path="/usuarios" element={<UsuariosPage />} />
          <Route path="/categorias" element={<CategoriasPage />} />
        </Route>
      </Routes>
    </Suspense>
  );
}

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/callback" element={<CallbackPage />} />
        <Route path="*" element={<AuthWrapper />} />
      </Routes>
    </BrowserRouter>
  );
}
