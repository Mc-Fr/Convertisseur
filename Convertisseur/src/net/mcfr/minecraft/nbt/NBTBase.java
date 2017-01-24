package net.mcfr.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public abstract class NBTBase {
  public static final byte END = 0;
  public static final byte BYTE = 1;
  public static final byte SHORT = 2;
  public static final byte INT = 3;
  public static final byte LONG = 4;
  public static final byte FLOAT = 5;
  public static final byte DOUBLE = 6;
  public static final byte BYTE_A = 7;
  public static final byte STRING = 8;
  public static final byte LIST = 9;
  public static final byte COMPOUND = 10;
  public static final byte INT_A = 11;
  
  public static final String[] NBT_TYPES = new String[]{"END", "BYTE", "SHORT", "INT", "LONG", "FLOAT", "DOUBLE", "BYTE[]", "STRING", "LIST", "COMPOUND", "INT[]"};
  
  /**
   * Write the actual data contents of the tag, implemented in NBT extension classes
   */
  abstract void write(DataOutput output) throws IOException;
  
  abstract void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException;
  
  @Override
  public abstract String toString();
  
  /**
   * Gets the type byte for the tag.
   */
  public abstract byte getId();
  
  /**
   * Creates a new NBTBase object that corresponds with the passed in id.
   */
  protected static NBTBase createNewByType(byte id) {
    switch (id) {
      case END:
        return new NBTTagEnd();
      case BYTE:
        return new NBTTagByte();
      case SHORT:
        return new NBTTagShort();
      case INT:
        return new NBTTagInt();
      case LONG:
        return new NBTTagLong();
      case FLOAT:
        return new NBTTagFloat();
      case DOUBLE:
        return new NBTTagDouble();
      case BYTE_A:
        return new NBTTagByteArray();
      case STRING:
        return new NBTTagString();
      case LIST:
        return new NBTTagList();
      case COMPOUND:
        return new NBTTagCompound();
      case INT_A:
        return new NBTTagIntArray();
      default:
        return null;
    }
  }
  
  /**
   * Creates a clone of the tag.
   */
  public abstract NBTBase copy();
  
  /**
   * Return whether this compound has no tags.
   */
  public boolean hasNoTags() {
    return false;
  }
  
  @Override
  public boolean equals(Object o) {
    if (o instanceof NBTBase) {
      return getId() == ((NBTBase) o).getId();
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return getId();
  }
  
  protected String getString() {
    return toString();
  }
  
  public abstract static class NBTPrimitive extends NBTBase {
    public abstract long getLong();
    
    public abstract int getInt();
    
    public abstract short getShort();
    
    public abstract byte getByte();
    
    public abstract double getDouble();
    
    public abstract float getFloat();
  }
}