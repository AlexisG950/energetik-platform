package com.energetik;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONObject;

public class TarifasTCPServer {
    private static String dbUrl;
    private static String dbUser;
    private static String dbPassword;

    public static void main(String[] args) {
        
        int port = Integer.parseInt(getEnvOrDefault("TCP_PORT", "5002"));
        String dbHost = getEnvOrDefault("DB_HOST", "localhost");
        String dbPort = getEnvOrDefault("DB_PORT", "5432");
        String dbName = getEnvOrDefault("DB_NAME", "energetik_db");
        dbUser = getEnvOrDefault("DB_USER", "postgres");
        dbPassword = getEnvOrDefault("DB_PASSWORD", "jdgc110346#rg");

        dbUrl = "jdbc:postgresql://" + dbHost + ":" + dbPort + "/" + dbName;

        System.out.println("==========================================================");
        System.out.println("   ENERGETIK - CONSULTA DE TARIFAS VIGENTES (TCP)         ");
        System.out.println("==========================================================");
        System.out.println("Base de datos configurada en: " + dbUrl);
        System.out.println("Usuario: " + dbUser);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("\n[Energetik] Servidor TCP de Tarifas activo en puerto " + port + "...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> atenderConsulta(clientSocket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void atenderConsulta(Socket socket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String peticionStr = in.readLine();
            JSONObject peticion = new JSONObject(peticionStr);
            String categoria = peticion.optString("categoria_cliente", "residencial");

            JSONObject response = new JSONObject();

            try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword)) {
                String sql = "SELECT tarifa_kwh, cargo_fijo, vigencia_desde, vigencia_hasta FROM tarifas_vigentes WHERE categoria_cliente = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, categoria);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    response.put("categoria_cliente", categoria);
                    response.put("tarifa_kWh", rs.getDouble("tarifa_kwh"));
                    response.put("cargo_fijo", rs.getDouble("cargo_fijo"));
                    response.put("vigencia_desde", rs.getString("vigencia_desde"));
                    response.put("vigencia_hasta", rs.getString("vigencia_hasta"));
                } else {
                    response.put("error", "Categoria no encontrada");
                }
            }

            out.println(response.toString());
        } catch (Exception e) {
            System.err.println("[Error procesando TCP]: " + e.getMessage());
        }
    }

    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isBlank()) ? value : defaultValue;
    }
}