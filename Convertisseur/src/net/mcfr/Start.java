package net.mcfr;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import net.mcfr.converter.MapConverter;
import net.mcfr.util.Logger;

public class Start {
  private static final Logger LOGGER = Logger.getLogger();

  /**
   * Arguments : <i>&lt;chemin_vers_region></i> <i>&lt;chemin_vers_config></i> [-a]
   * <ul>
   * <li><i>&lt;chemin_vers_region></i> : chemin vers le dossier contenant le dossier region ;</li>
   * <li><i>&lt;chemin_vers_config></i> : chemin vers le fichier de config ;</li>
   * <li>option <i>-a</i> : si renseignée, des règles supplémentaires sont ajoutées pour la
   * conversion des cartes d'Altria</li>
   * </ul>
   * La conversion va être effectuée à partir des ID définis dans le fichier 'level.dat' de la carte
   * convertie.<br/>
   * <b>N.B. : Si la carte n'a pas été chargée avec Forge, la conversion ne sera pas possible si le
   * fichier de config utilise des ID litéraux (ex. : minecraft:stone).</b><br/>
   * Procédure :
   * <ul>
   * <li>Récupérer le fichier 'ids.cfg'</li>
   * <li>Indiquer le chemin vers le monde à convertir (il doit contenir le dossier 'region' et le
   * fichier 'level.dat' contenant les nouveaux ID)</li>
   * <li>Indiquer le chemin vers le fichier de config 'ids.cfg' (sans ajouter le nom !)</li>
   * <li>Ajouter l'option -a si la carte à convertir fait partie de la version d'Altria du
   * serveur</li>
   * <li>Valider</li>
   * </ul>
   * 
   * @param args les arguments
   */
  public static void main(String[] args) {
    Logger.beginCatchingOutputs();

    if (args.length < 2 || args.length > 3) {
      printUsage();
      return;
    }
    else if (args.length == 3 && !"-a".equals(args[2])) {
      LOGGER.severe("Option inconnue " + args[2]);
      printUsage();
      return;
    }

    try {
      String version = String.format("= Map Converter v%s =", MapConverter.VERSION);
      String str = String.format("%" + version.length() + "s", "").replace(' ', '=');

      LOGGER.info(str);
      LOGGER.info(version);
      LOGGER.info(str);
      new MapConverter(addSeparator(args[0]), addSeparator(args[1]), "-a".equals(args[2]), 4).convert();
    }
    catch (IOException e) {
      LOGGER.severe("Une erreur est survenue : " + e.getMessage());
    }
    catch (ParseException e) {
      LOGGER.severe(String.format("%s ligne %d", e.getMessage(), e.getErrorOffset()));
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
    LOGGER.severe("Arguments : [<path_to_region> <path_to_config> [-a]]");
  }

  private Start() {}
}
