package bgu.spl.net.api.bidi;

import java.io.Serializable;

public class UserListMsg extends Message {
    private int opCode;

    public UserListMsg (int opCode){
        this.opCode= opCode;
    }

}
