package com.pixelmonmod.tcg.proxy;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.models.smd.GabeNewellException;
import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.gui.GuiBinder;
import com.pixelmonmod.tcg.gui.GuiDeck;
import com.pixelmonmod.tcg.gui.GuiPrinter;
import com.pixelmonmod.tcg.gui.enums.EnumGui;
import com.pixelmonmod.tcg.item.containers.ContainerBinder;
import com.pixelmonmod.tcg.item.containers.ContainerDeck;
import com.pixelmonmod.tcg.item.containers.ContainerPrinter;
import com.pixelmonmod.tcg.item.containers.InventoryBinder;
import com.pixelmonmod.tcg.item.containers.InventoryDeck;
import com.pixelmonmod.tcg.listener.ServerEventHandler;
import com.pixelmonmod.tcg.tileentity.ServerBattleController;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleSpectator;
import com.pixelmonmod.tcg.tileentity.TileEntityPrinter;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TCGCommonProxy implements IGuiHandler {
   public void preInit(FMLPreInitializationEvent e) {
   }

   public void init(FMLInitializationEvent e) throws GabeNewellException {
      TCG.serverEventHandler = new ServerEventHandler();
      MinecraftForge.EVENT_BUS.register(TCG.serverEventHandler);
      Pixelmon.EVENT_BUS.register(new ServerEventHandler());
      GameRegistry.registerTileEntity(ServerBattleController.class, new ResourceLocation("tcg", "battleController"));
      GameRegistry.registerTileEntity(TileEntityBattleSpectator.class, new ResourceLocation("tcg", "battleSpectator"));
      GameRegistry.registerTileEntity(TileEntityPrinter.class, new ResourceLocation("tcg", "CardPrinter"));
   }

   public void postInit(FMLPostInitializationEvent e) {
   }

   public Object getServerGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z) {
      switch (EnumGui.fromIndex(guiId)) {
         case Deck:
            return new ContainerDeck(player, player.field_71071_by, new InventoryDeck(player.func_184614_ca(), player));
         case Printer:
            return new ContainerPrinter(player.field_71071_by, (TileEntityPrinter)world.func_175625_s(new BlockPos(x, y, z)));
         case Binder:
            return new ContainerBinder(player, player.field_71071_by, new InventoryBinder(player));
         default:
            return null;
      }
   }

   public Object getClientGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z) {
      switch (EnumGui.fromIndex(guiId)) {
         case Deck:
            return new GuiDeck(player, player.field_71071_by, new InventoryDeck(player.func_184614_ca(), player));
         case Printer:
            return new GuiPrinter(player.field_71071_by, (TileEntityPrinter)world.func_175625_s(new BlockPos(x, y, z)));
         case Binder:
            return new GuiBinder(player, player.field_71071_by, new InventoryBinder(player));
         default:
            return null;
      }
   }

   public void displayGuiPack(Minecraft mc, int set, ImmutableCard[] cards) {
   }
}
