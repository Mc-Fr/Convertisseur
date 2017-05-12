package net.mcfr.finder;

import net.mcfr.BrowserThreadBase;
import net.mcfr.minecraft.nbt.NBTTagCompound;
import net.mcfr.minecraft.nbt.NBTTagList;

public class FinderThread extends BrowserThreadBase<Finder> {
  public FinderThread(Finder browser) {
    super(browser);
  }

  @Override
  protected void handleTileEntities(NBTTagCompound level, NBTTagList tagList) {
    // TODO Auto-generated method stub

  }

  @Override
  protected void handleBlocksInSection(NBTTagCompound level, NBTTagList sections, NBTTagCompound section, byte[] blocks, byte[] add, byte[] data) {
    // TODO Auto-generated method stub

  }
}
