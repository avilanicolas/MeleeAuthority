package net.arhar.meleeauthorityscanner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.arhar.meleeauthorityscanner.Animation.AnimationCommand;

public class MeleeAuthorityScanner {

    private static final String DIRECTORY_NAME = "sql/";
    private static final String INDENT = "    ";

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("usage: MeleeAuthorityScanner <melee.iso>");
            return;
        }
        if (!new File(args[0]).canRead()) {
            System.out.println("unable to read file: " + args[0]);
            return;
        }

        MeleeImage image = new MeleeImage(args[0]);
        MeleeImageFileSystem fileSystem = image.getFileSystem();

//        Scanner reader = new Scanner(MeleeAuthorityScanner.class.getResourceAsStream("/anm/Ms.anm"));
//        System.out.println(reader.nextLine());
        
        Map<Character, Map<SubAction, Animation>> animations = generateAnimations(fileSystem);

        Files.createDirectories(Paths.get(DIRECTORY_NAME));
        writeCharacters();
        writeCharacterAttributes(fileSystem);
        writeSharedAnimations(fileSystem);
        writeAnimationCommandTypes();
        writeCharacterAnimationCommands(fileSystem);
        writeFrameStrips();
        writeBuildScripts();
        System.out.println("Success");
    }
    
    private static Map<Character, Map<SubAction, Animation>> generateAnimations(MeleeImageFileSystem fileSystem) {
        Map<Character, Map<SubAction, Animation>> charactersToAnimations = new HashMap<>();
        
        for (Character character : Character.values()) {
            
            Map<SubAction, Animation> animations = new HashMap<>();
            
            ByteBuffer buffer = ByteBuffer.wrap(fileSystem.getFileData("Pl" + character.name() + ".dat"));

            for (SubAction subAction : SubAction.values()) {
                
                List<AnimationCommand> animationCommands = new ArrayList<>();

                String internalName = SubAction.getInternalName(fileSystem, character, subAction.offset);
                if (internalName.equals(SubAction.UNKNOWN_ANIMATION)) {
                    // TODO possible do something more intelligent when a character doesn't have a "shared" animation
                    continue;
                }
                if (!temp.contains(internalName)) {
                    System.out.println("internal name for " + character.name() + " " + subAction.description + " not found: " + internalName);
                }

                int subactionPointer = character.subOffset + 0x20 + 4 * 3 + subAction.offset * 6 * 4;
                buffer.position(subactionPointer);
                int offset = buffer.getInt();
                buffer.position(offset + 0x20);

                int bytesDown = 0;
                while (buffer.getInt() != 0) {
                    buffer.position(offset + 0x20 + bytesDown);
                    // read the first byte to figure out which command it is
                    int id = buffer.get() & 0xFF;
                    // zero out lowest two bits
                    id = (id & ~0b1) & ~0b10;
                    AnimationCommandType command = AnimationCommandType.getById(id);
                    buffer.position(offset + 0x20 + bytesDown);

                    // read command data
                    byte[] commandData = new byte[command.length];
                    for (int j = 0; j < command.length; j++) {
                        commandData[j] = buffer.get();
                    }

                    animationCommands.add(new AnimationCommand(command, commandData));

                    bytesDown += command.length;
                    buffer.position(offset + 0x20 + bytesDown);
                }
                
                animations.put(subAction, new Animation(animationCommands));
            }
        }
        
        return charactersToAnimations;
    }

    private static void writeCharacters() throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "Characters.sql"));

        // CREATE TABLE
        writer.write("CREATE TABLE Characters (");
        writer.newLine();
        writer.write(INDENT + "id CHAR(2),");
        writer.newLine();
        writer.write(INDENT + "fullName VARCHAR(32),");
        writer.newLine();
        writer.write(INDENT + "PRIMARY KEY (id)");
        writer.newLine();
        writer.write(");");
        writer.newLine();
        writer.newLine();
        writer.flush();

        // INSERT
        writer.write("INSERT INTO Characters");
        writer.newLine();
        writer.write(INDENT + "(id, fullName)");
        writer.newLine();
        writer.write("VALUES");
        writer.newLine();
        boolean first = true;
        for (Character character : Character.values()) {
            if (first) {
                first = false;
            } else {
                writer.write(", ");
                writer.newLine();
            }
            writer.write(INDENT + "('" + character.name() + "', '" + character.fullName + "')");
        }
        writer.write(";");
        writer.newLine();
        writer.flush();
        writer.close();
    }

    private static void writeCharacterAttributes(MeleeImageFileSystem fileSystem) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "CharacterAttributes.sql"));

        // CREATE TABLE
        writer.write("CREATE TABLE CharacterAttributes (");
        writer.newLine();
        writer.write(INDENT + "id CHAR(2),");
        writer.newLine();
        for (Attribute attribute : Attribute.values()) {
            writer.write(INDENT + attribute.name() + " " + attribute.numberType.getSimpleName().toUpperCase() + ",");
            writer.newLine();
        }
        writer.write(INDENT + "PRIMARY KEY (id),");
        writer.newLine();
        writer.write(INDENT + "FOREIGN KEY (id) REFERENCES Characters(id)");
        writer.newLine();
        writer.write(");");
        writer.newLine();
        writer.newLine();
        writer.flush();

        // INSERT
        writer.write("INSERT INTO CharacterAttributes");
        writer.newLine();
        writer.write(INDENT + "(id");
        for (Attribute attribute : Attribute.values()) {
            writer.write(", " + attribute.name());
        }
        writer.write(")");
        writer.newLine();
        writer.write("VALUES");
        writer.newLine();
        writer.flush();
        // add one line for each characters values
        for (int i = 0; i < Character.values().length; i++) {
            Character character = Character.values()[i];

            ByteBuffer buffer = ByteBuffer.wrap(fileSystem.getFileData("Pl" + character.name() + ".dat"));
            buffer.position(character.attributeOffset);

            writer.write(INDENT + "('" + character.name() + "'");

            for (Attribute attribute : Attribute.values()) {
                writer.write(", ");

                if (attribute.numberType == Float.class) {
                    writer.write(String.valueOf(buffer.getFloat()));
                } else {
                    writer.write(String.valueOf(buffer.getInt()));
                }
            }

            if (i == Character.values().length - 1) {
                // the last character gets a semicolon instead of a comma
                writer.write(");");
            } else {
                writer.write("),");
            }
            writer.newLine();
        }
        writer.flush();
        writer.close();
    }


    /**
     * ANIMATION SCHEMA
     *
     * CREATE TABLE SharedAnimations (
     *     internalName VARCHAR(32),
     *     description VARCHAR(64),
     *     PRIMARY KEY (internalName)
     * );
     *
     * CREATE TABLE AnimationCommandTypes (
     *     id INTEGER,
     *     name VARCHAR(32),
     *     numBytes INTEGER,
     *     PRIMARY KEY (id),
     *     UNIQUE (name)
     * );
     *
     * CREATE TABLE CharacterAnimationCommands (
     *     charId CHAR(2),
     *     animation VARCHAR(32),
     *     commandIndex INTEGER,
     *     commandType INTEGER,
     *     commandData TINYBLOB,
     *     PRIMARY KEY (charId, animation, commandIndex),
     *     FOREIGN KEY charId REFERENCES Characters(id),
     *     FOREIGN KEY animation REFERENCES SharedAnimations(internalName),
     *     FOREIGN KEY commandType REFERENCES AnimationCommandTypes(id)
     * );
     *
     * marth fair query:
     *
     * SELECT
     *     CAC.commandIndex,
     *     CAC.commandType,
     *     CAC.commandData
     * FROM
     *     SharedAnimations SA
     *     JOIN CharacterAnimationCommands CAC ON CAC.animation = SA.internalName
     *     JOIN AnimationCommandTypes ACT ON CAC.commandType = ACT.id
     * WHERE
     *     CAC.charId = 'Ms'
     *     AND SA.description = 'Forward-Air'
     * ORDER BY CAC.commandIndex DESC;
     */

    private static void writeSharedAnimations(MeleeImageFileSystem fileSystem) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "SharedAnimations.sql"));

        // CREATE TABLE
        writer.write("CREATE TABLE SharedAnimations (");
        writer.newLine();
        writer.write(INDENT + "internalName VARCHAR(32),");
        writer.newLine();
        writer.write(INDENT + "description VARCHAR(64),");
        writer.newLine();
        writer.write(INDENT + "PRIMARY KEY (internalName)");
        writer.newLine();
        writer.write(");");
        writer.newLine();
        writer.newLine();
        writer.flush();

        // INSERT
        writer.write("INSERT INTO SharedAnimations");
        writer.newLine();
        writer.write(INDENT + "(internalName, description)");
        writer.newLine();
        writer.write("VALUES");
        writer.newLine();
        Character character = Character.Ca;
        boolean first = true;
//        for (Entry<Integer, String> subAction : SubAction.SUBACTIONS.entrySet()) {
        for (SubAction subAction : SubAction.values()) {
            if (first) {
                first = false;
            } else {
                writer.write(",");
                writer.newLine();
            }
            String internalName = SubAction.getInternalName(fileSystem, character, subAction.offset);
            temp.add(internalName);
            writer.write(INDENT + "('" + internalName + "', '" + subAction.description + "')");
        }
        writer.write(";");
        writer.newLine();
        writer.flush();
        writer.close();
    }

    private static final Set<String> temp = new HashSet<>();

    private static void writeAnimationCommandTypes() throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "AnimationCommandTypes.sql"));

        // CREATE TABLE
        writer.write("CREATE TABLE AnimationCommandTypes (");
        writer.newLine();
        writer.write(INDENT + "id INTEGER,");
        writer.newLine();
        writer.write(INDENT + "name VARCHAR(32),");
        writer.newLine();
        writer.write(INDENT + "numBytes INTEGER,");
        writer.newLine();
        writer.write(INDENT + "PRIMARY KEY (id),");
        writer.newLine();
        writer.write(INDENT + "UNIQUE (name)");
        writer.newLine();
        writer.write(");");
        writer.newLine();
        writer.newLine();
        writer.flush();

        // INSERT
        writer.write("INSERT INTO AnimationCommandTypes");
        writer.newLine();
        writer.write(INDENT + "(id, name, numBytes)");
        writer.newLine();
        writer.write("VALUES");
        writer.newLine();
        boolean first = true;
        for (AnimationCommandType command : AnimationCommandType.values()) {
            if (first) {
                first = false;
            } else {
                writer.write(",");
                writer.newLine();
            }
            // TODO should name() also be used here instead of full description?
            writer.write(INDENT + "(" + command.id + ", '" + command.fullName + "', " + command.length + ")");
        }
        writer.write(";");
        writer.newLine();
        writer.flush();
        writer.close();
    }

    private static void writeCharacterAnimationCommands(MeleeImageFileSystem fileSystem) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "CharacterAnimationCommands.sql"));

        // CREATE TABLE
        writer.write("CREATE TABLE CharacterAnimationCommands (");
        writer.newLine();
        writer.write(INDENT + "charId CHAR(2),");
        writer.newLine();
        writer.write(INDENT + "animation VARCHAR(32),");
        writer.newLine();
        writer.write(INDENT + "commandIndex INTEGER,");
        writer.newLine();
        writer.write(INDENT + "commandType INTEGER,");
        writer.newLine();
        writer.write(INDENT + "commandData TINYBLOB,");
        writer.newLine();
        writer.write(INDENT + "PRIMARY KEY (charId, animation, commandIndex),");
        writer.newLine();
        writer.write(INDENT + "FOREIGN KEY (charId) REFERENCES Characters(id),");
        writer.newLine();
        writer.write(INDENT + "FOREIGN KEY (animation) REFERENCES SharedAnimations(internalName),");
        writer.newLine();
        writer.write(INDENT + "FOREIGN KEY (commandType) REFERENCES AnimationCommandTypes(id)");
        writer.newLine();
        writer.write(");");
        writer.newLine();
        writer.newLine();
        writer.flush();

        // INSERT
        writer.write("INSERT INTO CharacterAnimationCommands");
        writer.newLine();
        writer.write(INDENT + "(charId, animation, commandIndex, commandType, commandData)");
        writer.newLine();
        writer.write("VALUES");
        writer.newLine();
        boolean first = true;
        for (Character character : Character.values()) {
            ByteBuffer buffer = ByteBuffer.wrap(fileSystem.getFileData("Pl" + character.name() + ".dat"));

            for (SubAction subAction : SubAction.values()) {

                String internalName = SubAction.getInternalName(fileSystem, character, subAction.offset);
                if (internalName.equals(SubAction.UNKNOWN_ANIMATION)) {
                    // TODO possible do something more intelligent when a character doesn't have a "shared" animation
                    continue;
                }
                if (!temp.contains(internalName)) {
                    System.out.println("internal name for " + character.name() + " " + subAction.description + " not found: " + internalName);
                }

                int subactionPointer = character.subOffset + 0x20 + 4 * 3 + subAction.offset * 6 * 4;
                buffer.position(subactionPointer);
                int offset = buffer.getInt();
                buffer.position(offset + 0x20);

                int bytesDown = 0;
                for (int i = 0; buffer.getInt() != 0; i++) {
                    buffer.position(offset + 0x20 + bytesDown);
                    // read the first byte to figure out which command it is
                    int id = buffer.get() & 0xFF;
                    // zero out lowest two bits
                    id = (id & ~0b1) & ~0b10;
                    AnimationCommandType command = AnimationCommandType.getById(id);
                    buffer.position(offset + 0x20 + bytesDown);

                    // read command data
                    StringBuilder dataBuilder = new StringBuilder();
                    for (int j = 0; j < command.length; j++) {
                        int data = buffer.get() & 0xFF;
                        dataBuilder.append(String.format("%2X", data).replace(' ', '0'));
                    }

                    if (first) {
                        first = false;
                    } else {
                        writer.write(",");
                        writer.newLine();
                    }
                    writer.write(String.format(
                        "%s('%s', '%s', %d, %d, x'%s')",
                        INDENT,
                        character.name(),
//                        internalName,
                        subAction.name(),
                        i,
                        command.id,
                        dataBuilder.toString()));

                    bytesDown += command.length;
                    buffer.position(offset + 0x20 + bytesDown);
                }
            }
        }
        writer.write(";");
        writer.newLine();
        writer.flush();
        writer.close();
    }
    
    private static void writeFrameStrips() throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "FrameStrips.sql"));
        
        writer.flush();
        writer.close();
    }

    private static void writeBuildScripts() throws IOException {
        BufferedWriter buildWriter = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "build.sql"));
        buildWriter.write("source Characters.sql");
        buildWriter.newLine();
        buildWriter.write("source CharacterAttributes.sql");
        buildWriter.newLine();
        buildWriter.write("source SharedAnimations.sql");
        buildWriter.newLine();
        buildWriter.write("source AnimationCommandTypes.sql");
        buildWriter.newLine();
        buildWriter.write("source CharacterAnimationCommands.sql");
        buildWriter.newLine();
        buildWriter.flush();
        buildWriter.close();

        BufferedWriter cleanWriter = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "clean.sql"));
        cleanWriter.write("DROP TABLE IF EXISTS CharacterAnimationCommands;");
        cleanWriter.newLine();
        cleanWriter.write("DROP TABLE IF EXISTS AnimationCommandTypes;");
        cleanWriter.newLine();
        cleanWriter.write("DROP TABLE IF EXISTS SharedAnimations;");
        cleanWriter.newLine();
        cleanWriter.write("DROP TABLE IF EXISTS CharacterAttributes;");
        cleanWriter.newLine();
        cleanWriter.write("DROP TABLE IF EXISTS Characters;");
        cleanWriter.newLine();
        cleanWriter.flush();
        cleanWriter.close();
    }
}