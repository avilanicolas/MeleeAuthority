package net.arhar.meleeauthorityscanner;

public class MeleeImageFile {
    public final String name;
    public final long size, fileOffset, fstOffset;
    public final boolean isFolder;

    public byte[] data;

    public MeleeImageFile(
            String name,
            long size,
            long fileOffset,
            long fstOffset,
            boolean isFolder,
            byte[] data) {
        this.name = name;
        this.size = size;
        this.fileOffset = fileOffset;
        this.fstOffset = fstOffset;
        this.isFolder = isFolder;
        this.data = data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }
}