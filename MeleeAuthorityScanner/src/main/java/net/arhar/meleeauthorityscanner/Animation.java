package net.arhar.meleeauthorityscanner;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

public class Animation {
    
    public final List<AnimationCommand> commands;
    public final List<EnumSet<FrameStripType>> frameStrip;
    
    public static boolean temp = false;
        
    public Animation(List<AnimationCommand> commands) {
    // TODO public Animation(List<AnimationCommand> commands, int numFrames) {
    // the total number of frames is NOT determined by the commands
        if (temp) {
            System.out.println();
        }
        this.commands = commands;
        frameStrip = new ArrayList<>();
        
        boolean iasa = false;
        boolean hitbox = false;
        boolean autocancel = false;
        Iterator<AnimationCommand> commandIterator = commands.iterator();
        int waitFrames = 0, currentFrame = 0;
        while (commandIterator.hasNext() || waitFrames > 0) {
            if (waitFrames > 0) {
                // advance a frame
                waitFrames--;
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
            } else {
                // execute the next command because there is no wait left
                AnimationCommand command = commandIterator.next();
                switch (command.type) {
                case ASYNC_TIMER:
                    // async timers: execute the next command on this frame number
                    // async 4 -> do thing on 4th frame, or frames[3]
                    // if we are on frame[0], and we want to do something on frame[3], then we have 3 wait frames
                    // wait frames = asyncframe - 1
                    // it isnt wait this number of frames its do this thing on this frame
                    waitFrames = (command.data[3] & 0xFF) - currentFrame - 1;
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
                case TERMINATE_COLLISIONS:
                    hitbox = false;
                    break;
                case TERMINATE_COLLISION:
                    // TODO this should specify a hitbox or something
                    hitbox = false;
                    break;
                }
            }
        }
        if (temp) {
            System.out.println();
        }
    }
        
    public static enum FrameStripType {
        IASA,
        HITBOX,
        AUTOCANCEL
    }

    public static class AnimationCommand {
        public final AnimationCommandType type;
        public final byte[] data;
        
        public AnimationCommand(AnimationCommandType type, byte[] data) {
            this.type = type;
            this.data = data;
        }
    }    
}