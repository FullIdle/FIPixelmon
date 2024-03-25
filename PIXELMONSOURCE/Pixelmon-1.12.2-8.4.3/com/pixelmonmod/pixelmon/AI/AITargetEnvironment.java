package com.pixelmonmod.pixelmon.AI;

import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumType;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class AITargetEnvironment extends EntityAIBase {
   public World world;
   public EntityPixelmon pickUpPixelmon;
   private static Biome OCEAN;
   private static Biome RIVER;
   private static Biome SWAMPLAND;
   private static Biome FROZEN_RIVER;
   private static Biome PLAINS;
   private static Biome FOREST;
   private static Biome FOREST_HILLS;
   private static Biome JUNGLE;
   private static Biome JUNGLE_HILLS;

   public AITargetEnvironment(EntityPixelmon entity) {
      this.world = Minecraft.func_71410_x().field_71441_e;
      this.pickUpPixelmon = entity;
   }

   public boolean func_75250_a() {
      if (this.pickUpPixelmon.hasOwner() && this.isCorrectBiomeForType()) {
         this.shouldSearch();
      }

      return true;
   }

   public boolean isCorrectBiomeForType() {
      Biome biome = this.world.getBiomeForCoordsBody(this.pickUpPixelmon.func_180425_c());
      boolean boolIsCorrect = false;
      Iterator var3 = this.pickUpPixelmon.getBaseStats().types.iterator();

      while(true) {
         label88:
         do {
            while(var3.hasNext()) {
               EnumType type = (EnumType)var3.next();
               if (type == EnumType.Water) {
                  continue label88;
               }

               if (type != EnumType.Normal && type != EnumType.Fire && type != EnumType.Electric) {
                  if (type == EnumType.Grass) {
                     if (biome == PLAINS || biome == FOREST || biome == FOREST_HILLS || biome == JUNGLE || biome == JUNGLE_HILLS) {
                        boolIsCorrect = true;
                     }
                  } else if (type != EnumType.Poison && type != EnumType.Flying && type != EnumType.Ground && type != EnumType.Bug && type != EnumType.Psychic && type != EnumType.Rock && type != EnumType.Fighting && type != EnumType.Dark && type != EnumType.Steel && type != EnumType.Ghost && type == EnumType.Dragon) {
                  }
               }
            }

            return boolIsCorrect;
         } while(biome != OCEAN && biome != RIVER && biome != SWAMPLAND && biome != FROZEN_RIVER);

         boolIsCorrect = true;
      }
   }

   public boolean shouldSearch() {
      int intSuccessChances = 1;
      int intTotalChances = 10;
      int rand = FMLCommonHandler.instance().getMinecraftServerInstance().func_71218_a(0).field_73012_v.nextInt(intTotalChances);
      this.sendPlayerMessage("aitargetenvironment.seessomething");
      return rand <= intSuccessChances;
   }

   public void sendPlayerMessage(String message) {
      EntityPlayerMP player = (EntityPlayerMP)this.pickUpPixelmon.func_70902_q();
      ChatHandler.sendChat(player, message);
   }

   static {
      OCEAN = (Biome)Biome.field_185377_q.func_82594_a(new ResourceLocation("ocean"));
      RIVER = (Biome)Biome.field_185377_q.func_82594_a(new ResourceLocation("river"));
      SWAMPLAND = (Biome)Biome.field_185377_q.func_82594_a(new ResourceLocation("swampland"));
      FROZEN_RIVER = (Biome)Biome.field_185377_q.func_82594_a(new ResourceLocation("frozen_river"));
      PLAINS = (Biome)Biome.field_185377_q.func_82594_a(new ResourceLocation("plains"));
      FOREST = (Biome)Biome.field_185377_q.func_82594_a(new ResourceLocation("forest"));
      FOREST_HILLS = (Biome)Biome.field_185377_q.func_82594_a(new ResourceLocation("forest_hills"));
      JUNGLE = (Biome)Biome.field_185377_q.func_82594_a(new ResourceLocation("jungle"));
      JUNGLE_HILLS = (Biome)Biome.field_185377_q.func_82594_a(new ResourceLocation("jungle_hills"));
   }
}
