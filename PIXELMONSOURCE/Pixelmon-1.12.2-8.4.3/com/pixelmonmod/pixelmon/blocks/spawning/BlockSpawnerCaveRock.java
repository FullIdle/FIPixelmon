package com.pixelmonmod.pixelmon.blocks.spawning;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.config.PixelmonBlocks;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleStartTypes;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSpawnerCaveRock extends Block {
   public BlockSpawnerCaveRock() {
      super(Material.field_151576_e);
      this.func_149675_a(true);
      this.func_149672_a(SoundType.field_185851_d);
      this.func_149711_c(0.5F);
   }

   public void func_176199_a(World worldIn, BlockPos pos, Entity entityIn) {
      if (entityIn instanceof EntityPlayerMP) {
         if (BattleRegistry.getBattle((EntityPlayerMP)entityIn) != null) {
            return;
         }

         PlayerPartyStorage storage = Pixelmon.storageManager.getParty((EntityPlayerMP)entityIn);
         if (storage.getTicksTillEncounter() <= 1) {
            BlockSpawningHandler.getInstance().performBattleStartCheck(worldIn, pos, entityIn, (EntityPixelmon)null, EnumBattleStartTypes.CAVEROCK, (IBlockState)null);
         }

         storage.updateTicksTillEncounter();
      }

      super.func_176199_a(worldIn, pos, entityIn);
   }

   public List getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
      ArrayList drops = new ArrayList();
      drops.add(new ItemStack(PixelmonBlocks.caveRockBlock));
      return drops;
   }
}
