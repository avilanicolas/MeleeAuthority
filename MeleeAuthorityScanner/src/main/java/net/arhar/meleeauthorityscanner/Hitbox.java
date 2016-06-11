package net.arhar.meleeauthorityscanner;

public class Hitbox {

    public final int id;
    public final int bone;
    public final int damage;
//    public final int radius;
    public final float radius;
//    public final int zoffset;
    public final float zoffset;
//    public final int yoffset;
    public final float yoffset;
//    public final int xoffset;
    public final float xoffset;
    public final int angle;
    public final int knockbackScaling;
    public final int fixedKnockback;
    public final int baseKnockback;
    public final int shieldDamage;
//    public final boolean clang;
//    public final boolean hitsGrounded;
//    public final boolean hitsAerial;

//    public final Sound sound;

    public Hitbox(byte[] data) {
        // hitbox id 6-8
        id = getBits(data, 6, 8, false);

        // UNKNOWN 9-13

        // bone attachment 14-20
        bone = getBits(data, 14, 20, false);

        // UNKNOWN 21-22

        // damage 23-31
        damage = getBits(data, 23, 31, false);

        // size 32-47
        radius = getFloatBits(data, 32, 47);
//        radius = getBits(data, 32, 47, false);

        // offsets are signed
        // z offset 48-63
//        zoffset = getBits(data, 48, 63, true);
        zoffset = getFloatBits(data, 48, 63);
        // y offset 64-79
//        yoffset = getBits(data, 64, 79, true);
        yoffset = getFloatBits(data, 64, 79);
        // x offset 80-95
//        xoffset = getBits(data, 80, 95, true);
        xoffset = getFloatBits(data, 80, 95);

        // angle 96-104
        angle = getBits(data, 96, 104, false);

        // knockback scaling 105-113
        knockbackScaling = getBits(data, 105, 113, false);

        // weight based knockback WSDK 114-122
        fixedKnockback = getBits(data, 114, 122, false);

        // UNKNOWN 123-125

        // hitbox interaction 126-127

        // base knockback 128-136
        baseKnockback = getBits(data, 128, 136, false);

        // UNKNOWN 137-142

        // shield damage 143-149
        shieldDamage = getBits(data, 143, 149, false);

        // sound 150-157

        // hurtbox interaction 158-159
    }

    private static int getBits(byte[] data, int startIndex, int endIndex, boolean signed) {
        int byteOffset = startIndex / 8;
        int bitOffset = startIndex % 8;
        int length = endIndex - startIndex + 1;

        // put wanted bytes into a string
        if (bitOffset + length > 7) {
            // we have to access two bytes
            // assume that there is no information longer than 8 bits
            int bits = ((data[byteOffset] & 0xFF) << 8) | (data[byteOffset + 1] & 0xFF);
            bits = bits << ((32 - 8 - 8) + bitOffset);
            bits = signed ? bits >> (32 - length) : bits >>> (32 - length);
            return bits;
        } else {
            // only access one byte
            int bits = data[byteOffset] & 0xFF;
            bits = bits << ((32 - 8) + bitOffset);
            bits = signed ? bits >> (32 - length) : bits >>> (32 - length);
            return bits;
        }
    }

    private static float getFloatBits(byte[] data, int startIndex, int endIndex) {
        int byteOffset = startIndex / 8;
        int bitOffset = startIndex % 8;
        int length = endIndex - startIndex + 1;
        int bits;

        // all floats are 2 bytes, they could be minifloats
        // assume they are aligned to the bytes
        bits = ((data[byteOffset] & 0xFF) << 8) | (data[byteOffset + 1] & 0xFF);
        bits = bits << 16;
        bits = bits >> 16; // sign extend?

        int mantissa;
        if ((bits & 0x0800) != 0) {
            mantissa = (bits & 0x0FFF) | 0xF000;
        } else {
            mantissa = bits & 0x0FFF;
        }
        int exponent;
        if (bits < 0) {
            exponent = ((bits >> 12) & 0x0F) | 0xF0;
        } else {
            exponent = (bits >> 12) & 0x0F;
        }

        return mantissa * (float) Math.pow(10, exponent);
    }

    public static enum Sound {
        Slash
    }

    public static enum Effect {
        Slash
    }
}