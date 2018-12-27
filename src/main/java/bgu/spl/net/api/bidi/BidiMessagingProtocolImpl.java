package bgu.spl.net.api.bidi;

public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol<T> {
    @Override
    public void start(int connectionId, Connections<T> connections) {

    }

    @Override
    public void process(T message) {

    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }
}
