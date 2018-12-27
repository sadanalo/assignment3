package bgu.spl.net.api.bidi;

public class StatMsg implements Message {
    private String userName;
    private int opCode;

    public StatMsg(String userName, int opCode){
        this.userName = userName;
        this.opCode = opCode;
    }
}
