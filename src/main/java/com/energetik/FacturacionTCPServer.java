package com.energetik;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class FacturacionTCPServer {
    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;

    public static void main(String[] args) {
        
        int port = Integer.parseInt(getEnvOrDefault("TCP_PORT", "5003"));
        String dbHost = getEnvOrDefault("DB_HOST", "localhost");
        String dbPort = getEnvOrDefault("DB_PORT", "5432");
        String dbName = getEnvOrDefault("DB_NAME", "energetik_db");
        dbUser = getEnvOrDefault("DB_USER", "postgres");
        dbPassword = getEnvOrDefault("DB_PASSWORD", "jdgc110346#rg");

        dbUrl = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;

        System.out.println("==========================================================");
        System.out.println("   ENERGETIK - SERVICIO 3: SERVIDOR FACTURACION (TCP)     ");
        System.out.println("==========================================================");
        System.out.println("Base de datos configurada en: " + dbUrl);
        System.out.println("Usuario: " + dbUser);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("\n[Energetik] Servidor TCP de Facturacion activo en puerto " + port + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> procesarPeticion(clientSocket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void procesarPeticion(Socket socket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String requestStr = in.readLine();
            JSONObject request = new JSONObject(requestStr);
            String idCliente = request.optString("id_cliente", "");
            String periodo = request.optString("periodo", "");

            JSONObject response = new JSONObject();
            response.put("tipo_mensaje", "FACTURA_RESPONSE");

            try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
                String sql = "SELECT id_factura, consumo_kwh, tarifa_aplicada, monto_total, estado_pago FROM facturas WHERE id_cliente = ? AND periodo = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, idCliente);
                stmt.setString(2, periodo);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    response.put("id_factura", rs.getString("id_factura"));
                    response.put("id_cliente", idCliente);
                    response.put("periodo", periodo);
                    response.put("consumo_kwh", rs.getDouble("consumo_kwh"));
                    response.put("tarifa_aplicada", rs.getDouble("tarifa_aplicada"));
                    response.put("monto_total", rs.getDouble("monto_total"));
                    response.put("estado_pago", rs.getString("estado_pago"));
                } else {
                    response.put("error", "Factura no encontrada para el cliente y periodo solicitado");
                }
            }
            out.println(response.toString());
        } catch (Exception e) {
            System.err.println("[Error procesando TCP Factura]: " + e.getMessage());
        }
    }

    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isBlank()) ? value : defaultValue;
    }
}