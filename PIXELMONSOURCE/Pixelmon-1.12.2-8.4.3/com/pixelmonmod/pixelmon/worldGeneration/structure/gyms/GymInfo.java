package com.pixelmonmod.pixelmon.worldGeneration.structure.gyms;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.entities.npcs.registry.BaseShopItem;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ServerNPCRegistry;
import com.pixelmonmod.pixelmon.enums.EnumNPCType;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.worldGeneration.structure.StructureInfo;
import com.pixelmonmod.pixelmon.worldGeneration.structure.towns.NPCPlacementInfo;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import net.minecraft.util.JsonUtils;

public class GymInfo extends StructureInfo {
   public ArrayList pokemon = new ArrayList();
   public String name;
   public int level;
   public EnumType[] type;

   public void setGymInfo(String name, InputStream resourceAsStream) throws Exception {
      this.name = name;

      try {
         JsonObject json = (new JsonParser()).parse(new InputStreamReader(resourceAsStream, StandardCharsets.UTF_8)).getAsJsonObject();
         JsonArray jsonarray;
         int i;
         if (json.has("type")) {
            jsonarray = JsonUtils.func_151214_t(json, "type");
            this.type = new EnumType[jsonarray.size()];

            for(i = 0; i < jsonarray.size(); ++i) {
               this.type[i] = EnumType.parseType(jsonarray.get(i).getAsString());
            }
         }

         JsonObject npcel;
         if (json.has("pokemon")) {
            jsonarray = JsonUtils.func_151214_t(json, "pokemon");

            for(i = 0; i < jsonarray.size(); ++i) {
               npcel = jsonarray.get(i).getAsJsonObject();
               this.pokemon.add(PokemonDefinition.readPokemonDefinition(name, npcel));
            }
         }

         if (json.has("npcs")) {
            jsonarray = JsonUtils.func_151214_t(json, "npcs");

            for(i = 0; i < jsonarray.size(); ++i) {
               npcel = jsonarray.get(i).getAsJsonObject();
               String npcname = npcel.get("name").getAsString();
               EnumNPCType type = EnumNPCType.getFromString(npcel.get("type").getAsString());
               int x = npcel.get("x").getAsInt();
               int y = npcel.get("y").getAsInt();
               int z = npcel.get("z").getAsInt();
               NPCPlacementInfo info = null;
               int tier = -1;
               if (npcel.has("tier")) {
                  tier = npcel.get("tier").getAsInt();
               }

               if (npcel.has("rotation")) {
                  info = new NPCPlacementInfo(npcname, type, x, y, z, npcel.get("rotation").getAsInt(), tier);
               } else if (tier != -1) {
                  info = new NPCPlacementInfo(npcname, type, x, y, z, 0, tier);
               } else {
                  info = new NPCPlacementInfo(npcname, type, x, y, z);
               }

               if (npcel.has("drops")) {
                  JsonArray dropsArray = npcel.get("drops").getAsJsonArray();

                  for(int j = 0; j < dropsArray.size(); ++j) {
                     String itemName = dropsArray.get(j).getAsString();
                     BaseShopItem itembase = ServerNPCRegistry.shopkeepers.getItem(itemName);
                     if (itembase != null && itembase.getItem() != null) {
                        info.addDrop(itembase.getItem());
                     } else {
                        Pixelmon.LOGGER.warn("Item " + itemName + " could not be found in Gym " + name + " drops.");
                     }
                  }
               }

               this.npcs.add(info);
            }
         }

      } catch (Exception var18) {
         throw new Exception("Failed to load Gym NPC data: " + name, var18);
      }
   }
}
