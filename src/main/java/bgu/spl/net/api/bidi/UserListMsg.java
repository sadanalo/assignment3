package bgu.spl.net.api.bidi;

public class UserListMsg implements Message {
    private int opCode;

    public UserListMsg (int opCode){
        this.opCode= opCode;
    }

}
