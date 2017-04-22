package net.mcfr;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import net.mcfr.replacer.ReplacerTest;

@RunWith(Suite.class)
@SuiteClasses({BlockIdTest.class, UtilTest.class, ReplacerTest.class})
public class AllTests {
  public static final String MAP_PATH = "C:\\Users\\Darmo\\Darmo\\Programmation\\Java\\MCFR\\trunk\\Convertisseur\\Convertisseur\\res\\altria\\";
  public static final String DATA_PATH = "C:\\Users\\Darmo\\Darmo\\Programmation\\Java\\MCFR\\trunk\\Convertisseur\\Convertisseur\\res\\test.cfg";
}
