package net.mcfr.finder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.mcfr.MapBrowserBase;
import net.mcfr.util.BlockPos;

public class Finder extends MapBrowserBase {
  public static final String VERSION = "1.0";

  private final List<BlockPos> points;
  private final String id;
  private final int meta;
  private final boolean isBlock;

  public Finder(String regionDirectory, String id, int meta, boolean isBlock, int threadsNb) throws IOException {
    super(regionDirectory, threadsNb);
    this.points = new ArrayList<>();
    this.id = id;
    this.meta = meta;
    this.isBlock = isBlock;

    for (int i = 0; i < threadsNb; i++)
      this.threads.push(new FinderThread(this));
  }

  public List<BlockPos> getPositions() {
    return new ArrayList<>(this.points);
  }

  public synchronized void addPosition(BlockPos point) {
    this.points.add(point);
  }

  public String getId() {
    return this.id;
  }

  public int getMeta() {
    return this.meta;
  }

  public boolean isBlock() {
    return this.isBlock;
  }

  @Override
  public void start() {
    System.out.println(String.format("Recherche en cours..."));
    super.start();
  }
}
