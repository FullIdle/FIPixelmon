package com.pixelmonmod.pixelmon.entities.pixelmon.interactions;

import com.pixelmonmod.pixelmon.api.interactions.IInteraction;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.formChanges.CalyrexFormChange;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.formChanges.DeoxysFormChange;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.formChanges.ForcesOfNatureFormChange;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.formChanges.HoopaFormChange;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.formChanges.IFormChange;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.formChanges.KyuremFormChange;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.formChanges.NecrozmaFormChange;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.formChanges.OricorioFormChange;
import com.pixelmonmod.pixelmon.entities.pixelmon.interactions.formChanges.ShayminFormChange;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class InteractionChangeForm implements IInteraction {
   private static final ArrayList formChanges = new ArrayList();

   public boolean processInteract(EntityPixelmon pixelmon, EntityPlayer player, EnumHand hand, ItemStack itemStack) {
      if (pixelmon.func_70902_q() == player && player instanceof EntityPlayerMP) {
         Iterator var5 = formChanges.iterator();

         while(var5.hasNext()) {
            IFormChange formChange = (IFormChange)var5.next();
            if (formChange.isValidPokemon(pixelmon) && formChange.isValidItem(itemStack)) {
               return formChange.execute(pixelmon, itemStack, (EntityPlayerMP)player);
            }
         }
      }

      return false;
   }

   static {
      formChanges.add(new DeoxysFormChange());
      formChanges.add(new ForcesOfNatureFormChange());
      formChanges.add(new HoopaFormChange());
      formChanges.add(new KyuremFormChange());
      formChanges.add(new NecrozmaFormChange());
      formChanges.add(new OricorioFormChange());
      formChanges.add(new ShayminFormChange());
      formChanges.add(new CalyrexFormChange());
   }
}
