import React, { useEffect, useState } from "react";
import type { UserDTO } from "../types";

interface Props {
  token: string;
  userId: number;
}

const API_URL = "http://localhost:8888/auth";

const UserDetails: React.FC<Props> = ({ token, userId }) => {
  const [user, setUser] = useState<UserDTO | null>(null);

  useEffect(() => {
    if (!userId || !token) return;
    fetch(`${API_URL}/user/${userId}`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(r => r.json())
      .then(setUser)
      .catch(() => setUser(null));
  }, [userId, token]);

  if (!user) return <p>Cargando detalles...</p>;

  return (
    <div>
      <h3>Detalles de usuario</h3>
      <ul>
        <li>ID: {user.id}</li>
        <li>Nombre: {user.name} {user.lastName}</li>
        <li>Email: {user.email}</li>
        <li>Username: {user.username}</li>
        <li>Teléfono: {user.phoneNumber ?? ""}</li>
        <li>Fecha nacimiento: {user.bornDate ?? ""}</li>
        <li>Rol: {user.role}</li>
        <li>Activo: {user.enabled ? "Sí" : "No"}</li>
      </ul>
    </div>
  );
};

export default UserDetails;