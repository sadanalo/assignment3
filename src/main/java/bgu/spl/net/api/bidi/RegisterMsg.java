package bgu.spl.net.api.bidi;

public class RegisterMsg implements Message {

    private String userName;
    private String password;
    private int opCode;

    public RegisterMsg(int opCode, String userName, String password){
        this.userName = userName;
        this.password = password;
        this.opCode = opCode;
    }
}
