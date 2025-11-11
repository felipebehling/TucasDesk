import { Navigate, Outlet, useLocation } from "react-router-dom";
import LoadingOverlay from "../common/LoadingOverlay";

interface ProtectedRouteProps {
  isAuthenticated: boolean;
  isLoading?: boolean;
}

/**
 * A component to protect routes that require authentication.
 * If the user is not authenticated, it redirects them to the login page.
 * Displays a loading placeholder while the authentication state is being validated.
 */
export default function ProtectedRoute({ isAuthenticated, isLoading = false }: ProtectedRouteProps) {
  const location = useLocation();

  if (isLoading) {
    return <LoadingOverlay fullscreen message="Validando sessão..." />;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace state={{ from: location }} />;
  }

  return <Outlet />;
}
