package com.pixelmonmod.pixelmon.entities.pixelmon;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.api.storage.PartyStorage;
import com.pixelmonmod.pixelmon.api.storage.PokemonStorage;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.entities.SpawnLocationType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.ExtraStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.FlyingParameters;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Level;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.SwimmingParameters;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.extraStats.ShearableStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.tickHandlers.CastformTickHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.tickHandlers.ShearableTickHandler;
import com.pixelmonmod.pixelmon.entities.pixelmon.tickHandlers.TickHandlerBase;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumMegaPokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.enums.forms.EnumNoForm;
import com.pixelmonmod.pixelmon.enums.forms.EnumPrimal;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import com.pixelmonmod.pixelmon.storage.extras.PixelExtrasStorage;
import java.util.Optional;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.world.World;

public abstract class Entity3HasStats extends Entity2Client {
   private TickHandlerBase tickHandler;
   int friendshipTicker = 0;

   public Entity3HasStats(World par1World) {
      super(par1World);
      this.updateSize();
   }

   public Level getLvl() {
      return this.getPokemonData().getLevelContainer();
   }

   float getMoveSpeed() {
      return 0.3F + (1.0F - (200.0F - (float)this.pokemon.getStat(StatsType.Speed)) / 200.0F) * 0.3F;
   }

   public void setBoss(EnumBossMode mode) {
      super.setBoss(mode);
      if (this.getBossMode().isBossPokemon()) {
         if (this.getBossMode().isMegaBossPokemon()) {
            if (this.getSpecies().hasMega() && this.getSpecies() != EnumSpecies.Necrozma) {
               int numMegas = EnumMegaPokemon.getMega(this.getSpecies()).numMegaForms;
               int form = 1;
               if (numMegas > 1) {
                  form = RandomHelper.getRandomNumberBetween(1, numMegas);
               }

               this.setForm(form);
            }

            if (this.getSpecies().getFormEnum(1) instanceof EnumPrimal) {
               this.setForm(1);
            }
         }

         this.getPokemonData().setShiny(mode.getExtraLevels() >= EnumBossMode.Legendary.getExtraLevels());
      }

   }

   public void evolve(PokemonSpec evolveTo) {
      BaseStats oldBaseStats = this.getBaseStats();
      EnumSpecies previous = this.getSpecies();
      super.evolve(evolveTo);
      float oldHp = (float)this.pokemon.getMaxHealth();
      float oldHealth = this.func_110143_aJ();
      if (evolveTo.form != null) {
         this.setForm(evolveTo.form);
      } else if (this.getPokemonData().getForm() == -1) {
         if (previous.getPossibleForms(false).contains(Gender.Male) || EnumSpecies.genderForm.contains(previous) || !this.getSpecies().getPossibleForms(false).contains(Gender.Male) && !EnumSpecies.genderForm.contains(this.getSpecies())) {
            this.setForm(this.pokemon.getSpecies().getFormEnum(-1));
         } else {
            this.setForm(this.pokemon.getGender());
         }
      }

      this.getPokemonData().evolve(evolveTo);
      if (oldBaseStats.getAbilitiesArray()[1] == null && this.getBaseStats().getAbilitiesArray()[1] != null) {
         this.getPokemonData().setAbilitySlot(RandomHelper.getRandomNumberBetween(0, 1));
      }

      this.updateStats();
      float newHealth = (float)this.pokemon.getMaxHealth();
      if (oldHp != 0.0F) {
         newHealth = oldHealth / oldHp * (float)this.pokemon.getMaxHealth();
      }

      this.func_70606_j((float)((int)Math.ceil((double)newHealth)));
      if (this.func_70902_q() != null) {
         this.update(new EnumUpdateType[]{EnumUpdateType.Name, EnumUpdateType.Stats});
      }

   }

   public void func_70606_j(float par1) {
      super.func_70606_j(par1);
      this.updateHealth();
   }

   public void updateHealth() {
      if (this.func_110143_aJ() > this.func_110138_aP()) {
         super.func_70606_j(this.func_110138_aP());
      }

      if (this.func_110143_aJ() < 0.0F) {
         super.func_70606_j(0.0F);
      }

      if (this.pokemon != null && this.func_70902_q() != null && !this.field_70170_p.field_72995_K) {
         this.update(new EnumUpdateType[]{EnumUpdateType.HP, EnumUpdateType.Stats});
      }

   }

   public void updateStats() {
      this.pokemon.getStats().setLevelStats(this.pokemon.getNature(), this.getBaseStats(), this.pokemon.getLevel());
      this.field_70180_af.func_187227_b(EntityPixelmon.dwMaxHP, this.pokemon.getMaxHealth());
      this.func_110148_a(SharedMonsterAttributes.field_111267_a).func_111128_a((double)this.pokemon.getMaxHealth());
      this.updateHealth();
   }

   public Optional getPokerus() {
      return Optional.ofNullable(this.pokemon.getPokerus());
   }

   public IEnumForm getFormEnum() {
      return (IEnumForm)(EnumSpecies.genderForm.contains(this.getSpecies()) ? this.getPokemonData().getGender() : this.getSpecies().getFormEnum(this.getPokemonData().getForm()));
   }

   public static IEnumForm getFormEnum(EnumSpecies species, int gender, int form) {
      return (IEnumForm)(EnumSpecies.genderForm.contains(species) ? Gender.getGender((short)gender) : species.getFormEnum(form));
   }

   public void setForm(int form) {
      this.getPokemonData().setForm(form);
   }

   public void setForm(IEnumForm form) {
      this.getPokemonData().setForm(form);
   }

   public boolean hasForms() {
      return hasForms(this.getSpecies());
   }

   public int getNumForms() {
      return this.hasForms() ? getNumForms(this.getSpecies()) : 0;
   }

   public int getFormIncludeTransformed() {
      return this.transformed != null ? this.transformed.getForm() : this.pokemon.getForm();
   }

   public int getPartyPosition() {
      PokemonStorage storage = this.getStorage();
      return storage != null && storage instanceof PartyStorage ? storage.getPosition(this.pokemon).order : -1;
   }

   public SpawnLocationType getDefaultSpawnLocation() {
      return this.getBaseStats() != null && this.getBaseStats().spawnLocations != null && this.getBaseStats().spawnLocations.length > 0 ? this.getBaseStats().spawnLocations[0] : SpawnLocationType.Land;
   }

   public FlyingParameters getFlyingParameters() {
      return this.getBaseStats().flyingParameters;
   }

   public SwimmingParameters getSwimmingParameters() {
      return this.getBaseStats().swimmingParameters;
   }

   public boolean func_70648_aU() {
      return this.getBaseStats() == null || this.getSpawnLocation() == SpawnLocationType.Water || this.getBaseStats().getTypeList().contains(EnumType.Water);
   }

   public void func_70071_h_() {
      if (this.func_70902_q() != null && !this.field_70170_p.field_72995_K && ++this.friendshipTicker % 800 == 0) {
         this.friendshipTicker = 0;
         int amount = this.pokemon.getFriendship() < 200 ? 2 : 1;
         this.pokemon.increaseFriendship(amount);
         PixelExtrasStorage.getData(this.func_184753_b()).checkPokemon(this.pokemon);
      }

      if (this.hasOwner() && this.func_70902_q() == null && !this.field_70170_p.field_72995_K) {
         this.func_70106_y();
      }

      if (this.getSpecies() == EnumSpecies.Castform) {
         if (!(this.tickHandler instanceof CastformTickHandler)) {
            this.tickHandler = new CastformTickHandler(this);
         }
      } else if (ExtraStats.getExtraStats(this.getSpecies()) instanceof ShearableStats) {
         if (!(this.tickHandler instanceof ShearableTickHandler)) {
            this.tickHandler = new ShearableTickHandler(this);
         }
      } else {
         this.tickHandler = null;
      }

      if (this.tickHandler != null) {
         this.tickHandler.tick(this.field_70170_p);
      }

      super.func_70071_h_();
   }

   public static boolean hasForms(EnumSpecies species) {
      return species.getNumForms(false) > 0;
   }

   public static boolean hasForms(String name) {
      Optional species = EnumSpecies.getFromName(name);
      return species.isPresent() && hasForms((EnumSpecies)species.get());
   }

   public static int getNumForms(String name) {
      Optional species = EnumSpecies.getFromName(name);
      return species.isPresent() ? getNumForms((EnumSpecies)species.get()) : 0;
   }

   public static int getNumForms(EnumSpecies species) {
      return hasForms(species) ? species.getNumForms(false) : 0;
   }

   public static int getRandomForm(EnumSpecies species) {
      if (hasForms(species)) {
         return species.getFormEnum(0) instanceof EnumNoForm ? EnumNoForm.NoForm.getForm() : RandomHelper.rand.nextInt(getNumForms(species));
      } else {
         return -1;
      }
   }
}
