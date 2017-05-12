package net.mcfr;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;

import net.mcfr.converter.MapConverter;

public class Start {
  public static void main(String[] args) {
    if (args.length == 1 && args[0].equals("-h")) {
      printHelp();
    }
    else if ((args.length == 3 || args.length == 4 && args[3].equals("-a")) && args[0].equals("-c")) {
      convertMap(addSeparator(args[0]), addSeparator(args[1]), args.length == 4 && args[3].equals("-a"));
    }
    else
      printUsage();
  }

  private static void convertMap(String regionDirectory, String configFilePath, boolean isAltria) {
    try {
      String version = String.format("= Map Converter v%s =", MapConverter.VERSION);
      String str = String.format("%" + version.length() + "s", "").replace(' ', '=');

      System.out.println(str);
      System.out.println(version);
      System.out.println(str);
      new MapConverter(regionDirectory, configFilePath, isAltria, 4).convert();
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
