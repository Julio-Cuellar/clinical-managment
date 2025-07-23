import React, { useState } from "react";

const API_URL = "http://localhost:8080/auth";

const initialForm = {
  name: "",
  lastName: "",
  email: "",
  username: "",
  password: "",
  phoneNumber: "",
  bornDate: "",
  role: "USER"
};

const RegisterForm: React.FC = () => {
  const [form, setForm] = useState(initialForm);
  const [message, setMessage] = useState<string | null>(null);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setMessage(null);
    try {
      const resp = await fetch(`${API_URL}/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(form),
      });
      if (!resp.ok) throw new Error(await resp.text());
      setMessage("¡Registro exitoso! Ahora puedes iniciar sesión.");
    } catch (err: any) {
      setMessage("Error: " + err.message);
    }
  };

  return (
    <form onSubmit={handleSubmit}>
      <h2>Registro</h2>
      <input name="name" placeholder="Nombre" onChange={handleChange} required />
      <input name="lastName" placeholder="Apellido" onChange={handleChange} required />
      <input name="email" placeholder="Email" type="email" onChange={handleChange} required />
      <input name="username" placeholder="Usuario" onChange={handleChange} required />
      <input name="password" placeholder="Contraseña" type="password" onChange={handleChange} required />
      <input name="phoneNumber" placeholder="Teléfono" onChange={handleChange} />
      <input name="bornDate" placeholder="Fecha de nacimiento" type="date" onChange={handleChange} />
      <select name="role" onChange={handleChange} value={form.role}>
        <option value="USER">Usuario</option>
        <option value="ADMIN">Administrador</option>
      </select>
      <button type="submit">Registrarse</button>
      {message && <p>{message}</p>}
    </form>
  );
};

export default RegisterForm;