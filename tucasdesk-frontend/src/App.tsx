import React, { useState } from "react";
import { Eye, EyeOff, User } from "lucide-react";
import axios from "axios";
import { useNavigate } from "react-router-dom";

function App() {
  const [showPassword, setShowPassword] = useState(false);
  const [email, setEmail] = useState("");
  const [senha, setSenha] = useState("");
  const [rememberMe, setRememberMe] = useState(false);
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();

  const togglePasswordVisibility = () => {
    setShowPassword(!showPassword);
  };

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    setErrorMessage("");

    try {
      const response = await axios.post("http://localhost:8080/auth/login", {
        email: email,
        senha: senha,
      });

      if (response.data.token) {
        localStorage.setItem("token", response.data.token);
        localStorage.setItem("usuario", response.data.usuario);

        navigate("/dashboard"); // Redireciona para a dashboard
      }
    } catch (error: any) {
      if (error.response && error.response.status === 401) {
        setErrorMessage("Email ou senha incorretos!");
      } else {
        setErrorMessage("Erro ao conectar ao servidor. Tente novamente.");
      }
    }
  };

  return (
    <div className="min-h-screen flex">
      {/* Left side - Login Form */}
      <div className="flex-1 flex items-center justify-center bg-gray-50 p-8">
        <div className="w-full max-w-md">
          {/* Logo */}
          <div className="text-center mb-8">
            <div className="inline-flex items-center justify-center w-12 h-12 rounded-xl bg-gradient-to-r from-yellow-400 to-yellow-500 shadow-lg mb-6">
              <User className="w-6 h-6 text-white" />
            </div>
            <h1 className="text-3xl font-bold text-gray-900 mb-2">Bem-vindo!</h1>
            <p className="text-gray-600">
              Faça login para acessar sua conta.
            </p>
          </div>

          {/* Login Form */}
          <form className="space-y-6" onSubmit={handleLogin}>
            {/* Email Field */}
            <div>
              <label htmlFor="email" className="sr-only">Nome</label>
              <div className="relative">
                <input
                  id="email"
                  name="email"
                  type="text"
                  placeholder="user@example.com"
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  className="w-full px-4 py-3 border border-gray-300 rounded-lg text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-yellow-400 focus:border-transparent transition-all duration-200"
                />
              </div>
            </div>

            {/* Password Field */}
            <div>
              <label htmlFor="senha" className="sr-only">Senha</label>
              <div className="relative">
                <input
                  id="senha"
                  name="senha"
                  type={showPassword ? "text" : "password"}
                  placeholder=""
                  value={senha}
                  onChange={(e) => setSenha(e.target.value)}
                  className="w-full px-4 py-3 border border-gray-300 rounded-lg text-gray-900 placeholder-gray-500 focus:outline-none focus:ring-2 focus:ring-yellow-400 focus:border-transparent transition-all duration-200 pr-12"
                />
                <button
                  type="button"
                  onClick={togglePasswordVisibility}
                  className="absolute inset-y-0 right-0 pr-3 flex items-center text-gray-400 hover:text-gray-600 transition-colors duration-200"
                >
                  {showPassword ? <EyeOff className="h-5 w-5" /> : <Eye className="h-5 w-5" />}
                </button>
              </div>
            </div>

            {/* Remember Me and Forgot Password */}
            <div className="flex items-center justify-between">
              <div className="flex items-center">
                <input
                  id="remember-me"
                  name="remember-me"
                  type="checkbox"
                  checked={rememberMe}
                  onChange={(e) => setRememberMe(e.target.checked)}
                  className="h-4 w-4 text-yellow-500 border-gray-300 rounded focus:ring-yellow-400 focus:ring-2"
                />
                <label htmlFor="remember-me" className="ml-2 text-sm text-gray-700">
                  Lembrar-me
                </label>
              </div>
            </div>

            {/* Error Message */}
            {errorMessage && (
              <p className="text-red-500 text-sm font-medium">{errorMessage}</p>
            )}

            {/* Login Button */}
            <button
              type="submit"
              className="w-full bg-gradient-to-r from-yellow-400 to-yellow-500 text-white font-medium py-3 px-4 rounded-lg hover:from-yellow-500 hover:to-yellow-600 focus:outline-none focus:ring-2 focus:ring-yellow-400 focus:ring-offset-2 transform hover:scale-[1.02] transition-all duration-200 shadow-lg hover:shadow-xl"
            >
              Entrar
            </button>
          </form>
        </div>
      </div>

      {/* Right side - Decorative Pattern */}
      <div className="hidden lg:flex flex-1 bg-gradient-to-br from-gray-900 to-gray-800 relative overflow-hidden">
        <div className="absolute inset-0 opacity-90">
          <div className="grid grid-cols-4 grid-rows-5 h-full w-full gap-0">
            {[...Array(20)].map((_, index) => {
              const colors = [
                "bg-gradient-to-br from-yellow-400 to-yellow-500",
                "bg-gradient-to-br from-red-400 to-red-500",
                "bg-gradient-to-br from-green-400 to-green-500",
                "bg-gradient-to-br from-yellow-500 to-red-400",
                "bg-gradient-to-br from-red-500 to-green-400",
                "bg-gradient-to-br from-green-500 to-yellow-400"
              ];
              const colorClass = colors[index % colors.length];
              return (
                <div key={index} className="relative">
                  <div
                    className={`absolute inset-0 ${colorClass} transform rotate-45 rounded-full scale-150 origin-bottom-left opacity-80`}
                    style={{
                      clipPath: "polygon(0 100%, 100% 100%, 100% 0)",
                      transform: `rotate(${45 + (index * 15) % 360}deg) scale(${1.2 + (index % 3) * 0.2})`
                    }}
                  />
                </div>
              );
            })}
          </div>
        </div>
        <div className="absolute inset-0 bg-gradient-to-r from-transparent via-gray-900/20 to-gray-900/40" />
        <div className="relative z-10 flex flex-col justify-end p-8 text-white">
          <div className="mb-8">
            <h2 className="text-2xl font-bold mb-2">Sistema TucasDesk</h2>
            <p className="text-gray-300 opacity-90">
              Faça login para acessar seu painel e gerenciar seus chamados.
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}

export default App;
