import React from "react";

interface InicioPanelProps {
  nombreUsuario?: string;
  numPacientes?: number;
  ingresosMes?: number;
  // Puedes agregar más props si necesitas que sean dinámicas otras alertas o métricas
}

const cardData = (numPacientes: number = 127, ingresosMes: number = 12450) => [
  {
    title: "Pacientes",
    description: `Total de pacientes: ${numPacientes}`,
    color: "#e9f2ff",
    icon: (
      <svg width="48" height="48" fill="none" stroke="#3478F6" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <circle cx="24" cy="18" r="8" />
        <path d="M8 40c0-8.837 7.163-16 16-16s16 7.163 16 16" />
      </svg>
    ),
    textColor: "#3478F6"
  },
  {
    title: "Agenda",
    description: "Próximas citas",
    color: "#fff2f2",
    icon: (
      <svg width="48" height="48" fill="none" stroke="#FF4040" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <rect x="8" y="12" width="32" height="28" rx="4" />
        <path d="M16 8v8M32 8v8M8 20h32" />
      </svg>
    ),
    textColor: "#FF4040"
  },
  {
    title: "Ingresos",
    description: `Este mes: $${ingresosMes.toLocaleString()}`,
    color: "#f6f3ff",
    icon: (
      <svg width="48" height="48" fill="none" stroke="#884DFF" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <circle cx="24" cy="24" r="18" />
        <path d="M24 14v20M19 19h10m-10 10h10" />
      </svg>
    ),
    textColor: "#884DFF"
  },
  {
    title: "Insumos",
    description: "Stock actualizado",
    color: "#fffbe6",
    icon: (
      <svg width="48" height="48" fill="none" stroke="#FFB200" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
        <rect x="12" y="20" width="24" height="16" rx="2" />
        <path d="M16 20V12h16v8" />
      </svg>
    ),
    textColor: "#FFB200"
  }
];

const alerts = [
  {
    bold: "Cita programada para hoy",
    text: "Tienes una cita programada para hoy. ¡No la olvides!"
  },
  {
    bold: "Insumos en bajo stock",
    text: "El stock de insumos está bajo. Es recomendable hacer un pedido pronto."
  },
  {
    bold: "Ingresos de este mes",
    text: "Revisa los ingresos del mes para mantener el control financiero."
  }
];

const InicioPanel: React.FC<InicioPanelProps> = ({
  nombreUsuario,
  numPacientes = 127,
  ingresosMes = 12450
}) => (
  <div style={{
    background: "#fff",
    borderRadius: 20,
    boxShadow: "0 4px 24px rgba(0,0,0,0.06)",
    margin: 20,
    padding: 36,
    maxWidth: 1800
  }}>
    <div style={{ textAlign: "center", marginBottom: 32 }}>
      <h1 style={{ fontSize: 36, fontWeight: 700, color: "#212529" }}>Panel de Control</h1>
      {nombreUsuario && (
        <p style={{ fontSize: 20, color: "#374151", marginTop: 8 }}>
          Bienvenido, <b>{nombreUsuario}</b>
        </p>
      )}
    </div>
    <div style={{
      display: "flex",
      justifyContent: "space-between",
      gap: 24,
      marginBottom: 36,
      flexWrap: "wrap"
    }}>
      {cardData(numPacientes, ingresosMes).map((card) => (
        <div key={card.title}
          style={{
            background: card.color,
            borderRadius: 16,
            flex: 1,
            minWidth: 260,
            maxWidth: 360,
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            padding: "36px 18px",
            margin: "0 8px"
          }}>
          {card.icon}
          <div style={{
            fontSize: 24,
            margin: "16px 0 8px 0",
            fontWeight: 600,
            color: card.textColor
          }}>{card.title}</div>
          <div style={{
            fontSize: 18,
            color: "#374151",
            marginTop: 4
          }}>{card.description}</div>
        </div>
      ))}
    </div>
    <div>
      <h2 style={{ fontSize: 28, fontWeight: 700, color: "#212529" }}>Alertas y Notificaciones</h2>
      <ul style={{ listStyle: "none", padding: 0, marginTop: 24 }}>
        {alerts.map((alert, idx) => (
          <li key={idx} style={{ marginBottom: 12 }}>
            <span style={{ fontWeight: 700 }}>{alert.bold}</span><br />
            <span style={{ color: "#374151", fontSize: 18 }}>{alert.text}</span>
          </li>
        ))}
      </ul>
    </div>
  </div>
);

export default InicioPanel;