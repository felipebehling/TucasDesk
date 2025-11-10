import { Outlet } from "react-router-dom";
import { useContext } from "react";
import { ThemeContext } from "../../context/ThemeContext";

/**
 * A simple layout component that renders the nested route.
 * Used for authentication pages like Login and Register.
 * @returns {JSX.Element} An outlet that renders the matched child route.
 */
export default function AuthLayout() {
  const { theme, toggleTheme } = useContext(ThemeContext);

  const buttonStyle: React.CSSProperties = {
    position: "absolute",
    top: "20px",
    right: "20px",
    padding: "10px 15px",
    cursor: "pointer",
    borderRadius: "8px",
    border: "1px solid var(--border-default)",
    background: "var(--bg-accent)",
    color: "var(--text-primary)",
    zIndex: 1000,
  };

  return (
    <>
      <button onClick={toggleTheme} style={buttonStyle}>
        {theme === "dark" ? "Light" : "Dark"}
      </button>
      <Outlet />
    </>
  );
}
