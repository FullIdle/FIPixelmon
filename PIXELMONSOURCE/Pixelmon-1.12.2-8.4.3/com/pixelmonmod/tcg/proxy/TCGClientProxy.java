package com.pixelmonmod.tcg.proxy;

import com.pixelmonmod.pixelmon.client.PixelmonStateMapper;
import com.pixelmonmod.pixelmon.client.models.smd.GabeNewellException;
import com.pixelmonmod.tcg.BaseCardRegistry;
import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.block.BlockPrinter;
import com.pixelmonmod.tcg.block.renderers.RenderBattleController;
import com.pixelmonmod.tcg.block.renderers.RenderBattleSpectator;
import com.pixelmonmod.tcg.block.renderers.RenderTileEntityPrinter;
import com.pixelmonmod.tcg.config.TCGConfig;
import com.pixelmonmod.tcg.gui.GuiPack;
import com.pixelmonmod.tcg.gui.GuiTCGOverlay;
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
import com.pixelmonmod.tcg.listener.ClientEventHandler;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleSpectator;
import com.pixelmonmod.tcg.tileentity.TileEntityPrinter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Sphere;

public class TCGClientProxy extends TCGCommonProxy implements IGuiHandler {
   public static int sphereIdOutside;
   public static int sphereIdInside;
   public static KeyBinding openTCGCraftingMenu;

   public void preInit(FMLPreInitializationEvent e) {
      super.preInit(e);
      openTCGCraftingMenu = new KeyBinding("tcg.essence", 39, "tcg.keybindcategory");
      ClientRegistry.registerKeyBinding(openTCGCraftingMenu);
   }

   public void init(FMLInitializationEvent e) throws GabeNewellException {
      super.init(e);
      TCG.clientEventHandler = new ClientEventHandler();
      MinecraftForge.EVENT_BUS.register(TCG.clientEventHandler);
      MinecraftForge.EVENT_BUS.register(new GuiTCGOverlay());
      ItemModelMesher mesher = Minecraft.func_71410_x().func_175599_af().func_175037_a();
      mesher.func_178086_a(TCG.itemCard, 0, new ModelResourceLocation(new ResourceLocation("tcg", ItemCard.getName()), "inventory"));
      mesher.func_178086_a(TCG.itemPack, 0, new ModelResourceLocation("tcg:" + ItemPack.getName(), "inventory"));
      mesher.func_178086_a(TCG.itemDeck, 0, new ModelResourceLocation("tcg:" + ItemDeck.getName(), "inventory"));
      mesher.func_178086_a(TCG.itemBinder, 0, new ModelResourceLocation("tcg:" + ItemBinder.getName(), "inventory"));
      mesher.func_178086_a(TCG.itemRulebook, 0, new ModelResourceLocation("tcg:" + ItemRulebook.getName(), "inventory"));
      mesher.func_178086_a(TCG.itemBattleRule, 0, new ModelResourceLocation("tcg:" + ItemBattleRule.getName(), "inventory"));
      mesher.func_178086_a(TCG.itemCardBack, 0, new ModelResourceLocation("tcg:" + ItemCardBack.getName(), "inventory"));
      mesher.func_178086_a(TCG.itemCoin, 0, new ModelResourceLocation("tcg:" + ItemCoin.getName(), "inventory"));
      mesher.func_178086_a(TCG.itemCompendium, 0, new ModelResourceLocation("tcg:" + ItemCompendium.getName(), "inventory"));
      mesher.func_178086_a(TCG.itemEye, 0, new ModelResourceLocation("tcg:" + ItemAdminEye.getName(), "inventory"));
      mesher.func_178086_a(TCG.itemPendant, 0, new ModelResourceLocation("tcg:" + ItemAdminPendant.getName(), "inventory"));
      mesher.func_178086_a(TCG.itemRing, 0, new ModelResourceLocation("tcg:" + ItemAdminRing.getName(), "inventory"));
      mesher.func_178086_a(TCG.itemNecklace, 0, new ModelResourceLocation("tcg:" + ItemAdminNecklace.getName(), "inventory"));
      mesher.func_178086_a(TCG.itemRod, 0, new ModelResourceLocation("tcg:" + ItemAdminRod.getName(), "inventory"));
      mesher.func_178086_a(TCG.itemKey, 0, new ModelResourceLocation("tcg:" + ItemAdminKey.getName(), "inventory"));
      mesher.func_178086_a(TCG.itemScales, 0, new ModelResourceLocation("tcg:" + ItemAdminScales.getName(), "inventory"));
      mesher.func_178086_a(TCG.itemShadowWand, 0, new ModelResourceLocation("tcg:" + ItemShadowWand.getName(), "inventory"));
      mesher.func_178086_a(TCG.itemSmallBattleController, 0, new ModelResourceLocation("tcg:small_battle_controller", "inventory"));
      mesher.func_178086_a(TCG.itemMediumBattleController, 0, new ModelResourceLocation("tcg:medium_battle_controller", "inventory"));
      mesher.func_178086_a(TCG.itemLargeBattleController, 0, new ModelResourceLocation("tcg:large_battle_controller", "inventory"));
      mesher.func_178086_a(TCG.itemPrinter, 0, new ModelResourceLocation("tcg:" + BlockPrinter.getName(), "inventory"));
      BaseCardRegistry.registerRenderers(mesher);
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPrinter.class, new RenderTileEntityPrinter());
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBattleController.class, new RenderBattleController());
      ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBattleSpectator.class, new RenderBattleSpectator());
      new PixelmonStateMapper();
      this.initSphereRendering();
      TCGConfig config = TCGConfig.getInstance();
      if (config.savedUIScale != -1) {
         Minecraft.func_71410_x().field_71474_y.field_74335_Z = config.savedUIScale;
      } else {
         config.savedUIScale = Minecraft.func_71410_x().field_71474_y.field_74335_Z;
      }

   }

   public void initSphereRendering() {
      Sphere sphere = new Sphere();
      sphere.setDrawStyle(100012);
      sphere.setNormals(100000);
      sphere.setOrientation(100020);
      sphereIdOutside = GL11.glGenLists(1);
      GL11.glNewList(sphereIdOutside, 4864);
      ResourceLocation rL = new ResourceLocation("tcg:gui/cards/color/blue.png");
      Minecraft.func_71410_x().func_110434_K().func_110577_a(rL);
      sphere.draw(0.5F, 32, 32);
      GL11.glEndList();
      sphere.setOrientation(100021);
      sphereIdInside = GL11.glGenLists(1);
      GL11.glNewList(sphereIdInside, 4864);
      Minecraft.func_71410_x().func_110434_K().func_110577_a(rL);
      sphere.draw(0.5F, 32, 32);
      GL11.glEndList();
   }

   public void postInit(FMLPostInitializationEvent e) {
      super.postInit(e);
   }

   public void displayGuiPack(Minecraft mc, int set, ImmutableCard[] cards) {
      mc.func_147108_a(new GuiPack(set, cards));
   }

   public Object getClientGuiElement(int guiId, EntityPlayer player, World world, int x, int y, int z) {
      return super.getClientGuiElement(guiId, player, world, x, y, z);
   }
}
