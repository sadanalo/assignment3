package bgu.spl.net.api.bidi;

import com.sun.xml.internal.bind.v2.TODO;

import java.io.Serializable;

public class  Message implements Serializable {  // not sure if every other message need to do "implements Serializable//

    private short opCode;
    public Message (short opCode){
        this.opCode = opCode;
    }

    public int getOpCode(){
        return this.opCode;
    }
}
