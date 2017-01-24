package net.mcfr.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagShort extends NBTBase.NBTPrimitive {
  /** The short value for the tag. */
  private short data;
  
  public NBTTagShort() {
    this((short) 0);
  }
  
  public NBTTagShort(short data) {
    this.data = data;
  }
  
  /**
   * Write the actual data contents of the tag, implemented in NBT extension classes
   */
  @Override
  void write(DataOutput output) throws IOException {
    output.writeShort(this.data);
  }
  
  @Override
  void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
    sizeTracker.read(80);
    this.data = input.readShort();
  }
  
  /**
   * Gets the type byte for the tag.
   */
  @Override
  public byte getId() {
    return SHORT;
  }
  
  @Override
  public String toString() {
    return this.data + "s";
  }
  
  /**
   * Creates a clone of the tag.
   */
  @Override
  public NBTBase copy() {
    return new NBTTagShort(this.data);
  }
  
  @Override
  public boolean equals(Object o) {
    if (super.equals(o)) {
      return this.data == ((NBTTagShort) o).data;
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
    return this.data;
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