package com.energetik;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.time.Instant;
import java.util.Scanner;
import org.json.JSONObject;

public class MedidorUDPClient {
    public static void main(String[] args) {
        
        String host = getEnvOrDefault("SERVER_HOST", "localhost");
        int port = Integer.parseInt(getEnvOrDefault("UDP_PORT", "5001"));

        Scanner scanner = new Scanner(System.in);

        System.out.println("==========================================================");
        System.out.println("   ENERGETIK - SIMULADOR MEDIDOR INTELIGENTE IOT (UDP)    ");
        System.out.println("==========================================================");
        System.out.println("Destino configurado: " + host + ":" + port);

        System.out.print("ID Medidor (ej. MED-IOT-101): ");
        String idMedidor = scanner.nextLine().trim();

        System.out.print("ID Cliente (ej. CLI-5555875): ");
        String idCliente = scanner.nextLine().trim();

        System.out.print("Lectura en kWh (ej. 58.40): ");
        double lecturaKwh = Double.parseDouble(scanner.nextLine().trim());

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName(host);

            JSONObject lectura = new JSONObject();
            lectura.put("id_medidor", idMedidor);
            lectura.put("id_cliente", idCliente);
            lectura.put("timestamp", Instant.now().toString());
            lectura.put("lectura_kWh", lecturaKwh);

            byte[] buffer = lectura.toString().getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, port);

            socket.send(packet);
            System.out.println("\n[UDP] Datagrama enviado exitosamente a " + host + ":" + port);
            System.out.println("Contenido: " + lectura.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getEnvOrDefault(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value != null && !value.isBlank()) ? value : defaultValue;
    }
}