package com.pixelmonmod.pixelmon.items;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.blocks.BlockZygardeCell;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityZygardeCell;
import com.pixelmonmod.pixelmon.comm.ChatHandler;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.util.helpers.BlockHelper;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemZygardeCube extends PixelmonItem {
   public ItemZygardeCube() {
      super("zygarde_cube");
      this.func_77637_a(CreativeTabs.field_78040_i);
      this.func_77625_d(1);
   }

   @SideOnly(Side.CLIENT)
   public void func_77624_a(ItemStack stack, World world, List tooltip, ITooltipFlag advanced) {
      tooltip.add(I18n.func_135052_a("pixelmon.items.zygarde_cube.tooltip.cells", new Object[]{getCellCount(stack)}));
      tooltip.add(I18n.func_135052_a("pixelmon.items.zygarde_cube.tooltip.cores", new Object[]{getCoreCount(stack)}));
      Set cores = getCoreTypes(stack);
      if (!cores.isEmpty()) {
         tooltip.add(I18n.func_135052_a("pixelmon.items.zygarde_cube.tooltip.moves", new Object[0]));
         Iterator var6 = cores.iterator();

         while(var6.hasNext()) {
            CoreType type = (CoreType)var6.next();
            Optional opt = AttackBase.getAttackBaseFromEnglishName(type.getMoveName());
            opt.ifPresent((attackBase) -> {
               tooltip.add(" - " + attackBase.getLocalizedName());
            });
         }
      }

      super.func_77624_a(stack, world, tooltip, advanced);
   }

   public EnumActionResult func_180614_a(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
      IBlockState state = worldIn.func_180495_p(pos);
      if (state.func_177230_c() instanceof BlockZygardeCell) {
         if (worldIn.field_72995_K) {
            return EnumActionResult.SUCCESS;
         } else {
            ItemStack stack = player.func_184586_b(hand);
            if (stack.func_77973_b() != this) {
               return EnumActionResult.PASS;
            } else {
               TileEntityZygardeCell cell = (TileEntityZygardeCell)BlockHelper.getTileEntity(TileEntityZygardeCell.class, worldIn, pos);
               if (cell != null) {
                  if (cell.isPermanent()) {
                     if (cell.addEncounter(player.func_110124_au())) {
                        if (registerCell(cell, state, player, stack)) {
                           ChatHandler.sendChat(player, "pixelmon.items.zygarde_cube.pickup");
                        }
                     } else {
                        ChatHandler.sendChat(player, "pixelmon.items.zygarde_cube.encountered", new TextComponentTranslation(state.func_177230_c().func_149739_a() + ".name", new Object[0]));
                     }
                  } else if (worldIn.func_175698_g(pos) && registerCell(cell, state, player, stack)) {
                     ChatHandler.sendChat(player, "pixelmon.items.zygarde_cube.pickup");
                  }
               }

               return EnumActionResult.SUCCESS;
            }
         }
      } else {
         return super.func_180614_a(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
      }
   }

   public static int getCellCount(ItemStack stack) {
      NBTTagCompound compound = stack.func_190925_c("cells");
      return compound.func_74765_d("Count");
   }

   public static void setCellCount(ItemStack stack, int count) {
      if (count > 200) {
         count = 200;
      }

      if (count < 0) {
         count = 0;
      }

      NBTTagCompound compound = stack.func_190925_c("cells");
      compound.func_74777_a("Count", (short)count);
   }

   public static int getCoreCount(ItemStack stack) {
      NBTTagCompound compound = stack.func_190925_c("cells");
      return compound.func_74765_d("CoreCount");
   }

   public static void setCoreCount(ItemStack stack, int count) {
      if (count > 5) {
         count = 5;
      }

      if (count < 0) {
         count = 0;
      }

      NBTTagCompound compound = stack.func_190925_c("cells");
      compound.func_74777_a("CoreCount", (short)count);
   }

   public static boolean hasCore(ItemStack stack, CoreType type) {
      NBTTagCompound compound = stack.func_190925_c("cells");
      return compound.func_74767_n("Core" + type.ordinal());
   }

   public static void addCore(ItemStack stack, CoreType type) {
      setCoreCount(stack, getCoreCount(stack) + 1);
      NBTTagCompound compound = stack.func_190925_c("cells");
      compound.func_74757_a("Core" + type.ordinal(), true);
   }

   public static Set getCoreTypes(ItemStack stack) {
      Set set = EnumSet.allOf(CoreType.class);
      set.removeIf((type) -> {
         return !hasCore(stack, type);
      });
      set.remove(ItemZygardeCube.CoreType.RANDOM);
      return set;
   }

   public static boolean registerCell(TileEntityZygardeCell te, IBlockState state, EntityPlayer player, ItemStack stack) {
      boolean core = state.func_177230_c() == PixelmonBlocks.zygardeCore;
      if (core) {
         if (te.getCoreType() == ItemZygardeCube.CoreType.RANDOM) {
            Set cores = EnumSet.allOf(CoreType.class);
            cores.remove(ItemZygardeCube.CoreType.RANDOM);
            cores.removeAll(getCoreTypes(stack));
            if (cores.isEmpty()) {
               cores = EnumSet.allOf(CoreType.class);
               cores.remove(ItemZygardeCube.CoreType.RANDOM);
            }

            addCore(stack, (CoreType)RandomHelper.getRandomElementFromArray(cores.toArray(new CoreType[0])));
            return true;
         } else if (hasCore(stack, te.getCoreType())) {
            ChatHandler.sendChat(player, "pixelmon.items.zygarde_cube.encountered", new TextComponentTranslation(state.func_177230_c().func_149739_a() + ".name", new Object[0]));
            return false;
         } else {
            addCore(stack, te.getCoreType());
            return true;
         }
      } else {
         setCellCount(stack, getCellCount(stack) + 1);
         return true;
      }
   }

   public static enum CoreType {
      RANDOM("RANDOM"),
      CORE_ENFORCER("Core Enforcer"),
      THOUSAND_ARROWS("Thousand Arrows"),
      THOUSAND_WAVES("Thousand Waves"),
      EXTREME_SPEED("Extreme Speed"),
      DRAGON_DANCE("Dragon Dance");

      private static final CoreType[] VALUES = values();
      private final String moveName;

      private CoreType(String moveName) {
         this.moveName = moveName;
      }

      public String getMoveName() {
         return this.moveName;
      }

      public static CoreType[] getTypes() {
         return new CoreType[]{CORE_ENFORCER, THOUSAND_ARROWS, THOUSAND_WAVES, EXTREME_SPEED, DRAGON_DANCE};
      }

      public static CoreType fromIndex(int index) {
         return index >= 0 && index < VALUES.length ? VALUES[index] : RANDOM;
      }
   }
}
