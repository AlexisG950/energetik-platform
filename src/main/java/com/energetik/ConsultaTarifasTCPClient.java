package com.energetik;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ConsultaTarifasTCPClient {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("==========================================================");
        System.out.println("   CLIENTE TCP - CONSULTA DE TARIFAS VIGENTES             ");
        System.out.println("==========================================================");
        System.out.print("Host del servidor Energetik (ej. localhost): ");
        String host = scanner.nextLine().trim();

        System.out.print("Puerto TCP (ej. 6001): ");
        int port = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Categoria a consultar (residencial / comercial / industrial): ");
        String categoria = scanner.nextLine().trim();

        try (Socket socket = new Socket(host, port);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            JSONObject request = new JSONObject();
            request.put("categoria_cliente", categoria);

            out.println(request.toString());
            String response = in.readLine();

            System.out.println("\n[Respuesta recibida del Servidor TCP]:");
            System.out.println(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}