package bgu.spl.net.api.bidi;

public class LogoutMsg implements Message {
    private int opCode;

    public LogoutMsg (int opCode){
        this.opCode = opCode;
    }
}
