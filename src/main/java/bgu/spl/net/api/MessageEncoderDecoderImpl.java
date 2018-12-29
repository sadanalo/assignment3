package bgu.spl.net.api;

import java.io.*;
import java.nio.ByteBuffer;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<Serializable> {


    private final ByteBuffer lengthBuffer = ByteBuffer.allocate(4);
    private byte[] MessageBytes = null;
    private int MessageBytesIndex = 0;
    @Override
    public Serializable decodeNextByte(byte nextByte) {

        if (MessageBytes == null) { //indicates that we are still reading the length
            lengthBuffer.put(nextByte);
            if (!lengthBuffer.hasRemaining()) { //we read 4 bytes and therefore can take the opcode;
                lengthBuffer.flip();
                MessageBytes = new byte[getMessageLength(lengthBuffer.getInt())];
                MessageBytesIndex = 0;
                lengthBuffer.clear();
            }
        } else {
            MessageBytes[MessageBytesIndex] = nextByte;
            if (++MessageBytesIndex == MessageBytes.length) {
                Serializable result = deserializeObject();
                MessageBytes = null;
                return result;
            }
        }

        return null;
    }

    private int getMessageLength(int opCodeBufferInt) {
        return 0;
    }

    @Override
    public byte[] encode(Serializable message) {
        return serializeObject(message);
    }



    private Serializable deserializeObject() {
        try {
            ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(MessageBytes));
            return (Serializable) in.readObject();
        } catch (Exception ex) {
            throw new IllegalArgumentException("cannot deserialize Message", ex);
        }
    }

    private byte[] serializeObject(Serializable message) {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            //place holder for the object size
            for (int i = 0; i < 4; i++) {
                bytes.write(0);
            }
//we use ObjectOutput because you can't write object to ByteArrayOutputStream
// you can only write array of bites to ByteArrayOutputStream
// here we want to write Message//

            ObjectOutput out = new ObjectOutputStream(bytes);
            out.writeObject(message);
            out.flush();
            byte[] result = bytes.toByteArray();

            //now write the object size
            ByteBuffer.wrap(result).putInt(result.length - 4);
            return result;

        } catch (Exception ex) {
            throw new IllegalArgumentException("cannot serialize object", ex);
        }
    }
}
