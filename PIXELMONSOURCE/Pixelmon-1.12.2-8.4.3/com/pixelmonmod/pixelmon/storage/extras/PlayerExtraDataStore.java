package com.pixelmonmod.pixelmon.storage.extras;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.PixelExtrasDisplayPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.RequestExtrasDisplayData;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerExtraDataStore {
   private static PixelExtrasData client = new PixelExtrasData(Minecraft.func_71410_x().func_110432_I().func_148256_e().getId());
   private static final Map playerExtras = new HashMap();

   public static void add(PixelExtrasData data) {
      if (Objects.equals(client.id, data.id)) {
         client.updateServerCosmetics(data.getServerCosmetics());
      } else {
         playerExtras.put(data.id, data);
      }
   }

   public static PixelExtrasData get(EntityPlayer player) {
      return get(player.func_110124_au());
   }

   public static PixelExtrasData get(UUID uuid) {
      if (Objects.equals(client.id, uuid)) {
         return client;
      } else {
         if (!playerExtras.containsKey(uuid)) {
            playerExtras.put(uuid, new PixelExtrasData(uuid));
            Pixelmon.network.sendToServer(new RequestExtrasDisplayData(uuid));
         }

         return (PixelExtrasData)playerExtras.get(uuid);
      }
   }

   public static boolean canSeeTexture(EntityPlayer owner, EnumSpecies species) {
      return owner != null && get(owner).canSeeTexture(species);
   }

   public static void refreshClient(Consumer consumer) {
      ExtrasContact.updateSelf(client, true, consumer.andThen((data) -> {
         saveAndRefresh();
      }));
   }

   public static void saveAndSend() {
      sendDisplayPacket();
      saveClientData();
   }

   public static void saveAndRefresh() {
      saveClientData();
      if (client.isReady() && client.hasData()) {
         PixelExtrasDisplayPacket packet = new PixelExtrasDisplayPacket(client);
         packet.dataHash = -1;
         Pixelmon.network.sendToServer(packet);
      }

   }

   public static void sendDisplayPacket() {
      if (client.isReady() && client.hasData()) {
         Pixelmon.network.sendToServer(new PixelExtrasDisplayPacket(client));
      }

   }

   public static void loadClientData() {
      UUID uuid = Minecraft.func_71410_x().func_110432_I().func_148256_e().getId();
      PixelExtrasData data = new PixelExtrasData(uuid);
      File file = new File(Minecraft.func_71410_x().field_71412_D, "pixel.dat");
      if (file.exists()) {
         NBTTagCompound compound = new NBTTagCompound();

         try {
            FileInputStream in = new FileInputStream(file);
            Throwable var5 = null;

            try {
               compound = CompressedStreamTools.func_74796_a(in);
            } catch (Throwable var15) {
               var5 = var15;
               throw var15;
            } finally {
               if (in != null) {
                  if (var5 != null) {
                     try {
                        in.close();
                     } catch (Throwable var14) {
                        var5.addSuppressed(var14);
                     }
                  } else {
                     in.close();
                  }
               }

            }
         } catch (IOException var17) {
            System.out.println(file.getAbsolutePath());
            var17.printStackTrace();
         }

         if (compound.func_74764_b(uuid.toString())) {
            NBTTagCompound nbt = compound.func_74775_l(uuid.toString());
            ExtrasContact.updateSelf(data, false, (newData) -> {
               client = newData;
               newData.setHatType(PixelExtrasData.HatType.getFromId(nbt.func_74771_c("hatType")));
               newData.setMonocleType(PixelExtrasData.MonocleType.getFromId(nbt.func_74771_c("monocleType")));
               if (nbt.func_74764_b("sashType")) {
                  newData.setSashType(PixelExtrasData.SashType.values()[nbt.func_74771_c("sashType")]);
               }

               if (nbt.func_74764_b("robeType")) {
                  newData.setRobeType(PixelExtrasData.RobeType.values()[nbt.func_74771_c("robeType")]);
               }

               if (nbt.func_74764_b("sphealType")) {
                  newData.setSphealType(PixelExtrasData.SphealType.values()[nbt.func_74771_c("sphealType")]);
               }

               PixelExtrasData.Category[] var2 = PixelExtrasData.Category.values();
               int var3 = var2.length;

               for(int var4 = 0; var4 < var3; ++var4) {
                  PixelExtrasData.Category category = var2[var4];
                  if (nbt.func_74764_b("en" + category.name().toLowerCase())) {
                     newData.setEnabled(category, nbt.func_74767_n("en" + category.name().toLowerCase()));
                  } else {
                     newData.setEnabled(category, true);
                  }

                  if (nbt.func_74764_b("c" + category.name().toLowerCase())) {
                     newData.setColours(category, nbt.func_74759_k("c" + category.name().toLowerCase()));
                  }
               }

               saveClientData();
            });
            return;
         }
      }

      ExtrasContact.updateSelf(data, true, (newData) -> {
         client = newData;
         saveClientData();
      });
   }

   public static void saveClientData() {
      File file = new File(Minecraft.func_71410_x().field_71412_D, "pixel.dat");
      NBTTagCompound compound = new NBTTagCompound();
      if (!file.exists()) {
         try {
            file.createNewFile();
         } catch (IOException var34) {
            var34.printStackTrace();
            return;
         }
      } else {
         try {
            FileInputStream in = new FileInputStream(file);
            Throwable var3 = null;

            try {
               compound = CompressedStreamTools.func_74796_a(in);
            } catch (Throwable var33) {
               var3 = var33;
               throw var33;
            } finally {
               if (in != null) {
                  if (var3 != null) {
                     try {
                        in.close();
                     } catch (Throwable var32) {
                        var3.addSuppressed(var32);
                     }
                  } else {
                     in.close();
                  }
               }

            }
         } catch (IOException var38) {
            var38.printStackTrace();
         }
      }

      NBTTagCompound nbt = new NBTTagCompound();
      nbt.func_74774_a("hatType", (byte)client.getHatType().id);
      nbt.func_74774_a("monocleType", (byte)client.getMonocleType().id);
      if (client.sashType != null) {
         nbt.func_74774_a("sashType", (byte)client.getSashType().ordinal());
      }

      if (client.robeType != null) {
         nbt.func_74774_a("robeType", (byte)client.getRobeType().ordinal());
      }

      if (client.sphealType != null) {
         nbt.func_74774_a("sphealType", (byte)client.getSphealType().ordinal());
      }

      PixelExtrasData.Category[] var40 = PixelExtrasData.Category.values();
      int var4 = var40.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         PixelExtrasData.Category category = var40[var5];
         nbt.func_74757_a("en" + category.name().toLowerCase(), client.isEnabled(category));
         if (client.getColours(category) != null) {
            nbt.func_74783_a("c" + category.name().toLowerCase(), client.getColours(category));
         }
      }

      compound.func_74782_a(client.id.toString(), nbt);

      try {
         FileOutputStream out = new FileOutputStream(file);
         Throwable var42 = null;

         try {
            CompressedStreamTools.func_74799_a(compound, out);
         } catch (Throwable var31) {
            var42 = var31;
            throw var31;
         } finally {
            if (out != null) {
               if (var42 != null) {
                  try {
                     out.close();
                  } catch (Throwable var30) {
                     var42.addSuppressed(var30);
                  }
               } else {
                  out.close();
               }
            }

         }
      } catch (IOException var36) {
         var36.printStackTrace();
      }

   }
}
