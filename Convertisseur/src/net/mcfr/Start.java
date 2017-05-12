package net.mcfr;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mcfr.converter.BlocksConverter;
import net.mcfr.finder.Finder;
import net.mcfr.util.Util;

public class Start {
  private static final Scanner IN = new Scanner(System.in);

  public static void main(String[] args) {
    if (args.length == 1 && args[0].equals("-h")) {
      printHelp();
    }
    else if (args.length == 2 && args[0].equals("-f")) {
      search(args[1]);
    }
    else if ((args.length == 3 || args.length == 4 && args[3].equals("-a")) && args[0].equals("-c")) {
      convertMap(addSeparator(args[0]), addSeparator(args[1]), args.length == 4 && args[3].equals("-a"));
    }
    else
      printUsage();
  }

  private static void search(String regionDirectory) {
    String version = String.format("= Finder v%s =", Finder.VERSION);
    String str = String.format("%" + version.length() + "s", "").replace(' ', '=');

    System.out.println(str);
    System.out.println(version);
    System.out.println(str);

    Pattern p = Pattern.compile("(\\w+)/(-1|\\d+)");
    String input = null;

    while (!"exit".equals(input) || "quit".equals(input)) {
      System.out.println("ID de l'item/bloc à rechercher :");
      System.out.print("> ");
      input = IN.nextLine();
      Matcher m = p.matcher(input);

      if (m.matches()) {
        String id = m.group(1);
        int meta = Integer.parseInt(m.group(2));
        boolean isBlock;

        try {
          isBlock = Util.getBlocksNamesToIdsTable(regionDirectory).containsKey(id);
        }
        catch (IOException e) {
          System.out.println("Une erreur est survenue : " + e.getMessage());
          continue;
        }
        try {
          Finder finder = new Finder(regionDirectory, id, meta, isBlock, 4);
          finder.start();

        }
        catch (IOException e) {
          System.out.println("Une erreur est survenue : " + e.getMessage());
        }
      }
    }
  }

  private static void convertMap(String regionDirectory, String configFilePath, boolean isAltria) {
    try {
      String version = String.format("= Map Converter v%s =", BlocksConverter.VERSION);
      String str = String.format("%" + version.length() + "s", "").replace(' ', '=');

      System.out.println(str);
      System.out.println(version);
      System.out.println(str);
      new BlocksConverter(regionDirectory, configFilePath, isAltria, 4).start();
    }
    catch (IOException e) {
      System.out.println("Une erreur est survenue : " + e.getMessage());
    }
    catch (ParseException e) {
      System.out.println(String.format("%s ligne %d", e.getMessage(), e.getErrorOffset()));
    }
  }

  /**
   * Ajoute le séparateur de fichiers à la fin du chemin donné s'il n'est pas déjà présent.
   * 
   * @param path le chemin
   * @return le chemin avec le séparateur
   */
  private static String addSeparator(String path) {
    return path + (path.charAt(path.length() - 1) != File.separatorChar ? File.separator : "");
  }

  /**
   * Affiche l'utilisation correcte du programme.
   */
  private static void printUsage() {
    System.out.println("Arguments : [<path_to_region> <path_to_config> [-a]]");
    System.out.println("Pour afficher l'aide, utilisez l'option -h");
  }

  /**
   * Affiche l'aide.
   */
  private static void printHelp() {
    try {
      Files.readAllLines(Paths.get("assets/help.txt")).forEach(System.out::println);
    }
    catch (IOException __) {}
  }

  private Start() {}
}
