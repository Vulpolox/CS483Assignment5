package simplewebclient;

import java.io.*;
import java.net.*;

public class SimpleWebClient {
    private static final String hostName = "localhost";
    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        System.out.println("ENTER REQUEST");
        try (
                Socket serverSocket = new Socket(hostName, PORT);
                PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
                BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {
            String userInput = stdIn.readLine();
            if (userInput == null || userInput.trim().isEmpty()) {
                return;
            }

            if (userInput.startsWith("PUT ")) {
                // Handle PUT command: PUT <local-file> <server-path>
                String[] parts = userInput.split(" ", 3);
                if (parts.length < 3) {
                    System.err.println("Invalid PUT command. Usage: PUT <local-file> <server-path>");
                    return;
                }
                String localFile = parts[1];
                String serverPath = parts[2];

                // Read local file content
                StringBuilder fileContent = new StringBuilder();
                try (BufferedReader fileReader = new BufferedReader(new FileReader(localFile))) {
                    String line;
                    while ((line = fileReader.readLine()) != null) {
                        fileContent.append(line).append("\n");
                    }
                } catch (IOException e) {
                    System.err.println("Error reading file: " + e.getMessage());
                    return;
                }

                // Send PUT request to server
                out.println("PUT " + serverPath);
                out.println(fileContent.toString());
                out.println("EOF"); // Signal end of file
            } else {
                // Default behavior (GET or other commands)
                out.println(userInput);
            }

            // Print server response
            String response;
            while ((response = in.readLine()) != null) {
                System.out.println(response);
            }
        } catch (UnknownHostException e) {
            System.err.println("Unknown host: " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
            System.exit(1);
        }
    }
}