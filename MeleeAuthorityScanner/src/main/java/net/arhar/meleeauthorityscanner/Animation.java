package net.arhar.meleeauthorityscanner;

import static net.arhar.meleeauthorityscanner.AnimationCommandType.ASYNC_TIMER;
import static net.arhar.meleeauthorityscanner.AnimationCommandType.SYNC_TIMER;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

public class Animation {
    
    public final List<AnimationCommand> commands;
    public final EnumSet[] frameStrip;
        
    public Animation(List<AnimationCommand> commands) {
        this.commands = commands;
        
        // now build the frame strip
        
        // first, figure out how many total frames there are based on timers
        int lastFrame = 0;
        int currentFrame = 0;
        for (AnimationCommand command : commands) {
            if (command.type == ASYNC_TIMER) {
                if (command.data[3] > lastFrame) {
                    lastFrame = command.data[3];
                }
            } else if (command.type == SYNC_TIMER) {
                currentFrame += command.data[3];
            }
        }
        if (currentFrame > lastFrame) {
            lastFrame = currentFrame;
        }
        
        
        
        List<EnumSet<FrameStripType>> asdf = new ArrayList<>();
        
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
                    // check value?
                    break;
                }
            }
            
            Set<FrameStripType> derp = new HashSet<>();
            if (iasa) {
                derp.add(FrameStripType.IASA);
            }
            if (hitbox) {
                derp.add(FrameStripType.HITBOX);
            }
            if (autocancel) {
                derp.add(FrameStripType.AUTOCANCEL);
            }
            if (derp.isEmpty()) {
                derp.add(FrameStripType.NOTHING);
            }
            asdf.add(Sets.newEnumSet(derp, FrameStripType.class));
            
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