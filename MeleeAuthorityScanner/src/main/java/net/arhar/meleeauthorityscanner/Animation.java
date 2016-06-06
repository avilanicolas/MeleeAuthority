package net.arhar.meleeauthorityscanner;

import java.util.EnumSet;
import java.util.List;
import static net.arhar.meleeauthorityscanner.AnimationCommandType.*;

public class Animation {
    
    public final List<AnimationCommand> commands;
    public final EnumSet[] frameStrip;
        
    public Animation(List<AnimationCommand> commands) {
        this.commands = commands;
        frameStrip = new EnumSet[4];
        
        // now build the frame strip
        
        // first, figure out how many total frames there are based on timers
        int lastFrame = 0;
        int currentFrame = 0;
        for (AnimationCommand command : commands) {
            if (command.type == ASYNC_TIMER) {
                
            } else if (command.type == SYNC_TIMER) {
                
            }
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