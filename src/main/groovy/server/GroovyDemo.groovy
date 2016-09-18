package server

import groovyx.net.http.FromServer
import groovyx.net.http.HttpBuilder

class GroovyDemo {

    static void main(args) {
        HttpBuilder http = HttpBuilder.configure {
            request.uri = 'http://localhost:4567/message'
            response.success { FromServer from, Object body ->
                body.text
            }
        }

        // get the initial content

        String message = http.get(String) {}

        println "Starting content: $message"

        // update the content

        String updated = http.post(String) {
            request.contentType = 'application/json'
            request.body = { text 'HttpBuilder is alive!' }
        }

        println "Updated content: $updated"

        // delete the content

        String deleted = http.delete(String) {}

        println "Post-delete content: $deleted"
    }
}
