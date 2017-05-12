package net.mcfr.finder;

import java.io.IOException;
import java.util.Map;

import net.mcfr.BrowserThreadBase;
import net.mcfr.minecraft.nbt.NBTTagCompound;
import net.mcfr.minecraft.nbt.NBTTagList;
import net.mcfr.util.BlockId;
import net.mcfr.util.BlockPos;
import net.mcfr.util.Util;

public class FinderThread extends BrowserThreadBase<Finder> {
  private Map<Integer, String> ids;

  public FinderThread(Finder browser) {
    super(browser);
    try {
      this.ids = Util.getBlocksIdsToNamesTable(getBrowser().getRegionDirectory());
    }
    catch (IOException __) {}
  }

  @Override
  protected void handleTileEntities(NBTTagCompound level, NBTTagList tileEntities) {
    // TODO
    // Les coordonnées sont stockées dans chaque TE
  }

  @Override
  protected void handleBlocksInSection(NBTTagCompound level, NBTTagList sections, NBTTagCompound section, byte[] blocks, byte[] add, byte[] data) {
    if (getBrowser().isBlock()) {
      int chunkX = level.getInteger("xPos") * 16;
      int sectionY = section.getInteger("Y") * 16;
      int chunkZ = level.getInteger("zPos") * 16;

      for (int i = 0; i < blocks.length; i++) {
        BlockId id = new BlockId(Util.getId(blocks, add, i), Util.extractHalfByte(data, i));
        int targetMeta = getBrowser().getMeta();

        if (getBrowser().getId().equals(this.ids.get(id.getId())) && (targetMeta == -1 || targetMeta == id.getMeta())) {
          int x = chunkX + i % 16;
          int y = sectionY + i / 256;
          int z = chunkZ + (i / 16) % 16;
          getBrowser().addPosition(new BlockPos(x, y, z));
        }
      }
    }
  }
}