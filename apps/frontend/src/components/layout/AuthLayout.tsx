import { Outlet } from "react-router-dom";
import { useContext } from "react";
import { ThemeContext } from "../../context/ThemeContext";
import "../../styles/auth-pages.css";

/**
 * A simple layout component that renders the nested route.
 * Used for authentication pages like Login and Register.
 * @returns {JSX.Element} An outlet that renders the matched child route.
 */
export default function AuthLayout() {
  const { theme, toggleTheme } = useContext(ThemeContext);

  return (
    <div className="auth-layout">
      <button className="auth-layout__theme-toggle" onClick={toggleTheme}>
        {theme === "dark" ? "Light" : "Dark"}
      </button>
      <div className="auth-layout__content">
        <Outlet />
      </div>
    </div>
  );
}
