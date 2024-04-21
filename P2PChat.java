import java.io.*;
import java.net.*;

public class P2PChat {

    public static void main(String[] args) {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        // Pytanie użytkownika o adres IP i port partnera
        try {
            System.out.println("Podaj adres IP partnera:");
            String partnerAddress = consoleReader.readLine();

            System.out.println("Podaj port partnera:");
            int partnerPort = Integer.parseInt(consoleReader.readLine());

            int port = 12345; // Możesz ustawić domyślny port, jeśli chcesz

            try {
                // Tworzenie gniazda serwera na określonym porcie
                ServerSocket serverSocket = new ServerSocket(port);
                System.out.println("Serwer nasłuchuje na porcie " + port + "...");

                // Łączenie z partnerem
                System.out.println("Próba połączenia z partnerem: " + partnerAddress + ":" + partnerPort);
                final Socket partnerSocket = new Socket(partnerAddress, partnerPort);
                System.out.println("Połączono z partnerem.");

                // Wątek obsługujący odbieranie wiadomości od partnera
                Thread receivingThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            BufferedReader partnerReader = new BufferedReader(new InputStreamReader(partnerSocket.getInputStream()));
                            String message;
                            while ((message = partnerReader.readLine()) != null) {
                                System.out.println("Partner: " + message);
                            }
                        } catch (IOException e) {
                            System.err.println("Błąd podczas odbierania wiadomości od partnera: " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                });
                receivingThread.start();

                // Wysyłanie wiadomości do partnera
                try {
                    PrintWriter partnerWriter = new PrintWriter(partnerSocket.getOutputStream(), true);
                    String input;
                    while ((input = consoleReader.readLine()) != null) {
                        partnerWriter.println(input);
                    }
                } catch (IOException e) {
                    System.err.println("Błąd podczas wysyłania wiadomości do partnera: " + e.getMessage());
                    e.printStackTrace();
                }

            } catch (IOException e) {
                System.err.println("Błąd podczas uruchamiania serwera lub łączenia z partnerem: " + e.getMessage());
                e.printStackTrace();
            }

        } catch (IOException e) {
            System.err.println("Błąd podczas czytania danych wejściowych: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                consoleReader.close();
            } catch (IOException e) {
                System.err.println("Błąd podczas zamykania strumienia wejścia: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
