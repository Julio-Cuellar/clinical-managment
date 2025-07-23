import React, { useState, useEffect } from "react";
import type { UserDTO } from "../types";

interface Props {
  token: string;
  setSelectedUserId: (id: number) => void;
}

const API_URL = "http://localhost:8888/auth";

const UserList: React.FC<Props> = ({ token, setSelectedUserId }) => {
  const [users, setUsers] = useState<UserDTO[]>([]);

  useEffect(() => {
    if (!token) return;
    fetch(`${API_URL}/users`, {
      headers: { Authorization: `Bearer ${token}` },
    })
      .then(r => r.json())
      .then(setUsers)
      .catch(() => setUsers([]));
  }, [token]);

  return (
    <div>
      <h2>Usuarios</h2>
      <ul>
        {users.map(u => (
          <li key={u.id}>
            {u.username} ({u.email})
            <button onClick={() => setSelectedUserId(u.id)}>Ver detalles</button>
          </li>
        ))}
      </ul>
    </div>
  );
};

export default UserList;