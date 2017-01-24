package net.mcfr.util;

/**
 * Cette classe représente un ID/metadata.
 * 
 * @author Mc-Fr
 */
public class BlockId {
  private final int id, meta;

  public BlockId(int id, int meta) {
    this.id = id;
    this.meta = meta;
  }

  public int getId() {
    return this.id;
  }

  public int getMeta() {
    return this.meta;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;

    result = prime * result + getId();
    result = prime * result + getMeta();

    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof BlockId) {
      BlockId id = (BlockId) o;
      return getId() == id.getId() && getMeta() == id.getMeta();
    }

    return false;
  }

  @Override
  public String toString() {
    return String.format("%d/%d", getId(), getMeta());
  }
}