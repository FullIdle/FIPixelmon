package com.pixelmonmod.pixelmon.blocks.tileEntities;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.enums.ReceiveType;
import com.pixelmonmod.pixelmon.api.events.PixelmonReceivedEvent;
import com.pixelmonmod.pixelmon.api.events.PixelmonTradeEvent;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.api.trading.NPCTrades;
import com.pixelmonmod.pixelmon.comm.PixelmonStatsData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.trading.RegisterTrader;
import com.pixelmonmod.pixelmon.comm.packetHandlers.trading.SetSelectedStats;
import com.pixelmonmod.pixelmon.comm.packetHandlers.trading.SetTradeTarget;
import com.pixelmonmod.pixelmon.comm.packetHandlers.trading.TradeReady;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldServer;

public class TileEntityTradeMachine extends TileEntity implements ITickable, ISpecialTexture {
   private ResourceLocation texture = new ResourceLocation("pixelmon:textures/blocks/trademachine/Texture_red.png");
   private String colour = "red";
   private UUID owner = null;
   public int playerCount = 0;
   public EntityPlayerMP player1;
   public EntityPlayerMP player2;
   public boolean ready1;
   public boolean ready2;
   public int pos1 = -1;
   public int pos2 = -1;
   public String user1 = "";
   public String user2 = "";
   public NBTTagCompound poke1;
   public NBTTagCompound poke2;
   private int ticks = 1;
   private boolean tradePushed = false;

   public NBTTagCompound func_189515_b(NBTTagCompound compound) {
      super.func_189515_b(compound);
      compound.func_74778_a("colour", this.colour);
      if (this.owner != null) {
         compound.func_74778_a("owner", this.owner.toString());
      }

      return compound;
   }

   public void func_145839_a(NBTTagCompound compound) {
      super.func_145839_a(compound);
      if (compound.func_74764_b("colour")) {
         this.colour = compound.func_74779_i("colour");
         this.refreshTexture();
      }

      if (compound.func_74764_b("owner")) {
         this.owner = UUID.fromString(compound.func_74779_i("owner"));
      }

   }

   private void refreshTexture() {
      this.texture = new ResourceLocation("pixelmon:textures/blocks/trademachine/Texture_" + this.colour + ".png");
   }

   public String getColour() {
      return this.colour;
   }

   public void setColour(String colour) {
      this.colour = colour;
      this.sendChanges();
      this.func_70296_d();
   }

   public ResourceLocation getTexture() {
      return this.texture;
   }

   public UUID getOwnerUUID() {
      return this.owner;
   }

   public void setOwner(UUID owner) {
      this.owner = owner;
      this.func_70296_d();
   }

   public NBTTagCompound func_189517_E_() {
      NBTTagCompound nbt = new NBTTagCompound();
      this.func_189515_b(nbt);
      nbt.func_74778_a("user1", this.player1 == null ? "" : this.player1.func_70005_c_());
      nbt.func_74778_a("user2", this.player2 == null ? "" : this.player2.func_70005_c_());
      nbt.func_74757_a("ready1", this.ready1);
      nbt.func_74757_a("ready2", this.ready2);
      if (this.poke1 != null) {
         nbt.func_74782_a("poke1", this.poke1);
      }

      if (this.poke2 != null) {
         nbt.func_74782_a("poke2", this.poke2);
      }

      return nbt;
   }

   public SPacketUpdateTileEntity func_189518_D_() {
      return new SPacketUpdateTileEntity(this.field_174879_c, 0, this.func_189517_E_());
   }

   public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet) {
      NBTTagCompound data = packet.func_148857_g();
      this.colour = data.func_74779_i("colour");
      this.user1 = data.func_74779_i("user1");
      this.user2 = data.func_74779_i("user2");
      this.ready1 = data.func_74767_n("ready1");
      this.ready2 = data.func_74767_n("ready2");
      if (data.func_74764_b("poke1")) {
         this.poke1 = data.func_74775_l("poke1").func_82582_d() ? null : data.func_74775_l("poke1");
      } else {
         this.poke1 = null;
      }

      if (data.func_74764_b("poke2")) {
         this.poke2 = data.func_74775_l("poke2").func_82582_d() ? null : data.func_74775_l("poke2");
      } else {
         this.poke2 = null;
      }

      this.refreshTexture();
   }

   public void registerPlayer(EntityPlayerMP player) {
      if (this.playerCount != 1 || this.player1 != player) {
         ++this.playerCount;
         if (this.playerCount == 1) {
            this.player1 = player;
         } else {
            if (this.playerCount != 2) {
               return;
            }

            this.player2 = player;
         }

         OpenScreen.open(player, EnumGuiScreen.Trading, this.field_174879_c.func_177958_n(), this.field_174879_c.func_177956_o(), this.field_174879_c.func_177952_p());
         if (player == this.player2) {
            Pixelmon.network.sendTo(new RegisterTrader(this.player1.func_110124_au(), this.hasMoreThan1HatchedPokemon(this.player1.func_110124_au())), this.player2);
            Pixelmon.network.sendTo(new RegisterTrader(this.player2.func_110124_au(), this.hasMoreThan1HatchedPokemon(this.player2.func_110124_au())), this.player1);
            if (this.pos1 != -1) {
               PlayerPartyStorage party = Pixelmon.storageManager.getParty(this.player1);
               Pixelmon.network.sendTo(new SetTradeTarget(party.get(this.pos1), new PixelmonStatsData(party.get(this.pos1).getStats())), this.player2);
               Pixelmon.network.sendTo(new SetSelectedStats(new PixelmonStatsData(party.get(this.pos1).getStats())), this.player1);
            }
         }

         this.sendChanges();
      }
   }

   public boolean ready(EntityPlayer player, boolean ready) {
      if (this.player1 != null && this.player2 != null) {
         if (player.func_110124_au().equals(this.player1.func_110124_au())) {
            this.ready1 = ready;
            Pixelmon.network.sendTo(new TradeReady(ready), this.player2);
         }

         if (player.func_110124_au().equals(this.player2.func_110124_au())) {
            this.ready2 = ready;
            Pixelmon.network.sendTo(new TradeReady(ready), this.player1);
         }
      }

      this.tradePushed = false;
      this.sendChanges();
      return false;
   }

   public void setPos1(int pos) {
      this.ready1 = false;
      this.ready2 = false;
      PlayerPartyStorage party = Pixelmon.storageManager.getParty(this.player1);
      Pokemon pokemon = party.get(pos);
      if (pokemon != null && !NPCTrades.UNTRADEABLE.matches(pokemon)) {
         this.pos1 = pos;
         pokemon.ifEntityExists((pixelmon) -> {
            pixelmon.unloadEntity();
         });
         this.poke1 = new NBTTagCompound();
         this.poke1.func_74778_a("Name", pokemon.getSpecies().name);
         this.poke1.func_74768_a("Level", pokemon.getLevel());
         this.poke1.func_74757_a("isEgg", pokemon.isEgg());
         this.poke1.func_74768_a("eggCycles", pokemon.getEggCycles());
         this.poke1.func_74768_a("Variant", pokemon.getForm());
         this.poke1.func_74757_a("IsShiny", pokemon.isShiny());
         this.poke1.func_74768_a("CaughtBall", pokemon.getCaughtBall().getIndex());
         if (pokemon.getHeldItem() != ItemStack.field_190927_a) {
            this.poke1.func_74782_a("HeldItemStack", pokemon.getHeldItem().func_77955_b(new NBTTagCompound()));
         }

         this.sendChanges();
         Pixelmon.network.sendTo(new SetSelectedStats(new PixelmonStatsData(pokemon.getStats())), this.player1);
         if (this.player2 != null) {
            Pixelmon.network.sendTo(new SetTradeTarget(pokemon, new PixelmonStatsData(pokemon.getStats())), this.player2);
         }
      } else {
         this.pos1 = -1;
         this.poke1 = null;
         this.sendChanges();
      }
   }

   public void setPos2(int pos) {
      this.ready1 = false;
      this.ready2 = false;
      PlayerPartyStorage party = Pixelmon.storageManager.getParty(this.player2);
      Pokemon pokemon = party.get(pos);
      if (pokemon != null && !NPCTrades.UNTRADEABLE.matches(pokemon)) {
         this.pos2 = pos;
         pokemon.ifEntityExists((pixelmon) -> {
            pixelmon.unloadEntity();
         });
         this.poke2 = new NBTTagCompound();
         this.poke2.func_74778_a("Name", pokemon.getSpecies().name);
         this.poke2.func_74768_a("Level", pokemon.getLevel());
         this.poke2.func_74757_a("isEgg", pokemon.isEgg());
         this.poke2.func_74768_a("eggCycles", pokemon.getEggCycles());
         this.poke2.func_74768_a("Variant", pokemon.getForm());
         this.poke2.func_74757_a("IsShiny", pokemon.isShiny());
         this.poke2.func_74768_a("CaughtBall", pokemon.getCaughtBall().getIndex());
         if (pokemon.getHeldItem() != ItemStack.field_190927_a) {
            this.poke2.func_74782_a("HeldItemStack", pokemon.getHeldItem().func_77955_b(new NBTTagCompound()));
         }

         this.sendChanges();
         Pixelmon.network.sendTo(new SetSelectedStats(new PixelmonStatsData(pokemon.getStats())), this.player2);
         if (this.player1 != null) {
            Pixelmon.network.sendTo(new SetTradeTarget(pokemon, new PixelmonStatsData(pokemon.getStats())), this.player1);
         }
      } else {
         this.pos2 = -1;
         this.poke2 = null;
         this.sendChanges();
      }
   }

   public void removePlayer(EntityPlayer player) {
      this.ready1 = false;
      this.ready2 = false;
      if (this.player1 == player) {
         this.player1 = this.player2;
         this.player2 = null;
         this.pos1 = this.pos2;
         this.pos2 = -1;
         this.poke1 = this.poke2;
         this.poke2 = null;
         --this.playerCount;
      } else if (this.player2 == player) {
         this.player2 = null;
         this.pos2 = -1;
         this.poke2 = null;
         --this.playerCount;
      }

      if (this.playerCount < 0) {
         this.playerCount = 0;
      }

      if (this.playerCount == 1) {
         Pixelmon.network.sendTo(new RegisterTrader((UUID)null, false), this.player1);
         Pixelmon.network.sendTo(new SetTradeTarget(true), this.player1);
      }

      this.sendChanges();
   }

   public void trade() {
      if (!this.tradePushed && this.playerCount >= 2 && this.ready1 && this.ready2) {
         if (this.player1 == this.player2) {
            Pixelmon.LOGGER.warn("Self-trade detected! " + this.player1.func_70005_c_() + " is probably using hacks!");
            this.abortTrade();
         } else {
            PlayerPartyStorage party1 = Pixelmon.storageManager.getParty(this.player1);
            PlayerPartyStorage party2 = Pixelmon.storageManager.getParty(this.player2);
            party1.retrieveAll();
            party2.retrieveAll();
            this.tradePushed = true;
            Pokemon pokemon1 = party1.get(this.pos1);
            Pokemon pokemon2 = party2.get(this.pos2);
            if (pokemon1 != null && pokemon2 != null) {
               if (this.hasOtherHatchedPokemon(this.pos1, party1, pokemon2.isEgg()) && this.hasOtherHatchedPokemon(this.pos2, party2, pokemon1.isEgg())) {
                  if (Pixelmon.EVENT_BUS.post(new PixelmonTradeEvent(this.player1, this.player2, pokemon1, pokemon2))) {
                     this.abortTrade();
                  } else {
                     if (party1.pokedex.get(pokemon2.getSpecies().getNationalPokedexInteger()) != EnumPokedexRegisterStatus.caught && !Pixelmon.EVENT_BUS.post(new PokedexEvent(this.player1.func_110124_au(), pokemon2, EnumPokedexRegisterStatus.caught, "tradePlayer"))) {
                        party1.pokedex.set(pokemon2, EnumPokedexRegisterStatus.caught);
                        party1.pokedex.update();
                     }

                     if (party2.pokedex.get(pokemon1.getSpecies().getNationalPokedexInteger()) != EnumPokedexRegisterStatus.caught && !Pixelmon.EVENT_BUS.post(new PokedexEvent(this.player2.func_110124_au(), pokemon1, EnumPokedexRegisterStatus.caught, "tradePlayer"))) {
                        party2.pokedex.set(pokemon1, EnumPokedexRegisterStatus.caught);
                        party2.pokedex.update();
                     }

                     party1.transfer(party2, new StoragePosition(-1, this.pos2), new StoragePosition(-1, this.pos1));
                     this.player1.func_71053_j();
                     this.player2.func_71053_j();
                     this.playerCount = 0;
                     EntityPixelmon pixelmon1 = pokemon1.getOrSpawnPixelmon(this.player1);
                     EntityPixelmon pixelmon2 = pokemon2.getOrSpawnPixelmon(this.player2);
                     EnumSpecies species1 = pokemon1.getSpecies();
                     EnumSpecies species2 = pokemon2.getSpecies();
                     pokemon1.setFriendship(pokemon1.getBaseStats().getBaseFriendship());
                     if (!pixelmon1.testTradeEvolution(species2)) {
                        pixelmon1.retrieve();
                     }

                     Pixelmon.EVENT_BUS.post(new PixelmonReceivedEvent(this.player1, ReceiveType.Trade, pokemon1));
                     pokemon2.setFriendship(pokemon2.getBaseStats().getBaseFriendship());
                     if (!pixelmon2.testTradeEvolution(species1)) {
                        pixelmon2.retrieve();
                     }

                     Pixelmon.EVENT_BUS.post(new PixelmonReceivedEvent(this.player2, ReceiveType.Trade, pokemon2));
                     this.sendChanges();
                  }
               } else {
                  this.abortTrade();
               }
            } else {
               this.abortTrade();
            }
         }
      }
   }

   public void func_73660_a() {
      if (this.player1 == null && this.playerCount > 0) {
         this.player1 = this.player2;
         this.player2 = null;
         --this.playerCount;
         this.pos2 = -1;
         this.poke2 = null;
      } else if (this.player2 == null && this.playerCount > 1) {
         --this.playerCount;
         this.pos2 = -1;
         this.poke2 = null;
      }

      if (this.playerCount == 1) {
         Pixelmon.network.sendTo(new RegisterTrader((UUID)null, false), this.player1);
      }

      ++this.ticks;
      if (this.ticks % 20 == 0) {
         if (this.player1 != null && this.player1.func_193105_t()) {
            this.abortTrade();
         }

         if (this.player2 != null && this.player2.func_193105_t()) {
            this.abortTrade();
         }

         this.ticks = 0;
      }

   }

   public void sendChanges() {
      if (this.func_145830_o()) {
         ((WorldServer)this.field_145850_b).func_184164_w().func_180244_a(this.field_174879_c);
      }

   }

   private boolean hasOtherHatchedPokemon(int except, PlayerPartyStorage party, boolean isOtherAnEgg) {
      for(int i = 0; i < 6; ++i) {
         if (i != except) {
            Pokemon p = party.get(i);
            if (p != null && !p.isEgg()) {
               return true;
            }
         }
      }

      return !isOtherAnEgg;
   }

   private boolean hasMoreThan1HatchedPokemon(UUID player) {
      if (player == null) {
         return false;
      } else {
         PlayerPartyStorage party1 = Pixelmon.storageManager.getParty(player);
         return this.hasMoreThan1HatchedPokemon(party1);
      }
   }

   private boolean hasMoreThan1HatchedPokemon(PlayerPartyStorage party) {
      int nonEggs = 0;

      for(int i = 0; i < 6; ++i) {
         Pokemon p = party.get(i);
         if (p != null && !p.isEgg()) {
            ++nonEggs;
         }

         if (nonEggs > 1) {
            return true;
         }
      }

      return false;
   }

   private void abortTrade() {
      if (this.player1 != null) {
         this.player1.func_71053_j();
      }

      if (this.player2 != null) {
         this.player2.func_71053_j();
      }

      this.playerCount = 0;
      this.sendChanges();
   }
}
