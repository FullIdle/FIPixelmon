package com.pixelmonmod.pixelmon.commands;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.blocks.BlockProperties;
import com.pixelmonmod.pixelmon.blocks.MultiBlock;
import com.pixelmonmod.pixelmon.comm.CommandChatHandler;
import com.pixelmonmod.pixelmon.entities.npcs.NPCChatting;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityStatue;
import com.pixelmonmod.pixelmon.items.PixelmonItemBlock;
import com.pixelmonmod.pixelmon.util.PixelBlockSnapshot;
import com.pixelmonmod.pixelmon.worldGeneration.structure.util.StructureSnapshot;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class BlockSnapShot extends CommandBase {
   private ArrayList corners = new ArrayList();
   public static StructureSnapshot snapshot;

   public String func_71517_b() {
      return "psnapshot";
   }

   public String func_71518_a(ICommandSender sender) {
      return "/" + this.func_71517_b() + " <argument>";
   }

   public void func_184881_a(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
      if (sender instanceof EntityPlayer && args.length != 0) {
         EntityPlayer player = (EntityPlayer)sender;
         World world = player.field_70170_p;
         BlockPos p;
         if (args[0].equals("set")) {
            this.corners.add(player.func_180425_c());
            if (this.corners.size() > 2) {
               this.corners.remove(0);
            }

            CommandChatHandler.sendChat(sender, "pixelmon.command.snapshot.corners");
            Iterator var6 = this.corners.iterator();

            while(var6.hasNext()) {
               p = (BlockPos)var6.next();
               CommandChatHandler.sendChat(sender, p.toString());
            }
         } else if (args[0].equals("save")) {
            if (this.corners.size() < 2) {
               CommandChatHandler.sendChat(sender, "pixelmon.command.snapshot.nobounds");
               return;
            }

            BlockPos corner0 = (BlockPos)this.corners.get(0);
            p = (BlockPos)this.corners.get(1);
            int x0 = corner0.func_177958_n();
            int x1 = p.func_177958_n();
            int y0 = corner0.func_177956_o();
            int y1 = p.func_177956_o();
            int z0 = corner0.func_177952_p();
            int z1 = p.func_177952_p();
            int minX = Math.min(x0, x1);
            int maxX = Math.max(x0, x1);
            int minY = Math.min(y0, y1);
            int maxY = Math.max(y0, y1);
            int minZ = Math.min(z0, z1);
            int maxZ = Math.max(z0, z1);
            snapshot = StructureSnapshot.from(minX, maxX, minY, maxY, minZ, maxZ, world);
            AxisAlignedBB aabb = new AxisAlignedBB((double)minX, (double)minY, (double)minZ, (double)maxX, (double)maxY, (double)maxZ);
            List trainers = world.func_72872_a(NPCTrainer.class, aabb);
            Iterator var22 = trainers.iterator();

            while(var22.hasNext()) {
               NPCTrainer trainer = (NPCTrainer)var22.next();
               Pixelmon.LOGGER.info("Trainer position: " + (trainer.field_70165_t - (double)minX) + " " + (trainer.field_70163_u - (double)minY) + " " + (trainer.field_70161_v - (double)minZ));
            }

            List chattingNPCS = world.func_72872_a(NPCChatting.class, aabb);
            Iterator var48 = chattingNPCS.iterator();

            while(var48.hasNext()) {
               NPCChatting npc = (NPCChatting)var48.next();
               Pixelmon.LOGGER.info("Chatting NPC position: " + (npc.field_70165_t - (double)minX) + " " + (npc.field_70163_u - (double)minY) + " " + (npc.field_70161_v - (double)minZ));
            }

            if (args.length > 1) {
               String baseDirectory = Pixelmon.modDirectory + "/snapshots/";
               String filename = baseDirectory + args[1] + ".snapshot";
               File saveFile = new File(filename);
               File saveDirPath = new File(baseDirectory);
               if (!saveDirPath.exists()) {
                  saveDirPath.mkdirs();
               }

               try {
                  DataOutputStream out = new DataOutputStream(new FileOutputStream(saveFile));
                  Throwable var28 = null;

                  try {
                     NBTTagCompound compound = new NBTTagCompound();
                     snapshot.writeToNBT(compound);
                     CompressedStreamTools.func_74800_a(compound, out);
                     CommandChatHandler.sendChat(sender, "pixelmon.command.snapshot.savefile", args[1]);
                  } catch (Throwable var40) {
                     var28 = var40;
                     throw var40;
                  } finally {
                     if (out != null) {
                        if (var28 != null) {
                           try {
                              out.close();
                           } catch (Throwable var38) {
                              var28.addSuppressed(var38);
                           }
                        } else {
                           out.close();
                        }
                     }

                  }
               } catch (Exception var42) {
                  var42.printStackTrace();
               }
            } else {
               CommandChatHandler.sendChat(sender, "pixelmon.command.snapshot.save");
            }
         } else if (args[0].equals("read") && args.length > 1) {
            String filename = Pixelmon.modDirectory + "/snapshots/" + args[1] + ".snapshot";
            File saveFile = new File(filename);

            try {
               NBTTagCompound compound = CompressedStreamTools.func_74797_a(saveFile);
               snapshot = StructureSnapshot.readFromNBT(compound);
               CommandChatHandler.sendChat(sender, "pixelmon.command.snapshot.load", args[1]);
            } catch (Exception var39) {
               var39.printStackTrace();
               CommandChatHandler.sendFormattedChat(sender, TextFormatting.RED, "pixelmon.command.snapshot.errorread", args[1]);
               return;
            }
         } else if (args[0].equals("place")) {
            this.place(server, sender, player, world);
         } else {
            CommandChatHandler.sendChat(sender, this.func_71518_a(sender));
         }

      } else {
         CommandChatHandler.sendChat(sender, this.func_71518_a(sender));
      }
   }

   private void place(MinecraftServer server, ICommandSender sender, EntityPlayer player, World world) {
      if (BlockSnapShot.snapshot == null) {
         CommandChatHandler.sendChat(sender, "pixelmon.command.snapshot.nosave");
      } else {
         BlockPos basePos = player.func_180425_c();
         int rotation = (int)Math.abs(player.func_70079_am()) % 360;
         EnumFacing facing;
         if (rotation < 45) {
            facing = EnumFacing.NORTH;
         } else if (rotation < 135) {
            facing = EnumFacing.EAST;
         } else if (rotation < 225) {
            facing = EnumFacing.SOUTH;
         } else if (rotation < 315) {
            facing = EnumFacing.WEST;
         } else {
            facing = EnumFacing.NORTH;
         }

         ArrayList multiblocks = new ArrayList();
         int width = BlockSnapShot.snapshot.getWidth();
         int height = BlockSnapShot.snapshot.getHeight();
         int length = BlockSnapShot.snapshot.getLength();

         int y;
         int x;
         int z;
         PixelBlockSnapshot snapshot;
         BlockPos newPos;
         for(y = 0; y < height; ++y) {
            for(x = 0; x < width; ++x) {
               for(z = 0; z < length; ++z) {
                  snapshot = BlockSnapShot.snapshot.getBlockAt(x, y, z);
                  snapshot.world = world;
                  if (snapshot.getReplacedBlock().func_185904_a() != Material.field_151594_q) {
                     newPos = this.getPos(x, y, z, basePos, facing);
                     snapshot.restoreToLocationWithRotation(newPos, facing, y, false);
                     if (world.func_180495_p(newPos).func_177230_c() instanceof MultiBlock) {
                        multiblocks.add(newPos);
                     }
                  }
               }
            }
         }

         for(y = 0; y < height; ++y) {
            for(x = 0; x < width; ++x) {
               for(z = 0; z < length; ++z) {
                  snapshot = BlockSnapShot.snapshot.getBlockAt(x, y, z);
                  snapshot.world = world;
                  if (snapshot.getReplacedBlock().func_185904_a() == Material.field_151594_q) {
                     newPos = this.getPos(x, y, z, basePos, facing);
                     snapshot.restoreToLocationWithRotation(newPos, facing, y, false);
                     if (world.func_180495_p(newPos).func_177230_c() instanceof MultiBlock) {
                        multiblocks.add(newPos);
                     }
                  } else if (y + 1 < height) {
                     PixelBlockSnapshot nextSnapshot = BlockSnapShot.snapshot.getBlockAt(x, y, z);
                     snapshot.world = world;
                     if (snapshot.getReplacedBlock().func_177230_c() instanceof BlockDoublePlant && nextSnapshot.getReplacedBlock().func_177230_c() instanceof BlockDoublePlant) {
                        BlockPos newPos = this.getPos(x, y, z, basePos, facing);
                        snapshot.restoreToLocationWithRotation(newPos, facing, y, false);
                     }
                  }
               }
            }
         }

         Iterator var27 = multiblocks.iterator();

         while(var27.hasNext()) {
            BlockPos pos = (BlockPos)var27.next();
            IBlockState state = world.func_180495_p(pos);
            Block block = state.func_177230_c();
            if (block instanceof MultiBlock) {
               EnumFacing blockRot = (EnumFacing)state.func_177229_b(BlockProperties.FACING);
               PixelmonItemBlock.setMultiBlocksWidth(pos, blockRot, world, (MultiBlock)block, block, (EntityPlayer)null);
            }
         }

         var27 = BlockSnapShot.snapshot.getStatues().iterator();

         while(var27.hasNext()) {
            NBTTagCompound statueTag = (NBTTagCompound)var27.next();
            EntityStatue statue = new EntityStatue(world);
            statue.func_70020_e(statueTag);
            double posX = statue.field_70165_t;
            double posY = statue.field_70163_u;
            double posZ = statue.field_70161_v;
            double x = 0.0;
            double y = 0.0;
            double z = 0.0;
            if (facing == EnumFacing.EAST) {
               x = posX + (double)basePos.func_177958_n();
               y = posY + (double)basePos.func_177956_o();
               z = posZ + (double)basePos.func_177952_p();
            } else if (facing == EnumFacing.SOUTH) {
               x = -1.0 * posZ + 1.0 + (double)basePos.func_177958_n();
               y = posY + (double)basePos.func_177956_o();
               z = posX + (double)basePos.func_177952_p();
            } else if (facing == EnumFacing.WEST) {
               x = -1.0 * posX + 1.0 + (double)basePos.func_177958_n();
               y = posY + (double)basePos.func_177956_o();
               z = -1.0 * posZ + 1.0 + (double)basePos.func_177952_p();
            } else if (facing == EnumFacing.NORTH) {
               x = posZ + (double)basePos.func_177958_n();
               y = posY + (double)basePos.func_177956_o();
               z = -1.0 * posX + 1.0 + (double)basePos.func_177952_p();
            }

            if (facing != EnumFacing.EAST) {
               if (facing == EnumFacing.WEST) {
                  statue.setRotation(statue.getRotation() + 180.0F);
               } else if (facing == EnumFacing.NORTH) {
                  statue.setRotation(statue.getRotation() - 90.0F);
               } else if (facing == EnumFacing.SOUTH) {
                  statue.setRotation(statue.getRotation() + 90.0F);
               }

               statue.field_70126_B = statue.field_70177_z;
            }

            statue.func_70107_b(x, y, z);
            statue.func_184221_a(UUID.randomUUID());
            world.func_72838_d(statue);
         }

         CommandChatHandler.sendChat(sender, "pixelmon.command.snapshot.place");
      }
   }

   private BlockPos getPos(int x, int y, int z, BlockPos basePos, EnumFacing facing) {
      if (facing == EnumFacing.EAST) {
         return new BlockPos(x + basePos.func_177958_n(), y + basePos.func_177956_o(), z + basePos.func_177952_p());
      } else if (facing == EnumFacing.SOUTH) {
         return new BlockPos(-1 * z + basePos.func_177958_n(), y + basePos.func_177956_o(), x + basePos.func_177952_p());
      } else if (facing == EnumFacing.WEST) {
         return new BlockPos(-1 * x + basePos.func_177958_n(), y + basePos.func_177956_o(), -1 * z + basePos.func_177952_p());
      } else {
         return facing == EnumFacing.NORTH ? new BlockPos(z + basePos.func_177958_n(), y + basePos.func_177956_o(), -1 * x + basePos.func_177952_p()) : null;
      }
   }
}
