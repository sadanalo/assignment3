package bgu.spl.net.api.bidi;

public class BidiMessagingProtocolImpl<Message> implements BidiMessagingProtocol<Message> {

    private int conccectionsId;
    private Connections connections;
    private boolean shouldTerminate;

    @Override
    public void start(int connectionId, Connections connections) {
        this.conccectionsId = connectionId;
        this.connections = connections;
    }

    @Override
    public void process(Message message) {

        connections.send(conccectionsId, message);

    }

    @Override
    public boolean shouldTerminate() {
        return this.shouldTerminate;
    }
}
