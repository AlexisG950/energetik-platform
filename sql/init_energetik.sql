-- 1. Tabla para Servicio 2 (Consulta de Tarifas Vigentes - TCP 5002)
CREATE TABLE IF NOT EXISTS tarifas_vigentes (
    id SERIAL PRIMARY KEY,
    categoria_cliente VARCHAR(50) NOT NULL,
    zona_geografica VARCHAR(50) NOT NULL,
    tarifa_kwh NUMERIC(10, 4) NOT NULL,
    cargo_fijo NUMERIC(10, 2) NOT NULL,
    vigencia_desde VARCHAR(20) NOT NULL,
    vigencia_hasta VARCHAR(20) NOT NULL
);

-- Registros iniciales de tarifas
INSERT INTO tarifas_vigentes (categoria_cliente, zona_geografica, tarifa_kwh, cargo_fijo, vigencia_desde, vigencia_hasta) VALUES
('residencial', 'CENTRAL', 0.1150, 4.50, '2026-01-01', '2026-12-31'),
('comercial', 'CENTRAL', 0.0950, 10.00, '2026-01-01', '2026-12-31'),
('industrial', 'CENTRAL', 0.0750, 25.00, '2026-01-01', '2026-12-31');


-- 2. Tabla para Servicio 3 (Consulta / Sincronización de Facturación - TCP 5003)
CREATE TABLE IF NOT EXISTS facturas (
    id_factura VARCHAR(50) PRIMARY KEY,
    id_cliente VARCHAR(50) NOT NULL,
    periodo VARCHAR(20) NOT NULL,
    consumo_kwh NUMERIC(10, 2) NOT NULL,
    tarifa_aplicada NUMERIC(10, 4) NOT NULL,
    monto_total NUMERIC(10, 2) NOT NULL,
    estado_pago VARCHAR(20) NOT NULL
);

-- Registros iniciales de facturación
INSERT INTO facturas (id_factura, id_cliente, periodo, consumo_kwh, tarifa_aplicada, monto_total, estado_pago) VALUES
('FAC-2026-08-9921', 'CLI-88410', '08/2026', 482.35, 0.1150, 59.97, 'PENDIENTE'),
('FAC-2026-07-8810', 'CLI-88410', '07/2026', 450.10, 0.1150, 56.26, 'PAGADO'),
('FAC-2026-08-1002', 'CLI-55558', '08/2026', 210.00, 0.1150, 28.65, 'PENDIENTE');