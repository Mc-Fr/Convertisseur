package net.mcfr.minecraft;

import java.util.Random;

public class MathHelper {
  /**
   * A table of sin values computed from 0 (inclusive) to 2 * pi (exclusive), with steps of 2 * PI /
   * 65536.
   */
  private static float[] SIN_TABLE = new float[65536];
  
  static {
    for (int i = 0; i < 65536; ++i) {
      SIN_TABLE[i] = (float) Math.sin(i * Math.PI * 2 / 65536.0);
    }
  }
  
  /**
   * sin looked up in a table
   */
  public static final float sin(float x) {
    return SIN_TABLE[(int) (x * 10430.378F) & 65535];
  }
  
  /**
   * cos looked up in the sin table with the appropriate offset
   */
  public static final float cos(float x) {
    return SIN_TABLE[(int) (x * 10430.378F + 16384.0F) & 65535];
  }
  
  public static final float sqrt_float(float x) {
    return (float) Math.sqrt((double) x);
  }
  
  public static final float sqrt_double(double x) {
    return (float) Math.sqrt(x);
  }
  
  /**
   * Returns the greatest integer less than or equal to the float argument
   */
  public static int floor_float(float x) {
    int floor = (int) x;
    return x < (float) floor ? floor - 1 : floor;
  }
  
  /**
   * returns par0 cast as an int, and no greater than Integer.MAX_VALUE-1024
   */
  public static int truncateDoubleToInt(double x) {
    return (int) (x + 1024.0) - 1024;
  }
  
  /**
   * Returns the greatest integer less than or equal to the double argument
   */
  public static int floor_double(double x) {
    int floor = (int) x;
    return x < (double) floor ? floor - 1 : floor;
  }
  
  /**
   * Long version of floor_double
   */
  public static long floor_double_long(double x) {
    long floor = (long) x;
    return x < (double) floor ? floor - 1 : floor;
  }
  
  public static float abs(float x) {
    return x >= 0f ? x : -x;
  }
  
  public static int abs_int(int x) {
    return x >= 0 ? x : -x;
  }
  
  public static int ceiling_float_int(float x) {
    int ceil = (int) x;
    return x > (float) ceil ? ceil + 1 : ceil;
  }
  
  public static int ceiling_double_int(double x) {
    int ceil = (int) x;
    return x > (double) ceil ? ceil + 1 : ceil;
  }
  
  /**
   * Returns the value of the first parameter, clamped to be within the lower and upper limits given
   * by the second and third parameters.
   */
  public static int clamp_int(int x, int lower, int upper) {
    return x < lower ? lower : (x > upper ? upper : x);
  }
  
  /**
   * Returns the value of the first parameter, clamped to be within the lower and upper limits given
   * by the second and third parameters
   */
  public static float clamp_float(float x, float lower, float upper) {
    return x < lower ? lower : (x > upper ? upper : x);
  }
  
  /**
   * Maximum of the absolute value of two numbers.
   */
  public static double abs_max(double x, double y) {
    if (x < 0) {
      x = -x;
    }
    if (y < 0) {
      y = -y;
    }
    
    return x > y ? x : y;
  }
  
  /**
   * Buckets an integer with specifed bucket sizes. Args: i, bucketSize
   */
  public static int bucketInt(int i, int size) {
    return i < 0 ? -((-i - 1) / size) - 1 : i / size;
  }
  
  /**
   * Tests if a string is null or of length zero
   */
  public static boolean stringNullOrLengthZero(String str) {
    return str == null || str.length() == 0;
  }
  
  public static int getRandomIntegerInRange(Random rand, int lower, int upper) {
    return lower >= upper ? lower : rand.nextInt(upper - lower + 1) + lower;
  }
  
  public static double getRandomDoubleInRange(Random rand, double lower, double upper) {
    return lower >= upper ? lower : rand.nextDouble() * (upper - lower) + lower;
  }
  
  public static double average(long[] array) {
    long sum = 0;
    
    for (int i = 0; i < array.length; ++i) {
      sum += array[i];
    }
    
    return (double) sum / (double) array.length;
  }
  
  /**
   * the angle is reduced to an angle between -180 and +180 by mod, and a 360 check
   */
  public static float wrapAngleTo180_float(float angle) {
    angle %= 360f;
    
    if (angle >= 180f) {
      angle -= 360f;
    }
    
    if (angle < -180f) {
      angle += 360f;
    }
    
    return angle;
  }
  
  /**
   * the angle is reduced to an angle between -180 and +180 by mod, and a 360 check
   */
  public static double wrapAngleTo180_double(double angle) {
    angle %= 360.0;
    
    if (angle >= 180.0) {
      angle -= 360.0;
    }
    
    if (angle < -180.0) {
      angle += 360.0;
    }
    
    return angle;
  }
  
  /**
   * parses the string as integer or returns the second parameter if it fails
   */
  public static int parseIntWithDefault(String str, int defaultValue) {
    int res = defaultValue;
    
    try {
      res = Integer.parseInt(str);
    }
    catch (Throwable ex) {}
    
    return res;
  }
  
  /**
   * parses the string as integer or returns the second parameter if it fails. this value is capped
   * to par2
   */
  public static int parseIntWithDefaultAndMax(String str, int defaultValue, int cap) {
    int res = defaultValue;
    
    try {
      res = Integer.parseInt(str);
    }
    catch (Throwable ex) {}
    
    if (res < cap) {
      res = cap;
    }
    
    return res;
  }
  
  /**
   * parses the string as double or returns the second parameter if it fails.
   */
  public static double parseDoubleWithDefault(String str, double defaultValue) {
    double res = defaultValue;
    
    try {
      res = Double.parseDouble(str);
    }
    catch (Throwable ex) {}
    
    return res;
  }
  
  public static double parseDoubleWithDefaultAndMax(String str, double defaultValue, double cap) {
    double res = defaultValue;
    
    try {
      res = Double.parseDouble(str);
    }
    catch (Throwable ex) {}
    
    if (res < cap) {
      res = cap;
    }
    
    return res;
  }
}
