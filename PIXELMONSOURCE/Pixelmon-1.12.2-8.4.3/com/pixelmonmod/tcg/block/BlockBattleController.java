package com.pixelmonmod.tcg.block;

import com.pixelmonmod.pixelmon.blocks.GenericRotatableModelBlock;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.tcg.TCG;
import com.pixelmonmod.tcg.duel.state.GameServerState;
import com.pixelmonmod.tcg.duel.state.PlayerServerState;
import com.pixelmonmod.tcg.helper.LogicHelper;
import com.pixelmonmod.tcg.item.ItemDeck;
import com.pixelmonmod.tcg.item.ItemRulebook;
import com.pixelmonmod.tcg.item.ItemShadowWand;
import com.pixelmonmod.tcg.network.PacketHandler;
import com.pixelmonmod.tcg.network.packets.battles.TCGGuiClientPacket;
import com.pixelmonmod.tcg.tileentity.ServerBattleController;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleController;
import com.pixelmonmod.tcg.tileentity.TileEntityBattleRule;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockBattleController extends GenericRotatableModelBlock {
   private float scale;

   public BlockBattleController(String name, float scale) {
      super(Material.field_151573_f);
      this.func_149663_c(getName());
      this.func_149711_c(1.5F);
      this.func_149752_b(10.0F);
      this.setRegistryName("tcg", name);
      this.func_149647_a(TCG.tabTCG);
      this.scale = scale;
   }

   public static String getName() {
      return "battle_controller";
   }

   public float getScale() {
      return this.scale;
   }

   public Item func_180660_a(IBlockState state, Random rand, int fortune) {
      if (this.scale == 1.0F) {
         return TCG.itemSmallBattleController;
      } else {
         return this.scale == 2.0F ? TCG.itemMediumBattleController : TCG.itemLargeBattleController;
      }
   }

   public boolean hasTileEntity(IBlockState state) {
      return true;
   }

   public TileEntity func_149915_a(World worldIn, int meta) {
      return new ServerBattleController();
   }

   public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
      if (this.scale == 1.0F) {
         return new ItemStack(TCG.itemSmallBattleController);
      } else {
         return this.scale == 2.0F ? new ItemStack(TCG.itemMediumBattleController) : new ItemStack(TCG.itemLargeBattleController);
      }
   }

   public void func_180633_a(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack blockItem) {
      super.func_180633_a(world, pos, state, placer, blockItem);
      if (!world.field_72995_K && placer != null) {
         if (placer instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer)placer;
            if (player.field_71071_by != null) {
               Iterator var7 = player.field_71071_by.field_70462_a.iterator();

               while(var7.hasNext()) {
                  ItemStack stack = (ItemStack)var7.next();
                  if (stack != null && stack.func_77973_b() == TCG.itemRulebook && stack.func_77942_o() && world.func_175625_s(pos) != null) {
                     NBTTagCompound tag = stack.func_77978_p();
                     TileEntityBattleRule te = (TileEntityBattleRule)world.func_175625_s(pos);
                     te.setDeckSize(tag.func_74762_e("DeckSize"));
                     te.setPrizeCount(tag.func_74762_e("PrizeCount"));
                     te.setTimeLimit(tag.func_74762_e("TimeLimit"));
                     te.setEloMinimum(tag.func_74762_e("EloMinimum"));
                     te.setStartingCommand(tag.func_74779_i("StartingCommand"));
                     te.setEndingCommand(tag.func_74779_i("EndingCommand"));
                     te.setStartingMessage(tag.func_74779_i("StartingMessage"));
                     te.setEndingMessage(tag.func_74779_i("EndingMessage"));
                     player.func_146105_b(new TextComponentString(TextFormatting.AQUA + "Placed BattleBox with following rules:"), false);
                     player.func_146105_b(new TextComponentString(TextFormatting.LIGHT_PURPLE + "Deck size: " + TextFormatting.WHITE + tag.func_74762_e("DeckSize")), false);
                     player.func_146105_b(new TextComponentString(TextFormatting.LIGHT_PURPLE + "Prize count: " + TextFormatting.WHITE + tag.func_74762_e("PrizeCount")), false);
                     player.func_146105_b(new TextComponentString(TextFormatting.LIGHT_PURPLE + "Turn time limit: " + TextFormatting.WHITE + tag.func_74762_e("TimeLimit")), false);
                     player.func_146105_b(new TextComponentString(TextFormatting.LIGHT_PURPLE + "Elo requirement: " + TextFormatting.WHITE + tag.func_74762_e("EloMinimum")), false);
                     player.func_146105_b(new TextComponentString(TextFormatting.LIGHT_PURPLE + "Starting command: " + TextFormatting.WHITE + tag.func_74779_i("StartingCommand")), false);
                     player.func_146105_b(new TextComponentString(TextFormatting.LIGHT_PURPLE + "Ending command: " + TextFormatting.WHITE + tag.func_74779_i("EndingCommand")), false);
                     player.func_146105_b(new TextComponentString(TextFormatting.LIGHT_PURPLE + "Starting message: " + TextFormatting.WHITE + tag.func_74779_i("StartingMessage")), false);
                     player.func_146105_b(new TextComponentString(TextFormatting.LIGHT_PURPLE + "Ending message: " + TextFormatting.WHITE + tag.func_74779_i("EndingMessage")), false);
                  }
               }
            }

         }
      }
   }

   public boolean func_180639_a(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      if (player == null) {
         return false;
      } else if (hand == EnumHand.OFF_HAND) {
         return false;
      } else {
         TileEntityBattleController bc = (TileEntityBattleController)world.func_175625_s(pos);
         if (player.func_184614_ca() != ItemStack.field_190927_a && player.func_184614_ca().func_77973_b() instanceof ItemRulebook) {
            if (player.func_184586_b(hand).func_77942_o() && world.func_175625_s(pos) != null) {
               NBTTagCompound tag = player.func_184586_b(hand).func_77978_p();
               bc.setDeckSize(tag.func_74762_e("DeckSize"));
               bc.setPrizeCount(tag.func_74762_e("PrizeCount"));
               bc.setTimeLimit(tag.func_74762_e("TimeLimit"));
               bc.setEloMinimum(tag.func_74762_e("EloMinimum"));
               bc.setStartingCommand(tag.func_74779_i("StartingCommand"));
               bc.setEndingCommand(tag.func_74779_i("EndingCommand"));
               bc.setStartingMessage(tag.func_74779_i("StartingMessage"));
               bc.setEndingMessage(tag.func_74779_i("EndingMessage"));
               player.func_146105_b(new TextComponentString(TextFormatting.AQUA + "Updated BattleBox with following rules:"), false);
               player.func_146105_b(new TextComponentString(TextFormatting.LIGHT_PURPLE + "Deck size: " + TextFormatting.WHITE + tag.func_74762_e("DeckSize")), false);
               player.func_146105_b(new TextComponentString(TextFormatting.LIGHT_PURPLE + "Prize count: " + TextFormatting.WHITE + tag.func_74762_e("PrizeCount")), false);
               player.func_146105_b(new TextComponentString(TextFormatting.LIGHT_PURPLE + "Turn time limit: " + TextFormatting.WHITE + tag.func_74762_e("TimeLimit")), false);
               player.func_146105_b(new TextComponentString(TextFormatting.LIGHT_PURPLE + "Elo requirement: " + TextFormatting.WHITE + tag.func_74762_e("EloMinimum")), false);
               player.func_146105_b(new TextComponentString(TextFormatting.LIGHT_PURPLE + "Starting command: " + TextFormatting.WHITE + tag.func_74779_i("StartingCommand")), false);
               player.func_146105_b(new TextComponentString(TextFormatting.LIGHT_PURPLE + "Ending command: " + TextFormatting.WHITE + tag.func_74779_i("EndingCommand")), false);
               player.func_146105_b(new TextComponentString(TextFormatting.LIGHT_PURPLE + "Starting message: " + TextFormatting.WHITE + tag.func_74779_i("StartingMessage")), false);
               player.func_146105_b(new TextComponentString(TextFormatting.LIGHT_PURPLE + "Ending message: " + TextFormatting.WHITE + tag.func_74779_i("EndingMessage")), false);
            }

            return false;
         } else if (bc == null) {
            return false;
         } else {
            GameServerState server = bc.getGameServer();
            if (server == null) {
               return false;
            } else {
               EntityPlayerMP p = (EntityPlayerMP)player;
               boolean joined = false;

               for(int i = 0; i < 2; ++i) {
                  if (server.getPlayer(i) != null && server.getPlayer(i).getEntityPlayer().func_146103_bH().getId().equals(p.func_146103_bH().getId())) {
                     server.getPlayer(i).setEntityPlayer(p);
                     server.getPlayer(i).setInGUI(true);
                     joined = true;
                     TCG.logger.info("Activate battle block. Rejoin");
                     PacketHandler.net.sendTo(new TCGGuiClientPacket(pos, i, false), p);
                     break;
                  }
               }

               if (!joined) {
                  if (server.isGameInProgress()) {
                     int playerIndex = 0;
                     PacketHandler.net.sendTo(new TCGGuiClientPacket(pos, playerIndex, true), p);
                     server.getSpectators().put(p, Integer.valueOf(playerIndex));
                     return true;
                  }

                  if (player.func_184614_ca() != null && player.func_184614_ca().func_77973_b() instanceof ItemShadowWand) {
                     bc.toggleShadowGame();
                     if (bc.isShadowGame()) {
                        ChatHandler.sendFormattedChat(player, TextFormatting.DARK_PURPLE, "Dark powers now dwell in this BattleBox.");
                     } else {
                        ChatHandler.sendFormattedChat(player, TextFormatting.DARK_PURPLE, "Dark powers no longer dwell in this BattleBox.");
                     }

                     return false;
                  }

                  if (player.func_184614_ca() == null || !(player.func_184614_ca().func_77973_b() instanceof ItemDeck)) {
                     ChatHandler.sendFormattedChat(player, TextFormatting.RED, "You have to hold a deck to join!");
                     return false;
                  }

                  List cards = LogicHelper.getCards(player.func_184614_ca());
                  String error = LogicHelper.validateDeck(cards, bc.getDeckSize());
                  if (error != null) {
                     ChatHandler.sendFormattedChat(player, TextFormatting.RED, error);
                     return false;
                  }

                  for(int i = 0; i < 2; ++i) {
                     if (server.getPlayer(i) == null) {
                        server.setPlayer(i, new PlayerServerState(p));
                        joined = true;
                        TCG.logger.info("Activate battle block. Join");
                        PacketHandler.net.sendTo(new TCGGuiClientPacket(pos, i, false), p);
                        break;
                     }
                  }
               }

               if (!joined) {
                  ChatHandler.sendFormattedChat(p, TextFormatting.RED, "BattleBox is busy!");
               }

               return joined;
            }
         }
      }
   }

   public EnumBlockRenderType func_149645_b(IBlockState state) {
      return EnumBlockRenderType.MODEL;
   }

   public boolean func_149662_c(IBlockState state) {
      return false;
   }

   public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
      return false;
   }

   public boolean isSideSolid(IBlockState p_isSideSolid_1_, IBlockAccess p_isSideSolid_2_, BlockPos p_isSideSolid_3_, EnumFacing p_isSideSolid_4_) {
      return false;
   }

   public boolean isNormalCube(IBlockState state, IBlockAccess world, BlockPos pos) {
      return false;
   }
}
