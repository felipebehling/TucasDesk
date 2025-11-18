import { createContext } from "react";

/**
 * React context for managing the application's theme.
 *
 * @property {string} theme - The current theme ('light' or 'dark').
 * @property {() => void} toggleTheme - Function to toggle the theme.
 */
export const ThemeContext = createContext({
  theme: "dark",
  toggleTheme: () => {},
});
