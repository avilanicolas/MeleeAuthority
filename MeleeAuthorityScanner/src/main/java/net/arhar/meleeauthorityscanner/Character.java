package net.arhar.meleeauthorityscanner;

/**
 * Enum name represents internal character code
 */
public enum Character {

    Ca(0x00, "Captain Falcon"),
    Dk(0x01, "Donkey Kong"),
    Fx(0x02, "Fox"),
    Gw(0x03, "Game & Watch"),
    Kb(0x04, "Kirby"),
    Kp(0x05, "Bowser"),
    Lk(0x06, "Link"),
    Lg(0x07, "Luigi"),
    Mr(0x08, "Mario"),
    Ms(0x09, "Marth"),
    Mt(0x0A, "Mewtwo"),
    Ns(0x0B, "Ness"),
    Pe(0x0C, "Peach"),
    Pk(0x0D, "Pikachu"),
    Pp(0x0E, "Ice Climbers"),
    Pr(0x0F, "Jigglypuff"),
    Ss(0x10, "Samus"),
    Ys(0x11, "Yoshi"),
    Zd(0x12, "Zelda"),
    Sk(0x13, "Sheik"),
    Fc(0x14, "Falco"),
    Cl(0x15, "Young Link"),
    Dr(0x16, "Doctor Mario"),
    Fe(0x17, "Roy"),
    Pc(0x18, "Pichu"),
    Gn(0x19, "Ganondorf");
//    Nn(0x9999, "Ice Climbers (Nana)", 0x1188, 0x004d30, 0x006b48), // Nana is the same as popo but without animation commands

    // nonplayable characters
//    Bo(0x1b, "Wireframe (Male)", 0x31C8, 0x006214, 0x7DBC),
//    Gl(0x1c, "Wireframe (Female)", 0x2FF4, 0x006130, 0x7CD8),
//    Gk(0x1d, "Giga Bowser", 0x34D0, 0x007c40, 0x000099E0),
//    Sb(0x1f, "Sandbag", 0x1464, 0x001eb4, 0x00003A74),
//    Mh(0x1a, "Master Hand", 0x914 + 0x20, 0x00002824, 0x0000487C),
//    Ch(0x1e, "Crazy Hand", 0x898 + 0x20, 0x00002C40, 0x00004C80);

    public final int id;
    public final String fullName;

    private Character(int id, String fullName) {
        this.id = id;
        this.fullName = fullName;
    }
}