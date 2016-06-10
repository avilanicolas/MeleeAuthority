package net.arhar.meleeauthorityscanner;

import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Enum name represents internal character code
 */
public enum Character {

    Kp(0x5, "Bowser", 0x3644, 0x007b40, 0x0098e0),
    Ca(0x0, "Captain Falcon", 0x3774, 0x007a98, 0x009868),
    Dr(0x16, "Doctor Mario", 0x3540, 0x007264, 0x008ecc),
    Dk(0x1, "Donkey Kong", 0x39B0, 0x007e78, 0x009e10),
    Fc(0x14, "Falco", 0x37E4, 0x007804, 0x0096ac),
    Fx(0x2, "Fox", 0x3714, 0x00771c, 0x0095c4),
    Gw(0x3, "Game & Watch", 0x3614, 0x007c58, 0x009aa0),
    Gn(0x19, "Ganondorf", 0x371c, 0x0075f0, 0x0093c0),
    Pp(0xe, "Ice Climbers (Popo)", 0x32A4, 0x006f98, 0x008db0),
//    Nn(0x9999, "Ice Climbers (Nana)", 0x1188, 0x004d30, 0x006b48), // Nana is the same as popo but without animation commands
    Pr(0xf, "Jigglypuff", 0x38BC, 0x006f3c, 0x008de4),
    Kb(0x4, "Kirby", 0x4CE8, 0x00b280, 0x00df68),
    Lg(0x7, "Luigi", 0x33A0, 0x006f40, 0x008c80),
    Lk(0x6, "Link", 0x33FC, 0x007d90, 0x009b00),
    Mr(0x8, "Mario", 0x32D8, 0x006f20, 0x008b88),
    Ms(0x9, "Marth", 0x3744, 0x007cf0, 0x009b98),
    Mt(0xa, "Mewtwo", 0x3750, 0x0075a0, 0x009310),
    Ns(0xb, "Ness", 0x34E0, 0x007674, 0x009504),
    Pe(0xc, "Peach", 0x3894, 0x008680, 0x00a450),
    Pc(0x18, "Pichu", 0x3454, 0x0075c8, 0x0093c8),
    Pk(0xd, "Pikachu", 0x3584, 0x0075f4, 0x0093f4),
    Fe(0x17, "Roy", 0x389C, 0x00800c, 0x009eb4),
    Ss(0x10, "Samus", 0x3484, 0x0079a8, 0x009700),
    Sk(0x13, "Sheik", 0x3418, 0x007420, 0x0091d8),
    Ys(0x11, "Yoshi", 0x335C, 0x007320, 0x009090),
    Cl(0x15, "Young Link", 0x35A0, 0x00813c, 0x009eac),
    Zd(0x12, "Zelda", 0x37F4, 0x007cc8, 0x0099f0);

    // nonplayable characters, removing because hitbox calculation makes duplicates in sql
//    Bo(0x1b, "Wireframe (Male)", 0x31C8, 0x006214, 0x7DBC),
//    Gl(0x1c, "Wireframe (Female)", 0x2FF4, 0x006130, 0x7CD8),
//    Gk(0x1d, "Giga Bowser", 0x34D0, 0x007c40, 0x000099E0),
//    Sb(0x1f, "Sandbag", 0x1464, 0x001eb4, 0x00003A74),
//    Mh(0x1a, "Master Hand", 0x914 + 0x20, 0x00002824, 0x0000487C),
//    Ch(0x1e, "Crazy Hand", 0x898 + 0x20, 0x00002C40, 0x00004C80);

    public final int id;
    public final String fullName;
    public final int attributeOffset;
    public final int subOffset;
    public final int subEnd;

    private Character(int id, String fullName, int attributeOffset, int subOffset, int subEnd) {
        this.id = id;
        this.fullName = fullName;
        this.attributeOffset = attributeOffset;
        this.subOffset = subOffset;
        this.subEnd = subEnd;
    }

    public static Character getCharacter(Predicate<Character> matcher) {
        return Arrays.stream(values())
            .filter(matcher)
            .findFirst()
            .get();
    }
}