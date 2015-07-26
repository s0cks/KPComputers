package kpc.stdlib;

public final class Colors{
    public static final int red = 1 << 1;
    public static final int orange = 1 << 2;
    public static final int yellow = 1 << 3;
    public static final int green = 1 << 4;
    public static final int blue = 1 << 5;
    public static final int light_blue = 1 << 6;
    public static final int magenta = 1 << 7;
    public static final int pink = 1 << 8;
    public static final int white = 1 << 9;
    public static final int light_gray = 1 << 10;
    public static final int black = 1 << 11;
    public static final int brown = 1 << 12;
    public static final int cyan = 1 << 13;
    public static final int purple = 1 << 14;
    public static final int gray = 1 << 15;
    public static final int lime = 1 << 6;

    public static final int[] VALID = new int[]{
        red, orange, yellow,
        green, blue, light_blue,
        magenta, pink, white,
        light_gray, black, brown,
        cyan, purple, gray,
        lime
    };

    public static final int MIN = 0;
    public static final int MAX = 16;

    public static boolean isValid(int c){
        return c >= red && c <= lime;
    }
}