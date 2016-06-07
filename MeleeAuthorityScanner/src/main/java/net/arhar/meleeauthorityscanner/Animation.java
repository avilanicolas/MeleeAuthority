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
        
    public Animation(List<AnimationCommand> commands) {
        this.commands = commands;
        frameStrip = new ArrayList<>();
        
        boolean iasa = false;
        boolean hitbox = false;
        boolean autocancel = false;
        Iterator<AnimationCommand> commandIterator = commands.iterator();
        int waitFrames = 0, currentFrame = 0;
        while (commandIterator.hasNext() || waitFrames > 0) {
            if (waitFrames > 0) {
                waitFrames--;
            } else {
                AnimationCommand command = commandIterator.next();
                switch (command.type) {
                case ASYNC_TIMER:
                    waitFrames = (command.data[3] & 0xFF) + currentFrame;
                    break;
                case SYNC_TIMER:
                    waitFrames = command.data[3] & 0xFF;
                    break;
                case HITBOX:
                    hitbox = true;
                    break;
                case IASA:
                    iasa = true;
                    break;
                case AUTOCANCEL:
                    if ((command.data[3] & 0xFF) == 1) {
                        autocancel = false;
                    } else {
                        autocancel = true;
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
            if (stripEntry.isEmpty()) {
                stripEntry.add(FrameStripType.NOTHING);
            }
            frameStrip.add(Sets.newEnumSet(stripEntry, FrameStripType.class));
            
            currentFrame++;
        }
    }
        
    private static enum FrameStripType {
        NOTHING,
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