package net.mcfr;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import net.mcfr.util.BlockId;
import net.mcfr.util.Util;

public class UtilTest {
  private static final String PATH = "C:\\Users\\Darmo\\Darmo\\Programmation\\Java\\MCFR\\trunk\\Map_Converter\\res\\altria\\";

  private Map<Integer, String> idsToNames;
  private Map<String, Integer> namesToIds;
  private Map<BlockId, BlockId> ids;

  @Before
  public void setUp() throws Exception {
    this.idsToNames = Util.getIdsToNamesTable(PATH);
    this.namesToIds = Util.getNamesToIdsTable(PATH);
    this.ids = Util.getIdsTable("C:\\Users\\Darmo\\Darmo\\Programmation\\Java\\MCFR\\trunk\\Map_Converter\\res\\test.cfg", PATH);
  }

  @Test
  public void testGetIdsToNamesTable() {
    assertEquals("minecraft:air", this.idsToNames.get(0));
    assertEquals("minecraft:stone", this.idsToNames.get(1));
  }

  @Test
  public void testGetNamesToIdsTable() {
    assertEquals((Integer) 0, this.namesToIds.get("minecraft:air"));
    assertEquals((Integer) 1, this.namesToIds.get("minecraft:stone"));
  }

  @Test
  public void testGetIdsTable() {
    assertEquals(new BlockId(6, 0), this.ids.get(new BlockId(1, 0)));
    assertEquals(new BlockId(256, 1), this.ids.get(new BlockId(4, 0)));
    assertEquals(new BlockId(0, 0), this.ids.get(new BlockId(257, 6)));
    assertEquals(new BlockId(12, 0), this.ids.get(new BlockId(17, 0)));
    assertEquals(new BlockId(19, 0), this.ids.get(new BlockId(18, 1)));
  }

  @Test
  public void testGetId() {
    byte[] ids = {1, 2};
    byte[] add = {0};

    assertEquals(1, Util.getId(ids, add, 0));
    assertEquals(2, Util.getId(ids, add, 1));
  }

  @Test
  public void testGetIdWithAdd() {
    byte[] ids = {1, 2, 3, 4};
    byte[] add = {6 << 4 | 3, 5 << 4 | 7};

    assertEquals(3 << 8 | 1, Util.getId(ids, add, 0));
    assertEquals(6 << 8 | 2, Util.getId(ids, add, 1));
    assertEquals(7 << 8 | 3, Util.getId(ids, add, 2));
    assertEquals(5 << 8 | 4, Util.getId(ids, add, 3));
  }

  @Test
  public void testExtractHalfByte() {
    byte[] bytes = {5 << 4 | 2, 6 << 4 | 3};

    assertEquals(2, Util.extractHalfByte(bytes, 0));
    assertEquals(5, Util.extractHalfByte(bytes, 1));
    assertEquals(3, Util.extractHalfByte(bytes, 2));
    assertEquals(6, Util.extractHalfByte(bytes, 3));
  }
}
