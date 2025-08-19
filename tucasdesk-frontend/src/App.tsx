import { BrowserRouter as Router, Routes, Route, Navigate } from "react-router-dom";
import Login from "./pages/Login/Login.tsx";
import Chamados from "./pages/Chamados/Chamados.tsx";
import Categorias from "./pages/Categorias/Categorias.tsx";
import Usuarios from "./pages/Usuarios/Usuarios.tsx";
import { useContext, type JSX } from "react";
import { AuthContext } from "./context/AuthContext";

function PrivateRoute({ children }: { children: JSX.Element }) {
  const { autenticado } = useContext(AuthContext);
  return autenticado ? children : <Navigate to="/" />;
}

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/chamados" element={<PrivateRoute><Chamados /></PrivateRoute>} />
        <Route path="/categorias" element={<PrivateRoute><Categorias /></PrivateRoute>} />
        <Route path="/usuarios" element={<PrivateRoute><Usuarios /></PrivateRoute>} />
      </Routes>
    </Router>
  );
}

export default App;
