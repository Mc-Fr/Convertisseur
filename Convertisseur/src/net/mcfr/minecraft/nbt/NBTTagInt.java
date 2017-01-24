package net.mcfr.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagInt extends NBTBase.NBTPrimitive {
  /** The integer value for the tag. */
  private int data;
  
  NBTTagInt() {
    this(0);
  }
  
  public NBTTagInt(int data) {
    this.data = data;
  }
  
  /**
   * Write the actual data contents of the tag, implemented in NBT extension classes
   */
  @Override
  void write(DataOutput output) throws IOException {
    output.writeInt(this.data);
  }
  
  @Override
  void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
    sizeTracker.read(96);
    this.data = input.readInt();
  }
  
  /**
   * Gets the type byte for the tag.
   */
  @Override
  public byte getId() {
    return INT;
  }
  
  @Override
  public String toString() {
    return "" + this.data;
  }
  
  /**
   * Creates a clone of the tag.
   */
  @Override
  public NBTBase copy() {
    return new NBTTagInt(this.data);
  }
  
  @Override
  public boolean equals(Object o) {
    if (super.equals(o)) {
      return this.data == ((NBTTagInt) o).data;
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return super.hashCode() ^ this.data;
  }
  
  @Override
  public long getLong() {
    return this.data;
  }
  
  @Override
  public int getInt() {
    return this.data;
  }
  
  @Override
  public short getShort() {
    return (short) (this.data & 65535);
  }
  
  @Override
  public byte getByte() {
    return (byte) (this.data & 255);
  }
  
  @Override
  public double getDouble() {
    return this.data;
  }
  
  @Override
  public float getFloat() {
    return this.data;
  }
}