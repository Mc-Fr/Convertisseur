package net.mcfr.finder;

import java.io.IOException;
import java.util.Map;

import net.mcfr.BrowserThreadBase;
import net.mcfr.minecraft.nbt.NBTBase;
import net.mcfr.minecraft.nbt.NBTTagCompound;
import net.mcfr.minecraft.nbt.NBTTagList;
import net.mcfr.util.BlockId;
import net.mcfr.util.BlockPos;
import net.mcfr.util.Util;

public class FinderThread extends BrowserThreadBase<Finder> {
  private Map<Integer, String> blocksIdsToNames;

  public FinderThread(Finder browser) {
    super(browser, false);
    try {
      this.blocksIdsToNames = Util.getBlocksIdsToNamesTable(getBrowser().getRegionDirectory());
    }
    catch (IOException __) {}
  }

  @Override
  protected void handleTileEntities(NBTTagCompound level, NBTTagList tileEntities) {
    for (int i = 0; i < tileEntities.tagCount(); i++) {
      NBTTagCompound te = tileEntities.getCompoundTagAt(i);

      if (te.hasKey("Items")) {
        NBTTagList items = te.getTagList("Items", NBTBase.COMPOUND);

        for (int j = 0; j < items.tagCount(); j++) {
          NBTTagCompound item = items.getCompoundTagAt(j);
          int targetMeta = getBrowser().getMeta();

          if (getBrowser().getId().equals(item.getString("id")) && (targetMeta == -1 || targetMeta == item.getInteger("Damage"))) {
            int x = te.getInteger("x");
            int y = te.getInteger("y");
            int z = te.getInteger("z");
            getBrowser().addPosition(new BlockPos(x, y, z, te.getString("id")));
          }
        }
      }
    }
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

        if (getBrowser().getId().equals(this.blocksIdsToNames.get(id.getId())) && (targetMeta == -1 || targetMeta == id.getMeta())) {
          int x = chunkX + i % 16;
          int y = sectionY + i / 256;
          int z = chunkZ + (i / 16) % 16;
          getBrowser().addPosition(new BlockPos(x, y, z, null));
        }
      }
    }
  }
}
