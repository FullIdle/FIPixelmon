package com.pixelmonmod.pixelmon.storage;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.economy.IPixelmonBankAccount;
import com.pixelmonmod.pixelmon.api.enums.BattleEndTaskResult;
import com.pixelmonmod.pixelmon.api.enums.BattleEndTaskType;
import com.pixelmonmod.pixelmon.api.enums.EnumTriBoolean;
import com.pixelmonmod.pixelmon.api.enums.ServerCosmetics;
import com.pixelmonmod.pixelmon.api.events.EconomyEvent;
import com.pixelmonmod.pixelmon.api.events.PokedexEvent;
import com.pixelmonmod.pixelmon.api.events.PokerusEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.spawning.AbstractSpawner;
import com.pixelmonmod.pixelmon.api.spawning.IRarityTweak;
import com.pixelmonmod.pixelmon.api.spawning.SpawnInfo;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.api.storage.TransientData;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.comm.packetHandlers.PixelExtrasDisplayPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.QueryResourceLocationPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.CloseBattle;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.UpdateClientPlayerData;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Pokerus;
import com.pixelmonmod.pixelmon.enums.EnumBerryFlavor;
import com.pixelmonmod.pixelmon.enums.EnumCurryKey;
import com.pixelmonmod.pixelmon.enums.EnumCurryRating;
import com.pixelmonmod.pixelmon.enums.EnumFeatureState;
import com.pixelmonmod.pixelmon.enums.EnumMegaItem;
import com.pixelmonmod.pixelmon.enums.EnumMegaItemsUnlocked;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumTrainerCardColor;
import com.pixelmonmod.pixelmon.items.ItemLure;
import com.pixelmonmod.pixelmon.listener.EntityPlayerExtension;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import com.pixelmonmod.pixelmon.pokedex.Pokedex;
import com.pixelmonmod.pixelmon.quests.comm.ResetQuests;
import com.pixelmonmod.pixelmon.storage.extras.PixelExtrasData;
import com.pixelmonmod.pixelmon.storage.extras.PixelExtrasStorage;
import com.pixelmonmod.pixelmon.storage.playerData.PlayerData;
import com.pixelmonmod.pixelmon.storage.playerData.QuestData;
import com.pixelmonmod.pixelmon.storage.playerData.TeleportPosition;
import com.pixelmonmod.pixelmon.util.helpers.CommonHelper;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class PlayerPartyStorage extends PartyStorage implements IPixelmonBankAccount, IRarityTweak {
   protected String playerName;
   public EnumTrainerCardColor trainerCardColor;
   public TeleportPosition teleportPos;
   protected int pokeDollars;
   public Pokedex pokedex;
   public PlayerStats stats;
   public boolean guiOpened;
   public boolean battleEnabled;
   public boolean starterPicked;
   public boolean oldGen;
   private int ticksTillEncounter;
   private EnumMegaItemsUnlocked megaItemsUnlocked;
   private EnumMegaItem megaItem;
   private ItemStack lure;
   private EnumFeatureState shinyCharm;
   private EnumFeatureState ovalCharm;
   private EnumFeatureState expCharm;
   private EnumFeatureState catchingCharm;
   private EnumFeatureState markCharm;
   private EnumSet serverCosmetics;
   private int[] curryData;
   private HashMap megasObtained;
   public PlayerData playerData;
   private QuestData questData;
   public transient TransientData transientData;

   public PlayerPartyStorage(UUID uuid, boolean shouldSendUpdates) {
      super(uuid);
      this.playerName = null;
      this.trainerCardColor = EnumTrainerCardColor.WHITE;
      this.teleportPos = new TeleportPosition();
      this.pokeDollars = 0;
      this.pokedex = new Pokedex();
      this.stats = new PlayerStats();
      this.guiOpened = false;
      this.battleEnabled = true;
      this.starterPicked = false;
      this.oldGen = false;
      this.megaItemsUnlocked = EnumMegaItemsUnlocked.None;
      this.megaItem = EnumMegaItem.Disabled;
      this.lure = ItemStack.field_190927_a;
      this.shinyCharm = EnumFeatureState.Disabled;
      this.ovalCharm = EnumFeatureState.Disabled;
      this.expCharm = EnumFeatureState.Disabled;
      this.catchingCharm = EnumFeatureState.Disabled;
      this.markCharm = EnumFeatureState.Disabled;
      this.serverCosmetics = EnumSet.noneOf(ServerCosmetics.class);
      this.curryData = new int[26];
      this.megasObtained = new HashMap();
      this.playerData = new PlayerData();
      this.questData = new QuestData();
      this.transientData = new TransientData();
      this.shouldSendUpdates = shouldSendUpdates;
      this.pokedex.setUUID(uuid);
      if (shouldSendUpdates) {
         this.tryUpdatePlayerName();
      }

   }

   public PlayerPartyStorage(UUID uuid) {
      this(uuid, true);
   }

   public boolean add(Pokemon pokemon) {
      if (!super.add(pokemon)) {
         PCStorage pc = Pixelmon.storageManager.getPCForPlayer(this.uuid);
         EntityPlayerMP player = this.getPlayer();
         if (player != null) {
            player.func_145747_a(new TextComponentTranslation("pixelmon.storage.partyfull", new Object[]{pokemon.getDisplayName()}));
         }

         if (!pc.add(pokemon)) {
            if (player != null) {
               player.func_145747_a(new TextComponentTranslation("pixelmon.storage.compfull", new Object[0]));
            }

            return false;
         }
      }

      return true;
   }

   public EnumTriBoolean addSilently(Pokemon pokemon) {
      if (!super.add(pokemon)) {
         PCStorage pc = Pixelmon.storageManager.getPCForPlayer(this.uuid);
         return pc.add(pokemon) ? EnumTriBoolean.FALSE : EnumTriBoolean.NULL;
      } else {
         return EnumTriBoolean.TRUE;
      }
   }

   public void tryUpdatePlayerName() {
      if (this.shouldSendUpdates) {
         EntityPlayerMP player = FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_177451_a(this.uuid);
         if (player != null) {
            this.playerName = player.func_70005_c_();
         }
      }

   }

   @Nullable
   public EntityPlayerMP getPlayer() {
      return FMLCommonHandler.instance().getMinecraftServerInstance() == null ? null : FMLCommonHandler.instance().getMinecraftServerInstance().func_184103_al().func_177451_a(this.uuid);
   }

   @Nullable
   public String getPlayerName() {
      return this.playerName;
   }

   public UUID getPlayerUUID() {
      return this.uuid;
   }

   public void set(StoragePosition position, Pokemon pokemon) {
      super.set(position, pokemon);
      if (pokemon != null && this.playerName != null && (pokemon.getOriginalTrainer() == null || pokemon.getOriginalTrainer().equals(this.playerName) && pokemon.getOriginalTrainerUUID() == null)) {
         pokemon.setOriginalTrainer(this.uuid, this.playerName);
      }

      if (this.getShouldSendUpdates()) {
         if (this.getPlayer() != null) {
            EntityPlayerExtension.updatePlayerPokeballs(this.getPlayer(), this.getAll());
         }

         if (pokemon != null && !pokemon.isEgg() && this.pokedex.getSeenMap().get(pokemon.getSpecies().getNationalPokedexInteger()) != EnumPokedexRegisterStatus.caught && !Pixelmon.EVENT_BUS.post(new PokedexEvent(this.uuid, pokemon, EnumPokedexRegisterStatus.caught, "storageMovement"))) {
            this.pokedex.set(pokemon, EnumPokedexRegisterStatus.caught);
            this.pokedex.update();
         }
      }

   }

   public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
      if (this.playerName != null) {
         nbt.func_74778_a("player", this.playerName);
      }

      this.playerData.writeToNBT(nbt);
      this.teleportPos.writeToNBT(nbt);
      nbt.func_74783_a("curryDex", this.curryData);
      nbt.func_74768_a("pixelDollars", this.pokeDollars);
      nbt.func_74768_a("trainerCardColor", this.trainerCardColor.ordinal());
      nbt.func_74757_a("starterPicked", this.starterPicked);
      nbt.func_74757_a("oldGen", this.oldGen);
      this.pokedex.writeToNBT(nbt);
      this.stats.writeToNBT(nbt);
      nbt.func_74768_a("TicksTillNextEncounter", this.ticksTillEncounter);
      nbt.func_74778_a("MegaItemString", this.megaItem.toString());
      if (this.megaItem.canEvolve() && this.megaItemsUnlocked.isNone()) {
         this.setMegaItemsUnlocked(EnumMegaItemsUnlocked.Mega);
      }

      nbt.func_74768_a("MegaItemsUnlocked", this.megaItemsUnlocked.ordinal());
      nbt.func_74768_a("ShinyCharmID", this.shinyCharm.ordinal());
      nbt.func_74768_a("OvalCharmID", this.ovalCharm.ordinal());
      nbt.func_74768_a("ExpCharmID", this.expCharm.ordinal());
      nbt.func_74768_a("CatchingCharmID", this.catchingCharm.ordinal());
      nbt.func_74768_a("MarkCharmID", this.markCharm.ordinal());
      BitSet set = new BitSet();
      Iterator var3 = this.serverCosmetics.iterator();

      while(var3.hasNext()) {
         ServerCosmetics cosmetics = (ServerCosmetics)var3.next();
         set.set(cosmetics.ordinal());
      }

      nbt.func_74773_a("ServCosm", set.toByteArray());
      NBTTagList tagList = new NBTTagList();
      Iterator var9 = this.megasObtained.keySet().iterator();

      while(var9.hasNext()) {
         EnumSpecies pokemon = (EnumSpecies)var9.next();
         NBTTagCompound megaObtainedNBT = new NBTTagCompound();
         megaObtainedNBT.func_74778_a("Name", pokemon.toString());
         int[] forms = (int[])this.megasObtained.get(pokemon);
         megaObtainedNBT.func_74783_a("Variant", forms);
         tagList.func_74742_a(megaObtainedNBT);
      }

      nbt.func_74782_a("MegasObtained", tagList);
      this.questData.writeToNBT(nbt);
      NBTTagCompound lureTag = new NBTTagCompound();
      this.lure.func_77955_b(lureTag);
      nbt.func_74782_a("Lure", lureTag);
      return super.writeToNBT(nbt);
   }

   public PlayerPartyStorage readFromNBT(NBTTagCompound nbt) {
      super.readFromNBT(nbt);
      this.playerName = nbt.func_74779_i("player");
      this.playerData.readFromNBT(nbt);
      this.teleportPos.readFromNBT(nbt);
      this.pokeDollars = nbt.func_74762_e("pixelDollars");
      this.trainerCardColor = EnumTrainerCardColor.values()[nbt.func_74762_e("trainerCardColor")];
      if (nbt.func_74764_b("curryDex")) {
         this.curryData = nbt.func_74759_k("curryDex");
      }

      if (nbt.func_74764_b("starterPicked")) {
         this.starterPicked = nbt.func_74767_n("starterPicked");
      }

      if (nbt.func_74764_b("oldGen")) {
         this.oldGen = nbt.func_74767_n("oldGen");
      }

      if (nbt.func_74764_b("MegaItemString")) {
         this.setMegaItem(EnumMegaItem.getFromString(nbt.func_74779_i("MegaItemString")), false);
      }

      if (nbt.func_74764_b("MegaItemsUnlocked")) {
         this.setMegaItemsUnlocked(EnumMegaItemsUnlocked.values()[nbt.func_74762_e("MegaItemsUnlocked")]);
      } else if (this.getMegaItem().canEvolve()) {
         this.setMegaItemsUnlocked(EnumMegaItemsUnlocked.Mega);
      }

      if (nbt.func_74764_b("ShinyCharmID")) {
         this.setShinyCharm(EnumFeatureState.values()[nbt.func_74762_e("ShinyCharmID")]);
      }

      if (nbt.func_74764_b("OvalCharmID")) {
         this.setOvalCharm(EnumFeatureState.values()[nbt.func_74762_e("OvalCharmID")]);
      }

      if (nbt.func_74764_b("ExpCharmID")) {
         this.setExpCharm(EnumFeatureState.values()[nbt.func_74762_e("ExpCharmID")]);
      }

      if (nbt.func_74764_b("CatchingCharmID")) {
         this.setCatchingCharm(EnumFeatureState.values()[nbt.func_74762_e("CatchingCharmID")]);
      }

      if (nbt.func_74764_b("MarkCharmID")) {
         this.setMarkCharm(EnumFeatureState.values()[nbt.func_74762_e("MarkCharmID")]);
      }

      this.serverCosmetics.clear();
      if (nbt.func_74764_b("HweenRobe")) {
         this.serverCosmetics.add(ServerCosmetics.DROWNED_ROBE);
      }

      if (nbt.func_74764_b("ServCosm")) {
         BitSet set = BitSet.valueOf(nbt.func_74770_j("ServCosm"));
         ServerCosmetics[] var3 = ServerCosmetics.values();
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            ServerCosmetics cosmetics = var3[var5];
            if (set.get(cosmetics.ordinal())) {
               this.serverCosmetics.add(cosmetics);
            }
         }
      }

      this.pokedex.readFromNBT(nbt);
      this.stats.readFromNBT(nbt);
      if (nbt.func_74764_b("MegasObtained")) {
         NBTTagList list = nbt.func_150295_c("MegasObtained", 10);

         for(int i = 0; i < list.func_74745_c(); ++i) {
            NBTTagCompound megaInfo = (NBTTagCompound)list.func_179238_g(i);
            EnumSpecies pokemon = EnumSpecies.get(megaInfo.func_74779_i("Name"));
            int[] forms = megaInfo.func_74759_k("Variant");
            this.megasObtained.put(pokemon, forms);
         }
      }

      this.questData.readFromNBT(nbt);
      if (nbt.func_74764_b("Lure")) {
         NBTTagCompound lureTag = nbt.func_74775_l("Lure");
         ItemStack lure = new ItemStack(lureTag);
         this.lure = lure;
      }

      return this;
   }

   public List getPlayersToUpdate() {
      EntityPlayerMP player = this.getPlayer();
      return player == null ? new ArrayList() : Lists.newArrayList(new EntityPlayerMP[]{player});
   }

   public boolean getShouldSave() {
      return this.getPlayer() == null ? true : super.getShouldSave();
   }

   public int[] getCurryData() {
      return this.curryData;
   }

   public void updateSingleCurryData(EnumCurryKey curryKey, EnumBerryFlavor cookingFlavor, EnumCurryRating rating) {
      byte[] cd = CommonHelper.decodeInteger(this.curryData[curryKey.ordinal()], 3);
      if (cd[cookingFlavor.ordinal()] < rating.ordinal() + 1) {
         cd[cookingFlavor.ordinal()] = (byte)(rating.ordinal() + 1);
         this.curryData[curryKey.ordinal()] = CommonHelper.encodeInteger(cd, 3);
      }

   }

   public boolean isMegaItemObtained(EnumSpecies pokemon, int form) {
      if (this.megasObtained.containsKey(pokemon)) {
         int[] var3 = (int[])this.megasObtained.get(pokemon);
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            int f = var3[var5];
            if (f == form) {
               return true;
            }
         }
      }

      return false;
   }

   public void obtainedItem(EnumSpecies pokemon, int form, EntityPlayerMP player) {
      if (this.megasObtained.containsKey(pokemon)) {
         int[] forms = (int[])this.megasObtained.get(pokemon);
         int[] newForms = forms;
         int var6 = forms.length;

         for(int var7 = 0; var7 < var6; ++var7) {
            int f = newForms[var7];
            if (f == form) {
               return;
            }
         }

         newForms = new int[forms.length + 1];
         System.arraycopy(forms, 0, newForms, 0, forms.length);
         newForms[newForms.length - 1] = form;
         this.megasObtained.put(pokemon, newForms);
      } else {
         this.megasObtained.put(pokemon, new int[]{form});
      }

      this.unlockMega();
   }

   public void setMegaItem(EnumMegaItem megaItem, boolean giveChoice) {
      this.setMegaItem(megaItem, giveChoice ? 0 : -1);
   }

   public void setMegaItem(EnumMegaItem megaItem, int giveChoice) {
      this.megaItem = megaItem;
      if (this.getPlayer() != null) {
         if (giveChoice >= 0) {
            Pixelmon.network.sendTo(new UpdateClientPlayerData(this.megaItem, giveChoice), this.getPlayer());
         }

         EntityPlayerExtension.updatePlayerMegaItem(this.getPlayer(), this.megaItem);
      }

      this.setNeedsSaving();
   }

   public void unlockMega() {
      if (!this.getMegaItemsUnlocked().canMega()) {
         if (this.getMegaItemsUnlocked().canDynamax()) {
            this.setMegaItemsUnlocked(EnumMegaItemsUnlocked.Both);
         } else {
            this.setMegaItemsUnlocked(EnumMegaItemsUnlocked.Mega);
            this.setMegaItem(EnumMegaItem.None, true);
         }
      }

   }

   public void lockMega() {
      if (this.getMegaItemsUnlocked().canMega()) {
         if (this.getMegaItemsUnlocked().canDynamax()) {
            this.setMegaItemsUnlocked(EnumMegaItemsUnlocked.Dynamax);
            if (this.getMegaItem().canMega()) {
               this.setMegaItem(EnumMegaItem.None, false);
            }
         } else {
            this.setMegaItemsUnlocked(EnumMegaItemsUnlocked.None);
         }
      }

   }

   public void unlockDynamax() {
      if (!this.getMegaItemsUnlocked().canDynamax()) {
         if (this.getMegaItemsUnlocked().canMega()) {
            this.setMegaItemsUnlocked(EnumMegaItemsUnlocked.Both);
         } else {
            this.setMegaItemsUnlocked(EnumMegaItemsUnlocked.Dynamax);
            this.setMegaItem(EnumMegaItem.None, true);
         }
      }

   }

   public void lockDynamax() {
      if (this.getMegaItemsUnlocked().canDynamax()) {
         if (this.getMegaItemsUnlocked().canMega()) {
            this.setMegaItemsUnlocked(EnumMegaItemsUnlocked.Mega);
            if (this.getMegaItem().canDynamax()) {
               this.setMegaItem(EnumMegaItem.None, false);
            }
         } else {
            this.setMegaItemsUnlocked(EnumMegaItemsUnlocked.None);
         }
      }

   }

   public void setMegaItemsUnlocked(EnumMegaItemsUnlocked megaItemsUnlocked) {
      this.megaItemsUnlocked = megaItemsUnlocked;
      if (this.getPlayer() != null) {
         EntityPlayerExtension.updatePlayerMegaItemsUnlocked(this.getPlayer(), this.megaItemsUnlocked);
      }

      this.setNeedsSaving();
   }

   public void setShinyCharm(EnumFeatureState shinyCharm) {
      this.shinyCharm = shinyCharm;
      if (this.getPlayer() != null) {
         EntityPlayerExtension.updatePlayerShinyCharm(this.getPlayer(), this.shinyCharm);
      }

      this.setNeedsSaving();
   }

   public void setOvalCharm(EnumFeatureState ovalCharm) {
      this.ovalCharm = ovalCharm;
      if (this.getPlayer() != null) {
         EntityPlayerExtension.updatePlayerOvalCharm(this.getPlayer(), this.ovalCharm);
      }

      this.setNeedsSaving();
   }

   public void setExpCharm(EnumFeatureState expCharm) {
      this.expCharm = expCharm;
      if (this.getPlayer() != null) {
         EntityPlayerExtension.updatePlayerExpCharm(this.getPlayer(), this.expCharm);
      }

      this.setNeedsSaving();
   }

   public void setCatchingCharm(EnumFeatureState catchingCharm) {
      this.catchingCharm = catchingCharm;
      if (this.getPlayer() != null) {
         EntityPlayerExtension.updatePlayerCatchingCharm(this.getPlayer(), this.catchingCharm);
      }

      this.setNeedsSaving();
   }

   public void setMarkCharm(EnumFeatureState markCharm) {
      this.markCharm = markCharm;
      if (this.getPlayer() != null) {
         EntityPlayerExtension.updatePlayerMarkCharm(this.getPlayer(), this.markCharm);
      }

      this.setNeedsSaving();
   }

   /** @deprecated */
   @Deprecated
   public void setHweenRobe(EnumFeatureState hweenRobe) {
      if (hweenRobe.isAvailable()) {
         this.grantServerCosmetics(ServerCosmetics.DROWNED_ROBE);
      } else {
         this.revokeServerCosmetics(ServerCosmetics.DROWNED_ROBE);
      }

      this.setNeedsSaving();
   }

   public boolean canEquipMegaItem() {
      return this.megaItem != EnumMegaItem.Disabled;
   }

   public EnumMegaItem getMegaItem() {
      return this.megaItem;
   }

   public EnumMegaItemsUnlocked getMegaItemsUnlocked() {
      return this.megaItemsUnlocked;
   }

   public EnumFeatureState getShinyCharm() {
      return this.shinyCharm;
   }

   public EnumFeatureState getOvalCharm() {
      return this.ovalCharm;
   }

   public EnumFeatureState getExpCharm() {
      return this.expCharm;
   }

   public EnumFeatureState getCatchingCharm() {
      return this.catchingCharm;
   }

   public EnumFeatureState getMarkCharm() {
      return this.markCharm;
   }

   /** @deprecated */
   @Deprecated
   public EnumFeatureState getHweenRobe() {
      return this.getServerCosmetics().contains(ServerCosmetics.DROWNED_ROBE) ? EnumFeatureState.Available : EnumFeatureState.Disabled;
   }

   public Set getServerCosmetics() {
      return ImmutableSet.copyOf(this.serverCosmetics);
   }

   public void setServerCosmetics(Set cosmetics) {
      if (cosmetics.isEmpty()) {
         this.serverCosmetics = EnumSet.noneOf(ServerCosmetics.class);
      } else {
         this.serverCosmetics = EnumSet.copyOf(cosmetics);
      }

      PixelExtrasData data = PixelExtrasStorage.getData(this.uuid);
      data.updateServerCosmetics(this.serverCosmetics);
      Pixelmon.network.sendToAll(new PixelExtrasDisplayPacket(data));
      this.setNeedsSaving();
   }

   public void grantServerCosmetics(ServerCosmetics... cosmetics) {
      this.serverCosmetics.addAll(Arrays.asList(cosmetics));
      PixelExtrasData data = PixelExtrasStorage.getData(this.uuid);
      data.updateServerCosmetics(this.serverCosmetics);
      Pixelmon.network.sendToAll(new PixelExtrasDisplayPacket(data));
      this.setNeedsSaving();
   }

   public void revokeServerCosmetics(ServerCosmetics... cosmetics) {
      this.serverCosmetics.removeAll(Arrays.asList(cosmetics));
      PixelExtrasData data = PixelExtrasStorage.getData(this.uuid);
      data.updateServerCosmetics(this.serverCosmetics);
      Pixelmon.network.sendToAll(new PixelExtrasDisplayPacket(data));
      this.setNeedsSaving();
   }

   public ItemStack getLureStack() {
      return this.lure;
   }

   @Nullable
   public ItemLure getLure() {
      return this.lure.func_77973_b() instanceof ItemLure ? (ItemLure)this.lure.func_77973_b() : null;
   }

   public void setLureStack(ItemStack stack) {
      if (stack == null || stack.func_190926_b() || stack.func_77973_b() instanceof ItemLure) {
         if (stack != null && !stack.func_190926_b()) {
            this.lure = stack;
         } else {
            this.lure = ItemStack.field_190927_a;
         }

         this.setHasChanged(true);
         if (this.getShouldSendUpdates()) {
            EntityPlayerMP player = this.getPlayer();
            if (player != null) {
               Pixelmon.network.sendTo(new UpdateClientPlayerData(this.lure), player);
            }
         }

      }
   }

   public void setLure(ItemLure lure) {
      if (lure == null) {
         this.setLureStack(ItemStack.field_190927_a);
      } else {
         this.setLureStack(new ItemStack(lure));
      }

   }

   private void setCurrency(int amount) {
      if (this.pokeDollars != amount) {
         this.pokeDollars = amount;
         if (this.pokeDollars > 999999) {
            this.pokeDollars = 999999;
         } else if (this.pokeDollars < 0) {
            this.pokeDollars = 0;
         }

         this.updatePlayer(this.pokeDollars);
         this.setNeedsSaving();
      }
   }

   public int getMoney() {
      EconomyEvent.GetBalanceEvent getBalanceEvent = new EconomyEvent.GetBalanceEvent(this.getPlayer(), this.pokeDollars);
      Pixelmon.EVENT_BUS.post(getBalanceEvent);
      return getBalanceEvent.balance;
   }

   public void setMoney(int amount) {
      this.setCurrency(amount);
   }

   public int changeMoney(int change) {
      int oldBalance = this.pokeDollars;
      EconomyEvent.transactionType type = change < 0 ? EconomyEvent.transactionType.withdraw : EconomyEvent.transactionType.deposit;
      EconomyEvent.PreTransactionEvent preEvent = new EconomyEvent.PreTransactionEvent(this.getPlayer(), type, this.pokeDollars, change);
      if (!Pixelmon.EVENT_BUS.post(preEvent)) {
         this.setCurrency(this.pokeDollars + preEvent.change);
         Pixelmon.EVENT_BUS.post(new EconomyEvent.PostTransactionEvent(this.getPlayer(), type, oldBalance, this.pokeDollars));
      }

      return this.pokeDollars;
   }

   public UUID getOwnerUUID() {
      return this.uuid;
   }

   public void updatePartyCache() {
      this.transientData.lowestLevel = 100;
      this.transientData.highestLevel = 1;
      this.transientData.averageLevel = 0;
      int count = 0;
      Pokemon[] var2 = this.party;
      int var3 = var2.length;

      TransientData var10000;
      for(int var4 = 0; var4 < var3; ++var4) {
         Pokemon pokemon = var2[var4];
         if (pokemon != null && !pokemon.isEgg()) {
            int level = pokemon.getLevel();
            ++count;
            var10000 = this.transientData;
            var10000.averageLevel += level;
            this.transientData.lowestLevel = Math.min(this.transientData.lowestLevel, level);
            this.transientData.highestLevel = Math.max(this.transientData.highestLevel, level);
         }
      }

      if (count == 0) {
         this.transientData.averageLevel = 50;
         this.transientData.lowestLevel = 1;
         this.transientData.highestLevel = 100;
      } else {
         var10000 = this.transientData;
         var10000.averageLevel /= count;
      }

   }

   public int getTicksTillEncounter() {
      return this.ticksTillEncounter;
   }

   public void updateTicksTillEncounter() {
      if (this.ticksTillEncounter <= 1) {
         this.ticksTillEncounter = this.getPlayer().func_70681_au().nextInt(900) + 100;
      } else {
         --this.ticksTillEncounter;
      }

   }

   public void checkPokerus() {
      EntityPlayerMP player = this.getPlayer();
      Pokemon[] var2 = this.getAll();
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Pokemon pokemon = var2[var4];
         if (pokemon != null && !pokemon.isEgg() && pokemon.getPokerus() != null) {
            Pokerus pokerus = pokemon.getPokerus();
            if (pokerus.canInfect()) {
               int duration = pokerus.type.duration;
               if (duration > -1) {
                  int seconds = pokerus.secondsSinceInfection;
                  if (seconds != -1 && seconds < duration) {
                     ++seconds;
                     if (seconds >= duration) {
                        pokerus.secondsSinceInfection = -1;
                        Pixelmon.EVENT_BUS.post(new PokerusEvent.Cured(player, pokemon, this));
                     } else {
                        pokerus.secondsSinceInfection = seconds;
                     }

                     pokemon.markDirty(EnumUpdateType.Pokerus);
                  }
               }
            }
         }
      }

   }

   public float getMultiplier(AbstractSpawner spawner, SpawnInfo spawnInfo, float sum, float rarity) {
      float modifiedValue = 1.0F;
      if (this.getLure() != null) {
         modifiedValue *= this.getLure().getMultiplier(spawner, spawnInfo, sum, rarity);
      }

      if (this.getTeam().size() > 0) {
         modifiedValue *= ((Pokemon)this.getTeam().get(0)).getAbility().getMultiplier(spawner, spawnInfo, sum, rarity);
      }

      return modifiedValue;
   }

   public QuestData getQuestData(boolean update) {
      return this.questData.get(this.getPlayer(), update);
   }

   public QuestData getQuestData() {
      return this.getQuestData(true);
   }

   public void resetQuestData() {
      this.questData = new QuestData();
      if (!(this.getPlayer() instanceof FakePlayer)) {
         Pixelmon.network.sendTo(new ResetQuests(), this.getPlayer());
      }

      this.getQuestData(true);
   }

   public void closeBattleSafely() {
      if (!(this.getPlayer() instanceof FakePlayer)) {
         Pixelmon.network.sendTo(new CloseBattle(), this.getPlayer());
      }

   }

   public BattleEndTaskResult addTaskForBattleEnd(BattleEndTaskType type, Consumer consumer) {
      if (!this.guiOpened && type != BattleEndTaskType.ALWAYS_QUEUE) {
         if (type == BattleEndTaskType.QUEUE_IF_BATTLE_OTHERWISE_RUN) {
            consumer.accept(Optional.empty());
            return BattleEndTaskResult.EXECUTED;
         } else {
            return BattleEndTaskResult.IGNORED;
         }
      } else {
         this.transientData.onceBattleDone.add(consumer);
         return BattleEndTaskResult.QUEUED;
      }
   }

   /** @deprecated */
   @Deprecated
   public void addTaskForBattleEnd(Consumer consumer) {
      this.addTaskForBattleEnd(BattleEndTaskType.QUEUE_IF_BATTLE_OTHERWISE_RUN, consumer);
   }

   public void queryResourceLocationExistence(ResourceLocation resource, Consumer result) {
      EntityPlayerMP player = this.getPlayer();
      if (player != null) {
         UUID query = UUID.randomUUID();
         this.transientData.resourceQueries.put(query, result);
         Pixelmon.network.sendTo(new QueryResourceLocationPacket(query, resource), player);
      }

   }

   public void receiveResourceLocationQueryResult(UUID query, boolean result) {
      Consumer consumer = (Consumer)this.transientData.resourceQueries.remove(query);
      if (consumer != null) {
         consumer.accept(result);
      }

   }

   public void registerKeyListener(Object instance, Method callback) {
      this.transientData.keyListener = new Tuple(instance, callback);
   }

   public void receiveKeyInput(int code, char key) {
      if (this.transientData.keyListener != null) {
         EntityPlayerMP player = this.getPlayer();
         if (player != null && player.field_71070_bA != player.field_71069_bz && player.field_71070_bA != null) {
            try {
               ((Method)this.transientData.keyListener.func_76340_b()).invoke(this.transientData.keyListener.func_76341_a(), code, key);
            } catch (Exception var5) {
               Pixelmon.LOGGER.error("Key listener error: " + this.uuid.toString() + ", " + this.playerName);
            }
         } else {
            this.deregisterKeyListener();
         }
      }

   }

   public void deregisterKeyListener() {
      this.transientData.keyListener = null;
   }
}
