import java.util.*;
import java.io.*;

public class asdf {
    public static void main(String[] args) throws Throwable {

        RandomAccessFile plgnaj = new RandomAccessFile("PlGnAJ.dat", "rw");
        RandomAccessFile plcaaj = new RandomAccessFile("PlCaAJ.dat", "rw");
        RandomAccessFile plgn = new RandomAccessFile("PlGn.dat", "rw");
        RandomAccessFile plca = new RandomAccessFile("PlCa.dat", "rw");

        plgnaj.seek(0x54340);
        plcaaj.seek(0x55e20);

        byte[] buffer = new byte[0x26c5];

        plcaaj.read(buffer);
        plgnaj.write(buffer);

        MotherCommand gnuptilt = new MotherCommand(
                plgn.getInt(),
                plgn.getInt(),
                plgn.getInt(),
                plgn.getInt(),
                plgn.getInt());
    }

    private static void overwriteAnimation(
            MotherCommand target,
            RandomAccessFile targetAj,
            MotherCommand source,
            RandomAccessFile sourceAj) {
        if (target.ajLength < source.ajLength) {
            throw new RuntimeException("source > target");
        }

        byte[] buffer = new byte[source.ajLength];
        sourceAj.seek(source.ajPointer);
        sourceAj.

        targetAj.seek(target.ajPointer);
    }

    private static class MotherCommand {
        public final int unknown0;
        public final int ajPointer;
        public final int ajLength;
        public final int commandListPointer; // needs +0x20
        public final int unknown4;

        public MotherCommand(int unknown0, int ajPointer, int ajLength, int commandListPointer, int unknown4) {
            this.unknown0 = unknown0;
            this.ajPointer = ajPointer;
            this.ajLength = ajLength;
            this.commandListPointer = commandListPointer;
            this.unknown4 = unknown4;
        }
    }
}
