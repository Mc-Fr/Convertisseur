package net.mcfr.converter;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;

import net.mcfr.minecraft.RegionFileCache;
import net.mcfr.minecraft.nbt.CompressedStreamTools;
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
class RegionsConverterThread extends Thread {
  /** Le largeur d'une région en nombre de chunks. */
  private static final int REGION_SIZE = 32;

  private MapConverter converter;
  private Replacer replacer;

  /**
   * Instancie un nouveau thread pour effectuer la conversion.
   * 
   * @param converter le convertisseur
   * @param replacer le remplaceur
   */
  public RegionsConverterThread(MapConverter converter, Replacer replacer) {
    this.converter = converter;
    this.replacer = replacer;
  }

  @Override
  public void run() {
    super.run();

    Optional<Point> optionalCoordinate;
    File dir = new File(this.converter.getRegionDirectory());

    while (!isInterrupted() && (optionalCoordinate = this.converter.getNextRegionCoordinate()).isPresent()) {
      Point regionCoordinate = optionalCoordinate.get();

      for (int x = 0; x < REGION_SIZE; x++) {
        for (int y = 0; y < REGION_SIZE; y++) {
          int chunkX = regionCoordinate.x * REGION_SIZE + x;
          int chunkY = regionCoordinate.y * REGION_SIZE + y;
          NBTTagCompound chunk;

          try (DataInputStream in = RegionFileCache.getChunkInputStream(dir, chunkX, chunkY)) {
            if (in == null)
              continue;
            chunk = CompressedStreamTools.read(in);
          }
          catch (IOException e) {
            System.out.println(
                String.format("Erreur lors de la lecture du chunk [%d, %d]@(%d, %d)", regionCoordinate.x, regionCoordinate.y, chunkX, chunkY));
            continue;
          }

          NBTTagCompound level = chunk.getCompoundTag("Level");
          NBTTagList sections = level.getTagList("Sections", 10);

          // Suppression des entités.
          level.setTag("Entities", new NBTTagList());
          // Suppression des entités de blocs.
          level.setTag("TileEntities", new NBTTagList());
          // Suppression de la file d'attente de mise à jour.
          level.setTag("TileTicks", new NBTTagList());

          for (int i = 0; i < sections.tagCount(); i++) {
            NBTTagCompound section = (NBTTagCompound) sections.get(i);
            boolean addPresent = true;

            byte[] blocks = section.getByteArray("Blocks");
            byte[] add = section.getByteArray("Add");
            byte[] data = section.getByteArray("Data");

            if (blocks.length == 0 || data.length == 0) {
              System.out.println(String.format("Section vide [%d, %d]@(%d, %d)@%d, ce n'est pas normal !", regionCoordinate.x, regionCoordinate.y,
                  chunkX, chunkY, i));
              continue;
            }

            if (add.length == 0) {
              add = new byte[2048];
              addPresent = false;
            }

            if (MapConverter.DEBUG)
              displayArrays(blocks, add, data, MapConverter.DEBUG_DIR, "0");
            for (int j = 0; j < blocks.length; j++)
              this.replacer.replace(blocks, add, data, j, new BlockId(Util.getId(blocks, add, j), Util.extractHalfByte(data, j)));
            if (MapConverter.DEBUG)
              displayArrays(blocks, add, data, MapConverter.DEBUG_DIR, "1");

            if (!addPresent)
              section.setByteArray("Add", add);
          }

          if (!MapConverter.READ_ONLY) {
            try (DataOutputStream out = RegionFileCache.getChunkOutputStream(dir, chunkX, chunkY)) {
              CompressedStreamTools.write(chunk, out);
            }
            catch (IOException e) {
              System.out.println(
                  String.format("Erreur lors de l'écriture du chunk [%d, %d]@(%d, %d)", regionCoordinate.x, regionCoordinate.y, chunkX, chunkY));
              continue;
            }
          }
        }
      }

      this.converter.updateProgress();
    }

    this.converter.notifyFinished(getId());
  }

  /**
   * Affiche les tableaux.
   * 
   * @param blocks les blocs
   * @param add les valeurs additionnelles
   * @param data les metadatas
   * @param directory le chemin vers le dossier
   * @param fileName le nom du fichier
   * @throws IOException
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
    catch (IOException e) {
      System.out.println("L'écriture du log a échoué");
    }
  }
}