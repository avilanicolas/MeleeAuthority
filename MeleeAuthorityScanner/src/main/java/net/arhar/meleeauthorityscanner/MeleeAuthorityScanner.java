package net.arhar.meleeauthorityscanner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import net.arhar.meleeauthorityscanner.Animation.AnimationCommand;
import net.arhar.meleeauthorityscanner.Animation.FrameStripType;

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

        Path dirPath = Paths.get(DIRECTORY_NAME);
        Files.createDirectories(dirPath);
        writeCharacters();
        // TODO writeAttributes();
        writeCharacterAttributes(fileSystem);
        writeSharedAnimations(fileSystem);
        writeAnimationCommandTypes();
        writeCharacterAnimationCommands(fileSystem, animations);
        writeFrameStrips(animations);
        writeHitboxes(animations);
        writeBuildScripts();
        System.out.println("Wrote sql folder to " + dirPath.toAbsolutePath());
    }

    private static Map<Character, Map<SubAction, Animation>> generateAnimations(MeleeImageFileSystem fileSystem) {
        Map<Character, Map<SubAction, Animation>> charactersToAnimations = new LinkedHashMap<>();

        for (Character character : Character.values()) {
            Map<SubAction, Animation> animations = new LinkedHashMap<>();
            ByteBuffer buffer = ByteBuffer.wrap(fileSystem.getFileData("Pl" + character.name() + ".dat"));

            for (SubAction subAction : SubAction.values()) {

                List<AnimationCommand> animationCommands = new ArrayList<>();

                String internalName = SubAction.getInternalName(fileSystem, character, subAction.offset);
                if (internalName.equals(SubAction.UNKNOWN_ANIMATION)) {
                    // TODO possible do something more intelligent when a character doesn't have a "shared" animation
                    System.out.println("SubAction not found for " + character.name()
                            + " enum: <<" + subAction.name() + ">> desc: <<" + subAction.description + ">>");
                    continue;
                }
                if (!internalName.equals(subAction.name())) {
                    System.out.println("internal name does not match. " + character.name()
                            + " desc: <<" + subAction.description + ">> enum: <<" + subAction.name() + ">> internal: <<" + internalName + ">>");
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

                if (character == Character.Ms
                        && subAction == SubAction.AttackAirF) {
                    Animation.temp = true;
                }
                animations.put(subAction, new Animation(animationCommands));
                Animation.temp = false;
            }

            charactersToAnimations.put(character, animations);
        }

        return charactersToAnimations;
    }

    private static void writeCharacters() throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "Characters.sql"));

        // CREATE TABLE
        writer.write("CREATE TABLE Characters (\n");
        writer.write(INDENT + "id CHAR(2),\n");
        writer.write(INDENT + "fullName VARCHAR(32),\n");
        writer.write(INDENT + "PRIMARY KEY (id)\n");
        writer.write(");\n\n");
        writer.flush();

        // INSERT
        writer.write("INSERT INTO Characters\n");
        writer.write(INDENT + "(id, fullName)\n");
        writer.write("VALUES\n");
        boolean first = true;
        for (Character character : Character.values()) {
            if (first) {
                first = false;
            } else {
                writer.write(",\n");
            }
            writer.write(INDENT + "('" + character.name() + "', '" + character.fullName + "')");
        }
        writer.write(";\n");
        writer.flush();
        writer.close();
    }

    private static void writeCharacterAttributes(MeleeImageFileSystem fileSystem) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "CharacterAttributes.sql"));

        // CREATE TABLE
        writer.write("CREATE TABLE CharacterAttributes (\n");
        writer.write(INDENT + "id CHAR(2),\n");
        for (Attribute attribute : Attribute.values()) {
            if (attribute.known) {
                writer.write(INDENT + attribute.name() + " " + attribute.numberType.getSimpleName().toUpperCase() + ",\n");
            }
        }
        writer.write(INDENT + "PRIMARY KEY (id),\n");
        writer.write(INDENT + "FOREIGN KEY (id) REFERENCES Characters(id)\n");
        writer.write(");\n\n");
        writer.flush();

        // INSERT
        writer.write("INSERT INTO CharacterAttributes\n");
        writer.write(INDENT + "(id");
        for (Attribute attribute : Attribute.values()) {
            if (attribute.known) {
                writer.write(", " + attribute.name());
            }
        }
        writer.write(")\n");
        writer.write("VALUES\n");
        writer.flush();
        // add one line for each characters values
        for (int i = 0; i < Character.values().length; i++) {
            Character character = Character.values()[i];

            ByteBuffer buffer = ByteBuffer.wrap(fileSystem.getFileData("Pl" + character.name() + ".dat"));
            buffer.position(character.attributeOffset);

            writer.write(INDENT + "('" + character.name() + "'");

            for (Attribute attribute : Attribute.values()) {
                if (attribute.known) {
                    writer.write(", ");
                    if (attribute.numberType == Float.class) {
                        writer.write(String.valueOf(buffer.getFloat()));
                    } else {
                        writer.write(String.valueOf(buffer.getInt()));
                    }
                } else {
                    // advance down the buffer anyway so that we still have the ordinal-based offset
                    buffer.getInt();
                }
            }

            if (i == Character.values().length - 1) {
                // the last character gets a semicolon instead of a comma
                writer.write(");\n");
            } else {
                writer.write("),\n");
            }
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
        writer.write("CREATE TABLE SharedAnimations (\n");
        writer.write(INDENT + "internalName VARCHAR(32),\n");
        writer.write(INDENT + "description VARCHAR(64),\n");
        writer.write(INDENT + "PRIMARY KEY (internalName),\n");
        writer.write(INDENT + "UNIQUE (description)\n");
        writer.write(");\n\n");
        writer.flush();

        // INSERT
        writer.write("INSERT INTO SharedAnimations\n");
        writer.write(INDENT + "(internalName, description)\n");
        writer.write("VALUES\n");
//        Character character = Character.Ca; // falcon has all the moves we need names for
        boolean first = true;
        for (SubAction subAction : SubAction.values()) {
            if (first) {
                first = false;
            } else {
                writer.write(",\n");
            }
//            String internalName = SubAction.getInternalName(fileSystem, character, subAction.offset);
//            if (!internalName.equals(subAction.name())) {
//                System.out.println("\ninternal name for SharedAnimations does not match. enum: <<" + subAction.name() + ">> internal: <<" + internalName + ">>");
//            }
//            writer.write(INDENT + "('" + internalName + "', '" + subAction.description + "')");
            writer.write(String.format(INDENT + "('%s', '%s')", subAction.name(), subAction.description));
        }
        writer.write(";\n");
        writer.flush();
        writer.close();
    }

    private static void writeAnimationCommandTypes() throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "AnimationCommandTypes.sql"));

        // CREATE TABLE
        writer.write("CREATE TABLE AnimationCommandTypes (\n");
        writer.write(INDENT + "id INTEGER,\n");
        writer.write(INDENT + "name VARCHAR(32),\n");
        writer.write(INDENT + "numBytes INTEGER,\n");
        writer.write(INDENT + "PRIMARY KEY (id),\n");
        writer.write(INDENT + "UNIQUE (name)\n");
        writer.write(");\n\n");
        writer.flush();

        // INSERT
        writer.write("INSERT INTO AnimationCommandTypes\n");
        writer.write(INDENT + "(id, name, numBytes)\n");
        writer.write("VALUES\n");
        boolean first = true;
        for (AnimationCommandType command : AnimationCommandType.values()) {
            if (first) {
                first = false;
            } else {
                writer.write(",\n");
            }
            // TODO should name() also be used here instead of full description?
            writer.write(INDENT + "(" + command.id + ", '" + command.fullName + "', " + command.length + ")");
        }
        writer.write(";\n");
        writer.flush();
        writer.close();
    }

    private static void writeCharacterAnimationCommands(
            MeleeImageFileSystem fileSystem,
            Map<Character, Map<SubAction, Animation>> charactersToAnimations) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "CharacterAnimationCommands.sql"));

        // CREATE TABLE
        writer.write("CREATE TABLE CharacterAnimationCommands (\n");
        writer.write(INDENT + "id INT AUTO_INCREMENT,\n");
        writer.write(INDENT + "charId CHAR(2),\n");
        writer.write(INDENT + "animation VARCHAR(32),\n");
        writer.write(INDENT + "commandIndex INTEGER,\n");
        writer.write(INDENT + "commandType INTEGER,\n");
        writer.write(INDENT + "commandData TINYBLOB,\n");
        writer.write(INDENT + "PRIMARY KEY (id),\n");
        writer.write(INDENT + "UNIQUE (charId, animation, commandIndex),\n");
        writer.write(INDENT + "FOREIGN KEY (charId) REFERENCES Characters(id),\n");
        writer.write(INDENT + "FOREIGN KEY (animation) REFERENCES SharedAnimations(internalName),\n");
        writer.write(INDENT + "FOREIGN KEY (commandType) REFERENCES AnimationCommandTypes(id)\n");
        writer.write(");\n\n");
        writer.flush();

        // INSERT
        writer.write("INSERT INTO CharacterAnimationCommands\n");
        writer.write(INDENT + "(charId, animation, commandIndex, commandType, commandData)\n");
        writer.write("VALUES\n");
        AtomicBoolean first = new AtomicBoolean(true);
        charactersToAnimations.forEach((character, actionsToAnimations) -> {
            actionsToAnimations.forEach((action, animation) -> {
                for (int i = 0; i < animation.commands.size(); i++) {
                    AnimationCommand command = animation.commands.get(i);
                    StringBuilder dataBuilder = new StringBuilder();
                    for (int j = 0; j < command.data.length; j++) {
                        dataBuilder.append(String.format("%2X", command.data[j] & 0xFF).replace(' ', '0'));
                    }
                    if (first.get()) {
                        first.set(false);
                    } else {
                        tryWriteLine(writer, ",");
                    }
                    tryWrite(writer, String.format(
                        "%s('%s', '%s', %d, %d, x'%s')",
                        INDENT,
                        character.name(),
//                        internalName,
                        action.name(),
//                        subAction.name(),
                        i,
                        command.type.id,
                        dataBuilder.toString()));
                }
            });
        });
        writer.write(";\n");
        writer.flush();
        writer.close();
    }

    private static void writeFrameStrips(Map<Character, Map<SubAction, Animation>> charactersToAnimations) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "FrameStrips.sql"));

        // CREATE TABLE
        writer.write("CREATE TABLE FrameStrips (\n");
        writer.write(INDENT + "id INTEGER AUTO_INCREMENT,\n");
        writer.write(INDENT + "charId CHAR(2),\n");
        writer.write(INDENT + "animation VARCHAR(32),\n");
        writer.write(INDENT + "frame INTEGER,\n");
        writer.write(INDENT + "hitbox BOOLEAN,\n");
        writer.write(INDENT + "iasa BOOLEAN,\n");
        writer.write(INDENT + "autocancel BOOLEAN,\n");
        writer.write(INDENT + "PRIMARY KEY (id),\n");
        writer.write(INDENT + "UNIQUE (charId, animation, frame),\n");
        writer.write(INDENT + "FOREIGN KEY (charId) REFERENCES Characters(id),\n");
        writer.write(INDENT + "FOREIGN KEY (animation) REFERENCES SharedAnimations(internalName)\n");
        writer.write(");\n\n");
        writer.flush();

        // INSERT
        AtomicBoolean first = new AtomicBoolean(true);
        AtomicInteger totalInserts = new AtomicInteger(0);
        charactersToAnimations.forEach((character, actionToAnimation) -> {
            actionToAnimation.forEach((action, animation) -> {

                for (int i = 0; i < animation.frameStrip.size(); i++) {

                    // every 50,000 inserts, write another INSERT INTO so that we dont timeout
                    if (totalInserts.get() % 5000 == 0) {
                        if (first.get()) {
                            first.set(false);
                        } else {
                            tryWriteLine(writer, ");");
                            tryWriteLine(writer);
                        }

                        tryWriteLine(writer, "INSERT INTO FrameStrips");
                        tryWriteLine(writer, INDENT + "(charId, animation, frame, hitbox, iasa, autocancel)");
                        tryWriteLine(writer, "VALUES");
                    } else {
                        tryWriteLine(writer, "),");
                    }
                    totalInserts.incrementAndGet();

                    EnumSet<FrameStripType> flags = animation.frameStrip.get(i);
                    tryWrite(writer, INDENT + "('" + character.name() + "', '" + action.name() + "', " + i + ", ");
                    if (flags.contains(FrameStripType.HITBOX)) {
                        tryWrite(writer, "true, ");
                    } else {
                        tryWrite(writer, "false, ");
                    }
                    if (flags.contains(FrameStripType.IASA)) {
                        tryWrite(writer, "true, ");
                    } else {
                        tryWrite(writer, "false, ");
                    }
                    if (flags.contains(FrameStripType.AUTOCANCEL)) {
                        tryWrite(writer, "true");
                    } else {
                        tryWrite(writer, "false");
                    }
                }
            });
        });
        writer.write(");\n");
        writer.flush();
        writer.close();
    }

    private static void writeHitboxes(Map<Character, Map<SubAction, Animation>> charactersToAnimations) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "Hitboxes.sql"));

        // CREATE TABLE
        writer.write("CREATE TABLE Hitboxes (\n");
        writer.write(INDENT + "id INTEGER AUTO_INCREMENT,\n");
        writer.write(INDENT + "charId CHAR(2),\n");
        writer.write(INDENT + "animation VARCHAR(32),\n");

        writer.write(INDENT + "groupId INTEGER,\n");
        writer.write(INDENT + "hitboxId INTEGER,\n");
        writer.write(INDENT + "bone INTEGER,\n");
        writer.write(INDENT + "damage INTEGER,\n");
        writer.write(INDENT + "zoffset INTEGER,\n");
        writer.write(INDENT + "yoffset INTEGER,\n");
        writer.write(INDENT + "xoffset INTEGER,\n");
        writer.write(INDENT + "angle INTEGER,\n");
        writer.write(INDENT + "knockbackScaling INTEGER,\n");
        writer.write(INDENT + "fixedKnockback INTEGER,\n");
        writer.write(INDENT + "baseKnockback INTEGER,\n");
        writer.write(INDENT + "shieldDamage INTEGER,\n");

        writer.write(INDENT + "PRIMARY KEY (id),\n");
        writer.write(INDENT + "UNIQUE (charId, animation, groupId, hitboxId),\n");
        writer.write(INDENT + "FOREIGN KEY (charId) REFERENCES Characters(id),\n");
        writer.write(INDENT + "FOREIGN KEY (animation) REFERENCES SharedAnimations(internalName)\n");
        writer.write(");\n\n");
        writer.flush();

        // INSERT
        writer.write("INSERT INTO Hitboxes\n");
        writer.write(INDENT + "(charId, animation, groupId, hitboxId, bone, damage"
                + ", zoffset, yoffset, xoffset, angle, knockbackScaling, fixedKnockback, baseKnockback, shieldDamage)\n");
        writer.write("VALUES\n");
        AtomicBoolean first = new AtomicBoolean(true);
        charactersToAnimations.forEach((character, actionToAnimation) -> {
            actionToAnimation.forEach((action, animation) -> {
                for (int i = 0; i < animation.hitboxes.size(); i++) {
                    List<Hitbox> hitboxGroup = animation.hitboxes.get(i);
                    for (Hitbox hitbox : hitboxGroup) {
                        if (first.get()) {
                            first.set(false);
                        } else {
                            tryWriteLine(writer, ",");
                        }
                        tryWrite(writer, INDENT + "('" + character.name() + "', '" + action.name() + "', " + i
                                + ", " + hitbox.id + ", " + hitbox.bone + ", " + hitbox.damage
                                + ", " + hitbox.zoffset + ", " + hitbox.yoffset + ", " + hitbox.xoffset + ", " + hitbox.angle
                                + ", " + hitbox.knockbackScaling + ", " + hitbox.fixedKnockback + ", " + hitbox.baseKnockback
                                + ", " + hitbox.shieldDamage + ")");
                    }
                }
            });
        });
        writer.write(";\n");
        writer.flush();
        writer.close();
    }

    private static void writeBuildScripts() throws IOException {
        BufferedWriter buildWriter = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "build.sql"));
        buildWriter.write("source Characters.sql\n");
        buildWriter.write("source CharacterAttributes.sql\n");
        buildWriter.write("source SharedAnimations.sql\n");
        buildWriter.write("source AnimationCommandTypes.sql\n");
        buildWriter.write("source CharacterAnimationCommands.sql\n");
        buildWriter.write("source FrameStrips.sql\n");
        buildWriter.write("source Hitboxes.sql\n");
        buildWriter.flush();
        buildWriter.close();

        BufferedWriter cleanWriter = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "clean.sql"));
        cleanWriter.write("DROP TABLE IF EXISTS Hitboxes;\n");
        cleanWriter.write("DROP TABLE IF EXISTS FrameStrips;\n");
        cleanWriter.write("DROP TABLE IF EXISTS CharacterAnimationCommands;\n");
        cleanWriter.write("DROP TABLE IF EXISTS AnimationCommandTypes;\n");
        cleanWriter.write("DROP TABLE IF EXISTS SharedAnimations;\n");
        cleanWriter.write("DROP TABLE IF EXISTS CharacterAttributes;\n");
        cleanWriter.write("DROP TABLE IF EXISTS Characters;\n");
        cleanWriter.flush();
        cleanWriter.close();
    }

    private static void tryWriteLine(BufferedWriter writer, String line) {
        try {
            writer.write(line + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void tryWrite(BufferedWriter writer, String line) {
        try {
            writer.write(line);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void tryWriteLine(BufferedWriter writer) {
        try {
            writer.write("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}