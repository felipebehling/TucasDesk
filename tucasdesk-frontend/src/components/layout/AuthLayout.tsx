import { Outlet } from "react-router-dom";

/**
 * A simple layout component that renders the nested route.
 * Used for authentication pages like Login and Register.
 * @returns {JSX.Element} An outlet that renders the matched child route.
 */
export default function AuthLayout() {
  return <Outlet />;
}
