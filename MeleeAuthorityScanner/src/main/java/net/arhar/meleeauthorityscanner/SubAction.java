package net.arhar.meleeauthorityscanner;

import static net.arhar.meleeauthorityscanner.Character.Ca;
import static net.arhar.meleeauthorityscanner.Character.Cl;
import static net.arhar.meleeauthorityscanner.Character.Dk;
import static net.arhar.meleeauthorityscanner.Character.Dr;
import static net.arhar.meleeauthorityscanner.Character.Fc;
import static net.arhar.meleeauthorityscanner.Character.Fe;
import static net.arhar.meleeauthorityscanner.Character.Fx;
import static net.arhar.meleeauthorityscanner.Character.Gn;
import static net.arhar.meleeauthorityscanner.Character.Gw;
import static net.arhar.meleeauthorityscanner.Character.Kp;
import static net.arhar.meleeauthorityscanner.Character.Lg;
import static net.arhar.meleeauthorityscanner.Character.Lk;
import static net.arhar.meleeauthorityscanner.Character.Mr;
import static net.arhar.meleeauthorityscanner.Character.Ms;
import static net.arhar.meleeauthorityscanner.Character.Mt;
import static net.arhar.meleeauthorityscanner.Character.Ns;
import static net.arhar.meleeauthorityscanner.Character.Pc;
import static net.arhar.meleeauthorityscanner.Character.Pe;
import static net.arhar.meleeauthorityscanner.Character.Pk;
import static net.arhar.meleeauthorityscanner.Character.Pp;
import static net.arhar.meleeauthorityscanner.Character.Pr;
import static net.arhar.meleeauthorityscanner.Character.Sk;
import static net.arhar.meleeauthorityscanner.Character.Ss;
import static net.arhar.meleeauthorityscanner.Character.Ys;
import static net.arhar.meleeauthorityscanner.Character.Zd;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
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
    LandingAirLw(0x4d, "Dair Landing Lag"),
    //        (0x24, "Special/Wavedash Landing Lag"), // this is broken and keeps calling itself "Landing" instead of "LandingFallSpecial"

    // Special Moves

    // Bowser / Giga Bowser
    Kp_127(0x127, "Bowser Neutral-B Start (Ground)", Kp),
    Kp_128(0x128, "Bowser Neutral-B Loop (Ground)", Kp),
    Kp_129(0x129, "Bowser Neutral-B End (Ground)", Kp),
    Kp_12A(0x12A, "Bowser (Air)Neutral-B Start", Kp),
    Kp_12B(0x12B, "Bowser (Air)Neutral-B Loop", Kp),
    Kp_12C(0x12C, "Bowser (Air)Neutral-B End", Kp),
    Kp_12D(0x12D, "Bowser Side-B Start (Ground)", Kp),
    Kp_12E(0x12E, "Bowser Side-B Hit (Ground)", Kp),//One is forward, other is behind?
    Kp_12F(0x12F, "Bowser Side-B Hit(2) (Ground)", Kp),//One is forward, other is behind?
    Kp_130(0x130, "Bowser Side-B End Forward (Ground)", Kp),
    Kp_131(0x131, "Bowser Side-B End Backward (Ground)", Kp),
    Kp_132(0x132, "Bowser (Air)Side-B Start", Kp),
    Kp_133(0x133, "Bowser (Air)Side-B Hit", Kp),//One is forward, other is behind?
    Kp_134(0x134, "Bowser (Air)Side-B Hit(2)", Kp),//One is forward, other is behind?
    Kp_135(0x135, "Bowser (Air)Side-B End Forward", Kp),
    Kp_136(0x136, "Bowser (Air)Side-B End Backward", Kp),
    Kp_137(0x137, "Bowser (Ground)Up-B", Kp),
    Kp_138(0x138, "Bowser (Air)Up-B", Kp),
    Kp_139(0x139, "Bowser (Ground)Down-B", Kp),
    Kp_13A(0x13A, "Bowser (Air)Down-B", Kp),
    Kp_13B(0x13B, "Bowser Down-B Landing", Kp),

    // Ganon / Falcon
    Ca_12D(0x12D, "Falcon (Ground)Neutral-B", Ca, Gn),
    Ca_12E(0x12E, "Falcon (Air)Neutral-B", Ca, Gn),
    Ca_12F(0x12F, "Falcon (Ground)Side-B Start", Ca, Gn),
    Ca_130(0x130, "Falcon (Ground)Side-B", Ca, Gn),
    Ca_131(0x131, "Falcon (Air)Side-B Start", Ca, Gn),
    Ca_132(0x132, "Falcon (Air)Side-B", Ca, Gn),
    Ca_133(0x133, "Falcon (Ground)Up-B", Ca, Gn),
    Ca_134(0x134, "Falcon (Air)Up-B", Ca, Gn),
    Ca_135(0x135, "Falcon Up-B Hold", Ca, Gn),
    Ca_136(0x136, "Falcon Up-B Release", Ca, Gn),
    Ca_137(0x137, "Falcon (Ground)Down-B", Ca, Gn),
    Ca_138(0x138, "Falcon (Ground)Down-B End", Ca, Gn),
    Ca_139(0x139, "Falcon (Air)Down-B", Ca, Gn),
    Ca_13A(0x13A, "Falcon (Air)Down-B End", Ca, Gn),
    Ca_13B(0x13B, "Falcon (Ground)Down-B End In-Air", Ca, Gn),
    Ca_13C(0x13C, "Falcon (Air)Down-B End In-Air", Ca, Gn),

    // Mario / Doctor Mario
    Mr_127(0x127, "Mario (Ground)Neutral-B", Mr, Dr),
    Mr_128(0x128, "Mario (Air)Neutral-B", Mr, Dr),
    Mr_129(0x129, "Mario (Ground)Side-B", Mr, Dr),
    Mr_12a(0x12a, "Mario (Air)Side-B", Mr, Dr),
    Mr_12b(0x12b, "Mario (Ground)Up-B", Mr, Dr),
    Mr_12c(0x12c, "Mario (Air)Up-B", Mr, Dr),
    Mr_12d(0x12d, "Mario (Ground)Down-B", Mr, Dr),
    Mr_12e(0x12e, "Mario (Air)Down-B", Mr, Dr),

    // Donkey Kong
    Dk_13F(0x13F, "Donkey Kong (Ground)Neutral-B Start", Dk),
    Dk_140(0x140, "Donkey Kong (Ground)Neutral-B Loop", Dk),
    Dk_141(0x141, "Donkey Kong (Ground)Neutral-B Cancel", Dk),
    Dk_142(0x142, "Donkey Kong (Ground)Neutral-B", Dk),
    Dk_143(0x143, "Donkey Kong (Ground)Neutral-B(2)", Dk),
    Dk_144(0x144, "Donkey Kong (Air)Neutral-B Start", Dk),
    Dk_145(0x145, "Donkey Kong (Air)Neutral-B Loop", Dk),
    Dk_146(0x146, "Donkey Kong (Air)Neutral-B Cancel", Dk),
    Dk_147(0x147, "Donkey Kong (Air)Neutral-B", Dk),
    Dk_148(0x148, "Donkey Kong (Air)Neutral-B(2)", Dk),
    Dk_149(0x149, "Donkey Kong (Ground)Side-B", Dk),
    Dk_14A(0x14A, "Donkey Kong (Air)Side-B", Dk),
    Dk_14B(0x14B, "Donkey Kong (Ground)Up-B", Dk),
    Dk_14C(0x14C, "Donkey Kong (Air)Up-B", Dk),
    Dk_14D(0x14D, "Donkey Kong Down-B Start", Dk),
    Dk_14E(0x14E, "Donkey Kong Down-B Loop", Dk),
    Dk_14F(0x14F, "Donkey Kong Down-B End", Dk),
    Dk_150(0x150, "Donkey Kong Down-B End(2)", Dk),

    // Fox / Falco
    Fx_127(0x127, "Fox (Ground)Neutral-B Start", Fx, Fc),
    Fx_128(0x128, "Fox (Ground)Neutral-B Loop", Fx, Fc),
    Fx_129(0x129, "Fox (Ground)Neutral-B End", Fx, Fc),
    Fx_12A(0x12A, "Fox (Air)Neutral-B Start", Fx, Fc),
    Fx_12B(0x12B, "Fox (Air)Neutral-B Loop", Fx, Fc),
    Fx_12C(0x12C, "Fox (Air)Neutral-B End", Fx, Fc),
    Fx_12D(0x12D, "Fox (Ground)Side-B Start", Fx, Fc),
    Fx_12E(0x12E, "Fox (Ground)Side-B", Fx, Fc),
    Fx_12F(0x12F, "Fox (Ground)Side-B End", Fx, Fc),
    Fx_130(0x130, "Fox (Air)Side-B Start", Fx, Fc),
    Fx_131(0x131, "Fox (Air)Side-B", Fx, Fc),
    Fx_132(0x132, "Fox (Air)Side-B End", Fx, Fc),
    Fx_133(0x133, "Fox (Ground)Up-B Hold", Fx, Fc),
    Fx_134(0x134, "Fox (Air)Up-B Hold", Fx, Fc),
    Fx_135(0x135, "Fox Up-B", Fx, Fc),
    Fx_136(0x136, "Fox Up-B Landing", Fx, Fc),
    Fx_137(0x137, "Fox Up-B Fall", Fx, Fc),
    Fx_138(0x138, "Fox Up-B Bound", Fx, Fc),
    Fx_139(0x139, "Fox (Ground)Down-B Start", Fx, Fc),
    Fx_13A(0x13A, "Fox (Ground)Down-B Loop", Fx, Fc),
    Fx_13B(0x13B, "Fox (Ground)Down-B Hit", Fx, Fc),
    Fx_13C(0x13C, "Fox (Ground)Down-B End", Fx, Fc),
    Fx_13D(0x13D, "Fox (Air)Down-B Start", Fx, Fc),
    Fx_13E(0x13E, "Fox (Air)Down-B Loop", Fx, Fc),
    Fx_13F(0x13F, "Fox (Air)Down-B Hit", Fx, Fc),
    Fx_140(0x140, "Fox (Air)Down-B End", Fx, Fc),

    // Mr. Game and Watch
    Gw_127(0x127, "G&W (Ground)Neutral-B", Gw),
    Gw_128(0x128, "G&W (Air)Neutral-B", Gw),
    Gw_129(0x129, "G&W (Ground)Side-B (1)", Gw),
    Gw_12A(0x12A, "G&W (Ground)Side-B (2)", Gw),
    Gw_12B(0x12B, "G&W (Ground)Side-B (3)", Gw),
    Gw_12C(0x12C, "G&W (Ground)Side-B (4)", Gw),
    Gw_12D(0x12D, "G&W (Ground)Side-B (5)", Gw),
    Gw_12E(0x12E, "G&W (Ground)Side-B (6)", Gw),
    Gw_12F(0x12F, "G&W (Ground)Side-B (7)", Gw),
    Gw_130(0x130, "G&W (Ground)Side-B (8)", Gw),
    Gw_131(0x131, "G&W (Ground)Side-B (9)", Gw),
    Gw_132(0x132, "G&W (Air)Side-B (1)", Gw),
    Gw_133(0x133, "G&W (Air)Side-B (2)", Gw),
    Gw_134(0x134, "G&W (Air)Side-B (3)", Gw),
    Gw_135(0x135, "G&W (Air)Side-B (4)", Gw),
    Gw_136(0x136, "G&W (Air)Side-B (5)", Gw),
    Gw_137(0x137, "G&W (Air)Side-B (6)", Gw),
    Gw_138(0x138, "G&W (Air)Side-B (7)", Gw),
    Gw_139(0x139, "G&W (Air)Side-B (8)", Gw),
    Gw_13A(0x13A, "G&W (Air)Side-B (9)", Gw),
    Gw_13B(0x13B, "G&W (Ground)Up-B", Gw),
    Gw_13C(0x13C, "G&W (Air)Up-B", Gw),
    Gw_13D(0x13D, "G&W (Ground)Down-B", Gw),
    Gw_13E(0x13E, "G&W (Ground)Down-B Absorb", Gw),
    Gw_13F(0x13F, "G&W (Ground)Down-B Shoot", Gw),
    Gw_140(0x140, "G&W (Air)Down-B", Gw),
    Gw_141(0x141, "G&W (Air)Down-B Absorb", Gw),
    Gw_142(0x142, "G&W (Air)Down-B Shoot", Gw),

    // Ice Climbers
    Pp_127(0x127, "Ice Climbers (Ground)Neutral-B", Pp),
    Pp_128(0x128, "Ice Climbers (Air)Neutral-B", Pp),
    Pp_129(0x129, "Ice Climbers (Ground)Side-B (1)", Pp),
    Pp_12A(0x12A, "Ice Climbers (Ground)Side-B (2)", Pp),
    Pp_12B(0x12B, "Ice Climbers (Air)Side-B (1)", Pp),
    Pp_12C(0x12C, "Ice Climbers (Air)Side-B (2)", Pp),
    Pp_12D(0x12D, "Ice Climbers (Ground)Up-B Start", Pp),
    Pp_12E(0x12E, "Ice Climbers (Ground)Up-B Throw(1)", Pp),
    Pp_12F(0x12F, "Ice Climbers (Ground)Up-B Throw(2)", Pp),
    Pp_130(0x130, "Ice Climbers (Ground)Up-B Throw(3)", Pp),
    Pp_131(0x131, "Ice Climbers (Ground)Up-B Throw(4)", Pp),
    Pp_132(0x132, "Ice Climbers (Air)Up-B Start", Pp),
    Pp_133(0x133, "Ice Climbers (Air)Up-B Throw(1)", Pp),
    Pp_134(0x134, "Ice Climbers (Air)Up-B Throw(2)", Pp),
    Pp_135(0x135, "Ice Climbers (Air)Up-B Throw(3)", Pp),
    Pp_136(0x136, "Ice Climbers (Air)Up-B Throw(4)", Pp),
    Pp_137(0x137, "Ice Climbers (Ground)Down-B", Pp),
    Pp_138(0x138, "Ice Climbers (Air)Down-B", Pp),

    // Jigglypuff
    Pr_12C(0x12C, "Jigglypuff (Ground)Neutral-B Start (R)", Pr),
    Pr_12D(0x12D, "Jigglypuff (Ground)Neutral-B Start (L)", Pr),
    Pr_12E(0x12E, "Jigglypuff (Ground)Neutral-B(1)", Pr),
    Pr_12F(0x12F, "Jigglypuff (Ground)Neutral-B(2)", Pr),
    Pr_130(0x130, "Jigglypuff (Ground)Neutral-B(3)", Pr),
    Pr_131(0x131, "Jigglypuff (Ground)Neutral-B(4)", Pr),
    Pr_132(0x132, "Jigglypuff (Ground)Neutral-B End (R)", Pr),
    Pr_133(0x133, "Jigglypuff (Ground)Neutral-B End (L)", Pr),
//    Pr_12C(0x12C, "Jigglypuff (Air)Neutral-B Start (R)", Pr),
//    Pr_12D(0x12D, "Jigglypuff (Air)Neutral-B Start (L)", Pr),
//    Pr_12E(0x12E, "Jigglypuff (Air)Neutral-B(1)", Pr),
//    Pr_12F(0x12F, "Jigglypuff (Air)Neutral-B(2)", Pr),
//    Pr_130(0x130, "Jigglypuff (Air)Neutral-B(3)", Pr),
//    Pr_131(0x131, "Jigglypuff (Air)Neutral-B(4)", Pr),
//    Pr_132(0x132, "Jigglypuff (Air)Neutral-B End (R)", Pr),
//    Pr_133(0x133, "Jigglypuff (Air)Neutral-B End (L)", Pr),
    Pr_13C(0x13C, "Jigglypuff Neutral-B", Pr),
    Pr_13D(0x13D, "Jigglypuff (Ground)Side-B", Pr),
    Pr_13E(0x13E, "Jigglypuff (Air)Side-B", Pr),
    Pr_13F(0x13F, "Jigglypuff (Ground)Up-B (L)", Pr),
    Pr_140(0x140, "Jigglypuff (Air)Up-B (L)", Pr),
    Pr_141(0x141, "Jigglypuff (Ground)Up-B (R)", Pr),
    Pr_142(0x142, "Jigglypuff (Air)Up-B (R)", Pr),
    Pr_143(0x143, "Jigglypuff (Ground)Down-B (L)", Pr),
    Pr_144(0x144, "Jigglypuff (Air)Down-B (L)", Pr),
    Pr_145(0x145, "Jigglypuff (Ground)Down-B (R)", Pr),
    Pr_146(0x146, "Jigglypuff (Air)Down-B (R)", Pr),

    // Skipping Kirby TODO

    // Luigi
    Lg_127(0x127, "Luigi (Ground)Neutral-B", Lg),
    Lg_128(0x128, "Luigi (Air)Neutral-B", Lg),
    Lg_129(0x129, "Luigi (Ground)Side-B Start", Lg),
    Lg_12A(0x12A, "Luigi (Ground)Side-B Charge", Lg),
    Lg_12B(0x12B, "Luigi (Ground)Side-B(1)", Lg),
    Lg_12C(0x12C, "Luigi (Ground)Side-B(2)", Lg),
    Lg_12D(0x12D, "Luigi (Ground)Side-B(3)", Lg),
    Lg_12E(0x12E, "Luigi (Ground)Side-B End", Lg),
    Lg_12F(0x12F, "Luigi (Air)Side-B Start", Lg),
    Lg_130(0x130, "Luigi (Air)Side-B Charge", Lg),
    //Most likely, these two Side-B entries aren't air versions of the move, but are entries for misfire.
    //We can re-label them later once we're sure.
    Lg_131(0x131, "Luigi (Air)Side-B(1)", Lg),
    Lg_132(0x132, "Luigi (Air)Side-B(2)", Lg),
    Lg_133(0x133, "Luigi (Air)Side-B End", Lg),
    Lg_134(0x134, "Luigi (Ground)Up-B", Lg),
    Lg_135(0x135, "Luigi (Air)Up-B", Lg),
    Lg_136(0x136, "Luigi (Ground)Down-B", Lg),
    Lg_137(0x137, "Luigi (Air)Down-B", Lg),

    // Marth / Roy
    Ms_127(0x127, "Marth Neutral-B Start", Ms, Fe),
    Ms_128(0x128, "Marth Neutral-B Hold", Ms, Fe),
    Ms_129(0x129, "Marth Neutral-B End", Ms, Fe),
    Ms_12A(0x12A, "Marth Neutral-B End(2)", Ms, Fe),
    Ms_12B(0x12B, "Marth Neutral-B Start (Air)", Ms, Fe),
    Ms_12C(0x12C, "Marth Neutral-B Hold (Air)", Ms, Fe),
    Ms_12D(0x12D, "Marth Neutral-B End (Air)", Ms, Fe),
    Ms_12E(0x12E, "Marth Neutral-B End(2) (Air)", Ms, Fe),
    Ms_12F(0x12F, "Marth Side-B 1", Ms, Fe),
    Ms_130(0x130, "Marth Side-B 2 Up", Ms, Fe),
    Ms_131(0x131, "Marth Side-B 2 Side", Ms, Fe),
    Ms_132(0x132, "Marth Side-B 3 Up", Ms, Fe),
    Ms_133(0x133, "Marth Side-B 3 Side", Ms, Fe),
    Ms_134(0x134, "Marth Side-B 3 Down", Ms, Fe),
    Ms_135(0x135, "Marth Side-B 4 Up", Ms, Fe),
    Ms_136(0x136, "Marth Side-B 4 Side", Ms, Fe),
    Ms_137(0x137, "Marth Side-B 4 Down", Ms, Fe),
    Ms_138(0x138, "Marth Side-B 1 (Air)", Ms, Fe),
    Ms_139(0x139, "Marth Side-B 2 Up (Air)", Ms, Fe),
    Ms_13A(0x13A, "Marth Side-B 2 Side (Air)", Ms, Fe),
    Ms_13B(0x13B, "Marth Side-B 3 Up (Air)", Ms, Fe),
    Ms_13C(0x13C, "Marth Side-B 3 Side (Air)", Ms, Fe),
    Ms_13D(0x13D, "Marth Side-B 3 Down (Air)", Ms, Fe),
    Ms_13E(0x13E, "Marth Side-B 4 Up (Air)", Ms, Fe),
    Ms_13F(0x13F, "Marth Side-B 4 Side (Air)", Ms, Fe),
    Ms_140(0x140, "Marth Side-B 4 Down (Air)", Ms, Fe),
    Ms_141(0x141, "Marth Up-B (Ground)", Ms, Fe),
    Ms_142(0x142, "Marth Up-B (Air)", Ms, Fe),
    Ms_143(0x143, "Marth Down-B", Ms, Fe),
    Ms_144(0x144, "Marth Down-B Counter", Ms, Fe),
    Ms_145(0x145, "Marth Down-B (Air)", Ms, Fe),
    Ms_146(0x146, "Marth Down-B Counter (Air)", Ms, Fe),

    // Link / Young Link
    Lk_128(0x128, "Link (Ground)Neutral-B Start", Lk, Cl),
    Lk_129(0x129, "Link (Ground)Neutral-B Charge", Lk, Cl),
    Lk_12A(0x12A, "Link (Ground)Neutral-B End", Lk, Cl),
    Lk_12B(0x12B, "Link (Air)Neutral-B Start", Lk, Cl),
    Lk_12C(0x12C, "Link (Air)Neutral-B Charge", Lk, Cl),
    Lk_12D(0x12D, "Link (Air)Neutral-B End", Lk, Cl),
    Lk_12E(0x12E, "Link (Ground)Side-B Throw", Lk, Cl),
    Lk_12F(0x12F, "Link (Ground)Side-B Catch", Lk, Cl),
    Lk_130(0x130, "Link (Ground)Side-B Throw(Angled, Lk, Cl)", Lk, Cl),
    Lk_131(0x131, "Link (Air)Side-B Throw", Lk, Cl),
    Lk_132(0x132, "Link (Air)Side-B Catch", Lk, Cl),
    Lk_133(0x133, "Link (Air)Side-B Throw(Angled, Lk, Cl)", Lk, Cl),
    Lk_134(0x134, "Link (Ground)Up-B", Lk, Cl),
    Lk_135(0x135, "Link (Air)Up-B", Lk, Cl),
    Lk_136(0x136, "Link (Ground)Down-B", Lk, Cl),
    Lk_137(0x137, "Link (Air)Down-B", Lk, Cl),
    //Two more entries below SpecialAirLw, AirCatch and AirCatchHit.
    //I'd assume that's links zair.
    //There's also an entry above SpecialNStart, AttackS42.
    //Its entries look similar to down tilt, and all its hitboxes launch at angle 65
    //I'd assume this is the spike hitbox for link's down tilt.
    //Maybe consider creating a new list for misc. subactions for cases like this.

    // Mewtwo
    Mt_127(0x127, "Mewtwo (Ground)Neutral-B Start", Mt),
    Mt_128(0x128, "Mewtwo (Ground)Neutral-B Loop", Mt),
    Mt_129(0x129, "Mewtwo (Ground)Neutral-B Loop(2, Mt)", Mt),
    Mt_12A(0x12A, "Mewtwo (Ground)Neutral-B Cancel", Mt),
    Mt_12B(0x12B, "Mewtwo (Ground)Neutral-B End", Mt),
    Mt_12C(0x12C, "Mewtwo (Air)Neutral-B Start", Mt),
    Mt_12E(0x12E, "Mewtwo (Air)Neutral-B Loop", Mt),
    Mt_12D(0x12D, "Mewtwo (Air)Neutral-B Loop(2, Mt)", Mt),
    Mt_12F(0x12F, "Mewtwo (Air)Neutral-B Cancel", Mt),
    Mt_130(0x130, "Mewtwo (Air)Neutral-B End", Mt),
    Mt_131(0x131, "Mewtwo (Ground)Side-B", Mt),
    Mt_132(0x132, "Mewtwo (Air)Side-B", Mt),
    Mt_133(0x133, "Mewtwo (Ground)Up-B Start", Mt),
    Mt_134(0x134, "Mewtwo (Ground)Up-B", Mt),
    Mt_135(0x135, "Mewtwo Up-B Lost", Mt),
    Mt_136(0x136, "Mewtwo (Air)Up-B Start", Mt),
    Mt_137(0x137, "Mewtwo (Air)Up-B", Mt),
    Mt_138(0x138, "Mewtwo (Ground)Down-B ", Mt),
    Mt_139(0x139, "Mewtwo (Air)Down-B", Mt),

    // Ness
    Ns_12B(0x12B, "Ness (Ground)Neutral-B Start", Ns),
    Ns_12C(0x12C, "Ness (Ground)Neutral-B Hold", Ns),
    Ns_12D(0x12D, "Ness (Ground)Neutral-B Hold(2, Ns)", Ns),
    Ns_12E(0x12E, "Ness (Ground)Neutral-B End", Ns),
    Ns_12F(0x12F, "Ness (Air)Neutral-BStart", Ns),
    Ns_130(0x130, "Ness (Air)Neutral-B Hold", Ns),
    Ns_131(0x131, "Ness (Air)Neutral-B Hold(2, Ns)", Ns),
    Ns_132(0x132, "Ness (Air)Neutral-B End", Ns),
    Ns_133(0x133, "Ness (Ground)Side-B", Ns),
    Ns_134(0x134, "Ness (Air)Side-B", Ns),
    Ns_135(0x135, "Ness (Ground)Up-B Start", Ns),
    Ns_136(0x136, "Ness (Ground)Up-B Hold", Ns),
    Ns_137(0x137, "Ness (Ground)Up-B End", Ns),
    Ns_138(0x138, "Ness Up-B", Ns),
    Ns_139(0x139, "Ness (Air)Up-B Start", Ns),
    Ns_13A(0x13A, "Ness (Air)Up-B Hold", Ns),
    Ns_13B(0x13B, "Ness (Air)Up-B End", Ns),
    Ns_13C(0x13C, "Ness Up-B(2, Ns)", Ns),
    Ns_13D(0x13D, "Ness DamageFall", Ns),
    Ns_13E(0x13E, "Ness (Ground)Down-B Start", Ns),
    Ns_13F(0x13F, "Ness (Ground)Down-B Hold", Ns),
    Ns_140(0x140, "Ness (Ground)Down-B Hit", Ns),
    Ns_141(0x141, "Ness (Ground)Down-B End", Ns),
    Ns_142(0x142, "Ness (Air)Down-B Start", Ns),
    Ns_143(0x143, "Ness (Air)Down-B Hold", Ns),
    Ns_144(0x144, "Ness (Air)Down-B Hit", Ns),
    Ns_145(0x145, "Ness (Air)Down-B End", Ns),

    // Peach
    Pe_138(0x138, "Peach (Ground)Neutral-B", Pe),
    Pe_139(0x139, "Peach (Ground)Neutral-B Counter", Pe),
    Pe_13A(0x13A, "Peach (Air)Neutral-B", Pe),
    Pe_13B(0x13B, "Peach (Air)Neutral-B Counter", Pe),
    Pe_12E(0x12E, "Peach (Ground)Side-B Start", Pe),
    Pe_12F(0x12F, "Peach (Ground)Side-B Miss", Pe),
    Pe_130(0x130, "Peach Side-B Hit", Pe),
    Pe_131(0x131, "Peach (Air)Side-B Start", Pe),
    Pe_132(0x132, "Peach (Air)Side-B Miss", Pe),
    Pe_133(0x133, "Peach Side-B hit wall(?, Pe)", Pe),
    Pe_134(0x134, "Peach (Ground)Up-B Start", Pe),
    Pe_135(0x135, "Peach (Ground)Up-B End", Pe),
    Pe_136(0x136, "Peach (Air)Up-B Start", Pe),
    Pe_137(0x137, "Peach (Air)Up-B End", Pe),
    Pe_13C(0x13C, "Peach Open Parasol", Pe),
    Pe_13D(0x13D, "Peach Close Parasol", Pe),
    Pe_12D(0x12D, "Peach Down-B", Pe),

    // Pikachu / Pichu
    Pk_127(0x127, "Pikachu (Ground)Neutral-B", Pk, Pc),
    Pk_128(0x128, "Pikachu (Air)Neutral-B", Pk, Pc),
    Pk_129(0x129, "Pikachu (Ground)Side-B Start", Pk, Pc),
    Pk_12A(0x12A, "Pikachu (Ground)Side-B Hold", Pk, Pc),
    Pk_12B(0x12B, "Pikachu (Ground)Side-B", Pk, Pc),
    Pk_12C(0x12C, "Pikachu (Ground)Side-B(2)", Pk, Pc),
    Pk_12D(0x12D, "Pikachu (Ground)Side-B End", Pk, Pc),
    Pk_12E(0x12E, "Pikachu (Air)Side-B Start", Pk, Pc),
    Pk_12F(0x12F, "Pikachu (Air)Side-B Hold", Pk, Pc),
    Pk_130(0x130, "Pikachu (Air)Side-B(2)", Pk, Pc),
    Pk_131(0x131, "Pikachu (Air)Side-B End", Pk, Pc),
    Pk_132(0x132, "Pikachu (Ground)Up-B Start", Pk, Pc),
    Pk_133(0x133, "Pikachu (Ground)Up-B Start(2)", Pk, Pc),
    Pk_134(0x134, "Pikachu (Ground)Up-B End", Pk, Pc),
    Pk_135(0x135, "Pikachu (Air)Up-B Start", Pk, Pc),
    Pk_136(0x136, "Pikachu (Air)Up-B Start(2)", Pk, Pc),
    Pk_137(0x137, "Pikachu (Air)Up-B End", Pk, Pc),
    Pk_138(0x138, "Pikachu (Ground)Down-B Start", Pk, Pc),
    Pk_139(0x139, "Pikachu (Ground)Down-B Loop", Pk, Pc),
    Pk_13A(0x13A, "Pikachu (Ground)Down-B Loop(2)", Pk, Pc),
    Pk_13B(0x13B, "Pikachu (Ground)Down-B End", Pk, Pc),
    Pk_13C(0x13C, "Pikachu (Air)Down-B Start", Pk, Pc),
    Pk_13D(0x13D, "Pikachu (Air)Down-B Loop", Pk, Pc),
    Pk_13E(0x13E, "Pikachu (Air)Down-B Loop(2)", Pk, Pc),
    Pk_13F(0x13F, "Pikachu (Air)Down-B End", Pk, Pc),

    // Samus
    Ss_127(0x127, "Samus (Ground)Down-B?", Ss),
    Ss_128(0x128, "Samus (Air)Down-B?", Ss),
    Ss_129(0x129, "Samus Neutral-BStart", Ss),
    Ss_12A(0x12A, "Samus Neutral-B Hold", Ss),
    Ss_12B(0x12B, "Samus Neutral-B Cancel", Ss),
    Ss_12C(0x12C, "Samus Neutral-B", Ss),
    Ss_12D(0x12D, "Samus (Air)Neutral-B Start", Ss),
    Ss_12E(0x12E, "Samus (Air)Neutral-B", Ss),
    Ss_12F(0x12F, "Samus (Ground)Homing Rocket(?)", Ss),
    Ss_130(0x130, "Samus (Ground)Hard Rocket(?)", Ss),
    Ss_131(0x131, "Samus (Air)Homing Rocket(?)", Ss),
    Ss_132(0x132, "Samus (Air)Hard Rocket(?)", Ss),
    Ss_133(0x133, "Samus (Ground)Screw Attack", Ss),
    Ss_134(0x134, "Samus (Air)Screw Attack", Ss),
    Ss_135(0x135, "Samus (Ground)Down-B?(2)", Ss),
    Ss_136(0x136, "Samus (Air)Down-B?(2)", Ss),

    // Sheik
    Sk_127(0x127, "Sheik (Ground)Neutral-B Start", Sk),
    Sk_128(0x128, "Sheik (Ground)Neutral-B", Sk),
    Sk_129(0x129, "Sheik (Ground)Neutral-B Cancel", Sk),
    Sk_12A(0x12A, "Sheik (Ground)Shoot Needles(?)", Sk),
    Sk_12B(0x12B, "Sheik (Air)Neutral-B Start", Sk),
    Sk_12C(0x12C, "Sheik (Air)Neutral-B", Sk),
    Sk_12D(0x12D, "Sheik (Air)Neutral-B Cancel", Sk),
    Sk_12E(0x12E, "Sheik (Air)Shoot Needles(?)", Sk),
    Sk_12F(0x12F, "Sheik (Ground)Side-B Start", Sk),
    Sk_130(0x130, "Sheik (Ground)Side-B End", Sk),
    Sk_131(0x131, "Sheik (Ground)Side-B Loop(?)", Sk),
    Sk_132(0x132, "Sheik (Air)Side-B Start", Sk),
    Sk_133(0x133, "Sheik (Air)Side-B End", Sk),
    Sk_134(0x134, "Sheik (Air)Side-B Loop(?)", Sk),
    Sk_135(0x135, "Sheik (Ground)Up-B Start", Sk),
    Sk_136(0x136, "Sheik (Ground)Up-B", Sk),
    Sk_137(0x137, "Sheik (Air)Up-B Start", Sk),
    Sk_138(0x138, "Sheik (Air)Up-B", Sk),
    Sk_139(0x139, "Sheik (Ground)Down-B Animation(?)", Sk),
    Sk_13A(0x13A, "Sheik (Ground)Down-B(?)", Sk),
    Sk_13B(0x13B, "Sheik (Air)Down-B Animation(?)", Sk),
    Sk_13C(0x13C, "Sheik (Air)Down-B(?)", Sk),

    // Yoshi
    Ys_127(0x127, "Yoshi (Ground)Neutral-B", Ys),
    Ys_128(0x128, "Yoshi (Ground)Neutral-B(2)", Ys),
    Ys_129(0x129, "Yoshi (Ground)Neutral-B(3)", Ys),
    Ys_12A(0x12A, "Yoshi (Air)Neutral-B", Ys),
    Ys_12B(0x12B, "Yoshi (Air)Neutral-B(2)", Ys),
    Ys_12C(0x12C, "Yoshi (Air)Neutral-B(3)", Ys),
    Ys_12D(0x12D, "Yoshi (Ground)Side-B Start", Ys),
    Ys_12E(0x12E, "Yoshi (Ground)Side-B Loop", Ys),
    Ys_12F(0x12F, "Yoshi (Ground)Side-B Loop(2)", Ys),
    Ys_130(0x130, "Yoshi (Ground)Side-B End", Ys),
    Ys_131(0x131, "Yoshi (Air)Side-B Start", Ys),
    Ys_132(0x132, "Yoshi (Air)Side-B Loop", Ys),
    Ys_133(0x133, "Yoshi (Air)Side-B Loop(2)", Ys),
    Ys_134(0x134, "Yoshi (Air)Side-B End", Ys),
    Ys_135(0x135, "Yoshi Up-B", Ys),
    Ys_136(0x136, "Yoshi (Ground)Down-B", Ys),
    Ys_137(0x137, "Yoshi (Air)Down-B", Ys),
    Ys_139(0x139, "Yoshi (Air)Down-B land", Ys),
    Ys_138(0x138, "Yoshi Down-B Landing(?)", Ys),

    // Zelda
    Zd_127(0x127, "Zelda (Ground)Neutral-B", Zd),
    Zd_128(0x128, "Zelda (Air)Neutral-B", Zd),
    Zd_129(0x129, "Zelda (Ground)Side-B Start", Zd),
    Zd_12A(0x12A, "Zelda (Ground)Side-B Loop", Zd),
    Zd_12B(0x12B, "Zelda (Ground)Side-B End", Zd),
    Zd_12C(0x12C, "Zelda (Air)Side-B Start", Zd),
    Zd_12D(0x12D, "Zelda (Air)Side-B Loop", Zd),
    Zd_12E(0x12E, "Zelda (Air)Side-B End", Zd),
    Zd_12F(0x12F, "Zelda (Ground)Up-B Start", Zd),
    Zd_130(0x130, "Zelda (Ground)Up-B", Zd),
    Zd_131(0x131, "Zelda (Air)Up-B Start", Zd),
    Zd_132(0x132, "Zelda (Air)Up-B", Zd),
    Zd_133(0x133, "Zelda (Ground)Down-B(?)", Zd),
    Zd_134(0x134, "Zelda (Ground)Down-B(?)(2)", Zd),
    Zd_135(0x135, "Zelda (Air)Down-B(?)", Zd),
    Zd_136(0x136, "Zelda (Air)Down-B(?)(2)", Zd);

    public final int offset;
    public final String description;
    public final Character[] characters; // null for all characters

    private SubAction(int offset, String description) {
        this.offset = offset;
        this.description = description;
        this.characters = null;
    }

    private SubAction(int offset, String description, Character... characters) {
        this.offset = offset;
        this.description = description;
        this.characters = characters;
    }

    public static final Set<SubAction> L_CANCELLABLE = ImmutableSet.of(
        LandingAirN,
        LandingAirF,
        LandingAirB,
        LandingAirHi,
        LandingAirLw);

    public static final String UNKNOWN_ANIMATION = "[Unknown]";

    public static List<SubAction> getApplicableActions(Character character) {
        List<SubAction> actions = new ArrayList<>();
        for (SubAction action : values()) {
            if (action.characters == null) {
                actions.add(action);
            } else {
                for (Character applicableCharacter : action.characters) {
                    if (applicableCharacter == character) {
                        actions.add(action);
                    }
                }
            }
        }
        return actions;
    }

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