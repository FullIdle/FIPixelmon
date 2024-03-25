package com.pixelmonmod.pixelmon.client.render.tileEntities;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityTradeMachine;
import com.pixelmonmod.pixelmon.client.models.pokeballs.ModelPokeballs;
import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.client.render.GenericModelHolder;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.pokedex.Pokedex;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class RenderTileEntityTradingMachine extends TileEntityRenderer {
   private static final ResourceLocation bracket = new ResourceLocation("pixelmon", "textures/blocks/trademachine/Bracket.png");
   private static final ResourceLocation placeholder = new ResourceLocation("pixelmon", "textures/blocks/placeholder.png");
   private static final GenericModelHolder model = new GenericModelHolder("blocks/trade_machine/trader.pqc");
   private static final GenericModelHolder arrows = new GenericModelHolder("blocks/trade_machine/arrows.pqc");
   private static final GenericModelHolder poke1Poke = new GenericModelHolder("blocks/trade_machine/poke1poke.pqc");
   private static final GenericModelHolder poke1Bracket = new GenericModelHolder("blocks/trade_machine/poke1bracket.pqc");
   private static final GenericModelHolder poke1Player = new GenericModelHolder("blocks/trade_machine/poke1player.pqc");
   private static final GenericModelHolder poke1HeldItem = new GenericModelHolder("blocks/trade_machine/poke1helditem.pqc");
   private static final GenericModelHolder poke1Ready = new GenericModelHolder("blocks/trade_machine/poke1ready.pqc");
   private static final GenericModelHolder poke2Poke = new GenericModelHolder("blocks/trade_machine/poke2poke.pqc");
   private static final GenericModelHolder poke2Bracket = new GenericModelHolder("blocks/trade_machine/poke2bracket.pqc");
   private static final GenericModelHolder poke2Player = new GenericModelHolder("blocks/trade_machine/poke2player.pqc");
   private static final GenericModelHolder poke2HeldItem = new GenericModelHolder("blocks/trade_machine/poke2helditem.pqc");
   private static final GenericModelHolder poke2Ready = new GenericModelHolder("blocks/trade_machine/poke2ready.pqc");

   public RenderTileEntityTradingMachine() {
      this.correctionAngles = 180;
   }

   public void renderTileEntity(TileEntityTradeMachine tradeMachine, IBlockState state, double x, double y, double z, float partialTicks, int destroyStage) {
      this.func_147499_a(tradeMachine.getTexture());
      RenderTileEntityTradingMachine.model.render();
      this.func_147499_a(bracket);
      arrows.render();
      poke1Bracket.render();
      poke2Bracket.render();
      SkinManager skinManager = Minecraft.func_71410_x().func_152342_ad();
      if (tradeMachine.user1 != null && !tradeMachine.user1.isEmpty() || tradeMachine.user2 != null && !tradeMachine.user2.isEmpty()) {
         String[] users = new String[]{tradeMachine.user1, tradeMachine.user2};

         for(int i = 0; i < 2; ++i) {
            if (users[i] != null && !users[i].isEmpty()) {
               String username = users[i];
               EntityPlayer player = this.func_178459_a().func_72924_a(username);
               if (player != null) {
                  Map map = skinManager.func_152788_a(player.func_146103_bH());
                  ResourceLocation resourcelocation;
                  if (map.containsKey(Type.SKIN)) {
                     resourcelocation = skinManager.func_152792_a((MinecraftProfileTexture)map.get(Type.SKIN), Type.SKIN);
                  } else {
                     UUID uuid = EntityPlayer.func_146094_a(player.func_146103_bH());
                     resourcelocation = DefaultPlayerSkin.func_177334_a(uuid);
                  }

                  this.func_147499_a(resourcelocation);
               } else {
                  this.func_147499_a(placeholder);
               }

               (i == 0 ? poke1Player : poke2Player).render();
            } else {
               this.func_147499_a(placeholder);
            }

            (i == 0 ? poke1Player : poke2Player).render();
         }

         NBTTagCompound[] pokemon = new NBTTagCompound[]{tradeMachine.poke1, tradeMachine.poke2};

         for(int i = 0; i < 2; ++i) {
            if (pokemon[i] != null) {
               String name = pokemon[i].func_74779_i("Name");
               boolean isEgg = pokemon[i].func_74767_n("isEgg");
               int eggCycles = pokemon[i].func_74762_e("eggCycles");
               int variant = pokemon[i].func_74762_e("Variant");
               boolean isShiny = pokemon[i].func_74767_n("IsShiny");
               int pokeball = pokemon[i].func_74762_e("CaughtBall");
               int number = Pokedex.nameToID(name);
               EnumPokeballs pokeballs = EnumPokeballs.getFromIndex(pokeball);
               this.func_147499_a(pokeballs.getTextureLocation());
               GlStateManager.func_179121_F();
               GlStateManager.func_179094_E();
               int rotation = this.getRotation(state);
               if (i == 0) {
                  if (rotation == 270) {
                     GlStateManager.func_179137_b(x + 0.5, y + 0.93, z + 0.4);
                  } else if (rotation == 180) {
                     GlStateManager.func_179137_b(x + 0.6, y + 0.93, z + 0.5);
                  } else if (rotation == 90) {
                     GlStateManager.func_179137_b(x + 0.5, y + 0.93, z + 0.6);
                  } else {
                     GlStateManager.func_179137_b(x + 0.4, y + 0.93, z + 0.5);
                  }
               } else if (rotation == 270) {
                  GlStateManager.func_179137_b(x + 0.5, y + 0.93, z + 2.2 + 0.4);
               } else if (rotation == 180) {
                  GlStateManager.func_179137_b(x - 2.2 + 0.6, y + 0.93, z + 0.5);
               } else if (rotation == 90) {
                  GlStateManager.func_179137_b(x + 0.5, y + 0.93, z - 2.2 + 0.6);
               } else {
                  GlStateManager.func_179137_b(x + 2.2 + 0.4, y + 0.93, z + 0.5);
               }

               GlStateManager.func_179114_b((float)this.getRotation(state), 0.0F, 1.0F, 0.0F);
               GenericModelHolder model = SharedModels.getPokeballModel(pokeballs);
               ((ModelPokeballs)model.getModel()).theModel.setAnimation(AnimationType.IDLE);
               ((ModelPokeballs)model.getModel()).theModel.animate();
               model.render(0.0022F);
               GlStateManager.func_179121_F();
               GlStateManager.func_179094_E();
               GlStateManager.func_179109_b((float)x + 0.5F, (float)y + this.yOffset, (float)z + 0.5F);
               GlStateManager.func_179114_b((float)(this.getRotation(state) + this.correctionAngles), 0.0F, 1.0F, 0.0F);
               GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
               if (pokemon[i].func_74764_b("HeldItemStack")) {
                  ItemStack heldItem = new ItemStack(pokemon[i].func_74775_l("HeldItemStack"));
                  if (heldItem.func_190926_b()) {
                     this.func_147499_a(placeholder);
                     (i == 0 ? poke1HeldItem : poke2HeldItem).render();
                  } else {
                     this.func_147499_a(new ResourceLocation("pixelmon", "textures/helditem.png"));
                     (i == 0 ? poke1HeldItem : poke2HeldItem).render();
                  }
               } else {
                  this.func_147499_a(placeholder);
                  (i == 0 ? poke1HeldItem : poke2HeldItem).render();
               }

               this.func_147499_a(new ResourceLocation("pixelmon", this.getSpriteFromID(number, name, isShiny, isEgg, eggCycles, variant)));
               (i == 0 ? poke1Poke : poke2Poke).render();
            }
         }

         if (tradeMachine.ready1) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/trademachine/Ready.png"));
            poke1Ready.render();
         } else {
            this.func_147499_a(placeholder);
            poke1Ready.render();
         }

         if (tradeMachine.ready2) {
            this.func_147499_a(new ResourceLocation("pixelmon", "textures/blocks/trademachine/Ready.png"));
            poke2Ready.render();
         } else {
            this.func_147499_a(placeholder);
            poke2Ready.render();
         }
      }

   }

   private String getSpriteFromID(int nationalPokedexNumber, String pokemonName, boolean isShiny, boolean isEgg, int eggCycles, int variant) {
      String basePath = "textures/sprites/pokemon/";
      if (isShiny) {
         basePath = "textures/sprites/shinypokemon/";
      } else if (isEgg) {
         basePath = "textures/sprites/eggs/";
      }

      if (isEgg) {
         String eggType = "egg";
         if (nationalPokedexNumber == 175) {
            eggType = "togepi";
         } else if (nationalPokedexNumber == 490) {
            eggType = "manaphy";
         }

         if (eggCycles > 10) {
            return basePath + eggType + "1.png";
         } else {
            return eggCycles > 5 ? basePath + eggType + "2.png" : basePath + eggType + "3.png";
         }
      } else {
         Optional optional = EnumSpecies.getFromName(pokemonName);
         if (optional.isPresent()) {
            EnumSpecies pokemon = (EnumSpecies)optional.get();
            return basePath + pokemon.getNationalPokedexNumber() + pokemon.getFormEnum(variant).getSpriteSuffix(isShiny) + ".png";
         } else {
            return basePath + String.format("%03d", nationalPokedexNumber) + ".png";
         }
      }
   }
}
