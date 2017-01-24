package net.mcfr.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Arrays;

public class NBTTagIntArray extends NBTBase {
  /** The array of saved integers */
  private int[] intArray;
  
  NBTTagIntArray() {
    this(new int[0]);
  }
  
  public NBTTagIntArray(int[] array) {
    this.intArray = array;
  }
  
  /**
   * Write the actual data contents of the tag, implemented in NBT extension classes
   */
  @Override
  void write(DataOutput output) throws IOException {
    output.writeInt(this.intArray.length);
    
    for (int i = 0; i < this.intArray.length; ++i) {
      output.writeInt(this.intArray[i]);
    }
  }
  
  @Override
  void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
    sizeTracker.read(192);
    int i = input.readInt();
    sizeTracker.read(32 * i);
    this.intArray = new int[i];
    
    for (int j = 0; j < i; ++j) {
      this.intArray[j] = input.readInt();
    }
  }
  
  /**
   * Gets the type byte for the tag.
   */
  @Override
  public byte getId() {
    return INT_A;
  }
  
  @Override
  public String toString() {
    String s = "[";
    
    for (int i : this.intArray) {
      s = s + i + ",";
    }
    
    return s + "]";
  }
  
  /**
   * Creates a clone of the tag.
   */
  @Override
  public NBTBase copy() {
    int[] aint = new int[this.intArray.length];
    System.arraycopy(this.intArray, 0, aint, 0, this.intArray.length);
    return new NBTTagIntArray(aint);
  }
  
  @Override
  public boolean equals(Object o) {
    return super.equals(o) ? Arrays.equals(this.intArray, ((NBTTagIntArray) o).intArray) : false;
  }
  
  @Override
  public int hashCode() {
    return super.hashCode() ^ Arrays.hashCode(this.intArray);
  }
  
  public int[] getIntArray() {
    return this.intArray;
  }
}