package net.mcfr.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NBTTagByteArray extends NBTBase {
  /** The byte array stored in the tag. */
  private byte[] data;
  
  NBTTagByteArray() {
    this(new byte[0]);
  }
  
  public NBTTagByteArray(byte[] data) {
    this.data = data;
  }
  
  /**
   * Write the actual data contents of the tag, implemented in NBT extension classes
   */
  @Override
  void write(DataOutput output) throws IOException {
    output.writeInt(this.data.length);
    output.write(this.data);
  }
  
  @Override
  void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
    sizeTracker.read(192);
    int i = input.readInt();
    sizeTracker.read(8 * i);
    this.data = new byte[i];
    input.readFully(this.data);
  }
  
  /**
   * Gets the type byte for the tag.
   */
  @Override
  public byte getId() {
    return BYTE_A;
  }
  
  @Override
  public String toString() {
    return "[" + this.data.length + " bytes]";
  }
  
  /**
   * Creates a clone of the tag.
   */
  @Override
  public NBTBase copy() {
    byte[] abyte = new byte[this.data.length];
    System.arraycopy(this.data, 0, abyte, 0, this.data.length);
    return new NBTTagByteArray(abyte);
  }
  
  @Override
  public boolean equals(Object o) {
    return super.equals(o) ? Arrays.equals(this.data, ((NBTTagByteArray) o).data) : false;
  }
  
  @Override
  public int hashCode() {
    return super.hashCode() ^ Arrays.hashCode(this.data);
  }
  
  public byte[] getByteArray() {
    return this.data;
  }
}