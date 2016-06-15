package net.arhar.meleeauthorityscanner;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Enum name represents internal character code
 */
public enum Character {

    Kp(0x5, "Bowser"),
    Ca(0x0, "Captain Falcon"),
    Dr(0x16, "Doctor Mario"),
    Dk(0x1, "Donkey Kong"),
    Fc(0x14, "Falco"),
    Fx(0x2, "Fox"),
    Gw(0x3, "Game & Watch"),
    Gn(0x19, "Ganondorf"),
    Pp(0xe, "Ice Climbers (Popo)"),
//    Nn(0x9999, "Ice Climbers (Nana)", 0x1188, 0x004d30, 0x006b48), // Nana is the same as popo but without animation commands
    Pr(0xf, "Jigglypuff"),
    Kb(0x4, "Kirby"),
    Lg(0x7, "Luigi"),
    Lk(0x6, "Link"),
    Mr(0x8, "Mario"),
    Ms(0x9, "Marth"),
    Mt(0xa, "Mewtwo"),
    Ns(0xb, "Ness"),
    Pe(0xc, "Peach"),
    Pc(0x18, "Pichu"),
    Pk(0xd, "Pikachu"),
    Fe(0x17, "Roy"),
    Ss(0x10, "Samus"),
    Sk(0x13, "Sheik"),
    Ys(0x11, "Yoshi"),
    Cl(0x15, "Young Link"),
    Zd(0x12, "Zelda");

    // nonplayable characters, removing because hitbox calculation makes duplicates in sql
//    Bo(0x1b, "Wireframe (Male)", 0x31C8, 0x006214, 0x7DBC),
//    Gl(0x1c, "Wireframe (Female)", 0x2FF4, 0x006130, 0x7CD8),
//    Gk(0x1d, "Giga Bowser", 0x34D0, 0x007c40, 0x000099E0),
//    Sb(0x1f, "Sandbag", 0x1464, 0x001eb4, 0x00003A74),
//    Mh(0x1a, "Master Hand", 0x914 + 0x20, 0x00002824, 0x0000487C),
//    Ch(0x1e, "Crazy Hand", 0x898 + 0x20, 0x00002C40, 0x00004C80);

    public final int id;
    public final String fullName;

    private Character(int id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }

    private static final int DATA_OFFSET = 0x20;

    private static String getString(ByteBuffer file, int stringPointer) {
        if (stringPointer > file.limit() || stringPointer < 0) {
            throw new RuntimeException(String.format("out of range. stringPointer: %08X, file.limit(): %08X", stringPointer, file.limit()));
//            return "OUT_OF_BOUNDS";
        }
        file.position(stringPointer);
        StringBuilder builder = new StringBuilder();
        while (true) {
            char next = (char) file.get();
            if (next == '\0') {
                break;
            }
            builder.append(next);
        }
        return builder.toString();
    }

    public static Map<Character, List<Animation>> generateAllAnimations(MeleeImageFileSystem fileSystem) {
        Map<Character, List<Animation>> animations = new HashMap<>();

//        for (Character character : Character.values()) {
        Character character = Character.Ms;
            ByteBuffer pldat = ByteBuffer.wrap(fileSystem.getFileData("Pl" + character.name() + ".dat"));
            ByteBuffer ajdat = ByteBuffer.wrap(fileSystem.getFileData("Pl" + character.name() + "AJ.dat"));

            DatHeader plDatHeader = new DatHeader(pldat);

            // pldat only has one root node
            RootNode plDatRootNode = new RootNode(pldat, plDatHeader, plDatHeader.getRootOffset0());

            FtDataHeader ftDataHeader = new FtDataHeader(pldat, plDatHeader, plDatRootNode);

            // print out mother commands
            for (int i = 0; i < ftDataHeader.getNumSubActions(); i++) {
                SubActionHeader motherCommand = new SubActionHeader(pldat, ftDataHeader, i);
                SubActionAJHeader ajHeader = new SubActionAJHeader(ajdat, motherCommand);
//                System.out.println(motherCommand);
//                System.out.println("frames: " + ajHeader.frameCount);
            }

            // aj stuff
            // if each animation in the aj file is like its own dat file, then the string table goes at the end, right?
            // and the info from the web page would be a root node right before the string table!
            DatHeader ajDatHeader = new DatHeader(ajdat);
            RootNode ajDatRootNode = new RootNode(ajdat, ajDatHeader, ajDatHeader.getRootOffset0());
//        }

        return animations;
    }
    public static void main(String[] args) {
        System.out.printf("%08X\n", Float.floatToIntBits(39f));
    }

    /**
     * Dat Header
     * appears at the beginning of each dat file
     * data section starts at the end of the Dat Header, at 0x20
     *
     * 0x00 filesize
     * 0x04 data block size
     * 0x08 relocation table count
     * 0x0C root count
     * 0x10 secondary root count
     * 0x14 version? "001B"
     * 0x18 undefined
     * 0x1C undefined
     */
    private static class DatHeader {
        public final int filesize;
        public final int dataBlockSize;
        public final int relocationTableCount;
        public final int rootCount0x0C;
        public final int rootCount0x10;
        public final int version;
        public final int undefined0x18;
        public final int undefined0x1C;

        public DatHeader(ByteBuffer pldat) {
            pldat.position(0);
            filesize = pldat.getInt();
            dataBlockSize = pldat.getInt();
            relocationTableCount = pldat.getInt();
            rootCount0x0C = pldat.getInt();
            rootCount0x10 = pldat.getInt();
            version = pldat.getInt();
            undefined0x18 = pldat.getInt();
            undefined0x1C = pldat.getInt();
        }

        // TODO what is this relocation table for?
        public int getRelocationOffset() {
            return DATA_OFFSET + dataBlockSize;
        }
        public int getRootOffset0() {
            return getRelocationOffset() + relocationTableCount * 4;
        }
        public int getRootOffset1() {
            return getRootOffset0() + rootCount0x0C * 8;
        }
        public int getTableOffset() {
            return getRootOffset1() + rootCount0x10 * 8;
        }

        public int getNumRootNodes() {
            return rootCount0x0C + rootCount0x10;
        }
    }

    /**
     * Root Node
     * one or more appear near the end of dat files
     * has a pointer to data section
     *
     * 0x00 data pointer
     * 0x04 string pointer
     */
    private static class RootNode {
        public static final int ROOT_NODE_LENGTH_BYTES = 8;

        public final int dataPointer;
        public final int stringPointer;
        public final String string;

        public RootNode(ByteBuffer file, DatHeader datHeader, int rootOffset) {
            file.position(rootOffset);
            dataPointer = file.getInt();
            stringPointer = file.getInt();
            // the string comes from the string table at the end of the file
            string = getString(file, datHeader.getTableOffset() + stringPointer);
        }

        public int getDataOffset() {
            return DATA_OFFSET + dataPointer;
        }
    }

    /**
     * Ft Data Header
     * contains pointers to character info sections
     * pointed to by a root node
     *
     * 0x00 attributes start pointer
     * 0x04 attributes end pointer
     * 0x08 undefined
     * 0x0C subaction headers list start pointer
     * 0x10 undefined
     * 0x14 subaction headers list end pointer
     * 0x18 undefined
     */
    private static class FtDataHeader {
        public final int attributesStart;
        public final int attributesEnd;
        public final int undefined0x08;
        public final int subactionsStart;
        public final int undefined0x10;
        public final int subactionsEnd;
        public final int undefined0x18;

        public FtDataHeader(ByteBuffer pldat, DatHeader datHeader, RootNode rootNode) {
            pldat.position(rootNode.getDataOffset());
            attributesStart = pldat.getInt();
            attributesEnd = pldat.getInt();
            undefined0x08 = pldat.getInt();
            subactionsStart = pldat.getInt();
            undefined0x10 = pldat.getInt();
            subactionsEnd = pldat.getInt();
            undefined0x18 = pldat.getInt();
        }

        public int getNumSubActions() {
            return (subactionsEnd - subactionsStart) / SubActionHeader.SUBACTION_HEADER_LENGTH_BYTES;
        }
        public int getSubactionsStart() {
            return DATA_OFFSET + subactionsStart;
        }
        public int getSubactionsEnd() {
            return DATA_OFFSET + subactionsEnd;
        }
        public int getAttributesStart() {
            return DATA_OFFSET + attributesStart;
        }
        public int getAttributesEnd() {
            return DATA_OFFSET + attributesEnd;
        }
    }

    /**
     * SubAction Header
     * called "Mother Command" here: http://smashboards.com/threads/animation-hacking-documentation.426374/
     * FtDataHeader has a pointer to a list of these which exist in the data section
     *
     * 0x00 string pointer (in data section)
     * 0x04 Pl__AJ.dat pointer
     * 0x08 Pl__AJ.dat length
     * 0x0C subaction command list pointer
     * 0x10 undefined
     * 0x14 undefined
     */
    private static class SubActionHeader {
        public static final int SUBACTION_HEADER_LENGTH_BYTES = 6 * 4;

        public final int stringPointer;
        public final String string;
        public final int ajPointer;
        public final int ajLength;
        public final int commandListPointer;
        public final int undefined0x10;
        public final int undefined0x14;

        public SubActionHeader(ByteBuffer pldat, FtDataHeader ftDataHeader, int index) {
            pldat.position(ftDataHeader.getSubactionsStart() + index * SUBACTION_HEADER_LENGTH_BYTES);
            stringPointer = pldat.getInt();
            ajPointer = pldat.getInt();
            ajLength = pldat.getInt();
            commandListPointer = pldat.getInt();
            undefined0x10 = pldat.getInt();
            undefined0x14 = pldat.getInt();
            string = getString(pldat, DATA_OFFSET + stringPointer);
        }

        public int getAjOffset() {
            return DATA_OFFSET + ajPointer;
        }

        @Override
        public String toString() {
            return String.format("%08X %08X %08X %08X %08X %08X <<%s>>",
                stringPointer,
                ajPointer,
                ajLength,
                commandListPointer,
                undefined0x10,
                undefined0x14,
                string);
        }
    }

    private static class SubActionAJHeader {
        public final int unknown0x00;
        public final int unknown0x04;
        public final float frameCount;
        public final int boneListPointer;
        public final int animationInfo;

        public SubActionAJHeader(ByteBuffer ajdat, SubActionHeader subAction) {
            ajdat.position(subAction.getAjOffset());
            unknown0x00 = ajdat.getInt();
            unknown0x04 = ajdat.getInt();
            frameCount = ajdat.getFloat();
            boneListPointer = ajdat.getInt();
            animationInfo = ajdat.getInt();
        }
    }
}