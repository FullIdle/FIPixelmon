package com.pixelmonmod.pixelmon.client.gui.battles;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.NoStatus;
import com.pixelmonmod.pixelmon.battles.status.StatusPersist;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Illusion;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.items.ItemHeld;
import com.pixelmonmod.pixelmon.items.heldItems.NoItem;
import io.netty.buffer.ByteBuf;
import java.awt.Color;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PixelmonInGui {
   public UUID pokemonUUID;
   public EnumSpecies species;
   public short form = 0;
   public short gender;
   public float health;
   public int maxHealth;
   public int dynamax = 0;
   public boolean mega = false;
   public String nickname;
   public int status = -1;
   public int level;
   public int bossLevel;
   public boolean shiny;
   public boolean gmaxFactor;
   public int position = 0;
   public String customTexture = "";
   public Moveset moveset = null;
   public ItemHeld heldItem = null;
   public float expFraction = 0.0F;
   public int xPos = 0;
   public boolean isSwitchingOut;
   public boolean isSwitchingIn;
   public boolean blockCapture = false;
   public int maxShields = 0;
   public int shields = 0;
   public boolean lostShield = false;

   public PixelmonInGui() {
      this.nickname = "";
      this.species = null;
   }

   public PixelmonInGui(Pokemon poke) {
      this.set(poke);
   }

   public PixelmonInGui(PixelmonWrapper pixelmon) {
      this.pokemonUUID = pixelmon.getPokemonUUID();
      this.health = (float)pixelmon.getHealth();
      this.maxHealth = pixelmon.getMaxHealth();
      this.dynamax = pixelmon.isDynamax;
      this.mega = pixelmon.isMega;
      AbilityBase ability = pixelmon.getBattleAbility();
      boolean inIllusion = false;
      if (ability instanceof Illusion) {
         Illusion illusion = (Illusion)ability;
         if (illusion.disguisedGender != null) {
            this.nickname = illusion.disguisedNickname;
            this.species = illusion.disguisedPokemon;
            this.gender = (short)illusion.disguisedGender.ordinal();
            this.form = (short)illusion.disguisedForm;
            this.shiny = illusion.disguisedShiny;
            inIllusion = true;
         }
      }

      if (!inIllusion) {
         this.nickname = pixelmon.getRealNickname();
         this.species = pixelmon.getSpecies();
         this.gender = (short)pixelmon.getGender().ordinal();
         this.form = (short)pixelmon.getForm();
         this.shiny = pixelmon.getInnerLink().isShiny();
      }

      this.level = pixelmon.getLevelNum();
      if (pixelmon.entity != null) {
         this.bossLevel = pixelmon.entity.getBossMode().index;
      }

      this.gmaxFactor = pixelmon.getInnerLink().hasGigantamaxFactor();
      this.customTexture = pixelmon.pokemon.getCustomTexture();
      StatusPersist primaryStatus = pixelmon.getPrimaryStatus();
      if (primaryStatus != NoStatus.noStatus) {
         this.status = primaryStatus.type.ordinal();
      } else {
         this.status = -1;
      }

      this.moveset = pixelmon.getMoveset();
      this.heldItem = pixelmon.getHeldItem();
      this.expFraction = pixelmon.pokemon.getExperienceFraction();
      this.blockCapture = pixelmon.pokemon.getBonusStats().preventsCapture();
      this.position = pixelmon.battlePosition;
      this.shields = pixelmon.shields;
      this.maxShields = pixelmon.maxShields;
   }

   public void set(Pokemon poke) {
      this.pokemonUUID = poke.getUUID();
      this.health = (float)poke.getHealth();
      this.maxHealth = poke.getMaxHealth();
      this.nickname = poke.getDisplayName();
      this.species = poke.getSpecies();
      if (poke.getStatus() != null && poke.getStatus().type != StatusType.None) {
         this.status = poke.getStatus().type.ordinal();
      } else {
         this.status = -1;
      }

      this.level = poke.getLevel();
      this.gender = (short)poke.getGender().ordinal();
      this.shiny = poke.isShiny();
      this.gmaxFactor = poke.hasGigantamaxFactor();
      this.expFraction = poke.getExperienceFraction();
      this.form = (short)poke.getForm();
      this.customTexture = poke.getCustomTexture();
      this.moveset = poke.getMoveset();
      this.heldItem = poke.getHeldItemAsItemHeld();
      this.blockCapture = poke.getBonusStats().preventsCapture();
   }

   public static PixelmonInGui[] convertToGUI(List pokemon) {
      PixelmonInGui[] data = new PixelmonInGui[pokemon.size()];

      for(int i = 0; i < pokemon.size(); ++i) {
         PixelmonWrapper wrapper = (PixelmonWrapper)pokemon.get(i);
         data[i] = new PixelmonInGui(wrapper);
         data[i].position = wrapper.battlePosition;
      }

      return data;
   }

   public void decodeFrom(ByteBuf buffer) {
      this.pokemonUUID = new UUID(buffer.readLong(), buffer.readLong());
      this.species = EnumSpecies.getFromDex(buffer.readShort());
      this.form = buffer.readShort();
      this.health = buffer.readFloat();
      this.maxHealth = buffer.readInt();
      this.dynamax = buffer.readInt();
      this.mega = buffer.readBoolean();
      this.nickname = ByteBufUtils.readUTF8String(buffer);
      this.level = buffer.readInt();
      this.bossLevel = buffer.readInt();
      this.gender = buffer.readShort();
      this.shiny = buffer.readBoolean();
      this.gmaxFactor = buffer.readBoolean();
      this.status = buffer.readInt();
      this.expFraction = buffer.readFloat();
      this.customTexture = ByteBufUtils.readUTF8String(buffer);
      this.blockCapture = buffer.readBoolean();
      this.moveset = new Moveset();
      this.moveset.fromBytes(buffer);
      boolean noItem = buffer.readBoolean();
      this.heldItem = (ItemHeld)(noItem ? NoItem.noItem : ItemHeld.getItemHeld(ByteBufUtils.readItemStack(buffer)));
      this.position = buffer.readInt();
      this.shields = buffer.readInt();
      this.maxShields = buffer.readInt();
   }

   public void encodeInto(ByteBuf buffer) {
      if (this.nickname == null) {
         this.nickname = this.species.name;
      }

      buffer.writeLong(this.pokemonUUID.getMostSignificantBits()).writeLong(this.pokemonUUID.getLeastSignificantBits());
      buffer.writeShort(this.species.getNationalPokedexInteger());
      buffer.writeShort(this.form);
      buffer.writeFloat(this.health);
      buffer.writeInt(this.maxHealth);
      buffer.writeInt(this.dynamax);
      buffer.writeBoolean(this.mega);
      ByteBufUtils.writeUTF8String(buffer, this.nickname);
      buffer.writeInt(this.level);
      buffer.writeInt(this.bossLevel);
      buffer.writeShort(this.gender);
      buffer.writeBoolean(this.shiny);
      buffer.writeBoolean(this.gmaxFactor);
      buffer.writeInt(this.status);
      buffer.writeFloat(this.expFraction);
      ByteBufUtils.writeUTF8String(buffer, this.customTexture);
      buffer.writeBoolean(this.blockCapture);
      this.moveset.toBytes(buffer);
      boolean noItem = this.heldItem == NoItem.noItem || this.heldItem == null;
      buffer.writeBoolean(noItem);
      if (!noItem) {
         ByteBufUtils.writeItemStack(buffer, new ItemStack(this.heldItem));
      }

      buffer.writeInt(this.position);
      buffer.writeInt(this.shields);
      buffer.writeInt(this.maxShields);
   }

   public String getDisplayName() {
      String localizedName = this.species.getLocalizedName();
      return this.nickname != null && !this.nickname.isEmpty() && !this.nickname.equals(this.species.name) ? this.nickname : localizedName;
   }

   public int getDexNumber() {
      return this.species.getNationalPokedexInteger();
   }

   public BaseStats getBaseStats() {
      return this.species.getBaseStats(this.species.getFormEnum(this.form));
   }

   public Gender getGender() {
      return Gender.getGender(this.gender);
   }

   public float[] getStatusTexturePos() {
      return StatusType.getTexturePos(this.status == -1 ? StatusType.None : StatusType.getEffect(this.status));
   }

   public Color getHealthColor() {
      Color color = new Color(51, 204, 51);
      if (this.health <= (float)this.maxHealth / 5.0F) {
         color = new Color(204, 0, 0);
      } else if (this.health <= (float)this.maxHealth / 2.0F) {
         color = new Color(255, 255, 102);
      }

      return color;
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         PixelmonInGui that = (PixelmonInGui)o;
         return Objects.equals(this.pokemonUUID, that.pokemonUUID);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.pokemonUUID});
   }
}
