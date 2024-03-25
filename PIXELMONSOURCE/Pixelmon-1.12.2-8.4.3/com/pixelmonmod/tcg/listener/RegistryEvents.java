package com.pixelmonmod.tcg.listener;

import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.tcg.BaseCardRegistry;
import com.pixelmonmod.tcg.block.BlockBattleController;
import com.pixelmonmod.tcg.block.BlockPrinter;
import com.pixelmonmod.tcg.item.ItemAdminEye;
import com.pixelmonmod.tcg.item.ItemAdminKey;
import com.pixelmonmod.tcg.item.ItemAdminNecklace;
import com.pixelmonmod.tcg.item.ItemAdminPendant;
import com.pixelmonmod.tcg.item.ItemAdminRing;
import com.pixelmonmod.tcg.item.ItemAdminRod;
import com.pixelmonmod.tcg.item.ItemAdminScales;
import com.pixelmonmod.tcg.item.ItemBattleRule;
import com.pixelmonmod.tcg.item.ItemBinder;
import com.pixelmonmod.tcg.item.ItemCard;
import com.pixelmonmod.tcg.item.ItemCardBack;
import com.pixelmonmod.tcg.item.ItemCoin;
import com.pixelmonmod.tcg.item.ItemCompendium;
import com.pixelmonmod.tcg.item.ItemDeck;
import com.pixelmonmod.tcg.item.ItemPack;
import com.pixelmonmod.tcg.item.ItemRulebook;
import com.pixelmonmod.tcg.item.ItemShadowWand;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(
   modid = "tcg"
)
public class RegistryEvents {
   @SubscribeEvent
   public void registerItems(RegistryEvent.Register event) {
      event.getRegistry().registerAll(new Item[]{new ItemBattleRule(), new ItemBinder(), new ItemCard(), new ItemCardBack(), new ItemCoin(), new ItemCompendium(), new ItemDeck(), new ItemAdminEye(), new ItemAdminKey(), new ItemAdminNecklace(), new ItemPack(), new ItemAdminPendant(), new ItemAdminRing(), new ItemAdminRod(), new ItemRulebook(), new ItemAdminScales(), new ItemShadowWand()});
      BaseCardRegistry.registerItems(event);
   }

   @SubscribeEvent
   public void registerBlocks(RegistryEvent.Register event) {
      PixelmonBlocks.registerBlock(new BlockBattleController("small_battle_controller", 1.0F), (Class)ItemBlock.class, (String)"tcg:small_battle_controller");
      PixelmonBlocks.registerBlock(new BlockBattleController("medium_battle_controller", 2.0F), (Class)ItemBlock.class, (String)"tcg:medium_battle_controller");
      PixelmonBlocks.registerBlock(new BlockBattleController("large_battle_controller", 4.0F), (Class)ItemBlock.class, (String)"tcg:large_battle_controller");
      PixelmonBlocks.registerBlock(new BlockPrinter(), (Class)ItemBlock.class, (String)"tcg:card_printer");
   }
}
