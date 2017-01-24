package net.mcfr.minecraft.nbt;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class NBTTagCompound extends NBTBase {
  private Map<String, NBTBase> tagMap;
  
  public NBTTagCompound() {
    this.tagMap = new HashMap<>();
  }
  
  /**
   * Write the actual data contents of the tag, implemented in NBT extension classes
   */
  @Override
  void write(DataOutput output) throws IOException {
    for (String s : this.tagMap.keySet()) {
      writeEntry(s, this.tagMap.get(s), output);
    }
    
    output.writeByte(0);
  }
  
  @Override
  void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
    sizeTracker.read(384);
    
    if (depth > 512) {
      throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
    }
    else {
      this.tagMap.clear();
      byte b;
      
      while ((b = readType(input, sizeTracker)) != 0) {
        String s = readKey(input, sizeTracker);
        sizeTracker.read(224 + 16 * s.length());
        NBTBase nbtbase = readNBT(b, s, input, depth + 1, sizeTracker);
        
        if (this.tagMap.put(s, nbtbase) != null) {
          sizeTracker.read(288);
        }
      }
    }
  }
  
  public Set<String> getKeySet() {
    return this.tagMap.keySet();
  }
  
  /**
   * Gets the type byte for the tag.
   */
  @Override
  public byte getId() {
    return COMPOUND;
  }
  
  /**
   * Stores the given tag into the map with the given string key. This is mostly used to store tag
   * lists.
   */
  public void setTag(String key, NBTBase value) {
    this.tagMap.put(key, value);
  }
  
  /**
   * Stores a new NBTTagByte with the given byte value into the map with the given string key.
   */
  public void setByte(String key, byte value) {
    this.tagMap.put(key, new NBTTagByte(value));
  }
  
  /**
   * Stores a new NBTTagShort with the given short value into the map with the given string key.
   */
  public void setShort(String key, short value) {
    this.tagMap.put(key, new NBTTagShort(value));
  }
  
  /**
   * Stores a new NBTTagInt with the given integer value into the map with the given string key.
   */
  public void setInteger(String key, int value) {
    this.tagMap.put(key, new NBTTagInt(value));
  }
  
  /**
   * Stores a new NBTTagLong with the given long value into the map with the given string key.
   */
  public void setLong(String key, long value) {
    this.tagMap.put(key, new NBTTagLong(value));
  }
  
  /**
   * Stores a new NBTTagFloat with the given float value into the map with the given string key.
   */
  public void setFloat(String key, float value) {
    this.tagMap.put(key, new NBTTagFloat(value));
  }
  
  /**
   * Stores a new NBTTagDouble with the given double value into the map with the given string key.
   */
  public void setDouble(String key, double value) {
    this.tagMap.put(key, new NBTTagDouble(value));
  }
  
  /**
   * Stores a new NBTTagString with the given string value into the map with the given string key.
   */
  public void setString(String key, String value) {
    this.tagMap.put(key, new NBTTagString(value));
  }
  
  /**
   * Stores a new NBTTagByteArray with the given array as data into the map with the given string
   * key.
   */
  public void setByteArray(String key, byte[] value) {
    this.tagMap.put(key, new NBTTagByteArray(value));
  }
  
  /**
   * Stores a new NBTTagIntArray with the given array as data into the map with the given string
   * key.
   */
  public void setIntArray(String key, int[] value) {
    this.tagMap.put(key, new NBTTagIntArray(value));
  }
  
  /**
   * Stores the given boolean value as a NBTTagByte, storing 1 for true and 0 for false, using the
   * given string key.
   */
  public void setBoolean(String key, boolean value) {
    this.setByte(key, (byte) (value ? 1 : 0));
  }
  
  /**
   * gets a generic tag with the specified name
   */
  public NBTBase getTag(String key) {
    return this.tagMap.get(key);
  }
  
  /**
   * Gets the ID byte for the given tag key
   */
  public byte getTagId(String key) {
    NBTBase nbtbase = this.tagMap.get(key);
    return nbtbase != null ? nbtbase.getId() : 0;
  }
  
  /**
   * Returns whether the given string has been previously stored as a key in the map.
   */
  public boolean hasKey(String key) {
    return this.tagMap.containsKey(key);
  }
  
  public boolean hasKey(String key, int type) {
    int i = getTagId(key);
    
    if (i == type) {
      return true;
    }
    else if (type != 99) {
      return false;
    }
    else {
      return i == BYTE || i == SHORT || i == INT || i == LONG || i == FLOAT || i == DOUBLE;
    }
  }
  
  /**
   * Retrieves a byte value using the specified key, or 0 if no such key was stored.
   */
  public byte getByte(String key) {
    try {
      return !hasKey(key, 99) ? 0 : ((NBTBase.NBTPrimitive) this.tagMap.get(key)).getByte();
    }
    catch (ClassCastException ex) {
      return (byte) 0;
    }
  }
  
  /**
   * Retrieves a short value using the specified key, or 0 if no such key was stored.
   */
  public short getShort(String key) {
    try {
      return !hasKey(key, 99) ? 0 : ((NBTBase.NBTPrimitive) this.tagMap.get(key)).getShort();
    }
    catch (ClassCastException ex) {
      return (short) 0;
    }
  }
  
  /**
   * Retrieves an integer value using the specified key, or 0 if no such key was stored.
   */
  public int getInteger(String key) {
    try {
      return !hasKey(key, 99) ? 0 : ((NBTBase.NBTPrimitive) this.tagMap.get(key)).getInt();
    }
    catch (ClassCastException ex) {
      return 0;
    }
  }
  
  /**
   * Retrieves a long value using the specified key, or 0 if no such key was stored.
   */
  public long getLong(String key) {
    try {
      return !hasKey(key, 99) ? 0 : ((NBTBase.NBTPrimitive) this.tagMap.get(key)).getLong();
    }
    catch (ClassCastException ex) {
      return 0;
    }
  }
  
  /**
   * Retrieves a float value using the specified key, or 0 if no such key was stored.
   */
  public float getFloat(String key) {
    try {
      return !hasKey(key, 99) ? 0 : ((NBTBase.NBTPrimitive) this.tagMap.get(key)).getFloat();
    }
    catch (ClassCastException ex) {
      return 0;
    }
  }
  
  /**
   * Retrieves a double value using the specified key, or 0 if no such key was stored.
   */
  public double getDouble(String key) {
    try {
      return !hasKey(key, 99) ? 0 : ((NBTBase.NBTPrimitive) this.tagMap.get(key)).getDouble();
    }
    catch (ClassCastException ex) {
      return 0;
    }
  }
  
  /**
   * Retrieves a string value using the specified key, or an empty string if no such key was stored.
   */
  public String getString(String key) {
    try {
      return !hasKey(key, STRING) ? "" : ((NBTBase) this.tagMap.get(key)).getString();
    }
    catch (ClassCastException ex) {
      return "";
    }
  }
  
  /**
   * Retrieves a byte array using the specified key, or a zero-length array if no such key was
   * stored.
   */
  public byte[] getByteArray(String key) {
    try {
      return !hasKey(key, BYTE_A) ? new byte[0] : ((NBTTagByteArray) this.tagMap.get(key)).getByteArray();
    }
    catch (ClassCastException ex) {
      throw new RuntimeException(this.createCrashReport(key, BYTE_A, ex), ex);
    }
  }
  
  /**
   * Retrieves an int array using the specified key, or a zero-length array if no such key was
   * stored.
   */
  public int[] getIntArray(String key) {
    try {
      return !hasKey(key, INT_A) ? new int[0] : ((NBTTagIntArray) this.tagMap.get(key)).getIntArray();
    }
    catch (ClassCastException ex) {
      throw new RuntimeException(this.createCrashReport(key, INT_A, ex), ex);
    }
  }
  
  /**
   * Retrieves a NBTTagCompound subtag matching the specified key, or a new empty NBTTagCompound if
   * no such key was stored.
   */
  public NBTTagCompound getCompoundTag(String key) {
    try {
      return !hasKey(key, COMPOUND) ? new NBTTagCompound() : (NBTTagCompound) this.tagMap.get(key);
    }
    catch (ClassCastException ex) {
      throw new RuntimeException(this.createCrashReport(key, COMPOUND, ex), ex);
    }
  }
  
  /**
   * Gets the NBTTagList object with the given name. Args: name, NBTBase type
   */
  public NBTTagList getTagList(String key, int type) {
    try {
      if (getTagId(key) != LIST) {
        return new NBTTagList();
      }
      else {
        NBTTagList nbttaglist = (NBTTagList) this.tagMap.get(key);
        return nbttaglist.tagCount() > 0 && nbttaglist.getTagType() != type ? new NBTTagList() : nbttaglist;
      }
    }
    catch (ClassCastException ex) {
      throw new RuntimeException(this.createCrashReport(key, LIST, ex), ex);
    }
  }
  
  /**
   * Retrieves a boolean value using the specified key, or false if no such key was stored. This
   * uses the getByte method.
   */
  public boolean getBoolean(String key) {
    return this.getByte(key) != 0;
  }
  
  /**
   * Remove the specified tag.
   */
  public void removeTag(String key) {
    this.tagMap.remove(key);
  }
  
  @Override
  public String toString() {
    StringBuilder stringbuilder = new StringBuilder("{");
    
    for (Entry<String, NBTBase> entry : this.tagMap.entrySet()) {
      if (stringbuilder.length() != 1) {
        stringbuilder.append(',');
      }
      
      stringbuilder.append('"').append(entry.getKey()).append('"').append(':').append(entry.getValue());
    }
    
    return stringbuilder.append('}').toString();
  }
  
  /**
   * Return whether this compound has no tags.
   */
  @Override
  public boolean hasNoTags() {
    return this.tagMap.isEmpty();
  }
  
  /**
   * Create a crash report which indicates a NBT read error.
   */
  private String createCrashReport(final String key, final int expectedType, ClassCastException ex) {
    String found = NBTBase.NBT_TYPES[this.tagMap.get(key).getId()];
    String expected = NBTBase.NBT_TYPES[expectedType];
    
    return String.format("Found: TAG_%s; Expected: TAG_%s", found, expected);
  }
  
  /**
   * Creates a clone of the tag.
   */
  @Override
  public NBTBase copy() {
    NBTTagCompound nbttagcompound = new NBTTagCompound();
    
    for (String s : this.tagMap.keySet()) {
      nbttagcompound.setTag(s, this.tagMap.get(s).copy());
    }
    
    return nbttagcompound;
  }
  
  @Override
  public boolean equals(Object o) {
    if (super.equals(o)) {
      return this.tagMap.entrySet().equals(((NBTTagCompound) o).tagMap.entrySet());
    }
    return false;
  }
  
  @Override
  public int hashCode() {
    return super.hashCode() ^ this.tagMap.hashCode();
  }
  
  private static void writeEntry(String name, NBTBase data, DataOutput output) throws IOException {
    output.writeByte(data.getId());
    
    if (data.getId() != 0) {
      output.writeUTF(name);
      data.write(output);
    }
  }
  
  private static byte readType(DataInput input, NBTSizeTracker sizeTracker) throws IOException {
    sizeTracker.read(8);
    return input.readByte();
  }
  
  private static String readKey(DataInput input, NBTSizeTracker sizeTracker) throws IOException {
    return input.readUTF();
  }
  
  static NBTBase readNBT(byte id, String key, DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException {
    sizeTracker.read(32); // Forge: 4 extra bytes for the object allocation.
    NBTBase nbtbase = NBTBase.createNewByType(id);
    
    try {
      nbtbase.read(input, depth, sizeTracker);
      return nbtbase;
    }
    catch (IOException ex) {
      throw new RuntimeException("Tag type: " + Byte.valueOf(id), ex);
    }
  }
  
  /**
   * Merges this NBTTagCompound with the given compound. Any sub-compounds are merged using the same
   * methods, other types of tags are overwritten from the given compound.
   */
  public void merge(NBTTagCompound other) {
    for (String s : other.tagMap.keySet()) {
      NBTBase nbtbase = other.tagMap.get(s);
      
      if (nbtbase.getId() == COMPOUND) {
        if (hasKey(s, COMPOUND)) {
          getCompoundTag(s).merge((NBTTagCompound) nbtbase);
        }
        else {
          setTag(s, nbtbase.copy());
        }
      }
      else {
        setTag(s, nbtbase.copy());
      }
    }
  }
}