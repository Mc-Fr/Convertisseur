package net.mcfr.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

import net.mcfr.minecraft.MathHelper;

public class NBTTagFloat extends NBTBase.NBTPrimitive {
  /** The float value for the tag. */
  private float data;
  
  NBTTagFloat() {
    this(0);
  }
  
  public NBTTagFloat(float data) {
    this.data = data;
  }
  
  /**
   * Write the actual data contents of the tag, implemented in NBT extension classes
   */
  @Override
  void write(DataOutput output) throws IOException {
    output.writeFloat(this.data);
  }
  
  @Override
  void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
    sizeTracker.read(96);
    this.data = input.readFloat();
  }
  
  /**
   * Gets the type byte for the tag.
   */
  @Override
  public byte getId() {
    return FLOAT;
  }
  
  @Override
  public String toString() {
    return this.data + "f";
  }
  
  /**
   * Creates a clone of the tag.
   */
  @Override
  public NBTBase copy() {
    return new NBTTagFloat(this.data);
  }
  
  @Override
  public boolean equals(Object o) {
    if (super.equals(o)) {
      return this.data == ((NBTTagFloat) o).data;
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return super.hashCode() ^ Float.floatToIntBits(this.data);
  }
  
  @Override
  public long getLong() {
    return (long) this.data;
  }
  
  @Override
  public int getInt() {
    return MathHelper.floor_float(this.data);
  }
  
  @Override
  public short getShort() {
    return (short) (MathHelper.floor_float(this.data) & 65535);
  }
  
  @Override
  public byte getByte() {
    return (byte) (MathHelper.floor_float(this.data) & 255);
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