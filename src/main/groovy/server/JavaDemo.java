package server;

import groovyx.net.http.FromServer;
import groovyx.net.http.HttpBuilder;

import java.util.Map;
import java.util.function.BiFunction;

import static java.util.Collections.singletonMap;

@SuppressWarnings("unchecked")
public class JavaDemo {

    public static void main(final String[] args) {
        HttpBuilder http = HttpBuilder.configure(config -> {
            config.getRequest().setUri("http://localhost:4567/message");
            config.getResponse().success(new BiFunction<FromServer, Object, String>() {
                @Override public String apply(FromServer fromServer, Object body) {
                    return ((Map<String, Object>) body).get("text").toString();
                }
            });
        });

        String message = http.get(String.class, config -> {});

        System.out.println("Starting content: " + message);

        // update the content

        String updated = http.post(String.class, config -> {
            config.getRequest().setContentType("application/json");
            config.getRequest().setBody(singletonMap("text", "HttpBuilder works from Java too!"));
        });

        System.out.println("Updated content: " + updated);

        // delete the content

        String deleted = http.delete(String.class, config -> {});

        System.out.println("Post-delete content: " + deleted);
    }
}
