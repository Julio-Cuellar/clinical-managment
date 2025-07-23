import React, { useState } from "react";

interface LoginFormProps {
  setToken: (token: string) => void;
  setCurrentUser: (user: any) => void;
}

const API_URL = "http://localhost:8080/auth";

const LoginForm: React.FC<LoginFormProps> = ({ setToken, setCurrentUser }) => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError(null);

    try {
      const response = await fetch(`${API_URL}/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
      });

      if (!response.ok) {
        setError("Credenciales incorrectas o error de servidor.");
        return;
      }

      const data = await response.json();
      if (data.token && data.user) {
        setToken(data.token);
        setCurrentUser(data.user);
      } else {
        setError("Respuesta inesperada del servidor.");
      }
    } catch (err) {
      setError("Error al conectar con el servidor.");
    }
  };

  return (
    <form
      onSubmit={handleSubmit}
      className="bg-white rounded-xl shadow-md p-6 mb-6 max-w-md mx-auto"
    >
      <h2 className="text-2xl font-bold mb-4 text-gray-800">Iniciar Sesión</h2>
      {error && (
        <div className="mb-4 text-red-600 bg-red-100 rounded p-2 text-center">
          {error}
        </div>
      )}
      <div className="mb-4">
        <label className="block text-gray-700 mb-1">Usuario</label>
        <input
          type="text"
          className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring focus:border-blue-400"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
          autoFocus
          required
        />
      </div>
      <div className="mb-4">
        <label className="block text-gray-700 mb-1">Contraseña</label>
        <input
          type="password"
          className="w-full border border-gray-300 rounded px-3 py-2 focus:outline-none focus:ring focus:border-blue-400"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
      </div>
      <button
        type="submit"
        className="w-full bg-blue-600 text-white font-semibold py-2 rounded hover:bg-blue-700 transition"
      >
        Ingresar
      </button>
    </form>
  );
};

export default LoginForm;