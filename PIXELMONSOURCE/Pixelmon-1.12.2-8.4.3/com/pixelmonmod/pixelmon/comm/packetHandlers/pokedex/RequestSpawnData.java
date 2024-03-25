package com.pixelmonmod.pixelmon.comm.packetHandlers.pokedex;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.spawning.SpawnSet;
import com.pixelmonmod.pixelmon.api.world.WeatherType;
import com.pixelmonmod.pixelmon.api.world.WorldTime;
import com.pixelmonmod.pixelmon.comm.packetHandlers.ISyncHandler;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import com.pixelmonmod.pixelmon.pokedex.Pokedex;
import com.pixelmonmod.pixelmon.spawning.PixelmonSpawning;
import io.netty.buffer.ByteBuf;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RequestSpawnData implements IMessage {
   public int npn;

   public RequestSpawnData() {
   }

   public RequestSpawnData(int npn) {
      this.npn = npn;
   }

   public void toBytes(ByteBuf byteBuf) {
      byteBuf.writeInt(this.npn);
   }

   public void fromBytes(ByteBuf byteBuf) {
      this.npn = byteBuf.readInt();
   }

   public static class Handler implements ISyncHandler {
      public void onSyncMessage(RequestSpawnData message, MessageContext ctx) {
         EnumPokedexRegisterStatus status = Pixelmon.storageManager.getParty(ctx.getServerHandler().field_147369_b).pokedex.get(message.npn);
         List spawnData = Lists.newArrayList();
         if (status != EnumPokedexRegisterStatus.unknown) {
            EnumSpecies species = EnumSpecies.getFromDex(message.npn);
            List spawns = Arrays.stream(EnumSpecies.LEGENDARY_ENUMS).anyMatch((e) -> {
               return e == species;
            }) ? PixelmonSpawning.legendaries : PixelmonSpawning.standard;
            if (species != null) {
               Iterator var7 = spawns.iterator();

               label85:
               while(true) {
                  SpawnSet spawn;
                  do {
                     if (!var7.hasNext()) {
                        break label85;
                     }

                     spawn = (SpawnSet)var7.next();
                  } while(!spawn.id.equalsIgnoreCase(species.name));

                  Iterator var9 = spawn.spawnInfos.iterator();

                  while(true) {
                     SpawnInfo info;
                     do {
                        do {
                           do {
                              if (!var9.hasNext()) {
                                 continue label85;
                              }

                              info = (SpawnInfo)var9.next();
                           } while(info == null);
                        } while(info.condition == null);
                     } while(info.condition.biomes == null);

                     Pokedex.PokedexSpawnData data;
                     for(Iterator var11 = info.condition.biomes.iterator(); var11.hasNext(); spawnData.add(data)) {
                        Biome biome = (Biome)var11.next();
                        data = new Pokedex.PokedexSpawnData(biome);
                        Iterator var14;
                        if (info.condition.times != null) {
                           var14 = info.condition.times.iterator();

                           while(var14.hasNext()) {
                              WorldTime time = (WorldTime)var14.next();
                              data.addTime(time);
                           }
                        }

                        if (info.condition.weathers != null) {
                           var14 = info.condition.weathers.iterator();

                           while(var14.hasNext()) {
                              WeatherType weather = (WeatherType)var14.next();
                              data.addWeather(weather);
                           }
                        }

                        if (info.condition.maxY != null) {
                           data.setMaxY(info.condition.maxY);
                        }

                        if (info.condition.minY != null) {
                           data.setMinY(info.condition.minY);
                        }
                     }
                  }
               }
            }
         }

         Pixelmon.network.sendTo(new SendSpawnData(message.npn, spawnData), ctx.getServerHandler().field_147369_b);
      }
   }
}
