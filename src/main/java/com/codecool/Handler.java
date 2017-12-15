package com.codecool;

import com.sun.net.httpserver.HttpExchange;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class Handler {

    private String name;

    public Handler() {
        this.name = "Unknown";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @WebRoute(requestMethod = "GET")
    public void handleIndex(HttpExchange httpExchange) throws IOException {
        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/index.html");
        JtwigModel model = new JtwigModel();
        String response = template.render(model);
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    @WebRoute(requestMethod = "POST")
    public void handleForm(HttpExchange httpExchange)throws IOException {

        InputStreamReader isr = new InputStreamReader(httpExchange.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String formData = br.readLine();
        Map<String, String> parsedForm = parseFormData(formData);
        String name = parsedForm.get("name");
        setName(name);

        httpExchange.getResponseHeaders().add("Location", "/hello");
        httpExchange.sendResponseHeaders(302, -1);
    }

    @WebRoute(requestMethod = "GET", path = "/hello")
    public void handleHello(HttpExchange httpExchange)  throws IOException {

        JtwigTemplate template = JtwigTemplate.classpathTemplate("templates/hello.html");
        JtwigModel model = new JtwigModel();
        model.with("name", getName());
        String response = template.render(model);
        httpExchange.sendResponseHeaders(200, response.getBytes().length);
        OutputStream os = httpExchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }

    private Map<String, String> parseFormData(String formData) throws UnsupportedEncodingException {
        Map<String, String> parsedForm = new HashMap<>();
        String[] pairs = formData.split("&");

        for(String pair: pairs){
            String decodedPair = new URLDecoder().decode(pair, "UTF-8");
            String [] singleSplitedPair = decodedPair.split("=");

            parsedForm.put(singleSplitedPair[0], singleSplitedPair[1]);
        }
        return parsedForm;
    }
}
