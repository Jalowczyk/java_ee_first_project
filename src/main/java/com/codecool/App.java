package com.codecool;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class App 
{
    public static void main( String[] args )
    {
        try {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/", new WebHandler());
        server.setExecutor(null);
        server.start();
    } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
