package net.mcfr;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import net.mcfr.util.BlockId;

public class BlockIdTest {
  private BlockId blockId1;
  private BlockId blockId2;

  @Before
  public void setUp() throws Exception {
    this.blockId1 = new BlockId(1, 0);
    this.blockId2 = new BlockId(1, 1);
  }

  @Test
  public void testEquals() {
    assertEquals(new BlockId(1, 0), this.blockId1);
    assertEquals(new BlockId(1, 1), this.blockId2);
  }

  @Test
  public void testNotEquals() {
    assertNotEquals(new BlockId(0, 0), this.blockId1);
    assertNotEquals(new BlockId(1, 1), this.blockId1);
    assertNotEquals(new BlockId(0, 0), this.blockId2);
    assertNotEquals(new BlockId(2, 1), this.blockId2);
  }

  @Test
  public void testToString() {
    assertEquals("1/0", this.blockId1.toString());
    assertEquals("1/1", this.blockId2.toString());
  }
}
