package server

import groovy.json.JsonSlurper
import groovy.transform.Immutable
import spark.ResponseTransformer

import java.util.concurrent.atomic.AtomicReference

import static groovy.json.JsonOutput.toJson
import static server.Message.createMessage
import static server.Message.emptyMessage
import static spark.Spark.delete
import static spark.Spark.get
import static spark.Spark.post

class RestServer {

    private static final String CONTENT_TYPE = 'application/json'
    private static final ResponseTransformer transformer = new JsonTransformer()
    private static final AtomicReference<Message> holder = new AtomicReference<>(emptyMessage())
    private static final JsonSlurper slurper = new JsonSlurper()

    static void main(args) {
        get('/message', { req, res ->
            res.type(CONTENT_TYPE)
            holder.get()
        }, transformer)

        delete('/message', { req, res ->
            res.type(CONTENT_TYPE)

            def msg = emptyMessage()
            holder.set(msg)
            msg
        }, transformer)

        post('/message', { req, res ->
            res.type(CONTENT_TYPE)

            def json = slurper.parse(req.bodyAsBytes())
            def msg = createMessage(json.text)
            holder.set(msg)
            msg
        }, transformer)
    }
}

@Immutable
class Message {
    Date timestamp
    String text

    static Message emptyMessage() {
        createMessage('n/a')
    }

    static Message createMessage(String text){
        new Message(new Date(), text)
    }
}

class JsonTransformer implements ResponseTransformer {

    @Override
    String render(final Object model) throws Exception {
        toJson(model)
    }
}