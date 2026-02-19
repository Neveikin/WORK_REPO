package org.example;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.sql.Connection;
import java.sql.SQLException;

import org.example.DB.DataBaseConection;
import org.example.DB.TableFactoryDB;

public class Server {

    private static Socket clientSocket;
    private static ServerSocket server;
    private static BufferedReader in;
    private static BufferedWriter out;

    private static String[] parts;
    private static Connection connection;
    private static TableFactoryDB tableFactoryDB;

    public Server(String[] parts) {
        this.parts = parts;
    }

    public static void Init() throws IOException {
        try {
            DataBaseConection dbConnection = new DataBaseConection("", "", "");
            connection = dbConnection.connectToMyDatabase();
            tableFactoryDB = new TableFactoryDB();
            
            server = new ServerSocket(10032);
            System.out.println("Сервер запущен!");

                while (true) {
                    clientSocket = server.accept();
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                    byte working = 0;
                    while(working == 0) {
                        int num = 0;

                        String word = in.readLine();
                        if (word == null) break;

                        String parts[] = word.split(" ");

                        switch (parts[0].toLowerCase()) {
                            case ("get"):
                                getCabinets(parts);
                                break;

                            case ("del"):
                                if (parts.length > 1)
                                    deleteCabinet(parts);
                                break;

                            case ("add"):
                                if (parts.length > 1)
                                    addCabinetFromJson(parts);
                                break;

                            case ("upd"):
                                updateCabs(parts);
                                break;

                            case ("exit"):
                                exitClient();
                                working ++;
                                break;

                            case ("stop"):
                                stopServer();
                                break;

                            default:
                                break;
                        }
                    }
                }

            } catch (IOException e) {
                System.err.println(e);
            } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static String serializeCabinetsToJson(LinkedList<Kabinet> cabinets) {
        StringBuilder jsonResult = new StringBuilder("[");
        int i = 0;
        for (Kabinet cab : cabinets) {
            jsonResult.append(cab.toJSON());
            if (i < cabinets.size() - 1) {
                jsonResult.append(",");
            }
            i++;
        }
        jsonResult.append("]");
        return jsonResult.toString();
    }

    public static void getCabinets(String parts[]) throws IOException {
        try {
            LinkedList<Kabinet> cabinets = tableFactoryDB.getAllCabinets(connection);
            String jsonResult = serializeCabinetsToJson(cabinets);
            out.write(jsonResult);
            out.newLine();
            out.newLine();
            out.flush();
        } catch (SQLException e) {
            System.err.println("Ошибка при получении кабинетов: " + e.getMessage());
        }
    }

    public static void deleteCabinet(String parts[]) throws IOException {
        String word = parts[1];
        
        if (word.startsWith("{") || word.startsWith("[") || word.startsWith("\"")) {
            try {
                if (word.startsWith("\"") && word.endsWith("\"")) {
                    word = word.substring(1, word.length() - 1);
                } else if (word.startsWith("{") && word.endsWith("}")) {
                    Kabinet kabinet = Kabinet.getFromJSON(word);
                    if (kabinet != null) {
                        word = kabinet.getId();
                    }
                } else if (word.startsWith("[") && word.endsWith("]")) {
                    String jsonArray = word.substring(1, word.length() - 1).trim();
                    if (jsonArray.startsWith("{") && jsonArray.endsWith("}")) {
                        Kabinet kabinet = Kabinet.getFromJSON(jsonArray);
                        if (kabinet != null) {
                            word = kabinet.getId();
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        
        try {
            tableFactoryDB.deleteCab(connection, word);
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении кабинета: " + e.getMessage());
        }
    }


    public static void addCabinetFromJson(String[] parts) throws IOException {
        String jsonInput = String.join(" ", java.util.Arrays.copyOfRange(parts, 1, parts.length)).trim();

        if (jsonInput.isEmpty()) {
            return;
        }

        try {
            if (jsonInput.startsWith("[") && jsonInput.endsWith("]")) {
                String jsonArray = jsonInput.substring(1, jsonInput.length() - 1).trim();
                if (jsonArray.startsWith("{") && jsonArray.endsWith("}")) {
                    jsonInput = jsonArray;
                } else {
                    int firstBrace = jsonArray.indexOf("{");
                    int lastBrace = jsonArray.indexOf("}", firstBrace);
                    if (firstBrace != -1 && lastBrace != -1 && lastBrace > firstBrace) {
                        jsonInput = jsonArray.substring(firstBrace, lastBrace + 1);
                    }
                }
            }

            Kabinet newKabinet = Kabinet.getFromJSON(jsonInput);
            if (newKabinet == null) {
                return;
            }

            tableFactoryDB.createCab(connection, newKabinet);

        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении кабинета: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public static void updateCabs(String[] parts) throws IOException {
        String jsonInput = String.join(" ", java.util.Arrays.copyOfRange(parts, 1, parts.length)).trim();
        
        if (jsonInput.isEmpty()) {
            return;
        }
        
        try {
            if (jsonInput.startsWith("[") && jsonInput.endsWith("]")) {
                String jsonArray = jsonInput.substring(1, jsonInput.length() - 1).trim();
                if (jsonArray.startsWith("{") && jsonArray.endsWith("}")) {
                    jsonInput = jsonArray;
                } else {
                    int firstBrace = jsonArray.indexOf("{");
                    int lastBrace = jsonArray.indexOf("}", firstBrace);
                    if (firstBrace != -1 && lastBrace != -1 && lastBrace > firstBrace) {
                        jsonInput = jsonArray.substring(firstBrace, lastBrace + 1);
                    }
                }
            }
            
            Kabinet updatedKabinet = Kabinet.getFromJSON(jsonInput);
            if (updatedKabinet == null) {
                return;
            }
            
            tableFactoryDB.updateCab(connection, updatedKabinet);
            
        } catch (SQLException e) {
            System.err.println("Ошибка при обновлении кабинета: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка при обновлении кабинета: " + e.getMessage());
        }
    }

    public static void exitClient() throws IOException {
        clientSocket.close();
        in.close();
        out.close();
    }

    public static void stopServer() throws IOException {
        server.close();
        clientSocket.close();
        in.close();
        out.close();
    }
}