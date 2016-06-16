package net.arhar.meleeauthorityscanner;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class DatReader {

    private static final int DATA_OFFSET = 0x20;

    public static Map<Character, List<Animation>> readAllAnimations(MeleeImageFileSystem fileSystem) {
        Map<Character, List<Animation>> charactersToAnimations = new LinkedHashMap<>();

        Map<Integer, Map<String, Integer>> subactionToNameCounts = new TreeMap<>();

        for (Character character : Character.values()) {
//        Character character = Character.Ms; {
            List<Animation> animations = new ArrayList<>();

            ByteBuffer pldat = ByteBuffer.wrap(fileSystem.getFileData("Pl" + character.name() + ".dat"));
            ByteBuffer ajdat = ByteBuffer.wrap(fileSystem.getFileData("Pl" + character.name() + "AJ.dat"));

            DatHeader plDatHeader = new DatHeader(pldat, 0);
            RootNode plDatRootNode = new RootNode(pldat, plDatHeader, plDatHeader.getRootOffset0());
            FtDataHeader ftDataHeader = new FtDataHeader(pldat, plDatRootNode);

            for (int i = 0; i < ftDataHeader.getNumSubActions(); i++) {
                SubActionHeader motherCommand = new SubActionHeader(pldat, ftDataHeader, i);
                System.out.printf("id: %03X  ", i);
                System.out.println(motherCommand);
                if (motherCommand.isSubActionNull(pldat)) {
                    // this character does not implement this SubAction, so skip it
                    // TODO maybe have null entries instead or some indication that this existed?
                    continue;
                }

                subactionToNameCounts.putIfAbsent(i, new LinkedHashMap<>());
                Map<String, Integer> nameCounts = subactionToNameCounts.get(i);
                nameCounts.putIfAbsent(motherCommand.shortName, 0);
                nameCounts.put(motherCommand.shortName, nameCounts.get(motherCommand.shortName) + 1);

                // inner-aj file dat file
                DatHeader ajDatHeader = new DatHeader(ajdat, motherCommand.ajPointer);
                RootNode ajRootNode = new RootNode(ajdat, ajDatHeader, ajDatHeader.getRootOffset0());
                AJDataHeader ajDataHeader = new AJDataHeader(ajdat, ajRootNode);

                // read this animation
//                animations.add(new Animation(pldat, motherCommand.getCommandListOffset(), ajDataHeader.frameCount));
            }

            charactersToAnimations.put(character, animations);
        }

        System.out.println();
        subactionToNameCounts.forEach((id, nameCounts) -> {
            System.out.printf("0x%03X\n", id);
            nameCounts.forEach((string, count) -> {
                System.out.printf("    \"%s\": %d\n", string, count);
            });
        });
        System.out.println();


        return charactersToAnimations;
    }

    public static Map<Character, Map<Attribute, Number>> readAllAttributes(MeleeImageFileSystem fileSystem) {
        Map<Character, Map<Attribute, Number>> charactersToAttributes = new LinkedHashMap<>();

        for (Character character : Character.values()) {
            Map<Attribute, Number> attributes = new LinkedHashMap<>();

            charactersToAttributes.put(character, attributes);
        }

        return charactersToAttributes;
    }

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

    /**
     * Header Data
     *
     * Data Block
     *
     * relocationTableCount # file offsets relative to data section (+0x20)
     *
     * two Root Node lists
     * root nodes are a 4 byte data offset and a 4 byte string pointer
     * size of lists are rootCount0x0C and rootCount0x10
     *
     * .dat file structure
     * +-----------------------------------------------------------+
     * | 0x20 bytes Header                                         |
     * +-----------------------------------------------------------+
     * | datasize/dataBlockSize bytes data                         |
     * +-----------------------------------------------------------+
     * | offsetCount/relocationTableCount bytes pointers to data   |
     * +-----------------------------------------------------------+
     * | ftDataCount/rootCount0x0C * 2 bytes ptr/stringpointers    |
     * +-----------------------------------------------------------+
     * | secondarySectionCount/rootCount0x10 ^                     |
     * +-----------------------------------------------------------+
     * | ?? bytes string table                                     |
     * +-----------------------------------------------------------+
     *
     * PlMs.dat
     * 0x00000000 dat Header
     * 0x00000020 data section start
     * reset index to zero
     * ? string table
     * 0x00003724  attributes start
     * 0x000038A8  attributes end
     * ?
     * 0x00007CF0  subactions start
     * 0x00009B98  subactions end
     * 0x00009D6C  "ftDataMars" -> attributes ptrs, subaction ptrs
     * ?
     * 0x000278A0 relocation table
     * 0x0002AB9C rootOffset0
     * 0x0002AB9C  ROOT_NODE "ftDataMars" -> 0x00009D6C
     * 0x0002ABA4 rootOffset1
     * 0x0002ABA4 string table
     *
     *
     * PlGn.dat
     * [+0x20]
     * 0x000036FC attributes start
     * 0x00003880 attributes end
     * 0x000075F0 subactions start
     * 0x000093C0 subactions end
     *
     * 0x0002CE60 relocation offset
     * 0x000312A4 rootOffset0
     * 0x000312AC rootOffset1
     * 0x000312AC string table
     */

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
        public final int offset;
        public final int filesize;
        public final int dataBlockSize;
        public final int relocationTableCount;
        public final int rootCount0x0C;
        public final int rootCount0x10;
        public final int version;
        public final int undefined0x18;
        public final int undefined0x1C;

        public DatHeader(ByteBuffer pldat, int offset) {
            this.offset = offset;
            pldat.position(offset);
            filesize = pldat.getInt();
            dataBlockSize = pldat.getInt();
            relocationTableCount = pldat.getInt();
            rootCount0x0C = pldat.getInt();
            rootCount0x10 = pldat.getInt();
            version = pldat.getInt();
            undefined0x18 = pldat.getInt();
            undefined0x1C = pldat.getInt();
        }

        public int getDataBlockOffset() {
            return offset + DATA_OFFSET;
        }
        // TODO what is this relocation table for?
        public int getRelocationOffset() {
            return getDataBlockOffset() + dataBlockSize;
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
        public final int dataPointer;
        public final int stringPointer;
        public final String string;
        public final DatHeader datHeader;

        public RootNode(ByteBuffer file, DatHeader datHeader, int rootOffset) {
            this.datHeader = datHeader;
            file.position(rootOffset);
            dataPointer = file.getInt();
            stringPointer = file.getInt();
            // the string comes from the string table at the end of the file
            string = getString(file, datHeader.getTableOffset() + stringPointer);
        }

        public int getDataOffset() {
            return datHeader.getDataBlockOffset() + dataPointer;
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

        public FtDataHeader(ByteBuffer pldat, RootNode rootNode) {
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
     * 0x0C SubAction command list pointer
     *      there is a unique pointer for each Header, even if the list of commands is just a null terminator
     * 0x10 undefined
     * 0x14 undefined
     */
    private static class SubActionHeader {
        public static final int SUBACTION_HEADER_LENGTH_BYTES = 6 * 4;

        public final int stringPointer;
        public final int ajPointer;
        public final int ajLength;
        public final int commandListPointer;
        public final int undefined0x10;
        public final int undefined0x14;
        public final String string;
        public final String shortName;

        public SubActionHeader(ByteBuffer pldat, FtDataHeader ftDataHeader, int index) {
            pldat.position(ftDataHeader.getSubactionsStart() + index * SUBACTION_HEADER_LENGTH_BYTES);
            stringPointer = pldat.getInt();
            ajPointer = pldat.getInt();
            ajLength = pldat.getInt();
            commandListPointer = pldat.getInt();
            undefined0x10 = pldat.getInt();
            undefined0x14 = pldat.getInt();

            string = getString(pldat, DATA_OFFSET + stringPointer);
            if (string.equals("")) {
                shortName = string;
            } else {
                shortName = string.split("_")[3];
            }
        }

        public boolean isSubActionNull(ByteBuffer pldat) {
            // when a character does not implement a SubAction,
            // the string pointer will be zero, which will point to a null terminator,
            // the aj pointer and aj length will both be zero,
            // and the command list pointer will point to a 4-byte null terminator
            boolean nullStringPointer = stringPointer == 0;
            boolean nullString = string.equals("");
            boolean nullAjPointer = ajPointer == 0;
            boolean nullAjLength = ajLength == 0;
            pldat.position(DATA_OFFSET + commandListPointer);
            boolean nullCommandList = pldat.getInt() == 0;

            // some moves could have a null command list,
            // but all other indicators must all match nomatter what
            // verity here that they all match
            if (nullStringPointer) {
                // this subaction is null for this character. verify
                // TODO some "null" SubActions actually have commands, which seems wrong
                if (!nullString || !nullAjPointer || !nullAjLength) {
                    throw new RuntimeException(String.format("SubAction should have been null."
                        + " strPtr: 0x%08X, ajPtr: 0x%08X, ajLen: 0x%08X, commandPtr: 0x%08X, str: <<%s>>",
                        stringPointer,
                        ajPointer,
                        ajLength,
                        commandListPointer,
                        string));
                }
                return true;
            } else {
                // this subaction is present for this character. verify
                // command list can be "null" because some moves dont have any commands
                // aj pointer can be "null" because the first animation has an aj pointer of 0
                if (nullString || nullAjLength) {
                    throw new RuntimeException(String.format("SubAction should have been present."
                        + " strPtr: 0x%08X, ajPtr: 0x%08X, ajLen: 0x%08X, commandPtr: 0x%08X, str: <<%s>>",
                        stringPointer,
                        ajPointer,
                        ajLength,
                        commandListPointer,
                        string));
                }
                return false;
            }
        }

        public int getCommandListOffset() {
            return DATA_OFFSET + commandListPointer;
        }

        @Override
        public String toString() {
            return String.format("strPtr: %08X ajPtr: %08X ajLen: %08X commandsPtr: %08X \"%s\" \"%s\"",
                stringPointer,
                ajPointer,
                ajLength,
                commandListPointer,
//                undefined0x10,
//                undefined0x14,
                string,
                shortName);
        }
    }

    /**
     * Each animation in the AJ files have their own header
     *
     * 0x00 animation size
     * 0x04 data section size
     * 0x08 ? this seems important - 0x00000085 for falcon up tilt
     * 0x0C ? one of these is probably a string pointer set to zero
     * 0x10 ?
     * 0x14 ?
     * 0x18 ?
     * 0x1C ?
     *
     * Single animation within Pl__AJ.dat
     * +--------------------+
     * | 0x20 header        |
     * +--------------------+
     * | data section       |
     * +--------------------+
     * | ?                  |
     * +--------------------+
     * | string table       |
     * +--------------------+
     */

    /**
     * animation header data? for inner-AJ dat files
     * similar to FtDataHeader
     *
     * 0x00 ? could be number of animations, always 1
     * 0x04 ? always 0
     * 0x08 frame count FLOAT
     * 0x0C bone table pointer
     * 0x10-- animation info
     */
    private static class AJDataHeader {
        public final int undefined0x00;
        public final int undefined0x04;
        public final float frameCount;
        public final int boneTablePointer;

        public AJDataHeader(ByteBuffer ajdat, RootNode rootNode) {
            ajdat.position(rootNode.getDataOffset());
            undefined0x00 = ajdat.getInt();
            undefined0x04 = ajdat.getInt();
            frameCount = ajdat.getFloat();
            boneTablePointer = ajdat.getInt();
        }
    }
}