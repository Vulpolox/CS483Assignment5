package simplewebserver;

import java.io.*;
import java.net.*;
import java.util.*;

public class SimpleWebServer {
    private static final int PORT = 8080;
    private static ServerSocket dServerSocket;
    private static final String LOG_FILE = "./logs/server.log";
    private static final String ERROR_LOG_FILE = "./logs/error.log";

    public SimpleWebServer() throws Exception {
        dServerSocket = new ServerSocket(PORT);
    }

    public void run() throws Exception {
        while (true) {
            Socket s = dServerSocket.accept();
            processRequest(s);
        }
    }

    public void processRequest(Socket s) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        OutputStreamWriter osw = new OutputStreamWriter(s.getOutputStream());

        String request = br.readLine();
        log(LOG_FILE, request); // Log the request

        if (request == null || request.trim().isEmpty())
            return;

        StringTokenizer st = new StringTokenizer(request, " ");
        String command = st.nextToken();
        String pathname = st.nextToken();

        if (command.equals("GET")) {
            System.out.println("GET: " + pathname);
            serveFile(osw, pathname);
        } else if (command.equals("PUT")) {
            System.out.println("PUT: " + pathname);
            saveFile(br, osw, pathname);
        } else {
            osw.write("HTTP/1.0 501 Not Implemented\n\n");
        }

        osw.close();
    }

    public void serveFile(OutputStreamWriter osw, String pathname) throws Exception {
<<<<<<< Updated upstream
		long MAX_FILE_SIZE = 10; // only allows 10 bytes
	
		if (pathname.charAt(0) == '/') pathname = pathname.substring(1);
		if (pathname.equals("")) pathname = "index.html";
	
		File file = new File(pathname);
	
		// Check if the file exists
		if (!file.exists()) {
			osw.write("HTTP/1.0 404 Not Found\n\n");
			return;
		}
	
		// Check if the file size exceeds the maximum allowed size
		if (file.length() > MAX_FILE_SIZE) {
			// Log the error to error_log.txt
			log(ERROR_LOG_FILE, "File too large: " + pathname + " (size: " + file.length() + " bytes)");
	
			// Return 403 Forbidden response
			osw.write("HTTP/1.0 403 Forbidden\n\n");
			return;
		}
	
		// If the file is small enough, serve the file
		try (FileReader fr = new FileReader(file)) {
			osw.write("HTTP/1.0 200 OK\n\n");
			int c;
			while ((c = fr.read()) != -1) {
				osw.write((char) c);
			}
		} catch (IOException e) {
			// Log any error while reading the file
			log(ERROR_LOG_FILE, "Error reading file: " + pathname + " (" + e.getMessage() + ")");
			osw.write("HTTP/1.0 500 Internal Server Error\n\n");
		}
	}
	
=======
        // Max size of 1000 bytes
        long MAX_FILE_SIZE = 1000; // 1000 bytes

        if (pathname.charAt(0) == '/')
            pathname = pathname.substring(1);
        if (pathname.equals(""))
            pathname = "index.html";

        File file = new File(pathname);

        // Check if the file exists
        if (!file.exists()) {
            osw.write("HTTP/1.0 404 Not Found\n\n");
            return;
        }

        // Check if the file size exceeds the maximum allowed size
        if (file.length() > MAX_FILE_SIZE) {
            // Log the error to error_log.txt
            log(ERROR_LOG_FILE, "File too large: " + pathname + " (size: " + file.length() + " bytes)");

            // Return 403 Forbidden response
            osw.write("HTTP/1.0 403 Forbidden\n\n");
            return;
        }

        // If the file is small enough, serve the file
        try (FileReader fr = new FileReader(file)) {
            osw.write("HTTP/1.0 200 OK\n\n");
            int c;
            while ((c = fr.read()) != -1) {
                osw.write((char) c);
            }
        } catch (IOException e) {
            // Log any error while reading the file
            log(ERROR_LOG_FILE, "Error reading file: " + pathname + " (" + e.getMessage() + ")");
            osw.write("HTTP/1.0 500 Internal Server Error\n\n");
        }
    }
>>>>>>> Stashed changes

    public void saveFile(BufferedReader br, OutputStreamWriter osw, String pathname) throws Exception {
        if (pathname.charAt(0) == '/')
            pathname = pathname.substring(1);
        FileWriter fw = new FileWriter(pathname);
        String line;
        while ((line = br.readLine()) != null && !line.equals("EOF")) {
            fw.write(line + "\n");
        }
        fw.close();
        osw.write("HTTP/1.0 200 OK\n\n");
    }

    public void log(String logfile, String request) {
        try (FileWriter log = new FileWriter(logfile, true)) {
            log.write(new Date() + " - " + request + "\n");
        } catch (IOException e) {
            System.err.println("Failed to log request: " + e.getMessage());
        }
    }

    public static void main(String argv[]) throws Exception {
        SimpleWebServer sws = new SimpleWebServer();
        sws.run();
    }
}
