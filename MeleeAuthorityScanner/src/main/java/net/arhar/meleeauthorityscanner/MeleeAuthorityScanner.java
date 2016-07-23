package net.arhar.meleeauthorityscanner;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

        Map<Character, List<Animation>> charactersToAnimations = DatReader.readAllAnimations(fileSystem);
        Map<Character, Map<Attribute, Number>> charactersToAttributes = DatReader.readAllAttributes(fileSystem);

        Path dirPath = Paths.get(DIRECTORY_NAME);
        Files.createDirectories(dirPath);

        writeCharacters();
        // TODO writeAttributes();
        writeCharacterAttributes(charactersToAttributes);
        writeAnimations(charactersToAnimations);
        writeAnimationCommandTypes();
        writeCharacterAnimationCommands(fileSystem, charactersToAnimations);
        writeFrameStrips(charactersToAnimations);
        writeHitboxes(charactersToAnimations);
        writeBuildScripts();

        System.out.println("Wrote sql folder to " + dirPath.toAbsolutePath());
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

    private static void writeCharacterAttributes(Map<Character, Map<Attribute, Number>> charactersToAttributes) throws IOException {
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
        writer.write("\n");
        // add one line for each characters values
        AtomicBoolean first = new AtomicBoolean(true);
        charactersToAttributes.forEach((character, attributes) -> {
            if (first.get()) {
                first.set(false);
            } else {
                tryWrite(writer, ",\n");
            }

            tryWrite(writer, INDENT + "('" + character.name() + "'");
            attributes.forEach((attribute, number) -> {
                tryWrite(writer, ", ");
                tryWrite(writer, String.valueOf(number));
            });
            tryWrite(writer, ")");
        });
        writer.write(";\n");
        writer.flush();
        writer.close();
    }

    private static void writeAnimations(Map<Character, List<Animation>> charactersToAnimations) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "Animations.sql"));

        // CREATE TABLE
        writer.write("CREATE TABLE Animations (\n");
        writer.write(INDENT + "id INTEGER AUTO_INCREMENT,\n");
        writer.write(INDENT + "charId CHAR(2),\n");
        writer.write(INDENT + "subActionId INTEGER,\n");
        writer.write(INDENT + "internalName VARCHAR(64),\n");
        writer.write(INDENT + "description VARCHAR(64),\n");
        writer.write(INDENT + "PRIMARY KEY (id),\n");
        writer.write(INDENT + "UNIQUE (charId, subActionId),\n");
        writer.write(INDENT + "FOREIGN KEY (charId) REFERENCES Characters(id)\n");
        writer.write(");\n\n");
        writer.flush();

        // INSERT
        writer.write("INSERT INTO Animations\n");
        writer.write(INDENT + "(charId, subActionId, internalName, description)\n");
        writer.write("VALUES\n");
        AtomicBoolean first = new AtomicBoolean(true);
        charactersToAnimations.forEach((character, animations) -> {
            animations.forEach(animation -> {
                if (first.get()) {
                    first.set(false);
                } else {
                    tryWriteLine(writer, ",");
                }
                tryWrite(writer, String.format(INDENT + "('%s', %d, '%s', '%s')",
                    character.name(), animation.subActionId, animation.internalName, animation.description));
            });
        });
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
            Map<Character, List<Animation>> charactersToAnimations) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "CharacterAnimationCommands.sql"));

        // CREATE TABLE
        writer.write("CREATE TABLE CharacterAnimationCommands (\n");
        writer.write(INDENT + "id INT AUTO_INCREMENT,\n");
        writer.write(INDENT + "charId CHAR(2),\n");
        writer.write(INDENT + "subActionId INTEGER,\n");
        writer.write(INDENT + "commandIndex INTEGER,\n");
        writer.write(INDENT + "commandType INTEGER,\n");
        writer.write(INDENT + "commandData TINYBLOB,\n");
        writer.write(INDENT + "frame INTEGER,\n");
        writer.write(INDENT + "PRIMARY KEY (id),\n");
        writer.write(INDENT + "UNIQUE (charId, subActionId, commandIndex),\n");
        writer.write(INDENT + "FOREIGN KEY (charId, subActionId) REFERENCES Animations(charId, subActionId),\n");
        writer.write(INDENT + "FOREIGN KEY (commandType) REFERENCES AnimationCommandTypes(id)\n");
        writer.write(");\n\n");
        writer.flush();

        // INSERT
        AtomicBoolean first = new AtomicBoolean(true);
        AtomicInteger totalInserts = new AtomicInteger(0);
        charactersToAnimations.forEach((character, animations) -> {
            animations.forEach(animation -> {
                for (int i = 0; i < animation.commands.size(); i++) {
                    // split up inserts by 5,000 to prevent timeout
                    if (totalInserts.get() % 5000 == 0) {
                        if (first.get()) {
                            first.set(false);
                        } else {
                            tryWriteLine(writer, ";");
                            tryWriteLine(writer);
                        }

                        tryWriteLine(writer, "INSERT INTO CharacterAnimationCommands");
                        tryWriteLine(writer, INDENT + "(charId, subActionId, commandIndex, commandType, commandData, frame)");
                        tryWriteLine(writer, "VALUES");
                    } else {
                        tryWriteLine(writer, ",");
                    }
                    totalInserts.incrementAndGet();

                    AnimationCommand command = animation.commands.get(i);
                    StringBuilder dataBuilder = new StringBuilder();
                    for (int j = 0; j < command.data.length; j++) {
                        dataBuilder.append(String.format("%02X", command.data[j] & 0xFF));
                    }
//                    if (first.get()) {
//                        first.set(false);
//                    } else {
//                        tryWriteLine(writer, ",");
//                    }
                    tryWrite(writer, String.format(
                        INDENT + "('%s', %d, %d, %d, x'%s', %d)",
                        character.name(), // charId
                        animation.subActionId, // subActionId
                        i, // commandIndex
                        command.type.id, // commandType
                        dataBuilder.toString(), // commandData
                        command.frame)); // frame
                }
            });
        });
        writer.write(";\n");
        writer.flush();
        writer.close();
    }

    private static void writeFrameStrips(Map<Character, List<Animation>> charactersToAnimations) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "FrameStrips.sql"));

        // CREATE TABLE
        writer.write("CREATE TABLE FrameStrips (\n");
        writer.write(INDENT + "id INTEGER AUTO_INCREMENT,\n");
        writer.write(INDENT + "charId CHAR(2),\n");
        writer.write(INDENT + "subActionId INTEGER,\n");
        writer.write(INDENT + "frame INTEGER,\n");
        writer.write(INDENT + "hitbox BOOLEAN,\n");
        writer.write(INDENT + "iasa BOOLEAN,\n");
        writer.write(INDENT + "autocancel BOOLEAN,\n");
        writer.write(INDENT + "PRIMARY KEY (id),\n");
        writer.write(INDENT + "UNIQUE (charId, subActionId, frame),\n");
        writer.write(INDENT + "FOREIGN KEY (charId, subActionId) REFERENCES Animations(charId, subActionId)\n");
        writer.write(");\n\n");
        writer.flush();

        // INSERT
        AtomicBoolean first = new AtomicBoolean(true);
        AtomicInteger totalInserts = new AtomicInteger(0);
        charactersToAnimations.forEach((character, animations) -> {
            animations.forEach(animation -> {
                for (int i = 0; i < animation.frameStrip.size(); i++) {
                    // start numbering at frame 1 instead of frame 0
                    int frameStripNumber = i + 1;

                    // every 5,000 inserts, write another INSERT INTO so that we dont timeout
                    if (totalInserts.get() % 5000 == 0) {
                        if (first.get()) {
                            first.set(false);
                        } else {
                            tryWriteLine(writer, ");");
                            tryWriteLine(writer);
                        }

                        tryWriteLine(writer, "INSERT INTO FrameStrips");
                        tryWriteLine(writer, INDENT + "(charId, subActionId, frame, hitbox, iasa, autocancel)");
                        tryWriteLine(writer, "VALUES");
                    } else {
                        tryWriteLine(writer, "),");
                    }
                    totalInserts.incrementAndGet();

                    Set<FrameStripType> flags = animation.frameStrip.get(i);
                    tryWrite(writer, INDENT + "('" + character.name() + "', " + animation.subActionId + ", " + frameStripNumber + ", ");
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

    private static void writeHitboxes(Map<Character, List<Animation>> charactersToAnimations) throws IOException {
        BufferedWriter writer = Files.newBufferedWriter(Paths.get(DIRECTORY_NAME + "Hitboxes.sql"));

        // CREATE TABLE
        writer.write("CREATE TABLE Hitboxes (\n");
        writer.write(INDENT + "id INTEGER AUTO_INCREMENT,\n");
        writer.write(INDENT + "charId CHAR(2),\n");
        writer.write(INDENT + "subActionId INTEGER,\n");

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
        // TODO apparently you can have multiple hitboxes with the same id in the same group, see Kirby subAction 243
//        writer.write(INDENT + "UNIQUE (charId, subActionId, groupId, hitboxId),\n");
        writer.write(INDENT + "FOREIGN KEY (charId, subActionId) REFERENCES Animations(charId, subActionId)\n");
        writer.write(");\n\n");
        writer.flush();

        // INSERT
        writer.write("INSERT INTO Hitboxes\n");
        writer.write(INDENT + "(charId, subActionId, groupId, hitboxId, bone, damage"
                + ", zoffset, yoffset, xoffset, angle, knockbackScaling, fixedKnockback, baseKnockback, shieldDamage)\n");
        writer.write("VALUES\n");
        AtomicBoolean first = new AtomicBoolean(true);
        charactersToAnimations.forEach((character, animations) -> {
            animations.forEach(animation -> {
                for (int i = 0; i < animation.hitboxes.size(); i++) {
                    List<Hitbox> hitboxGroup = animation.hitboxes.get(i);
                    for (Hitbox hitbox : hitboxGroup) {
                        if (first.get()) {
                            first.set(false);
                        } else {
                            tryWriteLine(writer, ",");
                        }
                        tryWrite(writer, INDENT + "('" + character.name() + "', " + animation.subActionId + ", " + i
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
        buildWriter.write("source Animations.sql\n");
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
        cleanWriter.write("DROP TABLE IF EXISTS Animations;\n");
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