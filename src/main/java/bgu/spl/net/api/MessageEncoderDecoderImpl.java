package bgu.spl.net.api;

import bgu.spl.net.api.bidi.*;

import java.nio.ByteBuffer;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<Message> {


    private final ByteBuffer opCodeBuffer = ByteBuffer.allocate(2);
    private final ByteBuffer messageBuffer =  ByteBuffer.allocate( 1<<10 );
    private int zeroByteCounter = 0;

    @Override
    public Message decodeNextByte(byte nextByte) {

        if (!opCodeBuffer.hasRemaining()) {
            return popMessage(opCodeBuffer.getShort(), nextByte);
        }
        opCodeBuffer.put(nextByte);
        if (!opCodeBuffer.hasRemaining()){
            messageBuffer.clear();   // make sure we are clearing the messageBuffer before start working with it//
        }
        return null; //not a opCode yet
    }

    @Override
    public byte[] encode(Message message) {
        messageBuffer.clear();
        switch (message.getOpCode()) {
            case 9:
                 byte zeroByte = (byte) 0;   //this is how you create zeroByte?

                messageBuffer.put(shortToBytes(message.getOpCode()));
                messageBuffer.put(((NotificationMsg) message).getType());
                messageBuffer.put(((NotificationMsg) message).getPostingUser().getBytes());
                messageBuffer.put(zeroByte);
                messageBuffer.put(((NotificationMsg) message).getContent().getBytes());
                messageBuffer.put(zeroByte);
                break;
            case 11:
                messageBuffer.put(shortToBytes(message.getOpCode()));
                messageBuffer.put(shortToBytes(((ErrorMsg) message).getMsgRelatedOpcode()));
                break;
            case 10:
               if (message instanceof UserListAckMsg){
                   messageBuffer.put(shortToBytes(message.getOpCode()));
                   messageBuffer.put(shortToBytes(((UserListAckMsg) message).getMsgRelatedOpcode()));
                   messageBuffer.put(shortToBytes(((UserListAckMsg) message).getNumOfusers()));
                   messageBuffer.put(((UserListAckMsg) message).getUsersList().getBytes());
                   break;
               } else{
                   if(message instanceof StatAckMsg){
                       messageBuffer.put(shortToBytes(message.getOpCode()));
                       messageBuffer.put(shortToBytes(((StatAckMsg) message).getMsgRelatedOpcode()));
                       messageBuffer.put(shortToBytes(((StatAckMsg) message).getNumOfPosts()));
                       messageBuffer.put(shortToBytes(((StatAckMsg) message).getNumOfFollowers()));
                       messageBuffer.put(shortToBytes(((StatAckMsg) message).getNumOfFollowing()));
                   }
                   else{
                       if(message instanceof FollowAckMsg){
                           messageBuffer.put(shortToBytes(message.getOpCode()));
                           messageBuffer.put(shortToBytes(((FollowAckMsg) message).getMsgRelatedOpcode()));
                           messageBuffer.put(shortToBytes(((FollowAckMsg) message).getNumOfusers()));
                           messageBuffer.put(((FollowAckMsg) message).getUserNameList().getBytes());
                       }
                   }
               }

        }
        messageBuffer.flip();
        return messageBuffer.array();
    }
    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    private Message popMessage(short opCode, byte nextByte) {

        switch (opCode){
            case 1:
                return popRegisterLoginMsg(opCode,nextByte);
            case 2:
                return popRegisterLoginMsg(opCode, nextByte);
            case 3:
                return new LogoutMsg();
            case 4:
                return popFollowMsg( nextByte);
            case 5:
                return popPostMsg(nextByte);
            case 6:
                return popPmMsg(nextByte);
            case 7:
                return new UserListMsg();
            case 8:
                return popStatMsg(nextByte);
        }
        return null;
    }

    private Message popStatMsg(byte nextByte) {
        if (nextByte != '\0') {
            messageBuffer.put(nextByte);
            return null;
        }
        messageBuffer.flip();
        String userName = "";
        while (messageBuffer.hasRemaining()){
            userName += messageBuffer.getChar();
        }
        messageBuffer.clear();
        return new StatMsg(userName);
    }

    private Message popPmMsg(byte nextByte) {
        if (nextByte == '\0') {
            ++zeroByteCounter;
            if (zeroByteCounter == 2){   //we are counting the zero bytes to determine what we are reading
                String userName = "";
                String content = "";
                zeroByteCounter = 0;
                messageBuffer.flip();
                while(messageBuffer.hasRemaining()) {
                    char c = messageBuffer.getChar();
                    if (c != '\0'){
                        switch (zeroByteCounter){
                            case 0:
                                userName += c;
                                break;
                            case 1:
                                content += c;
                                break;
                            case 2:
                                zeroByteCounter = 0;
                                messageBuffer.clear();
                                return new LoginMsg(userName, content);
                        }
                    }
                    else{ // we are reading zero//
                        ++zeroByteCounter;
                    }
                }
            }
            messageBuffer.put(nextByte); //message is not complete
            return null;
        }
        messageBuffer.put(nextByte); // next byte is not zero//
        return null;
    }

    private Message popPostMsg(byte nextByte) {
        if (nextByte != '\0') {
            messageBuffer.put(nextByte);
            return null;
        }
        messageBuffer.flip();
        String content = "";
        while (messageBuffer.hasRemaining()){
            content += messageBuffer.getChar();
        }
        messageBuffer.clear();
        return new PostMsg(content);

    }

    private Message popFollowMsg( byte nextByte ) {

        if (nextByte == '\0') {
            ++zeroByteCounter;
            if (zeroByteCounter == messageBuffer.getShort(1)){
                String userNames = "";
                String password = "";
                messageBuffer.flip();  //limit = position, position = 0//
               byte followUnfollow = messageBuffer.get(); // move position = 1//
                short numOfUsers = messageBuffer.getShort(); // move position = 3
                while(messageBuffer.hasRemaining()) {   //position != limit
                    char c = messageBuffer.getChar(); //and do position ++//
                    userNames += c;
                }
                messageBuffer.clear();
                zeroByteCounter = 0;
                return new FollowMsg(followUnfollow, numOfUsers, userNames);
            }else{
                messageBuffer.put(nextByte);
                return null;
            }
        }
        messageBuffer.put(nextByte); //message is not complete
        return null;
    }

    private Message popRegisterLoginMsg(short opCode, byte nextByte) {

        if (nextByte == '\0') {
            ++zeroByteCounter;
            if (zeroByteCounter == 2){
                String userName = "";
                String password = "";
                zeroByteCounter = 0;
                messageBuffer.flip();
                while(messageBuffer.hasRemaining()) {
                    char c = messageBuffer.getChar();
                    if (c != '\0'){
                        switch (zeroByteCounter){
                            case 0:
                                userName += c;
                                break;
                            case 1:
                                password += c;
                                break;
                            case 2:
                                zeroByteCounter = 0;
                                if(opCode == 1) {
                                    messageBuffer.clear();
                                    return new RegisterMsg(userName, password);
                                }
                                if(opCode == 2){
                                    messageBuffer.clear();
                                    return new LoginMsg(userName, password);
                                }
                                break;
                        }
                    }
                    else{ // we are reading zero//
                        ++zeroByteCounter;
                    }
                }
            }
            messageBuffer.put(nextByte); //message is not complete
            return null;
        }
        messageBuffer.put(nextByte); // next byte is not zero//
        return null;
    }

}

