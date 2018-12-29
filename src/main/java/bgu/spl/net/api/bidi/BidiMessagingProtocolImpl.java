package bgu.spl.net.api.bidi;

public class BidiMessagingProtocolImpl<Message> implements BidiMessagingProtocol<Message> {

    private int connectionsId;
    private Connections connections;
    private boolean shouldTerminate;

    @Override
    public void start(int connectionId, Connections connections) {
        this.connectionsId = connectionId;
        this.connections = connections;
    }

    @Override
    public void process(Message message) {

    }

    @Override
    public boolean shouldTerminate() {
        return this.shouldTerminate;
    }
}
