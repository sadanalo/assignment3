package bgu.spl.net.api.bidi;

public class LoginMsg implements Message {
    private int opCode;
    public LoginMsg(int opCode){
        this.opCode = opCode;
    }

}
