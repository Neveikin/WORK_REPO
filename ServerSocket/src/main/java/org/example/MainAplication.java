package org.example;

import java.io.IOException;

import static org.example.Server.Init;

public class MainAplication {
    public static void main(String[] args) {
        try {
            Init();
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}
