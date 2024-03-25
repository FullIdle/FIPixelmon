package com.pixelmonmod.pixelmon.battles.rules.clauses;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.util.IEncodeable;
import io.netty.buffer.ByteBuf;
import java.util.Iterator;
import java.util.List;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class BattleClause implements Comparable, IEncodeable {
   private String id;
   private String description = "";

   public BattleClause(String id) {
      if (id == null) {
         id = "";
      }

      this.id = id;
   }

   public BattleClause setDescription(String description) {
      if (description == null) {
         description = "";
      }

      this.description = description;
      return this;
   }

   public String getID() {
      return this.id;
   }

   public String getDescription() {
      return !this.description.isEmpty() ? this.description : I18n.func_74838_a("gui.battlerules.description." + this.id);
   }

   public String getLocalizedName() {
      return getLocalizedName(this.id);
   }

   public String toString() {
      return this.getLocalizedName();
   }

   public static String getLocalizedName(String clauseID) {
      String langKey = "gui.battlerules." + clauseID;
      return I18n.func_94522_b(langKey) ? I18n.func_74838_a(langKey) : clauseID;
   }

   public boolean equals(Object other) {
      return other instanceof BattleClause ? ((BattleClause)other).getID().equals(this.id) : false;
   }

   public int hashCode() {
      return this.id.hashCode();
   }

   public boolean validateSingle(Pokemon pokemon) {
      return true;
   }

   public boolean validateTeam(List team) {
      Iterator var2 = team.iterator();

      Pokemon pokemon;
      do {
         if (!var2.hasNext()) {
            return true;
         }

         pokemon = (Pokemon)var2.next();
      } while(this.validateSingle(pokemon));

      return false;
   }

   public int compareTo(BattleClause o) {
      return this.id.compareTo(o.getID());
   }

   public void encodeInto(ByteBuf buffer) {
      ByteBufUtils.writeUTF8String(buffer, this.id);
      ByteBufUtils.writeUTF8String(buffer, this.description);
   }

   public void decodeInto(ByteBuf buffer) {
      this.id = ByteBufUtils.readUTF8String(buffer);
      this.description = ByteBufUtils.readUTF8String(buffer);
   }
}
