package net.arhar.meleeauthorityscanner;

import java.util.Arrays;

public enum AnimationCommandType {

    HITBOX(0x2C, 0x14, "Hitbox"),
    THROW(0x88, 0xC, "Throw"),
    CHARGE(0xE0, 0x8, "Start Smash Charge"),
    BODY_STATE(0x68, 0x4, "Body State"),
    SELF_DAMAGE(0xCC, 0x4, "Self-Damage"),
    TERMINATE_COLLISIONS(0x40, 0x4, "Terminate Collisions"),
    CONTINUATION(0xD0, 0x4, "Continuation control?"),
    IASA(0x5C, 0x4, "Allow Interrupt (IASA)"),
    AUTOCANCEL(0x4C, 0x4, "Autocancel"),
    REVERSE_DIRECTION(0x50, 0x4, "Reverse Direction?"),
    SYNC_TIMER(0x04, 0x4, "Synchronous Timer"),
    ASYNC_TIMER(0x08, 0x4, "Asynchronous Timer"),
    SET_LOOP(0x0C, 0x4, "Set Loop"),
    EXEC_LOOP(0x10, 0x4, "Execute Loop"),
    SUBROUTINE(0x1C, 0x8, "Subroutine"),
    GOTO(0x14, 0x8, "GoTo"),
    RETURN(0x18, 0x4, "Return"),
    ARTICLE(0xAC, 0x4, "Generate article?"), // TODO this also appears as "Rumble" in CrazyHand
    MODEL(0x7C, 0x4, "Model mod"),
    GRAPHIC(0x28, 0x14, "Graphic Effect"),
    SOUND(0x44, 0xC, "Sound Effect"),
    RAND_SFX(0x48, 0x4, "Random Smash SFX"),
    TERMINATE_COLLISION(0x3C, 0x4, "Terminate Specific Collision"),
    SET_BONES(0x6C, 0x4, "Set All Bones State"),
    SET_BONE(0x70, 0x4, "Set Specific Bone State"),

    //(0x4e, 0x4, "B button check?"),
    //01 checks if b button is held down, and if it is not continues the script. See young link's arrow start.

    TBD(0xE8, 0x10, "TBD"),
    UNKNOWN(0x74, 0x4, "Unknown");

    public final int id;
    public final int length;
    public final String fullName;

	private AnimationCommandType(int id, int length, String fullName) {
	    this.id = id;
	    this.length = length;
	    this.fullName = fullName;
	}

	public static final int MAX_COMMAND_LENGTH = Arrays.stream(AnimationCommandType.values())
	    .mapToInt(command -> command.length)
	    .max()
	    .getAsInt();

	public static AnimationCommandType getById(int id) {
	    for (AnimationCommandType command : AnimationCommandType.values()) {
	        if (command.id == id) {
	            return command;
	        }
	    }
	    return UNKNOWN;
	}
}