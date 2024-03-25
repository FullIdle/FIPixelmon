package com.pixelmonmod.tcg;

import com.pixelmonmod.tcg.api.card.CardRarity;
import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.gui.GuiChatOverlay;
import com.pixelmonmod.tcg.item.ItemBlankCard;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.StringUtils;

public class BaseCardRegistry {
   public static ArrayList cards = new ArrayList();

   public static void registerItems(RegistryEvent.Register event) {
      Energy[] var2 = Energy.values();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Energy energy = var2[var4];
         if (energy != Energy.RAINBOW && StringUtils.isNotBlank(energy.getUnlocalizedName())) {
            ItemBlankCard itemBaseCard = new ItemBlankCard(CardRarity.COMMON, energy);
            cards.add(itemBaseCard);
            event.getRegistry().register(itemBaseCard);
            itemBaseCard = new ItemBlankCard(CardRarity.UNCOMMON, energy);
            cards.add(itemBaseCard);
            event.getRegistry().register(itemBaseCard);
            itemBaseCard = new ItemBlankCard(CardRarity.RARE, energy);
            cards.add(itemBaseCard);
            event.getRegistry().register(itemBaseCard);
            itemBaseCard = new ItemBlankCard(CardRarity.HOLORARE, energy);
            cards.add(itemBaseCard);
            event.getRegistry().register(itemBaseCard);
            itemBaseCard = new ItemBlankCard(CardRarity.ULTRARARE, energy);
            cards.add(itemBaseCard);
            event.getRegistry().register(itemBaseCard);
         }
      }

   }

   @SideOnly(Side.CLIENT)
   public static void registerRenderers(ItemModelMesher mesher) {
      Iterator var1 = cards.iterator();

      while(var1.hasNext()) {
         ItemBlankCard card = (ItemBlankCard)var1.next();
         mesher.func_178086_a(card, 0, new ModelResourceLocation("tcg:" + card.getName(), "inventory"));
         ModelBakery.registerItemVariants(card, new ResourceLocation[]{new ResourceLocation("tcg:" + card.getName())});
      }

      MinecraftForge.EVENT_BUS.register(new GuiChatOverlay(Minecraft.func_71410_x()));
   }
}
