package bgu.spl.net.api.bidi;

public class PostMsg implements Message {
    private String content;
    private int opCode;

   public PostMsg(String content, int opCode){

        this.content = content;
        this.opCode= opCode;
    }
}
