CREATE TABLE IF NOT EXISTS tarifas_vigentes (
    id SERIAL PRIMARY KEY,
    categoria_cliente VARCHAR(50) NOT NULL,
    tarifa_kwh NUMERIC(10, 4) NOT NULL,
    cargo_fijo NUMERIC(10, 2) NOT NULL,
    vigencia_desde DATE,
    vigencia_hasta DATE
);

INSERT INTO tarifas_vigentes (categoria_cliente, tarifa_kwh, cargo_fijo, vigencia_desde, vigencia_hasta) VALUES
('residencial', 0.1200, 4.50, '2026-01-01', '2026-12-31'),
('comercial', 0.0950, 10.00, '2026-01-01', '2026-12-31'),
('industrial', 0.0750, 25.00, '2026-01-01', '2026-12-31');