package net.mcfr.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.mcfr.minecraft.nbt.CompressedStreamTools;
import net.mcfr.minecraft.nbt.NBTBase;
import net.mcfr.minecraft.nbt.NBTTagCompound;
import net.mcfr.minecraft.nbt.NBTTagList;

public final class Util {
  private static Map<Integer, String> blockIdsToNames;
  private static Map<String, Integer> blockNamesToIds;
  private static Map<Integer, String> itemIdsToNames;
  private static Map<String, Integer> itemNamesToIds;
  private static Map<BlockId, BlockId> idsTable;

  /**
   * Retourne la table associant les ID des blocs à leur nom.
   * 
   * @param levelDataPath le chemin vers le level.dat
   * @return la table des ID/noms
   * @throws IOException
   */
  public static Map<Integer, String> getBlocksIdsToNamesTable(String levelDataPath) throws IOException {
    if (blockIdsToNames == null) {
      NBTTagList list = getBlocksIdsList(levelDataPath);
      blockIdsToNames = new LinkedHashMap<>();

      for (int i = 0; i < list.tagCount(); i++) {
        NBTTagCompound c = list.getCompoundTagAt(i);
        blockIdsToNames.put(c.getInteger("V"), c.getString("K"));
      }
    }

    return Collections.unmodifiableMap(blockIdsToNames);
  }

  /**
   * Retourne la table associant les noms des blocs à leur ID.
   * 
   * @param levelDataPath le chemin vers le level.dat
   * @return la table des noms/ID
   * @throws IOException
   */
  public static Map<String, Integer> getBlocksNamesToIdsTable(String levelDataPath) throws IOException {
    if (blockNamesToIds == null) {
      NBTTagList list = getBlocksIdsList(levelDataPath);
      blockNamesToIds = new LinkedHashMap<>();

      for (int i = 0; i < list.tagCount(); i++) {
        NBTTagCompound c = list.getCompoundTagAt(i);
        blockNamesToIds.put(c.getString("K"), c.getInteger("V"));
      }
    }

    return Collections.unmodifiableMap(blockNamesToIds);
  }

  /**
   * Retourne la table associant les ID des items à leur nom.
   * 
   * @param levelDataPath le chemin vers le level.dat
   * @return la table des ID/noms
   * @throws IOException
   */
  public static Map<Integer, String> getItemsIdsToNamesTable(String levelDataPath) throws IOException {
    if (itemIdsToNames == null) {
      NBTTagList list = getItemsIdsList(levelDataPath);
      itemIdsToNames = new LinkedHashMap<>();

      for (int i = 0; i < list.tagCount(); i++) {
        NBTTagCompound c = list.getCompoundTagAt(i);
        itemIdsToNames.put(c.getInteger("V"), c.getString("K"));
      }
    }

    return Collections.unmodifiableMap(itemIdsToNames);
  }

  /**
   * Retourne la table associant les noms des items à leur ID.
   * 
   * @param levelDataPath le chemin vers le level.dat
   * @return la table des noms/ID
   * @throws IOException
   */
  public static Map<String, Integer> getItemsNamesToIdsTable(String levelDataPath) throws IOException {
    if (itemNamesToIds == null) {
      NBTTagList list = getItemsIdsList(levelDataPath);
      itemNamesToIds = new LinkedHashMap<>();

      for (int i = 0; i < list.tagCount(); i++) {
        NBTTagCompound c = list.getCompoundTagAt(i);
        itemNamesToIds.put(c.getString("K"), c.getInteger("V"));
      }
    }

    return Collections.unmodifiableMap(itemNamesToIds);
  }

  /**
   * Retourne la liste des ID de blocs du level.dat donné.
   * 
   * @param levelDataPath le chemin vers le level.dat
   * @return la liste des ID
   * @throws IOException
   */
  private static NBTTagList getBlocksIdsList(String levelDataPath) throws IOException {
    NBTTagCompound levelData = CompressedStreamTools.readCompressed(new FileInputStream(levelDataPath + "level.dat"));
    return levelData.getCompoundTag("FML").getCompoundTag("Registries").getCompoundTag("minecraft:blocks").getTagList("ids", NBTBase.COMPOUND);
  }

  /**
   * Retourne la liste des ID d'items du level.dat donné.
   * 
   * @param levelDataPath le chemin vers le level.dat
   * @return la liste des ID
   * @throws IOException
   */
  private static NBTTagList getItemsIdsList(String levelDataPath) throws IOException {
    NBTTagCompound levelData = CompressedStreamTools.readCompressed(new FileInputStream(levelDataPath + "level.dat"));
    return levelData.getCompoundTag("FML").getCompoundTag("Registries").getCompoundTag("minecraft:items").getTagList("ids", NBTBase.COMPOUND);
  }

  /**
   * Crée la table des ID.
   * 
   * @param configPath le chemin du fichier de config
   * @param levelDataPath le chemin vers le level.dat
   * @return la table des ID
   * @throws IOException si une erreur de lecture est survenue
   * @throws ParseException si une erreur de syntaxe a été rencontrée
   */
  public static Map<BlockId, BlockId> getIdsTable(String configPath, String levelDataPath) throws IOException, ParseException {
    if (idsTable == null) {
      Map<String, Integer> namesToIds = getBlocksNamesToIdsTable(levelDataPath);
      Pattern digitalIdPattern = Pattern.compile("^(\\d+)(?:/(\\d+))?$");
      Pattern literalIdPattern = Pattern.compile("^(\\w+:\\w+)(?:/(\\d+))?$");
      Pattern fullPattern = Pattern.compile("^(\\d+|\\w+:\\w+)(/\\d+)?->((\\d+|\\w+:\\w+)(/\\d+)?|\\*)$");

      try (BufferedReader in = new BufferedReader(new FileReader(configPath))) {
        String line;
        int i = 1;

        while ((line = in.readLine()) != null) {
          if (fullPattern.matcher(line).matches()) {
            BlockId oldId = null, newId = null;
            boolean oldHasMeta = true, newHasMeta = true;
            String[] ids = line.split("->");

            Matcher leftDigitalIdM = digitalIdPattern.matcher(ids[0]);
            Matcher leftLiteralIdM = literalIdPattern.matcher(ids[0]);

            if (leftDigitalIdM.matches()) {
              int id = Integer.parseInt(leftDigitalIdM.group(1));

              if (leftDigitalIdM.group(2) != null)
                oldId = new BlockId(id, Integer.parseInt(leftDigitalIdM.group(2)));
              else {
                oldHasMeta = false;
                oldId = new BlockId(id, -1);
              }
            }
            else if (leftLiteralIdM.matches()) {
              String litId = leftLiteralIdM.group(1);
              Integer id = namesToIds.get(litId);

              if (id == null)
                throw new ParseException(String.format("ID inconnu '%s'", litId), i);

              if (leftLiteralIdM.group(2) != null)
                oldId = new BlockId(id, Integer.parseInt(leftLiteralIdM.group(2)));
              else {
                oldHasMeta = false;
                oldId = new BlockId(id, -1);
              }
            }

            if (ids[1].equals("*")) {
              newId = new BlockId(0, 0);
            }
            else {
              Matcher rightDigitalIdM = digitalIdPattern.matcher(ids[1]);
              Matcher rightLiteralIdM = literalIdPattern.matcher(ids[1]);

              if (rightDigitalIdM.matches()) {
                int id = Integer.parseInt(rightDigitalIdM.group(1));

                if (rightDigitalIdM.group(2) != null)
                  newId = new BlockId(id, Integer.parseInt(rightDigitalIdM.group(2)));
                else {
                  newHasMeta = false;
                  newId = new BlockId(id, -1);
                }
              }
              else if (rightLiteralIdM.matches()) {
                String litId = rightLiteralIdM.group(1);
                Integer id = namesToIds.get(litId);

                if (id == null)
                  throw new ParseException(String.format("ID inconnu '%s'", litId), i);

                if (rightLiteralIdM.group(2) != null)
                  newId = new BlockId(id, Integer.parseInt(rightLiteralIdM.group(2)));
                else {
                  newHasMeta = false;
                  newId = new BlockId(id, -1);
                }
              }
            }

            if (!oldHasMeta && newHasMeta) {
              for (int j = 0; j < 16; j++) {
                idsTable.put(new BlockId(oldId.getId(), j), newId);
              }
            }
            if (oldHasMeta && !newHasMeta) {
              throw new ParseException("Règle incohérente", i);
            }
            else if (!oldHasMeta && !newHasMeta) {
              for (int j = 0; j < 16; j++) {
                idsTable.put(new BlockId(oldId.getId(), j), new BlockId(newId.getId(), j));
              }
            }
            else {
              idsTable.put(oldId, newId);
            }
          }
          else {
            throw new ParseException("Erreur de syntaxe", i);
          }
          i++;
        }
      }
    }

    return Collections.unmodifiableMap(idsTable);
  }

  /**
   * Retourne l'ID à l'indice donné.
   * 
   * @param blocks le tableau des blocs
   * @param add le tableau des valeurs additonnelles
   * @param i l'indice
   * @return l'ID
   */
  public static int getId(byte[] blocks, byte[] add, int i) {
    return (extractHalfByte(add, i) << 8) | Byte.toUnsignedInt(blocks[i]);
  }

  /**
   * Retoure la valeur du demi-octet dans le tableau donné à l'indice i (dans le tableau blocs).
   * 
   * @param array le tableau
   * @param i l'indice du bloc
   * @return la valeur
   */
  public static int extractHalfByte(byte[] array, int i) {
    boolean even = i % 2 == 0;
    return Byte.toUnsignedInt((byte) ((array[i / 2] & (even ? 0x0f : 0xf0)) >>> (even ? 0 : 4)));
  }

  /**
   * Affiche les nouveaux ID.
   * 
   * @param levelDataPath le chemin vers le level.dat
   * @throws IOException
   */
  public static void displayIds(String levelDataPath) throws IOException {
    Map<Integer, String> ids = getBlocksIdsToNamesTable(levelDataPath);

    for (int id : new TreeSet<>(ids.keySet())) {
      System.out.println(id + "=" + ids.get(id));
    }
  }

  private Util() {}
}
