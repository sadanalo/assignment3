package bgu.spl.net.api.bidi;

import com.sun.xml.internal.bind.v2.TODO;

import java.io.Serializable;

public abstract class  Message implements Serializable {
private int opCode;

    public int getOpCode(){
        return this.opCode;   //TODO find out if this method works for every other message//

    }

}
