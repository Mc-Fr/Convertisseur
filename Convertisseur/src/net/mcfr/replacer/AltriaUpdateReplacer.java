package net.mcfr.replacer;

import java.io.IOException;
import java.text.ParseException;
import java.util.Map;

import net.mcfr.util.BlockId;
import net.mcfr.util.Util;

/**
 * Un remplaceur spécial pour la conversion des cartes de la 1.4 vers la 1.10.
 * 
 * @author Mc-Fr
 */
public class AltriaUpdateReplacer extends Replacer {
  /** Ue table contenant associant les ID à leur nom. */
  private final Map<Integer, String> idsToName;

  /**
   * Crée un remplaceur pour mettre à jour les cartes de la 1.4 vers la 1.10.
   * 
   * @param configPath le chemin du fichier de config
   * @param levelDataPath le chemin vers le level.dat
   * @throws IOException si une erreur de lecture est survenue
   * @throws ParseException si une erreur de syntaxe a été rencontrée
   */
  public AltriaUpdateReplacer(String configPath, String levelDataPath) throws IOException, ParseException {
    super(configPath, levelDataPath);
    System.out.println("Génération de l'index ID/noms...");
    this.idsToName = Util.getBlocksIdsToNamesTable(levelDataPath);
    System.out.println("Fait.");
  }

  /**
   * {@inheritDoc} Les pentes sont réorientées pour être conformes aux nouveaux metadatas.
   */
  @Override
  public BlockId replace(byte[] blocks, byte[] add, byte[] data, int i, BlockId oldId) {
    BlockId newId = super.replace(blocks, add, data, i, oldId);

    if (newId != null && isSlope(newId.getId())) {
      int meta = Util.extractHalfByte(data, i);
      int newMeta = -1;

      switch (meta & 3) {
        case 0:
          newMeta = 2;
          break;
        case 1:
          newMeta = 1;
          break;
        case 2:
          newMeta = 3;
          break;
        case 3:
          newMeta = 0;
          break;
      }
      replaceHalfByte(data, i, (byte) (newMeta | meta & 4));
    }

    return newId;
  }

  /**
   * Indique si l'ID donné correspond à une pente.
   * 
   * @param id l'ID
   * @return true si le bloc est une pente
   */
  private boolean isSlope(int id) {
    return this.idsToName.containsKey(id) && this.idsToName.get(id).endsWith("_slope");
  }
}
