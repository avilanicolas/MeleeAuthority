package net.arhar.meleeauthorityscanner;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

public class MeleeImageFileSystem {

    private Map<String, MeleeImageFile> cachedFiles;

    private MeleeImage meleeFile;
    private MeleeImageFile currentLoadedFile;

    public MeleeImageFileSystem(MeleeImage meleeFile, MeleeImageFile imageFile) {
        cachedFiles = new HashMap<>();
        this.meleeFile = meleeFile;
        this.currentLoadedFile = imageFile;
		try {
			readDOL();
			readFiles(imageFile, meleeFile.getStringTableOffset());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

	private void readFiles(MeleeImageFile isoFolder, long offset) throws IOException {
		long currentOffset = isoFolder.fstOffset + 0xC;
		while (currentOffset < offset) {
			meleeFile.getFile().seek(currentOffset);
			boolean isFolder = meleeFile.getFile().readByte() == 1;

			byte[] buffer = new byte[4];
			meleeFile.getFile().read(buffer, 1, 3);
			buffer[0] = 0;
			MeleeImage.checkByteOrder(buffer);
			long stringOffset = MeleeImage.toUInt32(buffer, 0);
			String isoFileName = getAbsoluteFileName(stringOffset);

			meleeFile.getFile().read(buffer, 0, 4);
			MeleeImage.checkByteOrder(buffer);
			long fileOffset = MeleeImage.toUInt32(buffer, 0);

			meleeFile.getFile().read(buffer, 0, 4);
			MeleeImage.checkByteOrder(buffer);

			long fileSize = MeleeImage.toUInt32(buffer, 0);

			MeleeImageFile file = new MeleeImageFile(isoFileName, fileSize, fileOffset,
					currentOffset, isFolder, null);

			if (isMovesetFile(isoFileName)) {
				cacheFile(file);
			}

			currentOffset += 0xC;

			if (isFolder) {
				currentOffset = meleeFile.getFSTOffset() + (fileSize * 0xC);
				readFiles(file, currentOffset);
			}
		}

	}

	private void readDOL() throws IOException {
		if (meleeFile.getFile().length() > 0x438L) {
			// Start.dol file load
			meleeFile.getFile().seek(0x400L);
			int pos;
			int size;
			int fileOff;
			pos = 0;
			size = 0x2440;
			meleeFile.getFile().readInt();
			meleeFile.getFile().seek(
					meleeFile.getFile().getChannel().position() + 0x1cL);
			fileOff = meleeFile.getFile().readInt();
			pos = meleeFile.getFile().readInt();
			meleeFile.getFile().readInt();
			size = pos - fileOff;
			meleeFile.getFile()
					.seek(meleeFile.getFile().getChannel().position() + 8L);
			meleeFile.getFile().readInt();
			MeleeImageFile startDol = new MeleeImageFile("Start.dol", size, fileOff,
					meleeFile.getFSTOffset(), false, null);
			cacheFile(startDol);

		}
	}

	private void cacheFile(MeleeImageFile file) {
		try {
			readData(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		cachedFiles.put(file.name, file);
	}

	/**
	 * Reads the given ISO file into bytes and stores them for later use.
	 *
	 * @param file
	 *            - the file to read.
	 * @throws Exception
	 */
	private void readData(MeleeImageFile file) throws Exception {

		try {

			long fileSize = file.size;
			long offset = file.fileOffset;
			long totalLength = file.size + file.fileOffset;

			FileChannel inChannel = meleeFile.getFile().getChannel();

			while (offset < totalLength) {
				ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
				long len = offset + fileSize < totalLength ? fileSize
						: totalLength - offset;
				inChannel.read(buffer, offset);
				buffer.flip();
				file.setData(buffer.array());
				buffer.clear();
				offset += len;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets the file name that is stored in the ISO.
	 *
	 * @param stringOffset
	 * @return
	 * @throws IOException
	 */
	private String getAbsoluteFileName(long stringOffset) throws IOException {
		String filename = "";
		if (meleeFile.getStringTableOffset() == 0)
			return null;

		long position = meleeFile.getFile().getFilePointer();

		meleeFile.getFile().seek(meleeFile.getStringTableOffset() + stringOffset);
		int buffer = 1;
		while (buffer != 0) {
			buffer = meleeFile.getFile().readByte();
			if (buffer != 0)
				filename += (char) buffer;
		}
		meleeFile.getFile().seek(position);
		return filename;
	}

	private boolean isMovesetFile(String name) {// should be 34 files
		return (name.startsWith("Pl") && name.replace(".dat", "").length() == 4);
	}

	public boolean replaceFile(MeleeImageFile file, byte[] data) throws IOException {
		if (meleeFile.isOpen()) {
			if (file.size != data.length) {
				System.out.println("Length does not match!"
						+ " File to Replace: " + file.size + " With: "
						+ data.length);
				return false;
			}
			meleeFile.getFile().seek(file.fileOffset);
			meleeFile.getFile().write(data, 0, data.length);
			file.setData(data);

			return true;
		}
		System.out.println("File replace failed! iso size:" + data.length + " replace size:" + data.length);
		return false;

	}

	public byte[] getFileData(String fileName) {
		MeleeImageFile file = getISOFile(fileName);
		if (file.data != null) {
			currentLoadedFile = file;
			return file.data;
		}

		return null;
	}

	public MeleeImageFile getISOFile(String fileName) {
		MeleeImageFile info = cachedFiles.get(fileName);
		if (info == null) {
			throw new RuntimeException("Could not load cached ISO file!");
		}
		return info;
	}

	public void clearCachedISOFiles() {
		cachedFiles.clear();
	}

	public Map<String, MeleeImageFile> getISOFiles() {
		return cachedFiles;
	}

	public MeleeImageFile getCurrentFile() {
		return currentLoadedFile;
	}
}