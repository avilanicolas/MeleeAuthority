package net.arhar.meleeauthorityscanner;

import java.nio.ByteBuffer;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

public enum SubAction {

    Attack11(0x2e, "Jab 1"),
    Attack12(0x2f, "Jab 2"),
    Attack100Start(0x31, "Rapid Jab Start"),
    Attack100Loop(0x32, "Rapid Jab Loop"),
    Attack100End(0x33, "Rapid Jab End"),
    AttackDash(0x34, "Dash Attack"),
    AttackS3Hi(0x35, "Forward-Tilt (High)"),
    AttackS3S(0x37, "Forward-Tilt"),
    AttackS3Lw(0x39, "Forward-Tilt (Low)"),
    AttackHi3(0x3a, "Up-Tilt"),
    AttackLw3(0x3b, "Down-Tilt"),
    AttackS4Hi(0x3c, "Forward-Smash (High)"),
    AttackS4S(0x3e, "Forward-Smash"),
    AttackS4Lw(0x40, "Forward-Smash (Low)"),
    AttackHi4(0x42, "Up-Smash"),
    AttackLw4(0x43, "Down-Smash"),
    AttackAirN(0x44, "Neutral-Air"),
    AttackAirF(0x45, "Forward-Air"),
    AttackAirB(0x46, "Back-Air"),
    AttackAirHi(0x47, "Up-Air"),
    AttackAirLw(0x48, "Down-Air"),
    ThrowF(0xf7, "Forward Throw"),
    ThrowB(0xf8, "Back Throw"),
    ThrowHi(0xf9, "Up Throw"),
    ThrowLw(0xfa, "Down Throw"),

    GuardOn(0x25, "Start Shield"),
    GuardOff(0x27, "Stop Shield"),
    EscapeN(0x29, "Spot Dodge"),
    EscapeF(0x2a, "Dodge Roll Forward"),
    EscapeB(0x2b, "Dodge Roll Backward"),
    EscapeAir(0x2c, "Air Dodge"),
    //        (0xf, "Jump Squat/Charge"), // this information is in the character attribute "JumpFrames"

    LandingAirN(0x49, "Nair Landing Lag"),
    LandingAirF(0x4a, "Fair Landing Lag"),
    LandingAirB(0x4b, "Bair Landing Lag"),
    LandingAirHi(0x4c, "Uair Landing Lag"),
    LandingAirLw(0x4d, "Dair Landing Lag");
    //        (0x24, "Special/Wavedash Landing Lag"), // this is broken and keeps calling itself "Landing" instead of "LandingFallSpecial"

    public final int offset;
    public final String description;

    private SubAction(int offset, String description) {
        this.offset = offset;
        this.description = description;
    }

    public static final Set<SubAction> L_CANCELLABLE = ImmutableSet.of(
        LandingAirN,
        LandingAirF,
        LandingAirB,
        LandingAirHi,
        LandingAirLw);

    public static final String UNKNOWN_ANIMATION = "[Unknown]";

    // these names seem to be just plain wrong
    // in game fox's fsmash is AttackS4S, but this calls it AttackS4
    // even crazy hand AND master hand both use this name and have it wrong
    public static String getInternalName(MeleeImageFileSystem fileSystem, Character character, int subactionOffset) {

        ByteBuffer buffer = ByteBuffer.wrap(fileSystem.getFileData("Pl" + character.name() + ".dat"));
        int pointerPointer = character.subOffset + 0x20 + subactionOffset * 6 * 4;
        buffer.position(pointerPointer);
        int pointer = buffer.getInt() + 0x20;
        // TODO replace this check with a version check or something
        if (pointer > buffer.limit() || pointer < 0) {
            System.out.printf("Pl" + character.name() + ".dat - pointerpointer: %X, pointer: %X, limit: %X\n", pointerPointer, pointer, buffer.limit());
            return "asdf";
        }
        buffer.position(pointer);
        StringBuilder nameBuilder = new StringBuilder();
        char temp;
        int counter = 4;
        while (true) {
            temp = (char) buffer.get();
            if (temp == 0) {
                break;
            }

            if (temp == '_') {
                counter--;
            } else if (counter == 1) {
                nameBuilder.append(temp);
            }
            if (counter == 0) {
                break;
            }
        }

        String name = nameBuilder.toString();
        if (name.equals("")) {
            return UNKNOWN_ANIMATION;
        }
        return name;
    }
}