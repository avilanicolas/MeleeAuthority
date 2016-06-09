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
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
        Character character = Character.Ca; // falcon has all the moves we need names for
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

    private static void writeCharacterAnimationCommands(
            MeleeImageFileSystem fileSystem,
            Map<Character, Map<SubAction, Animation>> charactersToAnimations) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "CharacterAnimationCommands.sql"));

        // CREATE TABLE
        writer.write("CREATE TABLE CharacterAnimationCommands (");
        writer.newLine();
        writer.write(INDENT + "id INT AUTO_INCREMENT,");
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
        writer.write(INDENT + "PRIMARY KEY (id),");
        writer.newLine();
        writer.write(INDENT + "UNIQUE (charId, animation, commandIndex),");
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
        writer.write(";");
        writer.newLine();
        writer.flush();
        writer.close();
    }
    
    private static void writeFrameStrips(Map<Character, Map<SubAction, Animation>> charactersToAnimations) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "FrameStrips.sql"));
        
        // CREATE TABLE
        writer.write("CREATE TABLE FrameStrips (");
        writer.newLine();
        writer.write(INDENT + "id INTEGER AUTO_INCREMENT,");
        writer.newLine();
        writer.write(INDENT + "charId CHAR(2),");
        writer.newLine();
        writer.write(INDENT + "animation VARCHAR(32),");
        writer.newLine();
        writer.write(INDENT + "frame INTEGER,");
        writer.newLine();
        writer.write(INDENT + "hitbox BOOLEAN,");
        writer.newLine();
        writer.write(INDENT + "iasa BOOLEAN,");
        writer.newLine();
        writer.write(INDENT + "autocancel BOOLEAN,");
        writer.newLine();
        writer.write(INDENT + "PRIMARY KEY (id),");
        writer.newLine();
        writer.write(INDENT + "UNIQUE (charId, animation, frame),");
        writer.newLine();
        writer.write(INDENT + "FOREIGN KEY (charId) REFERENCES Characters(id),");
        writer.newLine();
        writer.write(INDENT + "FOREIGN KEY (animation) REFERENCES SharedAnimations(internalName)");
        writer.newLine();
        writer.write(");");
        writer.newLine();
        writer.newLine();
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
        writer.write(");");
        writer.newLine();
        writer.flush();
        writer.close();
    }
    
    private static void writeHitboxes(Map<Character, Map<SubAction, Animation>> charactersToAnimations) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "Hitboxes.sql"));
        
        // CREATE TABLE
        writer.write("CREATE TABLE Hitboxes (");
        writer.newLine();
        writer.write(INDENT + "id INTEGER AUTO_INCREMENT,");
        writer.newLine();
        writer.write(INDENT + "charId CHAR(2),");
        writer.newLine();
        writer.write(INDENT + "animation VARCHAR(32),");
        writer.newLine();
        
        writer.write(INDENT + "group INTEGER,");
        writer.newLine();
        writer.write(INDENT + "hitboxId INTEGER,");
        writer.newLine();
        writer.write(INDENT + "bone INTEGER,");
        writer.newLine();
        writer.write(INDENT + "damage INTEGER,");
        writer.newLine();
        writer.write(INDENT + "zoffset INTEGER,");
        writer.newLine();
        writer.write(INDENT + "yoffset INTEGER,");
        writer.newLine();
        writer.write(INDENT + "xoffset INTEGER,");
        writer.newLine();
        writer.write(INDENT + "angle INTEGER,");
        writer.newLine();
        writer.write(INDENT + "knockbackScaling INTEGER,");
        writer.newLine();
        writer.write(INDENT + "fixedKnockback INTEGER,");
        writer.newLine();
        writer.write(INDENT + "baseKnockback INTEGER,");
        writer.newLine();
        writer.write(INDENT + "shieldDamage INTEGER,");
        writer.newLine();
        
        writer.write(INDENT + "PRIMARY KEY (id),");
        writer.newLine();
        writer.write(INDENT + "UNIQUE (charId, animation, group, hitboxId),");
        writer.newLine();
        writer.write(INDENT + "FOREIGN KEY (charId) REFERENCES Characters(id),");
        writer.newLine();
        writer.write(INDENT + "FOREIGN KEY (animation) REFERENCES SharedAnimations(internalName)");
        writer.newLine();
        writer.write(");");
        writer.newLine();
        writer.newLine();
        writer.flush();
        
        // INSERT
        writer.write("INSERT INTO Hitboxes");
        writer.newLine();
        writer.write(INDENT + "(charId, animation, group, hitboxId, bone, damage"
                + ", zoffset, yoffset, xoffset, angle, knockbackScaling, fixedKnockback, baseKnockback, shieldDamage)");
        writer.newLine();
        writer.write("VALUES");
        writer.newLine();
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
        writer.write(";");
        writer.newLine();
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
        buildWriter.write("source FrameStrips.sql");
        buildWriter.newLine();
        buildWriter.write("source Hitboxes.sql");
        buildWriter.newLine();
        buildWriter.flush();
        buildWriter.close();

        BufferedWriter cleanWriter = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "clean.sql"));
        cleanWriter.write("DROP TABLE IF EXISTS Hitboxes;");
        cleanWriter.newLine();
        cleanWriter.write("DROP TABLE IF EXISTS FrameStrips;");
        cleanWriter.newLine();
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
    
    private static void tryWriteLine(BufferedWriter writer, String line) {
        try {
            writer.write(line); writer.newLine();
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
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}