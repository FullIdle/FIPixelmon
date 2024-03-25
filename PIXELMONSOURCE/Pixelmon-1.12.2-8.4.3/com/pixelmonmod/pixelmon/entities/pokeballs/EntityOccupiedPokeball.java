package com.pixelmonmod.pixelmon.entities.pokeballs;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.PokeballImpactEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.BattleQuery;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.battles.rules.teamselection.TeamSelectionList;
import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleType;
import com.pixelmonmod.pixelmon.enums.forms.EnumSpecial;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class EntityOccupiedPokeball extends EntityPokeBall {
   private static final int MAX_LIFETIME = 400;
   String pokeName = null;

   public EntityOccupiedPokeball(World world) {
      super(world);
      this.field_70180_af.func_187227_b(dwMode, EnumPokeBallMode.full.ordinal());
   }

   public String getPokeNameUnlocalized() {
      PlayerPartyStorage party = Pixelmon.storageManager.getParty((EntityPlayerMP)this.field_70192_c);
      return this.getPokeUUID() != null && party.find(this.getPokeUUID()) != null ? "pixelmon." + party.find(this.getPokeUUID()).getSpecies().name.toLowerCase() + ".name" : "";
   }

   public EntityOccupiedPokeball(World world, EntityLivingBase entityliving, int slot, EnumPokeballs type) {
      super(type, world, entityliving, EnumPokeBallMode.full);
      this.field_70192_c = entityliving;
      this.setOwnerId(this.field_70192_c.func_110124_au());
      this.endRotationYaw = entityliving.field_70759_as;
      this.field_70180_af.func_187227_b(dwSlot, (byte)slot);
      this.setPokeUUID(Pixelmon.storageManager.getParty((EntityPlayerMP)entityliving).get(slot).getUUID());
      this.func_70012_b(entityliving.field_70165_t, entityliving.field_70163_u + (double)entityliving.func_70047_e(), entityliving.field_70161_v, entityliving.field_70177_z, entityliving.field_70125_A);
      this.field_70165_t -= (double)(MathHelper.func_76134_b(this.field_70177_z / 180.0F * 3.1415927F) * 0.16F);
      this.field_70163_u -= 0.10000000149011612;
      this.field_70161_v -= (double)(MathHelper.func_76126_a(this.field_70177_z / 180.0F * 3.1415927F) * 0.16F);
      this.func_70107_b(this.field_70165_t, this.field_70163_u, this.field_70161_v);
      this.field_70159_w = (double)(-MathHelper.func_76126_a(this.field_70177_z / 180.0F * 3.1415927F) * MathHelper.func_76134_b(this.field_70125_A / 180.0F * 3.1415927F)) * 0.8;
      this.field_70179_y = (double)(MathHelper.func_76134_b(this.field_70177_z / 180.0F * 3.1415927F) * MathHelper.func_76134_b(this.field_70125_A / 180.0F * 3.1415927F)) * 0.8;
      this.field_70181_x = (double)(-MathHelper.func_76126_a(this.field_70125_A / 180.0F * 3.1415927F)) * 0.8;
      this.setInitialYaw(this.field_70192_c.field_70177_z);
      this.setInitialPitch(this.field_70192_c.field_70125_A);
   }

   public void func_70071_h_() {
      super.func_70071_h_();
      if (!this.field_70170_p.field_72995_K && (this.field_70173_aa > 400 || this.field_70163_u < 0.0)) {
         this.func_70106_y();
      }

   }

   public String func_70005_c_() {
      if (this.pokeName == null && this.field_70192_c instanceof EntityPlayerMP) {
         PlayerPartyStorage party = Pixelmon.storageManager.getParty((EntityPlayerMP)this.field_70192_c);
         Pokemon pokemon = party.find(this.getPokeUUID());
         if (pokemon != null) {
            this.pokeName = I18n.func_74838_a("pixelmon." + pokemon.getSpecies().name.toLowerCase() + ".name");
         }
      }

      return this.pokeName + " " + super.func_70005_c_();
   }

   protected void func_70184_a(RayTraceResult traceResult) {
      if (!this.field_70170_p.field_72995_K && this.field_70181_x <= 0.0) {
         if (traceResult.field_72313_a == Type.BLOCK) {
            BlockPos pos = traceResult.func_178782_a();
            IBlockState state = this.field_70170_p.func_180495_p(pos);
            if (!state.isSideSolid(this.field_70170_p, pos, traceResult.field_178784_b)) {
               AxisAlignedBB bounds = state.func_185890_d(this.field_70170_p, pos);
               if (bounds == null) {
                  return;
               }
            }
         }

         if (Pixelmon.EVENT_BUS.post(new PokeballImpactEvent(this, traceResult))) {
            return;
         }

         if (traceResult.field_72313_a != Type.BLOCK) {
            EntityPixelmon pokemonHit = null;
            if (traceResult.field_72308_g != null && traceResult.field_72308_g instanceof EntityPixelmon) {
               pokemonHit = (EntityPixelmon)traceResult.field_72308_g;
            }

            if (pokemonHit != null && pokemonHit.func_70902_q() == this.field_70192_c && pokemonHit.func_184207_aI()) {
               return;
            }

            if (traceResult.field_72308_g == this.field_70192_c) {
               return;
            }

            UUID throwerUUID = this.getOwnerId();
            if (throwerUUID == null || FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_177451_a(throwerUUID) == null) {
               this.func_70106_y();
               return;
            }

            PlayerPartyStorage storage = Pixelmon.storageManager.getParty((EntityPlayerMP)this.field_70192_c);
            Pokemon pokemon = storage.get((Byte)this.field_70180_af.func_187225_a(dwSlot));
            if (pokemon == null) {
               this.func_70106_y();
               return;
            }

            if (pokemon.getSpecies() == EnumSpecies.Wobbuffet && UUID.fromString("2f723150-24de-44ff-aeee-87c75f7c7a9e").equals(pokemon.getOwnerPlayerUUID())) {
               pokemon.setForm(EnumSpecial.Online);
            }

            EntityPixelmon pixelmon;
            if (pokemonHit != null && !Objects.equals(pokemonHit.func_184753_b(), throwerUUID)) {
               if (pokemonHit.battleController != null) {
                  if (pokemonHit.battleController.checkValid()) {
                     ChatHandler.sendChat(this.field_70192_c, "pixelmon.pokeballs.inbattle");
                     this.func_70106_y();
                     return;
                  }

                  if (pokemonHit.getTrainer() != null) {
                     pokemonHit.func_70106_y();
                     this.func_70106_y();
                     return;
                  }
               }

               if (pokemonHit.hitByPokeball != null) {
                  this.func_70106_y();
                  return;
               }

               if (pokemonHit.hasOwner()) {
                  PlayerPartyStorage targetStorage = Pixelmon.storageManager.getParty(pokemonHit.func_184753_b());
                  if (pokemonHit.func_70902_q() == null) {
                     this.func_70106_y();
                     return;
                  }

                  if (!targetStorage.battleEnabled) {
                     this.func_70106_y();
                     return;
                  }

                  if (pokemonHit.blockOwner != null) {
                     this.func_70106_y();
                     return;
                  }

                  if (targetStorage.guiOpened) {
                     ChatHandler.sendChat(this.field_70192_c, "pixelmon.general.playerbusy");
                     this.func_70106_y();
                     return;
                  }

                  pixelmon = pokemon.getOrSpawnPixelmon(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70177_z, 0.0F);
                  if (pixelmon != null) {
                     pixelmon.onSendout();
                     new BattleQuery((EntityPlayerMP)this.field_70192_c, pixelmon, (EntityPlayerMP)pokemonHit.func_70902_q(), pokemonHit);
                  }
               } else {
                  if (BattleRegistry.getBattle((EntityPlayer)this.field_70192_c) != null) {
                     this.func_70106_y();
                     return;
                  }

                  BattleParticipant part = new WildPixelmonParticipant(new EntityPixelmon[]{pokemonHit});
                  pixelmon = pokemon.getOrSpawnPixelmon(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70177_z, 0.0F);
                  if (pixelmon != null) {
                     pixelmon.onSendout();
                     pixelmon.startBattle(new PlayerParticipant((EntityPlayerMP)this.field_70192_c, new EntityPixelmon[]{pixelmon}), part);
                  }

                  this.func_70106_y();
               }
            } else {
               EntityPixelmon pixelmon;
               if (traceResult.field_72308_g instanceof NPCTrainer) {
                  NPCTrainer trainerEntity = (NPCTrainer)traceResult.field_72308_g;
                  BattleControllerBase bc = trainerEntity.getBattleController();
                  if (bc != null) {
                     if (!bc.battleEnded) {
                        this.func_70106_y();
                        return;
                     }

                     bc.endBattle(EnumBattleEndCause.FORCE);
                  }

                  EntityPlayerMP throwerPlayer = (EntityPlayerMP)this.field_70192_c;
                  if (!trainerEntity.canStartBattle(throwerPlayer, true)) {
                     return;
                  }

                  EnumBattleType battleType = trainerEntity.getBattleType();
                  if (trainerEntity.battleRules.isDefault()) {
                     pixelmon = pokemon.getOrSpawnPixelmon(this.field_70170_p, this.field_70165_t, this.field_70163_u, this.field_70161_v, this.field_70177_z, 0.0F);
                     if (pixelmon != null) {
                        pixelmon.onSendout();
                        TrainerParticipant trainer = new TrainerParticipant(trainerEntity, (EntityPlayer)this.field_70192_c, battleType.numPokemon);
                        PlayerParticipant playerPart;
                        if (battleType == EnumBattleType.Single) {
                           playerPart = new PlayerParticipant(throwerPlayer, new EntityPixelmon[]{pixelmon});
                        } else {
                           List list = new ArrayList();
                           list.add(pokemon);
                           list.addAll((Collection)storage.getTeam().stream().filter((p) -> {
                              return p.getHealth() > 0 && p != pokemon;
                           }).collect(Collectors.toList()));
                           playerPart = new PlayerParticipant(throwerPlayer, list, 2);
                        }

                        pixelmon.startBattle(playerPart, trainer, trainerEntity.battleRules);
                     }
                  } else {
                     TeamSelectionList.addTeamSelection(trainerEntity.battleRules, true, trainerEntity.getPokemonStorage(), Pixelmon.storageManager.getParty(throwerPlayer));
                  }

                  this.func_70106_y();
                  return;
               }

               if (PixelmonConfig.pokeBallPlayerEngage && traceResult.field_72308_g != null && traceResult.field_72308_g instanceof EntityPlayerMP && traceResult.field_72308_g != this.field_70192_c) {
                  EntityPlayerMP thrower = (EntityPlayerMP)this.func_85052_h();
                  EntityPlayerMP enemy = (EntityPlayerMP)traceResult.field_72308_g;
                  pixelmon = storage.getAndSendOutFirstAblePokemon(thrower);
                  PlayerPartyStorage enemyParty = Pixelmon.storageManager.getParty(enemy);
                  pixelmon = enemyParty.getAndSendOutFirstAblePokemon(enemy);
                  if (enemyParty.guiOpened) {
                     ChatHandler.sendChat(thrower, "pixelmon.general.playerbusy");
                     return;
                  }

                  if (pixelmon == null) {
                     ChatHandler.sendChat(thrower, "pixelmon.command.battle.nopokemon", enemy.getDisplayNameString());
                  } else {
                     new BattleQuery(thrower, pixelmon, enemy, pixelmon);
                  }
               }
            }
         }

         if (this.getIsWaiting()) {
            this.field_70181_x = 0.0;
            this.field_70159_w = 0.0;
            this.field_70179_y = 0.0;
            this.func_70106_y();
            this.setIsOnGround(true);
         } else {
            this.setAnimation(AnimationType.BOUNCEOPEN);
            this.setIsWaiting(true);
            this.field_70181_x = 0.0;
            this.field_70159_w = 0.0;
            this.field_70179_y = 0.0;
            this.field_70125_A = 0.0F;
         }
      }

   }
}
