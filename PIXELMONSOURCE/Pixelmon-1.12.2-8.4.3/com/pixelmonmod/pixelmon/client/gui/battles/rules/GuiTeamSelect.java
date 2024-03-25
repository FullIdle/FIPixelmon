package com.pixelmonmod.pixelmon.client.gui.battles.rules;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClause;
import com.pixelmonmod.pixelmon.battles.rules.teamselection.TeamSelectPokemon;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.battles.ClientBattleManager;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiVersus;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiRoundButton;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules.ConfirmTeamSelect;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules.ShowTeamSelect;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.rules.UnconfirmTeamSelect;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;

public class GuiTeamSelect extends GuiVersus {
   public static ShowTeamSelect teamSelectPacket;
   private NPCTrainer trainer;
   private GuiRoundButton confirmButton = new GuiRoundButton(85, 152, I18n.func_135052_a("gui.battlerules.selectteam", new Object[0]));
   private boolean confirmed;
   private boolean timeExpired;
   private ClientBattleManager bm;
   private List icons = new ArrayList();
   private int numSelected;
   public String rejectClause = "";
   private static final String SELECT_TEAM = "gui.battlerules.selectteam";

   public GuiTeamSelect() {
      this.bm = ClientProxy.battleManager;
      this.bm.setTeamSelectTime();
      if (teamSelectPacket.npcID != -1) {
         Optional entityNPCOptional = EntityNPC.locateNPCClient(Minecraft.func_71410_x().field_71441_e, teamSelectPacket.npcID, NPCTrainer.class);
         if (entityNPCOptional.isPresent()) {
            this.trainer = (NPCTrainer)entityNPCOptional.get();
            this.isNPC = true;
         }
      }

   }

   public void func_73866_w_() {
      super.func_73866_w_();
      int i;
      Iterator var7;
      if (this.icons.isEmpty()) {
         i = 0;
         Pokemon[] var2 = ClientStorageManager.party.getAll();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Pokemon data = var2[var4];
            TeamSelectPokemon pokemon = null;
            if (data != null) {
               pokemon = new TeamSelectPokemon(data);
            }

            this.icons.add(new TeamSelectPokemonIcon(pokemon, teamSelectPacket.disabled[i]));
            ++i;
         }

         if (this.bm.rules.teamPreview && teamSelectPacket.opponentTeam != null) {
            var7 = teamSelectPacket.opponentTeam.iterator();

            while(var7.hasNext()) {
               TeamSelectPokemon pokemon = (TeamSelectPokemon)var7.next();
               this.icons.add(new TeamSelectPokemonIcon(pokemon, "n"));
            }
         }
      }

      i = 0;

      for(var7 = this.icons.iterator(); var7.hasNext(); ++i) {
         TeamSelectPokemonIcon icon = (TeamSelectPokemonIcon)var7.next();
         if (i < 6) {
            icon.setPosition(this.playerPartyX + i * 12, this.playerPartyY);
         } else {
            icon.setPosition(this.opponentPartyX + (i - 6) * 12, this.opponentPartyY);
         }
      }

   }

   protected void func_73869_a(char key, int keyCode) {
   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      super.func_73863_a(mouseX, mouseY, partialTicks);
      if (this.ticks == 0) {
         boolean canHighlight = this.numSelected > 0;
         int currentX = canHighlight ? mouseX : 0;
         int currentY = canHighlight ? mouseY : 0;
         this.confirmButton.drawButton(this.leftX, this.topY, currentX, currentY, this.field_73735_i, this.confirmed);
      }

      int i = 0;
      Iterator var8 = this.icons.iterator();

      while(var8.hasNext()) {
         TeamSelectPokemonIcon icon = (TeamSelectPokemonIcon)var8.next();
         icon.setTickOffset(i++ < 6 ? this.offset1 : this.offset2);
         icon.drawIcon(mouseX, mouseY, this.field_73735_i, this.isFullSelect());
      }

      if (!this.bm.rules.teamPreview) {
         this.drawOpponentPokeBalls(teamSelectPacket.opponentSize);
      }

      if (this.bm.afkOn) {
         GuiHelper.drawBattleTimer(this, this.bm.afkTime);
         if (this.bm.afkTime <= 0 && !this.timeExpired) {
            this.confirmed = true;
            this.timeExpired = true;
            Pixelmon.network.sendToServer(new ConfirmTeamSelect(teamSelectPacket.teamSelectID, this.getSelectionOrder(), true));
         }
      }

      String centerMessage = "";
      if (!this.rejectClause.isEmpty()) {
         centerMessage = I18n.func_135052_a("gui.battlerules.teamviolated", new Object[0]) + " " + BattleClause.getLocalizedName(this.rejectClause);
         this.confirmed = false;
      } else if (this.confirmed) {
         centerMessage = I18n.func_135052_a("gui.battlerules.waitselect", new Object[0]);
      }

      if (!centerMessage.isEmpty()) {
         GuiHelper.renderTooltip(this.leftX + 140, this.topY + 60, this.field_146297_k.field_71466_p.func_78271_c(centerMessage, 100), Color.BLUE.getRGB(), Color.BLACK.getRGB(), 200, true, true);
      }

   }

   protected void func_73864_a(int mouseX, int mouseY, int par3) {
      if (!this.timeExpired) {
         if (!this.confirmed) {
            Iterator var4 = this.icons.iterator();

            while(var4.hasNext()) {
               TeamSelectPokemonIcon icon = (TeamSelectPokemonIcon)var4.next();
               if (icon.isMouseOver(mouseX, mouseY) && !icon.isDisabled()) {
                  if (icon.selectIndex == -1) {
                     if (this.isFullSelect()) {
                        break;
                     }

                     icon.selectIndex = this.numSelected++;
                     if (this.numSelected == 1) {
                        this.confirmButton.setText(I18n.func_135052_a("gui.battlerules.confirm", new Object[0]));
                     }
                  } else {
                     Iterator var6 = this.icons.iterator();

                     while(var6.hasNext()) {
                        TeamSelectPokemonIcon icon2 = (TeamSelectPokemonIcon)var6.next();
                        if (icon2.selectIndex > icon.selectIndex) {
                           --icon2.selectIndex;
                        }
                     }

                     icon.selectIndex = -1;
                     if (--this.numSelected == 0) {
                        this.confirmButton.setText(I18n.func_135052_a("gui.battlerules.selectteam", new Object[0]));
                     }
                  }

                  this.rejectClause = "";
                  break;
               }
            }
         }

         if (this.numSelected > 0 && this.confirmButton.isMouseOver(this.leftX, this.topY, mouseX, mouseY)) {
            this.confirmed = !this.confirmed;
            if (this.confirmed) {
               Pixelmon.network.sendToServer(new ConfirmTeamSelect(teamSelectPacket.teamSelectID, this.getSelectionOrder(), false));
            } else {
               Pixelmon.network.sendToServer(new UnconfirmTeamSelect(teamSelectPacket.teamSelectID));
            }
         }

      }
   }

   private boolean isFullSelect() {
      return this.numSelected >= this.bm.rules.numPokemon;
   }

   protected EntityLivingBase getOpponent() {
      return (EntityLivingBase)(teamSelectPacket.opponentUUID == null ? this.trainer : this.field_146297_k.field_71441_e.func_152378_a(teamSelectPacket.opponentUUID));
   }

   public boolean func_73868_f() {
      return false;
   }

   private int[] getSelectionOrder() {
      int[] selection = new int[6];

      for(int i = 0; i < selection.length; ++i) {
         selection[i] = ((TeamSelectPokemonIcon)this.icons.get(i)).selectIndex;
      }

      return selection;
   }
}
