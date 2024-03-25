package com.pixelmonmod.pixelmon.entities.pixelmon.moveSkills;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.entities.pixelmon.helpers.EvolutionQuery;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumAegislash;
import com.pixelmonmod.pixelmon.enums.forms.EnumDarmanitan;
import com.pixelmonmod.pixelmon.enums.forms.EnumEiscue;
import com.pixelmonmod.pixelmon.enums.forms.EnumGreninja;
import com.pixelmonmod.pixelmon.enums.forms.EnumHeroDuo;
import com.pixelmonmod.pixelmon.enums.forms.EnumMeloetta;
import com.pixelmonmod.pixelmon.enums.forms.EnumMimikyu;
import com.pixelmonmod.pixelmon.enums.forms.EnumMorpeko;
import com.pixelmonmod.pixelmon.enums.forms.EnumNecrozma;
import com.pixelmonmod.pixelmon.enums.forms.EnumPrimal;
import com.pixelmonmod.pixelmon.enums.forms.EnumWishiwashi;
import com.pixelmonmod.pixelmon.enums.forms.EnumXerneas;
import com.pixelmonmod.pixelmon.enums.forms.EnumZygarde;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.heldItems.ItemMegaStone;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;

public class MegaEvolution {
   private static Map formMap = new HashMap();

   public static MoveSkill createMegaMoveSkill() {
      MoveSkill moveSkill = (new MoveSkill("mega_evolve")).setName("pixelmon.moveskill.mega_evolve.name").describe("pixelmon.moveskill.mega_evolve.description").setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/mega.png")).setDefaultCooldownTicks(20).setUsePP(false).setAbleSpecs(Lists.newArrayList(new PokemonSpec[]{new PokemonSpec("canmegaevo")}));
      return moveSkill.setBehaviourNoTarget((pixelmon) -> {
         if (pixelmon.func_70902_q() != null && !pixelmon.getFormEnum().isTemporary()) {
            PlayerPartyStorage party = pixelmon.getPlayerParty();
            if (party == null) {
               return -1;
            }

            if (pixelmon.getSpecies() == EnumSpecies.Greninja) {
               IEnumForm formEnum = pixelmon.getFormEnum();
               if (formEnum == EnumGreninja.BATTLE_BOND) {
                  pixelmon.setPriorForm(pixelmon.getFormEnum());
                  new EvolutionQuery(pixelmon, EnumGreninja.ASH.getForm());
               } else if (formEnum == EnumGreninja.ZOMBIE_BATTLE_BOND) {
                  pixelmon.setPriorForm(pixelmon.getFormEnum());
                  new EvolutionQuery(pixelmon, EnumGreninja.ASH_ZOMBIE.getForm());
               } else if (formEnum == EnumGreninja.ALTER_BATTLE_BOND) {
                  pixelmon.setPriorForm(pixelmon.getFormEnum());
                  new EvolutionQuery(pixelmon, EnumGreninja.ASH_ALTER.getForm());
               }
            } else if (pixelmon.getSpecies() == EnumSpecies.Rayquaza && EnumSpecies.Rayquaza.hasMega()) {
               pixelmon.setPriorForm(pixelmon.getFormEnum());
               new EvolutionQuery(pixelmon, 1);
            } else {
               ItemHeld held = pixelmon.getPokemonData().getHeldItemAsItemHeld();
               if (!(held instanceof ItemMegaStone) || ((ItemMegaStone)held).pokemon != pixelmon.getSpecies()) {
                  return -1;
               }

               ItemMegaStone stone = (ItemMegaStone)held;
               if (!party.getMegaItem().canEvolve()) {
                  ChatHandler.sendChat(pixelmon.func_70902_q(), "pixelmon.moveskill.mega_evolve.noring");
                  return -1;
               }

               pixelmon.setPriorForm(pixelmon.getFormEnum());
               int form = pixelmon.getStoragePokemonData().getForm();
               if (!stone.isFormAllowed(form) || stone.getForm(form) == form) {
                  return -1;
               }

               new EvolutionQuery(pixelmon, stone.getForm(form));
            }
         }

         return moveSkill.cooldownTicks;
      });
   }

   public static MoveSkill createPrimalMoveSkill() {
      MoveSkill moveSkill = (new MoveSkill("primal_reversion")).setName("pixelmon.moveskill.primal_reversion.name").describe("pixelmon.moveskill.primal_reversion.description").setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/mega.png")).setDefaultCooldownTicks(20).setUsePP(false).setAbleSpecs(Lists.newArrayList(new PokemonSpec[]{new PokemonSpec("canprimalrevert")}));
      return moveSkill.setBehaviourNoTarget((pixelmon) -> {
         if (pixelmon.func_70902_q() != null && !pixelmon.getFormEnum().isTemporary()) {
            PlayerPartyStorage party = pixelmon.getPlayerParty();
            if (party == null) {
               return -1;
            }

            if (pixelmon.getSpecies() == EnumSpecies.Kyogre && pixelmon.getPokemonData().getHeldItemAsItemHeld() == PixelmonItemsHeld.blueOrb || pixelmon.getSpecies() == EnumSpecies.Groudon && pixelmon.getPokemonData().getHeldItemAsItemHeld() == PixelmonItemsHeld.redOrb) {
               pixelmon.setPriorForm(pixelmon.getFormEnum());
               new EvolutionQuery(pixelmon, EnumPrimal.PRIMAL.getForm());
            }
         }

         return moveSkill.cooldownTicks;
      });
   }

   public static MoveSkill createUltraMoveSkill() {
      MoveSkill moveSkill = (new MoveSkill("ultra_burst")).setName("pixelmon.moveskill.ultra_burst.name").describe("pixelmon.moveskill.ultra_burst.description").setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/mega.png")).setDefaultCooldownTicks(20).setUsePP(false).setAbleSpecs(Lists.newArrayList(new PokemonSpec[]{new PokemonSpec("canultraburst")}));
      return moveSkill.setBehaviourNoTarget((pixelmon) -> {
         if (pixelmon.func_70902_q() != null && !pixelmon.getFormEnum().isTemporary()) {
            PlayerPartyStorage party = pixelmon.getPlayerParty();
            if (party == null) {
               return -1;
            }

            if (pixelmon.getSpecies() == EnumSpecies.Necrozma && (pixelmon.getFormEnum() == EnumNecrozma.DAWN || pixelmon.getFormEnum() == EnumNecrozma.DUSK) && pixelmon.getPokemonData().getHeldItemAsItemHeld() == PixelmonItemsHeld.ultranecrozium_z) {
               pixelmon.getPokemonData().getPersistentData().func_74768_a("SrcForm", pixelmon.getPokemonData().getForm());
               pixelmon.setPriorForm(pixelmon.getFormEnum());
               new EvolutionQuery(pixelmon, EnumNecrozma.ULTRA.getForm());
            }
         }

         return moveSkill.cooldownTicks;
      });
   }

   public static MoveSkill createCrownedMoveSkill() {
      MoveSkill moveSkill = (new MoveSkill("crowned")).setName("pixelmon.moveskill.crowned.name").describe("pixelmon.moveskill.crowned.description").setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/mega.png")).setDefaultCooldownTicks(20).setUsePP(false).setAbleSpecs(Lists.newArrayList(new PokemonSpec[]{new PokemonSpec("cancrowned")}));
      return moveSkill.setBehaviourNoTarget((pixelmon) -> {
         if (pixelmon.func_70902_q() != null && !pixelmon.getFormEnum().isTemporary()) {
            PlayerPartyStorage party = pixelmon.getPlayerParty();
            if (party == null) {
               return -1;
            }

            if (pixelmon.getFormEnum() == EnumHeroDuo.HERO && (pixelmon.getSpecies() == EnumSpecies.Zacian && pixelmon.getPokemonData().getHeldItemAsItemHeld() == PixelmonItemsHeld.rustedSword || pixelmon.getSpecies() == EnumSpecies.Zamazenta && pixelmon.getPokemonData().getHeldItemAsItemHeld() == PixelmonItemsHeld.rustedShield)) {
               pixelmon.setPriorForm(pixelmon.getFormEnum());
               pixelmon.setForm(EnumHeroDuo.CROWNED);
               pixelmon.field_70170_p.func_184133_a((EntityPlayer)null, pixelmon.func_180425_c(), SoundEvents.field_187716_o, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
         }

         return moveSkill.cooldownTicks;
      });
   }

   public static MoveSkill createChangeForm() {
      MoveSkill moveSkill = (new MoveSkill("change_form")).setName("pixelmon.moveskill.change_form.name").describe("pixelmon.moveskill.change_form.description").setIcon(new ResourceLocation("pixelmon", "textures/gui/overlay/externalMoves/mega.png")).setDefaultCooldownTicks(20).setUsePP(false).setAbleSpecs(Lists.newArrayList(formMap.keySet()));
      return moveSkill.setBehaviourNoTarget((pixelmon) -> {
         if (pixelmon.func_70902_q() != null && !pixelmon.getFormEnum().isTemporary()) {
            PlayerPartyStorage party = pixelmon.getPlayerParty();
            if (party == null) {
               return -1;
            }

            Iterator var3 = formMap.entrySet().iterator();

            while(var3.hasNext()) {
               Map.Entry spec = (Map.Entry)var3.next();
               if (((PokemonSpec)spec.getKey()).matches(pixelmon)) {
                  pixelmon.setPriorForm(pixelmon.getFormEnum());
                  new EvolutionQuery(pixelmon, ((IEnumForm)spec.getValue()).getForm());
               }
            }
         }

         return moveSkill.cooldownTicks;
      });
   }

   static {
      formMap.put(new PokemonSpec("Aegislash f:0"), EnumAegislash.BLADE);
      formMap.put(new PokemonSpec("Aegislash f:2"), EnumAegislash.BLADE_ALTER);
      formMap.put(new PokemonSpec("Darmanitan f:0"), EnumDarmanitan.ZEN);
      formMap.put(new PokemonSpec("Darmanitan f:2"), EnumDarmanitan.GALAR_ZEN);
      formMap.put(new PokemonSpec("Eiscue f:0"), EnumEiscue.NOICE_FACE);
      formMap.put(new PokemonSpec("Greninja f:1"), EnumGreninja.ASH);
      formMap.put(new PokemonSpec("Greninja f:3"), EnumGreninja.ASH_ZOMBIE);
      formMap.put(new PokemonSpec("Greninja f:6"), EnumGreninja.ASH_ALTER);
      formMap.put(new PokemonSpec("Meloetta f:0"), EnumMeloetta.PIROUETTE);
      formMap.put(new PokemonSpec("Mimikyu f:0"), EnumMimikyu.Busted);
      formMap.put(new PokemonSpec("Morpeko f:0"), EnumMorpeko.HANGRY);
      formMap.put(new PokemonSpec("Wishiwashi f:0"), EnumWishiwashi.SCHOOL);
      formMap.put(new PokemonSpec("Xerneas f:0"), EnumXerneas.ACTIVE);
      formMap.put(new PokemonSpec("Xerneas f:2"), EnumXerneas.ACTIVE_CREATOR);
      formMap.put(new PokemonSpec("Zygarde f:0 ability:PowerConstruct"), EnumZygarde.COMPLETE);
      formMap.put(new PokemonSpec("Zygarde f:1 ability:PowerConstruct"), EnumZygarde.COMPLETE);
   }
}
