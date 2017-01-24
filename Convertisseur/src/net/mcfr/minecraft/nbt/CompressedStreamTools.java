package net.mcfr.minecraft.nbt;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompressedStreamTools {
  /**
   * Load the gzipped compound from the inputstream.
   */
  public static NBTTagCompound readCompressed(InputStream is) throws IOException {
    DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new GZIPInputStream(is)));
    NBTTagCompound nbttagcompound;
    
    try {
      nbttagcompound = read(datainputstream, NBTSizeTracker.INFINITE);
    }
    finally {
      datainputstream.close();
    }
    
    return nbttagcompound;
  }
  
  /**
   * Write the compound, gzipped, to the outputstream.
   */
  public static void writeCompressed(NBTTagCompound compound, OutputStream outputStream) throws IOException {
    DataOutputStream dataoutputstream = new DataOutputStream(new BufferedOutputStream(new GZIPOutputStream(outputStream)));
    
    try {
      write(compound, dataoutputstream);
    }
    finally {
      dataoutputstream.close();
    }
  }
  
  public static void safeWrite(NBTTagCompound compound, File file) throws IOException {
    File file1 = new File(file.getAbsolutePath() + "_tmp");
    
    if (file1.exists()) {
      file1.delete();
    }
    
    write(compound, file1);
    
    if (file.exists()) {
      file.delete();
    }
    
    if (file.exists()) {
      throw new IOException("Failed to delete " + file);
    }
    else {
      file1.renameTo(file);
    }
  }
  
  /**
   * Reads from a CompressedStream.
   */
  public static NBTTagCompound read(DataInputStream inputStream) throws IOException {
    /**
     * Reads the given DataInput, constructs, and returns an NBTTagCompound with the data from the
     * DataInput
     */
    return read(inputStream, NBTSizeTracker.INFINITE);
  }
  
  /**
   * Reads the given DataInput, constructs, and returns an NBTTagCompound with the data from the
   * DataInput
   */
  public static NBTTagCompound read(DataInput in, NBTSizeTracker tracker) throws IOException {
    NBTBase nbtbase = getTag(in, 0, tracker);
    
    if (nbtbase instanceof NBTTagCompound) {
      return (NBTTagCompound) nbtbase;
    }
    else {
      throw new IOException("Root tag must be a named compound tag");
    }
  }
  
  public static void write(NBTTagCompound compound, DataOutput out) throws IOException {
    writeTag(compound, out);
  }
  
  private static void writeTag(NBTBase tag, DataOutput out) throws IOException {
    out.writeByte(tag.getId());
    
    if (tag.getId() != 0) {
      out.writeUTF("");
      tag.write(out);
    }
  }
  
  private static NBTBase getTag(DataInput in, int depth, NBTSizeTracker tracker) throws IOException {
    byte b0 = in.readByte();
    tracker.read(8); // Forge: Count everything!
    
    if (b0 == 0) {
      return new NBTTagEnd();
    }
    else {
      NBTSizeTracker.readUTF(tracker, in.readUTF()); // Forge: Count this string.
      tracker.read(32); // Forge: 4 extra bytes for the object allocation.
      NBTBase nbtbase = NBTBase.createNewByType(b0);
      
      try {
        nbtbase.read(in, depth, tracker);
        return nbtbase;
      }
      catch (IOException ioexception) {
        throw new RuntimeException("Tag type" + Byte.valueOf(b0), ioexception);
      }
    }
  }
  
  public static void write(NBTTagCompound compound, File file) throws IOException {
    DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file));
    
    try {
      write(compound, dataoutputstream);
    }
    finally {
      dataoutputstream.close();
    }
  }
  
  public static NBTTagCompound read(File file) throws IOException {
    if (!file.exists()) {
      return null;
    }
    else {
      DataInputStream datainputstream = new DataInputStream(new FileInputStream(file));
      NBTTagCompound nbttagcompound;
      
      try {
        nbttagcompound = read(datainputstream, NBTSizeTracker.INFINITE);
      }
      finally {
        datainputstream.close();
      }
      
      return nbttagcompound;
    }
  }
}