package net.arhar.meleeauthorityscanner;

import java.util.Map;
import static net.arhar.meleeauthorityscanner.Character.*;

import com.google.common.collect.ImmutableMap;

public class SubAction {

    public static final Map<Integer, SubAction> SUBACTION_ID_TO_DESCRIPTION = ImmutableMap.<Integer, SubAction>builder()
        .put(0x000, new SubAction())
        // TODO ...
        .put(0x00F, new SubAction("Jump Squat/Charge")) // this information is in the character attribute "JumpFrames"

        .put(0x02E, new SubAction("Jab 1"))
        .put(0x02F, new SubAction("Jab 2"))
        .put(0x031, new SubAction("Rapid Jab Start"))
        .put(0x032, new SubAction("Rapid Jab Loop"))
        .put(0x033, new SubAction("Rapid Jab End"))
        .put(0x034, new SubAction("Dash Attack"))
        .put(0x035, new SubAction("Forward-Tilt (High)"))
        .put(0x037, new SubAction("Forward-Tilt"))
        .put(0x039, new SubAction("Forward-Tilt (Low)"))
        .put(0x03A, new SubAction("Up-Tilt"))
        .put(0x03B, new SubAction("Down-Tilt"))
        .put(0x03C, new SubAction("Forward-Smash (High)"))
        .put(0x03E, new SubAction("Forward-Smash"))
        .put(0x040, new SubAction("Forward-Smash (Low)"))
        .put(0x042, new SubAction("Up-Smash"))
        .put(0x043, new SubAction("Down-Smash"))
        .put(0x044, new SubAction("Neutral-Air"))
        .put(0x045, new SubAction("Forward-Air"))
        .put(0x046, new SubAction("Back-Air"))
        .put(0x047, new SubAction("Up-Air"))
        .put(0x048, new SubAction("Down-Air"))

        .put(0x0F7, new SubAction("Forward Throw"))
        .put(0x0F8, new SubAction("Back Throw"))
        .put(0x0F9, new SubAction("Up Throw"))
        .put(0x0FA, new SubAction("Down Throw"))

        .put(0x024, new SubAction("Special/Wavedash Landing Lag")) // this is broken?

        .put(0x025, new SubAction("Start Shield"))
        .put(0x027, new SubAction("Stop Shield"))
        .put(0x029, new SubAction("Spot Dodge"))
        .put(0x02A, new SubAction("Dodge Roll Forward"))
        .put(0x02B, new SubAction("Dodge Roll Backward"))
        .put(0x02C, new SubAction("Air Dodge"))

        .put(0x049, new SubAction("Nair Landing Lag"))
        .put(0x04A, new SubAction("Fair Landing Lag"))
        .put(0x04B, new SubAction("Bair Landing Lag"))
        .put(0x04C, new SubAction("Uair Landing Lag"))
        .put(0x04D, new SubAction("Dair Landing Lag"))



        // Special Moves



        .put(0x127, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Kp, new SubActionDescription("Bowser Neutral-B Start (Ground)"))
            .put(Mr, new SubActionDescription("Mario Neutral-B (Ground)"))
            .put(Dr, new SubActionDescription("Dr. Mario Neutral-B (Ground)"))
            .put(Fx, new SubActionDescription("Fox Neutral-B Start (Ground)"))
            .put(Fc, new SubActionDescription("Falco Neutral-B Start (Ground)"))
            .put(Gw, new SubActionDescription("G&W Neutral-B (Ground)"))
            .put(Pp, new SubActionDescription("Ice Climbers Neutral-B (Ground)"))
            .build()))
        .put(0x128, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Kp, new SubActionDescription("Bowser Neutral-B Loop (Ground)"))
            .put(Mr, new SubActionDescription("Mario Neutral-B (Air)"))
            .put(Dr, new SubActionDescription("Dr. Mario Neutral-B (Air)"))
            .put(Fx, new SubActionDescription("Fox Neutral-B Loop (Ground)"))
            .put(Fc, new SubActionDescription("Falco Neutral-B Loop (Ground)"))
            .put(Gw, new SubActionDescription("G&W Neutral-B (Air)"))
            .put(Pp, new SubActionDescription("Ice Climbers Neutral-B (Air)"))
            .build()))
        .put(0x129, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Kp, new SubActionDescription("Bowser Neutral-B End (Ground)"))
            .put(Mr, new SubActionDescription("Mario Side-B (Ground)"))
            .put(Dr, new SubActionDescription("Dr. Mario Side-B (Ground)"))
            .put(Fx, new SubActionDescription("Fox Neutral-B End (Ground)"))
            .put(Fc, new SubActionDescription("Falco Neutral-B End (Ground)"))
            .put(Gw, new SubActionDescription("G&W Side-B(1) (Ground)"))
            .put(Pp, new SubActionDescription("Ice Climbers Side-B(1) (Ground)"))
            .build()))
        .put(0x12A, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Kp, new SubActionDescription("Bowser Neutral-B Start (Air)"))
            .put(Mr, new SubActionDescription("Mario Side-B (Air)"))
            .put(Dr, new SubActionDescription("Dr. Mario Side-B (Air)"))
            .put(Fx, new SubActionDescription("Fox Neutral-B Start (Air)"))
            .put(Fc, new SubActionDescription("Falco Neutral-B Start (Air)"))
            .put(Gw, new SubActionDescription("G&W Side-B(2) (Ground)"))
            .put(Pp, new SubActionDescription("Ice Climbers Side-B(2) (Ground)"))
            .build()))
        .put(0x12B, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Kp, new SubActionDescription("Bowser Neutral-B Loop (Air)"))
            .put(Mr, new SubActionDescription("Mario Up-B (Ground)"))
            .put(Dr, new SubActionDescription("Dr. Mario Up-B (Ground)"))
            .put(Fx, new SubActionDescription("Fox Neutral-B Loop (Air)"))
            .put(Fc, new SubActionDescription("Falco Neutral-B Loop (Air)"))
            .put(Gw, new SubActionDescription("G&W Side-B(3) (Ground)"))
            .put(Pp, new SubActionDescription("Ice Climbers Side-B(1) (Air)"))
            .build()))
        .put(0x12C, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Kp, new SubActionDescription("Bowser Neutral-B End (Air)"))
            .put(Mr, new SubActionDescription("Mario Up-B (Air)"))
            .put(Dr, new SubActionDescription("Dr. Mario Up-B (Air)"))
            .put(Fx, new SubActionDescription("Fox Neutral-B End (Air)"))
            .put(Fc, new SubActionDescription("Falco Neutral-B End (Air)"))
            .put(Gw, new SubActionDescription("G&W Side-B(4) (Ground)"))
            .put(Pp, new SubActionDescription("Ice Climbers Side-B(2) (Air)"))
            .put(Pr, new SubActionDescription("Jigglypuff Neutral-B Start (R)"))
            .build()))
        .put(0x12D, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Kp, new SubActionDescription("Bowser Side-B Start (Ground)"))
            .put(Ca, new SubActionDescription("Falcon Neutral-B (Ground)"))
            .put(Gn, new SubActionDescription("Ganon Neutral-B (Ground)"))
            .put(Mr, new SubActionDescription("Mario Down-B (Ground)"))
            .put(Dr, new SubActionDescription("Dr. Mario Down-B (Ground)"))
            .put(Fx, new SubActionDescription("Fox Side-B Start (Ground)"))
            .put(Fc, new SubActionDescription("Falco Side-B Start (Ground)"))
            .put(Gw, new SubActionDescription("G&W Side-B(5) (Ground)"))
            .put(Pp, new SubActionDescription("Ice Climbers Up-B Start (Ground)"))
            .put(Pr, new SubActionDescription("Jigglypuff Neutral-B Start (L)"))
            .build()))
        .put(0x12E, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Kp, new SubActionDescription("Bowser Side-B Hit (Ground)")) //One is forward, other is behind?
            .put(Ca, new SubActionDescription("Falcon Neutral-B (Air)"))
            .put(Gn, new SubActionDescription("Ganon Neutral-B (Air)"))
            .put(Mr, new SubActionDescription("Mario Down-B (Air)"))
            .put(Dr, new SubActionDescription("Dr. Mario Down-B (Air)"))
            .put(Fx, new SubActionDescription("Fox Side-B (Ground)"))
            .put(Fc, new SubActionDescription("Falco Side-B (Ground)"))
            .put(Gw, new SubActionDescription("G&W Side-B(6) (Ground)"))
            .put(Pp, new SubActionDescription("Ice Climbers Up-B Throw(1) (Ground)"))
            .put(Pr, new SubActionDescription("Jigglypuff Neutral-B(1)"))
            .build()))
        .put(0x12F, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Kp, new SubActionDescription("Bowser Side-B Hit(2) (Ground)")) //One is forward, other is behind?
            .put(Ca, new SubActionDescription("Falcon Side-B Start (Ground)"))
            .put(Gn, new SubActionDescription("Ganon Side-B Start (Ground)"))
            .put(Fx, new SubActionDescription("Fox Side-B End (Ground)"))
            .put(Fc, new SubActionDescription("Falco Side-B End (Ground)"))
            .put(Gw, new SubActionDescription("G&W Side-B(7) (Ground)"))
            .put(Pp, new SubActionDescription("Ice Climbers Up-B Throw(2) (Ground)"))
            .put(Pr, new SubActionDescription("Jigglypuff Neutral-B(2)"))
            .build()))
        .put(0x130, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Kp, new SubActionDescription("Bowser Side-B End Forward (Ground)"))
            .put(Ca, new SubActionDescription("Falcon Side-B (Ground)"))
            .put(Gn, new SubActionDescription("Ganon Side-B (Ground)"))
            .put(Fx, new SubActionDescription("Fox Side-B Start (Air)"))
            .put(Fc, new SubActionDescription("Falco Side-B Start (Air)"))
            .put(Gw, new SubActionDescription("G&W Side-B(8) (Ground)"))
            .put(Pp, new SubActionDescription("Ice Climbers Up-B Throw(3) (Ground)"))
            .put(Pr, new SubActionDescription("Jigglypuff Neutral-B(3)"))
            .build()))
        .put(0x131, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Kp, new SubActionDescription("Bowser Side-B End Backward (Ground)"))
            .put(Ca, new SubActionDescription("Falcon Side-B Start (Air)"))
            .put(Gn, new SubActionDescription("Ganon Side-B Start (Air)"))
            .put(Fx, new SubActionDescription("Fox Side-B (Air)"))
            .put(Fc, new SubActionDescription("Falco Side-B (Air)"))
            .put(Gw, new SubActionDescription("G&W Side-B(9) (Ground)"))
            .put(Pp, new SubActionDescription("Ice Climbers Up-B Throw(4) (Ground)"))
            .put(Pr, new SubActionDescription("Jigglypuff Neutral-B(4)"))
            .build()))
        .put(0x132, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Kp, new SubActionDescription("Bowser Side-B Start (Air)"))
            .put(Ca, new SubActionDescription("Falcon Side-B (Air)"))
            .put(Fx, new SubActionDescription("Fox Side-B End (Air)"))
            .put(Fc, new SubActionDescription("Falco Side-B End (Air)"))
            .put(Gw, new SubActionDescription("G&W Side-B(1) (Air)"))
            .put(Pp, new SubActionDescription("Ice Climbers Up-B Start (Air)"))
            .put(Pr, new SubActionDescription("Jigglypuff Neutral-B End (R)"))
            .build()))
        .put(0x133, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Kp, new SubActionDescription("Bowser Side-B Hit (Air)")) //One is forward, other is behind?
            .put(Ca, new SubActionDescription("Falcon Up-B (Ground)"))
            .put(Gn, new SubActionDescription("Ganon Up-B (Ground)"))
            .put(Fx, new SubActionDescription("Fox Up-B Hold (Ground)"))
            .put(Fc, new SubActionDescription("Falco Up-B Hold (Ground)"))
            .put(Gw, new SubActionDescription("G&W Side-B(2) (Air)"))
            .put(Pp, new SubActionDescription("Ice Climbers Up-B Throw(1) (Air)"))
            .build()))
        .put(0x134, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Kp, new SubActionDescription("Bowser Side-B Hit(2) (Air)")) //One is forward, other is behind?
            .put(Ca, new SubActionDescription("Falcon Up-B (Air)"))
            .put(Gn, new SubActionDescription("Ganon Up-B (Air)"))
            .put(Fx, new SubActionDescription("Fox Up-B Hold (Air)"))
            .put(Fc, new SubActionDescription("Falco Up-B Hold (Air)"))
            .put(Gw, new SubActionDescription("G&W Side-B(3) (Air)"))
            .put(Pp, new SubActionDescription("Ice Climbers Up-B Throw(2) (Air)"))
            .build()))
        .put(0x135, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Kp, new SubActionDescription("Bowser Side-B End Forward (Air)"))
            .put(Ca, new SubActionDescription("Falcon Up-B Hold"))
            .put(Gn, new SubActionDescription("Ganon Up-B Hold"))
            .put(Fx, new SubActionDescription("Fox Up-B"))
            .put(Fc, new SubActionDescription("Falco Up-B"))
            .put(Gw, new SubActionDescription("G&W Side-B(4) (Air)"))
            .put(Pp, new SubActionDescription("Ice Climbers Up-B Throw(3) (Air)"))
            .build()))
        .put(0x136, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Kp, new SubActionDescription("Bowser Side-B End Backward (Air)"))
            .put(Ca, new SubActionDescription("Falcon Up-B Release"))
            .put(Gn, new SubActionDescription("Ganon Up-B Release"))
            .put(Fx, new SubActionDescription("Fox Up-B Landing"))
            .put(Fc, new SubActionDescription("Falco Up-B Landing"))
            .put(Gw, new SubActionDescription("G&W Side-B(5) (Air)"))
            .put(Pp, new SubActionDescription("Ice Climbers Up-B Throw(4) (Air)"))
            .build()))
        .put(0x137, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Kp, new SubActionDescription("Bowser Up-B (Ground)"))
            .put(Ca, new SubActionDescription("Falcon Down-B (Ground)"))
            .put(Gn, new SubActionDescription("Ganon Down-B (Ground)"))
            .put(Fx, new SubActionDescription("Fox Up-B Fall"))
            .put(Fc, new SubActionDescription("Falco Up-B Fall"))
            .put(Gw, new SubActionDescription("G&W Side-B(6) (Air)"))
            .put(Pp, new SubActionDescription("Ice Climbers Down-B (Ground)"))
            .build()))
        .put(0x138, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Kp, new SubActionDescription("Bowser Up-B (Air)"))
            .put(Ca, new SubActionDescription("Falcon Down-B End (Ground)"))
            .put(Gn, new SubActionDescription("Ganon Down-B End (Ground)"))
            .put(Fx, new SubActionDescription("Fox Up-B Bound"))
            .put(Fc, new SubActionDescription("Falco Up-B Bound"))
            .put(Gw, new SubActionDescription("G&W Side-B(7) (Air)"))
            .put(Pp, new SubActionDescription("Ice Climbers Down-B (Air)"))
            .build()))
        .put(0x139, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Kp, new SubActionDescription("Bowser Down-B (Ground)"))
            .put(Ca, new SubActionDescription("Falcon Down-B (Air)"))
            .put(Gn, new SubActionDescription("Ganon Down-B (Air)"))
            .put(Fx, new SubActionDescription("Fox Down-B Start (Ground)"))
            .put(Fc, new SubActionDescription("Falco Down-B Start (Ground)"))
            .put(Gw, new SubActionDescription("G&W Side-B(8) (Air)"))
            .build()))
        .put(0x13A, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Kp, new SubActionDescription("Bowser Down-B (Air)"))
            .put(Ca, new SubActionDescription("Falcon Down-B End (Air)"))
            .put(Gn, new SubActionDescription("Ganon Down-B End (Air)"))
            .put(Fx, new SubActionDescription("Fox Down-B Loop (Ground)"))
            .put(Fc, new SubActionDescription("Falco Down-B Loop (Ground)"))
            .put(Gw, new SubActionDescription("G&W Side-B(9) (Air)"))
            .build()))
        .put(0x13B, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Kp, new SubActionDescription("Bowser Down-B Landing"))
            .put(Ca, new SubActionDescription("Falcon Down-B End In-Air (Ground)"))
            .put(Gn, new SubActionDescription("Ganon Down-B End In-Air (Ground)"))
            .put(Fx, new SubActionDescription("Fox Down-B Hit (Ground)"))
            .put(Fc, new SubActionDescription("Falco Down-B Hit (Ground)"))
            .put(Gw, new SubActionDescription("G&W Up-B (Ground)"))
            .build()))
        .put(0x13C, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Ca, new SubActionDescription("Falcon Down-B End In-Air (Air)"))
            .put(Gn, new SubActionDescription("Ganon Down-B End In-Air (Air)"))
            .put(Fx, new SubActionDescription("Fox Down-B End (Ground)"))
            .put(Fc, new SubActionDescription("Falco Down-B End (Ground)"))
            .put(Gw, new SubActionDescription("G&W Up-B (Air)"))
            .build()))
        .put(0x13D, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Fx, new SubActionDescription("Fox Down-B Start (Air)"))
            .put(Fc, new SubActionDescription("Falco Down-B Start (Air)"))
            .put(Gw, new SubActionDescription("G&W Down-B (Ground)"))
            .build()))
        .put(0x13E, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Fx, new SubActionDescription("Fox Down-B Loop (Air)"))
            .put(Fc, new SubActionDescription("Falco Down-B Loop (Air)"))
            .put(Gw, new SubActionDescription("G&W Down-B Absorb (Ground)"))
            .build()))
        .put(0x13F, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Dk, new SubActionDescription("Donkey Kong Neutral-B Start (Ground)"))
            .put(Fx, new SubActionDescription("Fox Down-B Hit (Air)"))
            .put(Fc, new SubActionDescription("Falco Down-B Hit (Air)"))
            .put(Gw, new SubActionDescription("G&W Down-B Shoot (Ground)"))
            .build()))
        .put(0x140, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Dk, new SubActionDescription("Donkey Kong Neutral-B Loop (Ground)"))
            .put(Fx, new SubActionDescription("Fox Down-B End (Air)"))
            .put(Fc, new SubActionDescription("Falco Down-B End (Air)"))
            .put(Gw, new SubActionDescription("G&W Down-B (Air)"))
            .build()))
        .put(0x141, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Dk, new SubActionDescription("Donkey Kong Neutral-B Cancel (Ground)"))
            .put(Gw, new SubActionDescription("G&W Down-B Absorb (Air)"))
            .build()))
        .put(0x142, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Dk, new SubActionDescription("Donkey Kong Neutral-B (Ground)"))
            .put(Gw, new SubActionDescription("G&W Down-B Shoot (Air)"))
            .build()))
        .put(0x143, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Dk, new SubActionDescription("Donkey Kong Neutral-B(2) (Ground)"))
            .build()))
        .put(0x144, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Dk, new SubActionDescription("Donkey Kong Neutral-B Start (Air)"))
            .build()))
        .put(0x145, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Dk, new SubActionDescription("Donkey Kong Neutral-B Loop (Air)"))
            .build()))
        .put(0x146, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Dk, new SubActionDescription("Donkey Kong Neutral-B Cancel (Air)"))
            .build()))
        .put(0x147, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Dk, new SubActionDescription("Donkey Kong Neutral-B (Air)"))
            .build()))
        .put(0x148, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Dk, new SubActionDescription("Donkey Kong Neutral-B(2) (Air)"))
            .build()))
        .put(0x149, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Dk, new SubActionDescription("Donkey Kong Side-B (Ground)"))
            .build()))
        .put(0x14A, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Dk, new SubActionDescription("Donkey Kong Side-B (Air)"))
            .build()))
        .put(0x14B, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Dk, new SubActionDescription("Donkey Kong Up-B (Ground)"))
            .build()))
        .put(0x14C, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Dk, new SubActionDescription("Donkey Kong Up-B (Air)"))
            .build()))
        .put(0x14D, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Dk, new SubActionDescription("Donkey Kong Down-B Start"))
            .build()))
        .put(0x14E, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Dk, new SubActionDescription("Donkey Kong Down-B Loop"))
            .build()))
        .put(0x14F, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Dk, new SubActionDescription("Donkey Kong Down-B End"))
            .build()))
        .put(0x150, new SubAction(ImmutableMap.<Character, SubActionDescription>builder()
            .put(Dk, new SubActionDescription("Donkey Kong Down-B End(2)"))
            .build()))
        .build();


    Pr_133(Pr, new SubActionDescription("Jigglypuff Neutral-B End (L)"))
    Pr_13C(Pr, new SubActionDescription("Jigglypuff Neutral-B"))
    Pr_13D(Pr, new SubActionDescription("Jigglypuff Side-B (Ground)"))
    Pr_13F(Pr, new SubActionDescription("Jigglypuff Up-B (L) (Ground)"))
    Pr_141(Pr, new SubActionDescription("Jigglypuff Up-B (R) (Ground)"))
    Pr_143(Pr, new SubActionDescription("Jigglypuff Down-B (L) (Ground)"))
    Pr_145(Pr, new SubActionDescription("Jigglypuff Down-B (R) (Ground)"))
    Pr_142(Pr, new SubActionDescription("Jigglypuff Up-B (R) (Air)"))
    Pr_144(Pr, new SubActionDescription("Jigglypuff Down-B (L) (Air)"))
    Pr_146(Pr, new SubActionDescription("Jigglypuff Down-B (R) (Air)"))
    Pr_13E(Pr, new SubActionDescription("Jigglypuff Side-B (Air)"))
    Pr_140(Pr, new SubActionDescription("Jigglypuff Up-B (L) (Air)"))

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


        .build();

    // TODO represent l cancelling somehow, this is also related to variable "frame" per frame speeds
//    public static final Set<SubAction> L_CANCELLABLE = ImmutableSet.of(
//        LandingAirN,
//        LandingAirF,
//        LandingAirB,
//        LandingAirHi,
//        LandingAirLw);
//    public static final String UNKNOWN_ANIMATION = "[Unknown]";

    private final SubActionDescription description;
    private final Map<Character, SubActionDescription> characterToDescription;

    public SubAction() {
        // no description
        this.description = null;
        this.characterToDescription = null;
    }

    public SubAction(String description, SubActionCategory... categories) {
        this.description = new SubActionDescription(description, categories);
        this.characterToDescription = null;
    }

    public SubAction(Map<Character, SubActionDescription> characterToDescription) {
        this.description = null;
        this.characterToDescription = characterToDescription;
    }

    public SubActionDescription getDescription(Character character) {
        if (description != null) {
            return description;
        }
        if (characterToDescription != null && characterToDescription.containsKey(character)) {
            return characterToDescription.get(character);
        }
        return NO_DESCRIPTION;
    }

    public static final SubActionDescription NO_DESCRIPTION = new SubActionDescription("(No Description)");

    public static class SubActionDescription {
        private final String description;
        private final SubActionCategory[] categories;

        public SubActionDescription(String description, SubActionCategory... categories) {
            this.description = description;
            this.categories = categories;
        }
    }

    public static enum SubActionCategory {
        ATTACK,
        THROW,
        ITEM,
        SPECIAL,
        MOVEMENT,
        OTHER
    }
}