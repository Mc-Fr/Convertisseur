package net.mcfr.converter;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import net.mcfr.MapBrowserBase;
import net.mcfr.replacer.AltriaUpdateReplacer;
import net.mcfr.replacer.Replacer;

/**
 * Convertisseur de carte.
 * 
 * @author Mc-Fr
 */
public class BlocksConverter extends MapBrowserBase {
  public static final String VERSION = "1.2.1";
  /** Le chemin vers les fichiers de config pour le debug. */
  public static final String DEBUG_DIR = "C:\\Users\\Darmo\\Darmo\\Programmation\\Java\\MCFR\\trunk\\Convertisseur\\res\\";
  /** Constante magique pour le debug empêchant l'écriture des fichiers. */
  public static final boolean READ_ONLY = false;

  /**
   * Crée un convertisseur pour la carte donnée.
   * 
   * @param regionDirectory le chemin vers le dossier region (contient aussi le fichier level.dat)
   * @param configFilePath le chemin du fichier de config
   * @param isAltria indique si la carte fait partie d'Altria
   * @param threadsNb le nombre de threads
   * @throws IOException si une erreur de lecture/écriture est survenue
   * @throws ParseException si une erreur de syntaxe a été rencontrée dans le fichier de config
   */
  public BlocksConverter(String regionDirectory, String configFilePath, boolean isAltria, int threadsNb) throws IOException, ParseException {
    super(regionDirectory, threadsNb);

    String configFile = configFilePath + "ids.cfg";
    Replacer replacer = isAltria ? new AltriaUpdateReplacer(configFile, getRegionDirectory()) : new Replacer(configFile, getRegionDirectory());
    for (int i = 0; i < threadsNb; i++)
      this.threads.push(new BlocksConverterThread(this, replacer));
  }

  /**
   * Convertit la carte.
   * 
   * @throws IOException si une erreur de lecture est survenue
   * @throws ParseException si une erreur de syntaxe a été rencontrée dans le fichier de config
   */
  @Override
  public void start() {
    if (BlocksConverter.READ_ONLY)
      System.out.println("Attention : le mode lecture seule est activé, les changement seront ignorés !");

    File dir = new File(getRegionDirectory());
    System.out.println(String.format("Conversion de la carte '%s' en cours...", dir.getName()));
    super.start();
  }
}
