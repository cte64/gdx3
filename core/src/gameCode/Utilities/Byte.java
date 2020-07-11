package gameCode.Utilities;

public class Byte {


    public byte data[];

    public Byte(StringUtils str) {
        data = str.data.getBytes();
    }

    public Byte(String str) {
        data = str.getBytes();
    }



}
