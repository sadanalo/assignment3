package bgu.spl.net.api.bidi;

import java.nio.channels.SocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentHashMap;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessageEncoderDecoderImpl;
import bgu.spl.net.srv.BlockingConnectionHandler;
import bgu.spl.net.srv.ConnectionHandler;
import bgu.spl.net.srv.NonBlockingConnectionHandler;

public class ConnectionsImpl<T> implements Connections<T> {



    private ConcurrentHashMap<Integer, ConnectionHandler<T>> handlersByClient;   // i dont know why concurrent, maybe it is not necessary//

    public ConnectionsImpl() {
        this.handlersByClient = new ConcurrentHashMap<>();
    }

    @Override
    public boolean send(int connectionId, T msg) {
        boolean sendingWasSuccessful;
        if (handlersByClient.containsKey(connectionId)) {  // client is not registered(or something)//
            handlersByClient.get(connectionId).send(msg);
            sendingWasSuccessful = true;
        } else {
            sendingWasSuccessful = false;
        }
        return sendingWasSuccessful;
    }

    @Override
    public void broadcast(T msg) {
        for (Integer client : handlersByClient.keySet()) {
            handlersByClient.get(client).send(msg);
        }
    }

    @Override
    public void disconnect(int connectionId) {
        if (handlersByClient.containsKey(connectionId)) {
            handlersByClient.remove(connectionId);
        }
    }
    public void connect(int connectionId, ConnectionHandler<T> handler){
        this.handlersByClient.put(connectionId,handler);

    }


}
