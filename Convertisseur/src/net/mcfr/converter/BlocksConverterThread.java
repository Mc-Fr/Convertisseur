package net.mcfr.converter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import net.mcfr.BrowserThreadBase;
import net.mcfr.minecraft.nbt.NBTTagCompound;
import net.mcfr.minecraft.nbt.NBTTagList;
import net.mcfr.replacer.Replacer;
import net.mcfr.util.BlockId;
import net.mcfr.util.Util;

/**
 * Thread chargé de la conversion des régions.
 * 
 * @author Mc-Fr
 */
class BlocksConverterThread extends BrowserThreadBase<BlocksConverter> {
  private Replacer replacer;

  /**
   * Instancie un nouveau thread pour effectuer la conversion.
   * 
   * @param converter le convertisseur
   * @param replacer le remplaceur
   */
  public BlocksConverterThread(BlocksConverter converter, Replacer replacer) {
    super(converter, true);
    this.replacer = replacer;
  }

  @Override
  protected void handleTileEntities(NBTTagCompound level, NBTTagList tileEntities) {
    level.setTag("Entities", new NBTTagList());
    level.setTag("TileEntities", new NBTTagList());
    if (level.hasKey("TileTicks"))
      level.setTag("TileTicks", new NBTTagList());
  }

  @Override
  public void handleBlocksInSection(NBTTagCompound level, NBTTagList sections, NBTTagCompound section, byte[] blocks, byte[] add, byte[] data) {
    if (BlocksConverter.DEBUG)
      displayArrays(blocks, add, data, BlocksConverter.DEBUG_DIR, "0");
    for (int i = 0; i < blocks.length; i++)
      this.replacer.replace(blocks, add, data, i, new BlockId(Util.getId(blocks, add, i), Util.extractHalfByte(data, i)));
    if (BlocksConverter.DEBUG)
      displayArrays(blocks, add, data, BlocksConverter.DEBUG_DIR, "1");
    setChanged(true);
  }

  /**
   * Affiche les tableaux.
   * 
   * @param blocks les blocs
   * @param add les valeurs additionnelles
   * @param data les metadatas
   * @param directory le chemin vers le dossier
   * @param fileName le nom du fichier
   */
  private void displayArrays(byte[] blocks, byte[] add, byte[] data, String directory, String fileName) {
    try (FileWriter fw = new FileWriter(new File(directory + "dump" + fileName + ".txt"))) {
      for (int i = 0; i < blocks.length; i++) {
        if (blocks[i] != 0) {
          if (i % 256 == 0)
            fw.write("------------\n");
          fw.write(String.format("%d:\tBlock:\t%d\t(ID: %d, Add: %d); Data:\t%d;\t(%d,\t%d,\t%d); %s %s\n", i / 2, Util.getId(blocks, add, i),
              Byte.toUnsignedInt(blocks[i]), Util.extractHalfByte(add, i), Util.extractHalfByte(data, i), i % 16, i / 256, (i / 16) % 16,
              String.format("%8s", Integer.toBinaryString(Byte.toUnsignedInt(data[i / 2]))).replace(' ', '0'),
              String.format("%8s", Integer.toBinaryString(Byte.toUnsignedInt(add[i / 2]))).replace(' ', '0')));
        }
      }
    }
    catch (IOException __) {
      System.out.println("L'écriture du log a échoué");
    }
  }
}