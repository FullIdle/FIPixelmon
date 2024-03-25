package com.pixelmonmod.pixelmon.battles.status;

import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.comm.packetHandlers.Transform;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.Illusion;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumGigantamaxPokemon;
import com.pixelmonmod.pixelmon.enums.forms.EnumGigantamax;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import java.util.List;
import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Transformed extends StatusBase {
   private transient int tempAttack;
   private transient int tempDefence;
   private transient int tempSpecialAttack;
   private transient int tempSpecialDefence;
   private transient int tempSpeed;
   private transient Moveset moveset;

   public Transformed(PixelmonWrapper user, PixelmonWrapper target) {
      super(StatusType.Transformed);
      int[] targetStats = target.getBattleStats().getBaseBattleStats();
      this.tempAttack = targetStats[StatsType.Attack.getStatIndex()];
      this.tempDefence = targetStats[StatsType.Defence.getStatIndex()];
      this.tempSpecialAttack = targetStats[StatsType.SpecialAttack.getStatIndex()];
      this.tempSpecialDefence = targetStats[StatsType.SpecialDefence.getStatIndex()];
      this.tempSpeed = targetStats[StatsType.Speed.getStatIndex()];
      user.getBattleStats().copyStats(target.getBattleStats());
      if (!(target.getBattleAbility(false) instanceof Illusion)) {
         user.setTempAbility(target.getBattleAbility(false));
      }

      List newType = target.type;
      user.setTempType(newType);
      this.moveset = (new Moveset()).withPokemon(user.getInnerLink());
      Moveset targetMoveset = target.getMoveset();

      for(int i = 0; i < targetMoveset.size(); ++i) {
         Attack a = new Attack(targetMoveset.get(i).getMove());
         if (a.pp != 1) {
            a.pp = 5;
         }

         this.moveset.add(a);
      }

   }

   @SideOnly(Side.CLIENT)
   public static void applyToClientEntity(Transform p) {
      Minecraft mc = Minecraft.func_71410_x();
      World world = mc.field_71441_e;

      for(int i = 0; i < world.field_72996_f.size(); ++i) {
         Entity e = (Entity)world.field_72996_f.get(i);
         if (e instanceof EntityPixelmon && e.func_145782_y() == p.pixelmonID) {
            EntityPixelmon pokemon = (EntityPixelmon)e;
            pokemon.transform(p.transformedModel, p.transformedForm, p.transformedTexture);
            break;
         }
      }

   }

   public void applyBeforeEffect(PixelmonWrapper user, PixelmonWrapper target) {
      EnumGigantamaxPokemon gigantamax = EnumGigantamaxPokemon.getGigantamax(target.getSpecies());
      String texture;
      if (!Objects.equals(EnumGigantamax.Gigantamax, target.getFormEnum()) && (gigantamax == null || !this.isGMaxForm(gigantamax, target))) {
         texture = target.entity.getTextureNoCheck().toString();
      } else {
         texture = Entity2Client.getTextureFor(target.getSpecies(), target.getFormEnum().getDefaultFromTemporary(target.pokemon), target.getGender(), "", false).toString();
      }

      user.entity.transformServer(target.getSpecies(), target.getForm(), texture);
   }

   private boolean isGMaxForm(EnumGigantamaxPokemon gigantamax, PixelmonWrapper target) {
      IEnumForm[] var3 = gigantamax.forms;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         IEnumForm form = var3[var5];
         if (Objects.equals(form, target.getFormEnum())) {
            return true;
         }
      }

      return false;
   }

   public void applyEndOfBattleEffect(PixelmonWrapper pokemon) {
      pokemon.entity.cancelTransform();
   }

   public int[] modifyBaseStats(PixelmonWrapper user, int[] stats) {
      stats[StatsType.Attack.getStatIndex()] = this.tempAttack;
      stats[StatsType.Defence.getStatIndex()] = this.tempDefence;
      stats[StatsType.SpecialAttack.getStatIndex()] = this.tempSpecialAttack;
      stats[StatsType.SpecialDefence.getStatIndex()] = this.tempSpecialDefence;
      stats[StatsType.Speed.getStatIndex()] = this.tempSpeed;
      return stats;
   }
}
