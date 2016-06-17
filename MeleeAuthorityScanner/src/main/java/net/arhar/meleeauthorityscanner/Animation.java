package net.arhar.meleeauthorityscanner;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.common.collect.Sets;

import net.arhar.meleeauthorityscanner.DatReader.AJDataHeader;
import net.arhar.meleeauthorityscanner.DatReader.SubActionHeader;

public class Animation {

    public final float frameCount;
    public final List<AnimationCommand> commands;
    public final List<EnumSet<FrameStripType>> frameStrip;
    public final List<List<Hitbox>> hitboxes; // group to list of hitboxes

    public final String internalName;
    public final String description;
    public final int subActionId;

            // TODO remove this
    public static boolean temp = false;

    public Animation(ByteBuffer pldat, SubActionHeader motherCommand, AJDataHeader ajHeader, Character character, int subActionId) {
        if (temp) {
            System.out.println();
        }

        this.frameCount = ajHeader.frameCount;

        // add description
        this.subActionId = subActionId;
        this.internalName = motherCommand.shortName;
        this.description = SubAction.getDescription(character, subActionId).description;

        // parse the commands from the file
        commands = new ArrayList<>();
        frameStrip = new ArrayList<>();
        int commandListOffset = motherCommand.getCommandListOffset();
        int bytesDown = 0;
        pldat.position(commandListOffset + bytesDown);
        // command lists are null terminated by four zero bytes
        while (pldat.getInt() != 0) {
            pldat.position(commandListOffset + bytesDown);
            AnimationCommandType commandType = AnimationCommandType.getById(pldat.get() & 0xFF);
            byte[] commandData = new byte[commandType.length];
            pldat.position(commandListOffset + bytesDown);
            for (int i = 0; i < commandType.length; i++) {
                commandData[i] = pldat.get();
            }
            commands.add(new AnimationCommand(commandType, commandData));
            bytesDown += commandType.length;
            pldat.position(commandListOffset + bytesDown);
        }

        Map<Integer, List<Hitbox>> hitboxMap = new TreeMap<>((one, two) -> one - two); // TODO this could be the wrong order

        boolean iasa = false;
        boolean hitbox = false;
        boolean autocancel = false;
        Iterator<AnimationCommand> commandIterator = commands.iterator();
        int waitFrames = 0;
        int currentFrame = 1; // start frame numbering at 1 instead of 0
        int totalFrames = (int) frameCount; // TODO this is a guess, and some animations advance "frames" per frame faster than others
        frameLoop: while (commandIterator.hasNext() || waitFrames > 0 || currentFrame < totalFrames) { // TODO check is frameCount comparison is correct here
            if (commandIterator.hasNext() && waitFrames == 0) {
                // execute the next command because there is no wait left
                AnimationCommand command = commandIterator.next();
                command.frame = currentFrame;
                switch (command.type) {
                    case ASYNC_TIMER:
                        // async timers: execute the next command on this frame number
                        // async 4 -> do thing on 4th frame, or frames[3]
                        // if we are on frame[0], and we want to do something on frame[3], then we have 3 wait frames
                        // wait frames = asyncframe - 1
                        // it isnt wait this number of frames its do this thing on this frame
                        waitFrames = (command.data[3] & 0xFF) - currentFrame;
                        if (waitFrames < 0) {
                            // the command is telling us to do something on a frame that already occured
                            // just do it now i guess
                            // TODO investigate this more, there are aync timers for frame 0 that worked before for some reason
                            waitFrames = 0;
                        }
                        if (temp) {
                            System.out.println("async on frame " + currentFrame + ": " + waitFrames);
                        }
                        break;
                    case SYNC_TIMER:
                        waitFrames = command.data[3] & 0xFF;
                        if (temp) {
                            System.out.println("sync on frame " + currentFrame + ": " + waitFrames);
                        }
                        break;
                    case HITBOX:
                        // add the hitbox
                        hitboxMap.putIfAbsent(currentFrame, new ArrayList<>());
                        hitboxMap.get(currentFrame).add(new Hitbox(command.data));
                        hitbox = true;
                        break;
                    case IASA:
                        iasa = true;
                        if (temp) {
                            System.out.println("iasa on frame " + currentFrame);
                        }
                        break;
                    case AUTOCANCEL:
                        if ((command.data[3] & 0xFF) == 1) {
                            autocancel = false;
                        } else {
                            autocancel = true;
                        }
                        if (temp) {
                            System.out.println("autocancel on frame " + currentFrame + ": " + autocancel);
                        }
                        break;
                    case TERMINATE_ALL_COLLISIONS:
                        hitbox = false;
                        break;
                    case TERMINATE_COLLISION:
                        // TODO this should specify a hitbox or something
                        hitbox = false;
                        break;
                    case EXEC_LOOP:
                    case GOTO:
                    case RETURN:
                    case SET_LOOP:
                    case SUBROUTINE:
                        // TODO
                        break frameLoop;
                    case ARTICLE:
                    case BODY_STATE:
                    case CHARGE:
                    case CONTINUATION:
                    case GRAPHIC:
                    case MODEL:
                    case RAND_SFX:
                    case REVERSE_DIRECTION:
                    case SELF_DAMAGE:
                    case SET_BONE:
                    case SET_BONES:
                    case SOUND:
                    case TBD:
                    case THROW:
                    case UNKNOWN:
                        // TODO
                        break;
                }
            } else {
                // advance a frame
                if (waitFrames > 0) {
                    waitFrames--;
                }
                currentFrame++;

                Set<FrameStripType> stripEntry = new HashSet<>();
                if (iasa) {
                    stripEntry.add(FrameStripType.IASA);
                }
                if (hitbox) {
                    stripEntry.add(FrameStripType.HITBOX);
                }
                if (autocancel) {
                    stripEntry.add(FrameStripType.AUTOCANCEL);
                }
                frameStrip.add(Sets.newEnumSet(stripEntry, FrameStripType.class));
            }
        }

        // convert the hitboxes to "groups" of hitboxes based on what frame they start on
        hitboxes = new ArrayList<>();
        // assuming .forEach will go over the set TreeMap iteration order
        hitboxMap.forEach((frame, hitboxSet) -> {
            hitboxes.add(hitboxSet);
        });

        if (temp) {
            System.out.println();
        }
    }

    public static enum FrameStripType {
        IASA,
        HITBOX,
        AUTOCANCEL
        // TODO add invulnerability, etc. here
    }

    public static class AnimationCommand {
        public final AnimationCommandType type;
        public final byte[] data;

        public int frame = -1;

        public AnimationCommand(AnimationCommandType type, byte[] data) {
            this.type = type;
            this.data = data;
        }
    }
}