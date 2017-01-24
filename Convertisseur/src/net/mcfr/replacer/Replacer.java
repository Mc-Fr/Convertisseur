package net.mcfr.replacer;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import net.mcfr.util.BlockId;
import net.mcfr.util.Logger;
import net.mcfr.util.Util;

/**
 * Cette classe permet de remplacer des blocs par d'autres à partir d'une table chargée depuis le
 * fichier de config passé en paramètre.
 * 
 * @author Mc-Fr
 */
public class Replacer {
  private static final Logger LOGGER = Logger.getLogger();

  /** Une table associant les anciens ID aux nouveaux. */
  private final Map<BlockId, BlockId> idsTable;

  /**
   * Crée un remplaceur avec la configuration donnée.
   * 
   * @param configPath le chemin du fichier de config
   * @param levelDataPath le chemin vers le level.dat
   * @throws IOException si une erreur de lecture est survenue
   * @throws ParseException si une erreur de syntaxe a été rencontrée
   */
  public Replacer(String configPath, String levelDataPath) throws IOException, ParseException {
    LOGGER.info("Génération de la table des ID...");
    this.idsTable = Util.getIdsTable(configPath, levelDataPath);
    LOGGER.info("Fait.");
  }

  /**
   * Convertit l'ID donné en la nouvelle valeur.
   * 
   * @param blocks le tableau des blocs
   * @param add le tableau des valeurs additionnelles des blocs
   * @param data le tableau des metadatas
   * @param i l'indice dans le tableau des blocs
   * @param oldId l'ID à remplacer
   * @param le nouvel ID
   */
  public BlockId replace(byte[] blocks, byte[] add, byte[] data, int i, BlockId oldId) {
    BlockId newId = this.idsTable.get(oldId);

    if (newId != null)
      replaceBlockWithMeta(blocks, add, data, i, newId);

    return newId;
  }

  /**
   * Remplace l'ID d'un bloc et son metadata.
   * 
   * @param blocks les blocs
   * @param add les valeurs additionnelles
   * @param data les metadatas
   * @param i l'indice dans le tableau des blocs
   * @param newId le nouvel ID
   * @param newMeta le nouveau metadata
   */
  public static void replaceBlockWithMeta(byte[] blocks, byte[] add, byte[] data, int i, BlockId newId) {
    replaceBlock(blocks, add, i, newId);
    replaceHalfByte(data, i, (byte) (newId.getMeta() & 0xf));
  }

  /**
   * Remplace l'ID d'un bloc.
   * 
   * @param blocks les blocs
   * @param add les valeurs additionnelles
   * @param i l'indice dans le tableau des blocs
   * @param newId le nouvel ID
   */
  public static void replaceBlock(byte[] blocks, byte[] add, int i, BlockId newId) {
    boolean even = i % 2 == 0;
    int id = newId.getId();

    if (id > 255) {
      byte b = (byte) ((id & 0xf00) >>> 8);
      replaceHalfByte(add, i, b);
    }
    else {
      add[i / 2] &= (even ? 0xf0 : 0x0f);
    }
    blocks[i] = (byte) (id & 0xff);
  }

  /**
   * Remplace la valeur dans le demi-octet du tableau donné à l'indice i (dans le tableau blocs).
   * 
   * @param array le tableau
   * @param i l'indice du bloc
   * @param newValue la nouvelle valeur
   */
  public static void replaceHalfByte(byte[] array, int i, byte newValue) {
    boolean even = i % 2 == 0;
    array[i / 2] = (byte) ((array[i / 2] & (even ? 0xf0 : 0x0f)) | ((newValue & 0xf) << (even ? 0 : 4)));
  }
}
