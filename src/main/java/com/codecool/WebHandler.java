package com.codecool;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class WebHandler implements HttpHandler {

    private Handler handler;

    public WebHandler() {
        this.handler = create();
    }

    private Handler create() {
        Handler handlerObj = null;
        try {
            Class c = Class.forName("com.codecool.Handler");
            Constructor constructor = c.getConstructor();
            handlerObj = (Handler) constructor.newInstance();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return handlerObj;
    }

    public void handle(HttpExchange httpExchange) throws IOException {

        String path = httpExchange.getRequestURI().getPath();
        String method = httpExchange.getRequestMethod();

        try {
            Class c = Class.forName("com.codecool.Handler");
            Method[] methods = c.getMethods();
            Method properMethod = selectProperMethod(path, methods, method);
            properMethod.invoke(handler, httpExchange);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Method selectProperMethod(String uri, Method[] methods, String requestedMethod) {
        String path;
        String supportedMethod;
        Method selectedMethod = null;

        for (Method method : methods) {
            WebRoute annotation = method.getAnnotation(WebRoute.class);
            path = annotation.path();
            supportedMethod = annotation.requestMethod();

           if (uri.equals(path) && requestedMethod.equals(supportedMethod)) {
               selectedMethod = method;
               break;
           }
        }
        return selectedMethod;
    }
}