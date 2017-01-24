package net.mcfr.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class NBTTagString extends NBTBase {
  /** The string value for the tag (cannot be empty). */
  private String data;
  
  public NBTTagString() {
    this("");
  }
  
  public NBTTagString(String data) {
    this.data = data;
    
    if (data == null) {
      throw new IllegalArgumentException("Empty string not allowed");
    }
  }
  
  /**
   * Write the actual data contents of the tag, implemented in NBT extension classes
   */
  @Override
  void write(DataOutput output) throws IOException {
    output.writeUTF(this.data);
  }
  
  @Override
  void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
    sizeTracker.read(288);
    this.data = input.readUTF();
    NBTSizeTracker.readUTF(sizeTracker, this.data); // Forge: Correctly read String length including
    // header.
  }
  
  /**
   * Gets the type byte for the tag.
   */
  @Override
  public byte getId() {
    return STRING;
  }
  
  @Override
  public String toString() {
    return "\"" + this.data.replace("\"", "\\\"") + "\"";
  }
  
  /**
   * Creates a clone of the tag.
   */
  @Override
  public NBTBase copy() {
    return new NBTTagString(this.data);
  }
  
  /**
   * Return whether this compound has no tags.
   */
  @Override
  public boolean hasNoTags() {
    return this.data.isEmpty();
  }
  
  @Override
  public boolean equals(Object o) {
    if (!super.equals(o)) {
      return false;
    }
    else {
      NBTTagString nbttagstring = (NBTTagString) o;
      return this.data == null && nbttagstring.data == null || this.data != null && this.data.equals(nbttagstring.data);
    }
  }
  
  @Override
  public int hashCode() {
    return super.hashCode() ^ this.data.hashCode();
  }
  
  @Override
  public String getString() {
    return this.data;
  }
}