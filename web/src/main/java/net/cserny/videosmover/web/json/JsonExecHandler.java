package net.cserny.videosmover.web.json;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public abstract class JsonExecHandler implements HttpHandler {

    private static final String JAVA_COMMAND = "/usr/lib/jvm/jdk-11.0.1/bin/java";
    private static final String LIB_FOLDER = "/home/leonardo/workspace/projects/java/concurrentrequests/target/";

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Map<String, String> requestParamsMap = parseQueryString(exchange.getRequestURI().getQuery());
        String jar = requestParamsMap.get("jar");
        String threads = requestParamsMap.get("threads");
        String url = requestParamsMap.get("url");

        Process proc = Runtime.getRuntime().exec(JAVA_COMMAND + " -jar " + LIB_FOLDER + jar + " " + threads + " " + url);

        BufferedReader inReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        String line;
        while ((line = inReader.readLine()) != null) {
            byteArrayOutputStream.write((line + "\n").getBytes(StandardCharsets.UTF_8));
        }
        inReader.close();
        byte[] response = byteArrayOutputStream.toByteArray();
        byteArrayOutputStream.close();

        exchange.sendResponseHeaders(200, response.length);
        exchange.getResponseHeaders().add("Content-Type", "text/html");

        OutputStream outputStream = exchange.getResponseBody();
        outputStream.write(response);
        outputStream.flush();
    }

    private static Map<String, String> parseQueryString(String qs) {
        Map<String, String> result = new HashMap<>();
        if (qs == null) {
            return result;
        }

        int last = 0, next, l = qs.length();
        while (last < l) {
            next = qs.indexOf('&', last);
            if (next == -1) {
                next = l;
            }

            if (next > last) {
                int eqPos = qs.indexOf('=', last);
                if (eqPos < 0 || eqPos > next) {
                    result.put(URLDecoder.decode(qs.substring(last, next), StandardCharsets.UTF_8), "");
                } else {
                    result.put(URLDecoder.decode(qs.substring(last, eqPos), StandardCharsets.UTF_8),
                            URLDecoder.decode(qs.substring(eqPos + 1, next), StandardCharsets.UTF_8));
                }
            }
            last = next + 1;
        }

        return result;
    }
}
