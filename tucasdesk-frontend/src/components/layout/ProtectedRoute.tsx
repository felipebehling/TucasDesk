import { Navigate, Outlet } from "react-router-dom";

interface ProtectedRouteProps {
  isAuthenticated: boolean;
  isLoading?: boolean;
}

/**
 * A component to protect routes that require authentication.
 * If the user is not authenticated, it redirects them to the login page.
 * Displays a loading placeholder while the authentication state is being validated.
 * @param {object} props - The component props.
 * @param {boolean} props.isAuthenticated - Flag indicating if the user is authenticated.
 * @param {boolean} [props.isLoading=false] - Flag indicating if the auth state is loading.
 * @returns {JSX.Element} The nested route (Outlet) if authenticated, or a redirect.
 */
export default function ProtectedRoute({ isAuthenticated, isLoading = false }: ProtectedRouteProps) {
  if (isLoading) {
    return <div className="loading-fallback">Validando sessão...</div>;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return <Outlet />; // If authenticated, render the nested main layout.
}
