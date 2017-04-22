package net.mcfr.replacer;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import net.mcfr.AllTests;
import net.mcfr.util.BlockId;

public class ReplacerTest {
  private static Replacer replacer;

  @BeforeClass
  public static void setUpBeforeClass() throws Exception {
    replacer = new Replacer(AllTests.DATA_PATH, AllTests.MAP_PATH);
  }

  private byte[] ids, add, meta;

  @Before
  public void setUp() {
    this.ids = new byte[]{1, 2, 1, 4, 1, 2};
    this.add = new byte[]{1 << 4 | 1, 1, 0};
    this.meta = new byte[]{1, 6, 5};
  }

  @Test
  public void testReplace() {
    replacer.replace(this.ids, this.add, this.meta, 2, new BlockId(257, 6));
    assertEquals(0, this.ids[2]);
    assertEquals(0, this.add[1]);
    assertEquals(0, this.meta[1]);
    replacer.replace(this.ids, this.add, this.meta, 3, new BlockId(4, 0));
    assertEquals(0, this.ids[3]);
    assertEquals(1 << 4, this.add[1]);
    assertEquals(1 << 4, this.meta[1]);
    replacer.replace(this.ids, this.add, this.meta, 4, new BlockId(1, 5));
    assertEquals(6, this.ids[4]);
    assertEquals(0, this.add[2]);
    assertEquals(5, this.meta[2]);
  }

  @Test
  public void testReplaceBlockWithMeta() {
    byte[] add = {0};

    Replacer.replaceBlockWithMeta(this.ids, add, this.meta, 0, new BlockId(4, 2));
    assertEquals(4, this.ids[0]);
    assertEquals(0, add[0]);
    assertEquals(2, this.meta[0]);
    Replacer.replaceBlockWithMeta(this.ids, add, this.meta, 1, new BlockId(3, 6));
    assertEquals(3, this.ids[1]);
    assertEquals(0, add[0]);
    assertEquals(6 << 4 | 2, this.meta[0]);
  }

  @Test
  public void testReplaceBlockWithAddAndMeta() {
    Replacer.replaceBlockWithMeta(this.ids, this.add, this.meta, 0, new BlockId(5 << 8 | 4, 2));
    assertEquals(4, this.ids[0]);
    assertEquals(1 << 4 | 5, this.add[0]);
    assertEquals(2, this.meta[0]);
    Replacer.replaceBlockWithMeta(this.ids, this.add, this.meta, 1, new BlockId(7 << 8 | 3, 6));
    assertEquals(3, this.ids[1]);
    assertEquals(7 << 4 | 5, this.add[0]);
    assertEquals(6 << 4 | 2, this.meta[0]);
  }

  @Test
  public void testReplaceBlock() {
    byte[] add = {0};

    Replacer.replaceBlock(this.ids, add, 0, new BlockId(4, 0));
    assertEquals(4, this.ids[0]);
    assertEquals(0, add[0]);
    Replacer.replaceBlock(this.ids, add, 1, new BlockId(3, 0));
    assertEquals(3, this.ids[1]);
    assertEquals(0, add[0]);
  }

  @Test
  public void testReplaceBlockWithAdd() {
    Replacer.replaceBlock(this.ids, this.add, 0, new BlockId(5 << 8 | 4, 0));
    assertEquals(4, this.ids[0]);
    assertEquals(1 << 4 | 5, this.add[0]);
    Replacer.replaceBlock(this.ids, this.add, 1, new BlockId(7 << 8 | 3, 0));
    assertEquals(3, this.ids[1]);
    assertEquals(7 << 4 | 5, this.add[0]);
  }

  @Test
  public void testReplaceLowerHalfByte() {
    Replacer.replaceHalfByte(this.meta, 0, (byte) 2);
    assertEquals(2, this.meta[0]);
  }

  @Test
  public void testReplaceHigherHalfByte() {
    Replacer.replaceHalfByte(this.meta, 1, (byte) 1);
    assertEquals(1 << 4 | 1, this.meta[0]);
  }
}
