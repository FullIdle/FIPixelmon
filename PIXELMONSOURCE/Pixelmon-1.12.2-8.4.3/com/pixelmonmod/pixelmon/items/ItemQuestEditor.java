package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonCreativeTabs;
import com.pixelmonmod.pixelmon.quests.client.editor.QuestEditorState;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.server.command.TextComponentHelper;

public class ItemQuestEditor extends PixelmonItem {
   public ItemQuestEditor() {
      super("quest_editor");
      this.func_77637_a(PixelmonCreativeTabs.quests);
      this.setNoRepair();
      this.field_77777_bU = 1;
   }

   public ActionResult func_77659_a(World world, EntityPlayer player, EnumHand hand) {
      ItemStack stack = player.func_184586_b(hand);
      if (world.field_72995_K) {
         QuestEditorState.get().fetch(true);
      }

      return new ActionResult(EnumActionResult.PASS, stack);
   }

   public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
      if (player.field_70170_p.field_72995_K && player.field_71075_bZ.field_75098_d) {
         StringSelection stringSelection = new StringSelection(entity.getPersistentID().toString());
         Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
         clipboard.setContents(stringSelection, (ClipboardOwner)null);
         player.func_145747_a(TextComponentHelper.createComponentTranslation(player, "item.quest_editor.copy", new Object[0]));
      }

      return true;
   }

   public boolean func_111207_a(ItemStack stack, EntityPlayer player, EntityLivingBase target, EnumHand hand) {
      return false;
   }

   public boolean func_77616_k(ItemStack stack) {
      return false;
   }

   public static boolean checkPermission(EntityPlayer player) {
      if (!PixelmonConfig.opToUseQuestEditor && player.func_184812_l_()) {
         return true;
      } else if (PixelmonConfig.opToUseQuestEditor && player.func_70003_b(4, "pixelmon.questeditor.use")) {
         return true;
      } else {
         ChatHandler.sendChat(player, "pixelmon.general.needop");
         return false;
      }
   }
}
