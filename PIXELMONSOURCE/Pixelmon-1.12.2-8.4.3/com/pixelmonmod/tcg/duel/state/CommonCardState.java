package com.pixelmonmod.tcg.duel.state;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.tcg.api.card.CardRarity;
import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.Energy;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.card.ability.AbilityCard;
import com.pixelmonmod.tcg.network.ByteBufTCG;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import org.lwjgl.util.vector.Vector3f;

public class CommonCardState {
   public List parameters = new ArrayList();
   protected ImmutableCard data = null;
   private int setId;
   private CardType cardType;
   private String code;
   private String name;
   private String description;
   private Energy energy1;
   private Energy energy2;
   private CardRarity cardRarity;
   private int pokemonId;
   private int hp;
   private ImmutableCard transformation;
   private Vector3f overrideModelColor;
   protected Energy overwriteEnergy = null;

   public CommonCardState(ImmutableCard data) {
      this.data = data;
      this.transform(data, (PokemonCardStatus)null);
   }

   public void transform(ImmutableCard data, PokemonCardStatus status) {
      this.setId = data.getSetID();
      this.cardType = data.getCardType();
      this.pokemonId = data.getPokemonID();
      this.hp = data.getHP();
      this.name = data.getName();
      this.code = data.getCode();
      this.description = data.getDescription();
      this.energy1 = data.getMainEnergy();
      this.energy2 = data.getSecondaryEnergy();
      this.cardRarity = data.getRarity();
      this.transformation = data;
   }

   public CommonCardState(ByteBuf buf) {
      this.data = ByteBufTCG.readCard(buf);
      int energy1Index;
      int energy2Index;
      if (this.data.getID() != ImmutableCard.FACE_DOWN_ID) {
         this.setId = buf.readInt();
         this.cardType = CardType.values()[buf.readInt()];
         this.pokemonId = buf.readInt();
         this.hp = buf.readInt();
         this.code = ByteBufUtils.readUTF8String(buf);
         this.name = ByteBufUtils.readUTF8String(buf);
         if (buf.readBoolean()) {
            this.description = ByteBufUtils.readUTF8String(buf);
         }

         energy1Index = buf.readInt();
         if (energy1Index > -1) {
            this.energy1 = Energy.values()[energy1Index];
         }

         energy2Index = buf.readInt();
         if (energy2Index > -1) {
            this.energy2 = Energy.values()[energy2Index];
         }

         this.cardRarity = CardRarity.values()[buf.readInt()];
         if (buf.readBoolean()) {
            this.overrideModelColor = new Vector3f(buf.readFloat(), buf.readFloat(), buf.readFloat());
         }

         if (buf.readBoolean()) {
            this.transformation = ByteBufTCG.readCard(buf);
         }
      }

      energy1Index = buf.readInt();

      for(energy2Index = 0; energy2Index < energy1Index; ++energy2Index) {
         this.parameters.add(new CommonCardState(buf));
      }

      energy2Index = buf.readInt();
      if (energy2Index >= 0) {
         this.overwriteEnergy = Energy.values()[energy2Index];
      }

   }

   public void write(ByteBuf buf, boolean faceUp) {
      this.write(buf, faceUp, true);
   }

   protected void write(ByteBuf buf, boolean faceUp, boolean writeParams) {
      if (faceUp) {
         ByteBufTCG.writeCard(buf, this.data);
         buf.writeInt(this.setId);
         buf.writeInt(this.cardType.ordinal());
         buf.writeInt(this.pokemonId);
         buf.writeInt(this.hp);
         ByteBufUtils.writeUTF8String(buf, this.code);
         ByteBufUtils.writeUTF8String(buf, this.name);
         buf.writeBoolean(this.description != null);
         if (this.description != null) {
            ByteBufUtils.writeUTF8String(buf, this.description);
         }

         buf.writeInt(this.energy1 == null ? -1 : this.energy1.ordinal());
         buf.writeInt(this.energy2 == null ? -1 : this.energy2.ordinal());
         buf.writeInt(this.cardRarity.ordinal());
         buf.writeBoolean(this.overrideModelColor != null);
         if (this.overrideModelColor != null) {
            buf.writeFloat(this.overrideModelColor.x);
            buf.writeFloat(this.overrideModelColor.y);
            buf.writeFloat(this.overrideModelColor.z);
         }

         buf.writeBoolean(this.transformation != null);
         if (this.transformation != null) {
            ByteBufTCG.writeCard(buf, this.transformation);
         }
      } else {
         ByteBufTCG.writeFaceDownCard(buf);
      }

      if (writeParams) {
         buf.writeInt(this.parameters.size());
         Iterator var4 = this.parameters.iterator();

         while(var4.hasNext()) {
            CommonCardState card = (CommonCardState)var4.next();
            if (card instanceof PokemonCardState) {
               ((PokemonCardState)card).writeAsCommonCardState(buf, faceUp, false);
            } else {
               card.write(buf, faceUp, false);
            }
         }
      } else {
         buf.writeInt(0);
      }

      if (this.overwriteEnergy == null) {
         buf.writeInt(-1);
      } else {
         buf.writeInt(this.overwriteEnergy.ordinal());
      }

   }

   public ImmutableCard getData() {
      return this.data;
   }

   public void setData(ImmutableCard data) {
      this.data = data;
   }

   public List getParameters() {
      return this.parameters;
   }

   public boolean isEnergyEquivalence() {
      return this.cardType == CardType.ENERGY || this.getAbility() != null && this.getAbility().getEffect() != null && !isNullOrEmpty(this.getAbility().getEffect().getEnergyEquivalence(this));
   }

   protected static boolean isNullOrEmpty(List list) {
      return list == null || list.isEmpty();
   }

   public int getSetID() {
      return this.setId;
   }

   public void setSetID(int setId) {
      this.setId = setId;
   }

   public CardType getCardType() {
      return this.cardType;
   }

   public void setCardType(CardType cardType) {
      this.cardType = cardType;
   }

   public int getPokemonID() {
      return this.pokemonId;
   }

   public void setPokemonID(int pokemonId) {
      this.pokemonId = pokemonId;
   }

   public int getHP() {
      return this.hp;
   }

   public void setHP(int hp) {
      this.hp = hp;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getCode() {
      return this.code;
   }

   public void setCode(String code) {
      this.code = code;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public AbilityCard getAbility() {
      return this.data.getAbility();
   }

   public Energy getMainEnergy() {
      return this.overwriteEnergy != null ? this.overwriteEnergy : this.energy1;
   }

   public Energy getSecondaryEnergy() {
      return this.data.getSecondaryEnergy() != null && this.overwriteEnergy != null ? this.overwriteEnergy : this.energy2;
   }

   public boolean hasTransformation() {
      return this.data != this.transformation;
   }

   public ImmutableCard getTransformation() {
      return this.transformation;
   }

   public void setOverwriteEnergy(Energy overwriteEnergy) {
      this.overwriteEnergy = overwriteEnergy;
   }

   public CardRarity getRarity() {
      return this.cardRarity;
   }

   public void setRarity(CardRarity cardRarity) {
      this.cardRarity = cardRarity;
   }

   public Vector3f getOverrideModelColor() {
      return this.overrideModelColor;
   }

   public void setOverrideModelColor(Vector3f overrideModelColor) {
      this.overrideModelColor = overrideModelColor;
   }

   public void handleEndTurn(PokemonCardState attachTo, PlayerServerState player, GameServerState server) {
      this.overwriteEnergy = null;
      if (this.getAbility() == null || this.getAbility().getEffect() == null || !this.getAbility().getEffect().holdParameters()) {
         this.parameters.clear();
      }

   }

   public void bindPokemonSprite(Minecraft mc) {
      if (this.pokemonId > 0) {
         GuiHelper.bindPokemonSprite(EnumSpecies.getFromDex(this.pokemonId), 0, Gender.None, "", false, 0, mc);
      }

   }
}
