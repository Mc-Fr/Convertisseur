package net.mcfr.util;

import java.util.Optional;

public class BlockPos {
  private final int x, y, z;
  private String comment;

  public BlockPos(int x, int y, int z, String comment) {
    this.x = x;
    this.y = y;
    this.z = z;
    this.comment = comment;
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }

  public int getZ() {
    return this.z;
  }

  public Optional<String> getComment() {
    return Optional.ofNullable(this.comment);
  }

  @Override
  public String toString() {
    return String.format("(%d, %d, %d) %s", getX(), getY(), getZ(), getComment().orElse("bloc"));
  }
}
