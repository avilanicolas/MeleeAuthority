package net.arhar.meleeauthorityscanner;

/**
 * Ordinal value indicates position on disc
 * Enum name is used for SQL schema
 */
public enum Attribute {
    InitWalkVel("Initial Walk Velocity", Float.class, true, null),
    WalkAccel("Walk Acceleration?", Float.class, false, null),
    WalkMaxVel("Walk Maximum Velocity", Float.class, true, null),
    SlowWalkMax("Slow Walk Max?", Float.class, false, null),
    MidWalkPoint("Mid Walk Point?", Float.class, false, null),
    FastWalkMin("Fast Walk Min?", Float.class, false, null),
    Friction("Friction/Stop Deccel", Float.class, true, "Influences wavedash length, lower is slidier"),
    DashInitVel("Dash Initial Velocity", Float.class, true, null),
    DashAccelA("Dash & Run Acceleration A", Float.class, true, null),
    DashAccelB("Dash & Run Acceleration B", Float.class, true, null),
    DashTermVel("Dash & Run Terminal Velocity", Float.class, true, null),
    RunAnimScal("Run Animation Scaling", Float.class, true, null),
    RunAccel("Run Acceleration?", Float.class, false, null),
    Unknown14(Attribute.UNKNOWN, Float.class, false, null),
    JumpFrames("Jump Startup Lag (Frames)", Float.class, true, null),
    JumpHInitVel("Jump H Initial Velocity", Float.class, true, null),
    JumpVInitVel("Jump V Initial Velocity", Float.class, true, null),
    JumpMomentMult("Ground to Air Jump Momentum Multiplier", Float.class, true, null),
    JumpHMaxVel("Jump H Maximum Velocity", Float.class, true, null),
    SHVInitVel("Shorthop V Initial Velocity", Float.class, true, null),
    AirJMult("Air Jump Multiplier", Float.class, true, null),
    DblJMult("Double Jump Momentum", Float.class, true, null),
    NumJumps("Number of Jumps", Integer.class, true, null),
    Gravity("Gravity", Float.class, true, null),
    TermVel("Terminal Velocity", Float.class, true, null),
    AirMobA("Air Mobility A", Float.class, true, null),
    AirMobB("Air Mobility B", Float.class, true, null),
    MaxAirHVel("Max Aerial H Velocity", Float.class, true, null),
    AirFriction("Air Friction", Float.class, true, null),
    FFTermVel("Fast Fall Terminal Velocity", Float.class, true, null),
    Unknown31(Attribute.UNKNOWN, Integer.class, false, null),
    Jab2Window("Jab 2 Window?", Integer.class, false, null),
    Jab3Window("Jab 3 Window?", Integer.class, false, null),
    ChDirFrames("Frames to Change Direction on Standing Turn", Integer.class, true, null),
    Weight("Weight", Integer.class, true, null),
    ModelScaling("Model Scaling", Float.class, true, null),
    ShieldSize("Shield Size", Float.class, true, null),
    ShldBrkInitVel("Shield Break Initial Velocity", Float.class, true, null),
    RpdJabWindow("Rapid Jab Window", Integer.class, true, null),
    Unknown40(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown41(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown42(Attribute.UNKNOWN, Integer.class, false, null),
    LdgJmpHVel("Ledgejump Horizontal Velocity", Float.class, true, null),
    LdgJmpVVel("Ledgejump Vertical Velocity", Float.class, true, null),
    ThrowVel("Item Throw Velocity", Float.class, true, null),
    Unknown46(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown47(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown48(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown49(Attribute.UNKNOWN, Integer.class, false, null),
    Unkonwn50(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown51(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown52(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown53(Attribute.UNKNOWN, Integer.class, false, null),
    Unkonwn54(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown55(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown56(Attribute.UNKNOWN, Integer.class, false, null),
    StarDmg("Kirby Neutral+B Star Damage", Integer.class, true, null),
    ALag("Normal Landing Lag", Integer.class, true, null),
    NLag("N-Air Landing Lag", Integer.class, true, null),
    FLag("F-Air Landing Lag", Integer.class, true, null),
    BLag("B-Air Landing Lag", Integer.class, true, null),
    ULag("U-Air Landing Lag", Integer.class, true, null),
    DLag("D-Air Landing Lag", Integer.class, true, null),
    VMdlScaling("Victory Screen Window Model Scaling", Float.class, true, null),
    Unknown65(Attribute.UNKNOWN, Integer.class, false, null),
    WJmpHVel("WallJump H Velocity", Float.class, true, null),
    WJmpVVel("WallJump V Velocity", Float.class, true, null),
    Unknown68(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown69(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown70(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown71(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown72(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown73(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown74(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown75(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown76(Attribute.UNKNOWN, Integer.class, false, "DJC?"),
    Unknown77(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown78(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown79(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown80(Attribute.UNKNOWN, Integer.class, false, "DJC?"),
    Unknown81(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown82(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown83(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown84(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown85(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown86(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown87(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown88(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown89(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown90(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown91(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown92(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown93(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown94(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown95(Attribute.UNKNOWN, Integer.class, false, null),
    Unknown96(Attribute.UNKNOWN, Integer.class, false, "Double jump cancel?");

    private static final String UNKNOWN = "????";

    public final String fullName;
    public final Class<?> numberType;
    public final boolean known;
    public final String notes;

    private Attribute(String fullName, Class<?> numberType, boolean known, String notes) {
        this.fullName = fullName;
        this.numberType = numberType;
        this.known = known;
        this.notes = notes;
    }
}