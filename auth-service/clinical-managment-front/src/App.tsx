import React, { useState } from "react";
import RegisterForm from "./components/RegisterForm";
import LoginForm from "./components/LoginForm";
import UserList from "./components/UserList";
import UserDetails from "./components/UserDetails";
import InicioPanel from "./components/DashBoard";
import type { UserDTO } from "./types";

const API_URL = "http://localhost:8080/auth";

const App: React.FC = () => {
  const [token, setToken] = useState<string | null>(null);
  const [currentUser, setCurrentUser] = useState<UserDTO | null>(null);
  const [selectedUserId, setSelectedUserId] = useState<number | null>(null);

  // Puedes cargar métricas reales aquí, por ejemplo:
  const [numPacientes, setNumPacientes] = useState<number>(127); // valor demo
  const [ingresosMes, setIngresosMes] = useState<number>(12450); // valor demo

  const handleLogout = async () => {
    if (!token) return;
    await fetch(`${API_URL}/logout`, {
      method: "POST",
      headers: { Authorization: `Bearer ${token}` },
    });
    setToken(null);
    setCurrentUser(null);
  };

  return (
    <div className="bg-gray-100 min-h-screen font-sans">
      <div className="container mx-auto px-4 py-8">
        {!token ? (
          <>
            <LoginForm setToken={setToken} setCurrentUser={setCurrentUser} />
            <RegisterForm />
          </>
        ) : (
          <>
            {/* Aquí el Dashboard recibe props */}
            <InicioPanel
              nombreUsuario={currentUser?.username}
              numPacientes={numPacientes}
              ingresosMes={ingresosMes}
            />

            <button
              className="bg-red-600 text-white px-4 py-2 rounded-md mb-4"
              onClick={handleLogout}
            >
              Cerrar Sesión
            </button>
            <p className="mb-4">Bienvenido: {currentUser?.username}</p>
            <UserList token={token} setSelectedUserId={setSelectedUserId} />
            <button
              className="bg-red-400 text-white px-4 py-2 rounded-md mt-4"
              onClick={async () => {
                if (
                  window.confirm(
                    "¿Seguro que deseas eliminar tu usuario? Esta acción es irreversible."
                  )
                ) {
                  await fetch(`${API_URL}/user/${currentUser?.id}`, {
                    method: "DELETE",
                    headers: { Authorization: `Bearer ${token}` },
                  });
                  setToken(null);
                  setCurrentUser(null);
                  alert("Usuario eliminado.");
                }
              }}
            >
              Eliminar mi usuario
            </button>
          </>
        )}
        {selectedUserId && token && (
          <UserDetails token={token} userId={selectedUserId} />
        )}
      </div>
    </div>
  );
};

export default App;