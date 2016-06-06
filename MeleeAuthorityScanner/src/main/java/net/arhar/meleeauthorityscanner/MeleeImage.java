package net.arhar.meleeauthorityscanner;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;

public class MeleeImage {

	private static final int OffsetFSTOffset = 0x424;

    private RandomAccessFile meleeImageFile;

    private long FSTOffset;
    private long numberOfEntries;
    private long stringTableOffset;

    private MeleeImageFileSystem fileSystem;

    public MeleeImage(String filename) throws IOException {
        try {
            meleeImageFile = new RandomAccessFile(filename, "r");
        } catch (FileNotFoundException e) {
            throw new RuntimeException("unable to read file " + filename, e);
        }
        readSectors();
    }

    public RandomAccessFile getFile() {
        return meleeImageFile;
    }

    public long getFSTOffset() {
        return FSTOffset;
    }

    public long getStringTableOffset() {
        return stringTableOffset;
    }

    public boolean isOpen() {
        return meleeImageFile != null;
    }

    public MeleeImageFileSystem getFileSystem() {
        return fileSystem;
    }

	private void readSectors() throws IOException {
        meleeImageFile.seek(OffsetFSTOffset);
        byte[] buffer = new byte[4];
        meleeImageFile.read(buffer, 0, 4);
        checkByteOrder(buffer);
        FSTOffset = toUInt32(buffer, 0);

        meleeImageFile.read(buffer, 0, 4);
        checkByteOrder(buffer);
        toUInt32(buffer, 0);

        meleeImageFile.seek(FSTOffset + 0x8);
        meleeImageFile.read(buffer, 0, 4);
        checkByteOrder(buffer);
        numberOfEntries = toUInt32(buffer, 0);
        stringTableOffset = FSTOffset + (numberOfEntries * 0xC);
        initFileSystem();
	}

	private void initFileSystem() {
	    MeleeImageFile rootFolder = new MeleeImageFile("root", 0, 0, FSTOffset, true, null);
	    fileSystem = new MeleeImageFileSystem(this, rootFolder);
	    close();
	}

	private void close() {
		if (meleeImageFile != null) {
			try {
				meleeImageFile.close();
				meleeImageFile = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static long toUInt32(byte[] bytes, int offset) {
		long result = bytes[offset] & 0xff;
		result |= (bytes[offset + 1] & 0xff) << 8;
		result |= (bytes[offset + 2] & 0xff) << 16;
		result |= (bytes[offset + 3] & 0xff) << 24;

		return result & 0xFFFFFFFFL;
	}

    public static void checkByteOrder(byte[] buffer) {
		if (ByteOrder.nativeOrder().equals(ByteOrder.LITTLE_ENDIAN)) {
			reverse(buffer);
		}
    }

	private static void reverse(byte[] array) {
		if (array == null) {
			return;
		}
		int i = 0;
		int j = array.length - 1;
		byte tmp;
		while (j > i) {
			tmp = array[j];
			array[j] = array[i];
			array[i] = tmp;
			j--;
			i++;
		}
	}
}