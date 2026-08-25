# Energetik - Plataforma de Suministros y Clientes

## Integrantes
- Joaquin Onieva Zymanscki - 5678285
- Alex Rodrigo Gauto Cardozo - 5555875

## Descripción
Módulo correspondiente a la Organización 2 (Energetik). Implementa:
1. **Servicio de Consulta de Tarifas Vigentes (TCP en puerto 6001):** Expone las tarifas eléctricas por categoría leyendo de PostgreSQL.
2. **Servicio de Lectura de Medidor (Cliente UDP):** Emite telemetría IoT de consumo hacia el servicio de Rastrenergy.
3. **Cliente TCP de Tarifas:** Aplicación interactiva de consola para realizar consultas al servidor de tarifas.

## Base de Datos y Compilación
1. Crear la base de datos `energetik_db` y ejecutar el archivo `sql/init_energetik.sql`.
2. Compilar con Maven (Java 8):
   ```bash
   mvn clean compile