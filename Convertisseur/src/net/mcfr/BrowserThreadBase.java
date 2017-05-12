package net.mcfr;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import net.mcfr.converter.BlocksConverter;
import net.mcfr.minecraft.RegionFileCache;
import net.mcfr.minecraft.nbt.CompressedStreamTools;
import net.mcfr.minecraft.nbt.NBTBase;
import net.mcfr.minecraft.nbt.NBTTagCompound;
import net.mcfr.minecraft.nbt.NBTTagList;

public abstract class BrowserThreadBase<T extends MapBrowserBase> extends Thread {
  /** Le largeur d'une région en nombre de chunks */
  private static final int REGION_SIZE = 32;

  private final T browser;
  private boolean changed;

  public BrowserThreadBase(T browser) {
    this.browser = browser;
    this.changed = false;
  }

  public T getBrowser() {
    return this.browser;
  }

  public void setChanged(boolean changed) {
    this.changed = changed;
  }

  @Override
  public final void run() {
    Optional<Point> optionalCoordinate;
    File dir = new File(this.browser.getRegionDirectory());

    while (!isInterrupted() && (optionalCoordinate = this.browser.getNextRegionCoordinate()).isPresent()) {
      Point regionCoordinate = optionalCoordinate.get();

      for (int x = 0; x < REGION_SIZE; x++) {
        for (int y = 0; y < REGION_SIZE; y++) {
          int chunkX = regionCoordinate.x * REGION_SIZE + x;
          int chunkY = regionCoordinate.y * REGION_SIZE + y;
          NBTTagCompound chunk;

          // Lecture du chunk
          try (DataInputStream in = RegionFileCache.getChunkInputStream(dir, chunkX, chunkY)) {
            if (in == null)
              continue;
            chunk = CompressedStreamTools.read(in);
          }
          catch (IOException __) {
            System.out.println(
                String.format("Erreur lors de la lecture du chunk [%d, %d]@(%d, %d)", regionCoordinate.x, regionCoordinate.y, chunkX, chunkY));
            continue;
          }

          NBTTagCompound level = chunk.getCompoundTag("Level");
          NBTTagList sections = level.getTagList("Sections", NBTBase.COMPOUND);

          handleTileEntities(level, level.getTagList("TileEntities", NBTBase.COMPOUND));

          for (int i = 0; i < sections.tagCount(); i++) {
            this.changed = false;

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

            handleBlocksInSection(level, sections, section, blocks, add, data);

            if (this.changed && !addPresent)
              section.setByteArray("Add", add);
          }

          if (!BlocksConverter.READ_ONLY) {
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

      this.browser.updateProgress();
    }

    this.browser.notifyFinished(getId());
  }

  protected void handleTileEntities(NBTTagCompound level, NBTTagList tileEntities) {}

  protected void handleBlocksInSection(NBTTagCompound level, NBTTagList sections, NBTTagCompound section, byte[] blocks, byte[] add, byte[] data) {}
}
