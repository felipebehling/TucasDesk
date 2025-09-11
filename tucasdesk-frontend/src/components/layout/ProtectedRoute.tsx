import { Navigate, Outlet } from "react-router-dom";

/**
 * A component to protect routes that require authentication.
 * If the user is not authenticated, it redirects them to the login page.
 * @param {object} props - The component props.
 * @param {boolean} props.isAuthenticated - Flag indicating if the user is authenticated.
 * @returns {JSX.Element} The nested route (Outlet) if authenticated, or a redirect.
 */
export default function ProtectedRoute({ isAuthenticated }: { isAuthenticated: boolean }) {
  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }
  return <Outlet />; // If authenticated, render the nested main layout.
}
