package com.pixelmonmod.pixelmon.api.pokemon;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.comm.EnumUpdateType;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.ComingSoon;
import com.pixelmonmod.pixelmon.entities.pixelmon.specs.CanCrownedSpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.specs.CanMegaEvoSpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.specs.CanPrimalEvoSpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.specs.CanUltraBurstSpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.specs.CustomTextureSpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.specs.GenerationSpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.specs.IVEVSpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.specs.IsLegendarySpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.specs.SetAllIVsEVsFlag;
import com.pixelmonmod.pixelmon.entities.pixelmon.specs.StatusSpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.specs.Type1Spec;
import com.pixelmonmod.pixelmon.entities.pixelmon.specs.Type2Spec;
import com.pixelmonmod.pixelmon.entities.pixelmon.specs.TypeSpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.specs.UnbreedableFlag;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Pokerus;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.StatsType;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumPokerusType;
import com.pixelmonmod.pixelmon.enums.EnumRibbonType;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.items.EnumPokeballs;
import io.netty.buffer.ByteBuf;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PokemonSpec {
   public static final SpecValueTypeAdapter SPEC_VALUE_TYPE_ADAPTER = new SpecValueTypeAdapter();
   public static final Adapter SPEC_ADAPTER = new Adapter();
   public static final List extraSpecTypes = new ArrayList();
   public String[] args;
   public String name;
   public Integer level;
   public Byte gender;
   public Byte growth;
   public Byte nature;
   public String ability;
   public Byte boss;
   public Boolean shiny;
   public Boolean formInvert;
   public Byte form;
   public String formString;
   public Byte ball;
   public Byte pokerusType;
   public Byte pokerusSpread;
   public Boolean egg;
   public Boolean gmaxFactor;
   public ArrayList ribbons;
   public ArrayList extraSpecs;

   public static ISpecType getSpecForKey(String key) {
      Iterator var1 = extraSpecTypes.iterator();

      while(var1.hasNext()) {
         ISpecType specType = (ISpecType)var1.next();
         Iterator var3 = specType.getKeys().iterator();

         while(var3.hasNext()) {
            String knownKey = (String)var3.next();
            if (knownKey.equalsIgnoreCase(key)) {
               return specType;
            }
         }
      }

      return null;
   }

   public PokemonSpec(ByteBuf buf) {
      this.args = new String[0];
      this.name = null;
      this.level = null;
      this.gender = null;
      this.growth = null;
      this.nature = null;
      this.ability = null;
      this.boss = null;
      this.shiny = null;
      this.formInvert = false;
      this.form = null;
      this.formString = null;
      this.ball = null;
      this.pokerusType = null;
      this.pokerusSpread = null;
      this.egg = null;
      this.gmaxFactor = null;
      this.ribbons = new ArrayList();
      this.extraSpecs = null;
      this.fromBytes(buf);
   }

   public PokemonSpec(String arg) {
      this(arg == null ? null : arg.replaceAll(" +", " ").split(" "));
   }

   public PokemonSpec(String... args) {
      this.args = new String[0];
      this.name = null;
      this.level = null;
      this.gender = null;
      this.growth = null;
      this.nature = null;
      this.ability = null;
      this.boss = null;
      this.shiny = null;
      this.formInvert = false;
      this.form = null;
      this.formString = null;
      this.ball = null;
      this.pokerusType = null;
      this.pokerusSpread = null;
      this.egg = null;
      this.gmaxFactor = null;
      this.ribbons = new ArrayList();
      this.extraSpecs = null;
      if (args != null && args.length > 0) {
         this.args = args;
         String[] var2 = args;
         int var3 = args.length;

         int var4;
         String arg;
         String[] splits;
         String key;
         String value;
         for(var4 = 0; var4 < var3; ++var4) {
            arg = var2[var4];
            if (arg != null && !arg.isEmpty()) {
               splits = arg.contains(":") ? arg.split(":") : null;
               if (splits != null && splits.length == 0) {
                  splits = null;
               }

               key = splits == null ? arg : splits[0];
               value = splits != null && splits.length != 1 ? splits[1] : null;
               boolean exclamation = key.startsWith("!");
               if (exclamation) {
                  key = key.substring(1);
                  value = "false";
               }

               ISpecType specType = getSpecForKey(key);
               if (specType != null) {
                  SpecValue spec = specType.parse(value);
                  if (spec != null) {
                     if (this.extraSpecs == null) {
                        this.extraSpecs = new ArrayList();
                     }

                     this.extraSpecs.add(spec);
                  }
               }
            }
         }

         var2 = args;
         var3 = args.length;

         for(var4 = 0; var4 < var3; ++var4) {
            arg = var2[var4];
            if (EnumSpecies.hasPokemonAnyCase(arg)) {
               this.name = EnumSpecies.getFromNameAnyCase(arg).name;
            } else if (arg.equalsIgnoreCase("random")) {
               this.name = EnumSpecies.randomPoke(PixelmonConfig.allowRandomPokemonToBeLegendary).name;
            } else {
               splits = arg.split(":");

               try {
                  key = splits[0].toLowerCase();
                  if (!key.equals("s") && !key.equals("shiny")) {
                     if (!key.equals("!s") && !key.equals("!shiny") && !key.equals("ns") && !key.equals("notshiny")) {
                        if (!key.equals("cured") && !key.equals("c")) {
                           if (key.equals("egg")) {
                              this.egg = true;
                           } else if (key.equals("!egg")) {
                              this.egg = false;
                           } else if (key.equals("gmaxfactor")) {
                              this.gmaxFactor = true;
                           } else if (key.equals("!gmaxfactor")) {
                              this.gmaxFactor = false;
                           } else {
                              value = splits[1].replaceAll("_", "");
                              int var12;
                              int var13;
                              switch (key) {
                                 case "lvl":
                                 case "level":
                                    this.level = Integer.parseInt(value);
                                    if (this.level > PixelmonServerConfig.maxLevel) {
                                       this.level = PixelmonServerConfig.maxLevel;
                                    }

                                    if (this.level < 1) {
                                       this.level = 1;
                                    }
                                    break;
                                 case "g":
                                 case "ge":
                                 case "gender":
                                    if (Gender.getGender(value) != null) {
                                       this.gender = (byte)Gender.getGender(value).ordinal();
                                    } else {
                                       this.gender = (byte)Gender.getGender(Short.parseShort(value)).ordinal();
                                    }
                                    break;
                                 case "n":
                                 case "nature":
                                    if (EnumNature.hasNature(value)) {
                                       this.nature = (byte)EnumNature.natureFromString(value).index;
                                    } else {
                                       this.nature = (byte)EnumNature.getNatureFromIndex(Integer.parseInt(value)).index;
                                    }
                                    break;
                                 case "gr":
                                 case "growth":
                                    if (EnumGrowth.hasGrowth(value)) {
                                       this.growth = (byte)EnumGrowth.growthFromString(value).index;
                                    } else {
                                       this.growth = (byte)EnumGrowth.getGrowthFromIndex(Integer.parseInt(value)).index;
                                    }
                                    break;
                                 case "ab":
                                 case "ability":
                                    this.ability = (String)AbilityBase.getAbility(value).map(AbilityBase::getName).orElse((Object)null);
                                    break;
                                 case "b":
                                 case "boss":
                                    if (EnumBossMode.hasBossMode(value)) {
                                       this.boss = (byte)EnumBossMode.getBossMode(value).index;
                                    } else {
                                       this.boss = (byte)EnumBossMode.getMode(Integer.parseInt(value)).index;
                                    }

                                    if (this.boss == EnumBossMode.Equal.index) {
                                       this.boss = null;
                                    }
                                    break;
                                 case "f":
                                 case "form":
                                 case "st":
                                 case "specialtexture":
                                    if (value.startsWith("!")) {
                                       this.formInvert = true;
                                       value = value.substring(1);
                                    }

                                    try {
                                       this.form = (byte)Integer.parseInt(value);
                                    } catch (Exception var17) {
                                       this.formString = value;
                                    }
                                    break;
                                 case "ba":
                                 case "ball":
                                    EnumPokeballs[] var21 = EnumPokeballs.values();
                                    var12 = var21.length;
                                    var13 = 0;

                                    for(; var13 < var12; ++var13) {
                                       EnumPokeballs pokeball = var21[var13];
                                       if (pokeball.name().toLowerCase().contains(value.toLowerCase())) {
                                          this.ball = (byte)pokeball.getIndex();
                                       }
                                    }

                                    this.ball = (byte)EnumPokeballs.getFromIndex(Integer.parseInt(value)).getIndex();
                                    break;
                                 case "pkrs":
                                 case "pokerus":
                                    try {
                                       this.pokerusType = (byte)EnumPokerusType.valueOf(value.toUpperCase()).ordinal();
                                    } catch (IllegalArgumentException var16) {
                                       this.pokerusType = Byte.parseByte(value);
                                    }

                                    if (this.pokerusType <= 0 || this.pokerusType >= EnumPokerusType.values().length) {
                                       this.pokerusType = null;
                                    }

                                    if (this.pokerusSpread == null) {
                                       this.pokerusSpread = 0;
                                    }
                                    break;
                                 case "ribbon":
                                 case "ribbons":
                                    if (this.ribbons == null) {
                                       this.ribbons = new ArrayList();
                                    }

                                    String[] var20 = value.split(",");
                                    var12 = var20.length;

                                    for(var13 = 0; var13 < var12; ++var13) {
                                       String ribbon = var20[var13];
                                       EnumRibbonType ribbonType = EnumRibbonType.getRibbonFor(ribbon);
                                       if (ribbonType != null) {
                                          this.ribbons.add(ribbonType);
                                       }
                                    }
                              }
                           }
                        } else {
                           this.pokerusSpread = -1;
                           if (this.pokerusType == null) {
                              this.pokerusType = 1;
                           }
                        }
                     } else {
                        this.shiny = false;
                     }
                  } else {
                     this.shiny = true;
                  }
               } catch (NumberFormatException | NullPointerException | ArrayIndexOutOfBoundsException var18) {
               }
            }
         }

         if (this.formString != null && EnumSpecies.hasPokemonAnyCase(this.name)) {
            EnumSpecies.getFromNameAnyCase(this.name).getPossibleForms(true).forEach((ef) -> {
               if (this.formString.replace("_", "").equalsIgnoreCase(((Enum)ef).name().replace("_", ""))) {
                  this.form = ef.getForm();
               }

            });
         }
      }

   }

   public static PokemonSpec from(String arg) {
      return from(arg == null ? null : arg.replaceAll(" +", " ").split(" "));
   }

   public static PokemonSpec from(String... args) {
      return new PokemonSpec(args);
   }

   public PokemonSpec copy() {
      PokemonSpec copy = new PokemonSpec(new String[0]);

      try {
         Field[] var2 = this.getClass().getFields();
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Field field = var2[var4];
            if (!Modifier.isFinal(field.getModifiers()) && !Modifier.isStatic(field.getModifiers()) && !field.getName().equals("extraSpecs")) {
               field.set(copy, field.get(this));
            }
         }
      } catch (Exception var6) {
         var6.printStackTrace();
      }

      if (this.extraSpecs != null && !this.extraSpecs.isEmpty()) {
         copy.extraSpecs = new ArrayList();
         Iterator var7 = this.extraSpecs.iterator();

         while(var7.hasNext()) {
            SpecValue specValue = (SpecValue)var7.next();
            copy.extraSpecs.add(specValue.clone());
         }
      }

      return copy;
   }

   public void fromBytes(ByteBuf buf) {
      String string = ByteBufUtils.readUTF8String(buf);
      string = string.substring(string.indexOf("{") + 1, string.length() - 1);
      NBTTagCompound nbt = null;

      try {
         nbt = JsonToNBT.func_180713_a(string);
         this.readFromNBT(nbt);
      } catch (NBTException var5) {
         var5.printStackTrace();
      }

   }

   public void toBytes(ByteBuf buf) {
      ByteBufUtils.writeUTF8String(buf, this.toString());
   }

   /** @deprecated */
   @Deprecated
   public boolean matches(NBTTagCompound nbt) {
      if (nbt == null) {
         return false;
      } else if (this.name != null && !nbt.func_74779_i("Name").equals(this.name)) {
         return false;
      } else if (this.level != null && nbt.func_74762_e("Level") != this.level) {
         return false;
      } else if (this.gender != null && nbt.func_74765_d("Gender") != this.gender) {
         return false;
      } else if (this.growth != null && nbt.func_74765_d("Growth") != this.growth) {
         return false;
      } else if (this.nature != null && nbt.func_74765_d("Nature") != this.nature) {
         return false;
      } else if (this.ability != null && !nbt.func_74779_i("Ability").equalsIgnoreCase(this.ability)) {
         return false;
      } else if (this.gmaxFactor != null && nbt.func_74767_n("GigantamaxFactor") != this.gmaxFactor) {
         return false;
      } else if (this.boss != null && nbt.func_74765_d("BossMode") != this.boss) {
         return false;
      } else if (this.shiny != null && nbt.func_74767_n("IsShiny") != this.shiny) {
         return false;
      } else {
         if (this.form != null) {
            if (this.formInvert != null && this.formInvert) {
               if (nbt.func_74765_d("Variant") == this.form) {
                  return false;
               }
            } else if (nbt.func_74765_d("Variant") != this.form) {
               return false;
            }
         }

         if (this.ball != null && nbt.func_74762_e("CaughtBall") != this.ball) {
            return false;
         } else if (this.pokerusType != null && (!nbt.func_74764_b("Pokerus") || ((NBTTagCompound)nbt.func_74781_a("Pokerus")).func_74765_d("Type") != this.pokerusType.shortValue())) {
            return false;
         } else if (this.pokerusSpread != null && (!nbt.func_74764_b("Pokerus") || ((NBTTagCompound)nbt.func_74781_a("Pokerus")).func_74762_e("Spread") != this.pokerusSpread)) {
            return false;
         } else {
            if (this.egg != null) {
               if (this.egg && !nbt.func_150297_b("eggCycles", 3)) {
                  return false;
               }

               if (!this.egg && nbt.func_150297_b("eggCycles", 3)) {
                  return false;
               }
            }

            if (this.extraSpecs != null) {
               Iterator var2 = this.extraSpecs.iterator();

               while(var2.hasNext()) {
                  SpecValue spec = (SpecValue)var2.next();
                  if (!spec.matches(nbt)) {
                     return false;
                  }
               }
            }

            return true;
         }
      }
   }

   public boolean matches(EntityPixelmon pokemon) {
      if (pokemon == null) {
         return false;
      } else if (this.name != null && !pokemon.getPokemonName().equals(this.name)) {
         return false;
      } else if (this.level != null && pokemon.getLvl().getLevel() != this.level) {
         return false;
      } else if (this.gender != null && pokemon.getPokemonData().getGender().ordinal() != this.gender) {
         return false;
      } else if (this.growth != null && pokemon.getPokemonData().getGrowth().index != this.growth) {
         return false;
      } else if (this.nature != null && pokemon.getPokemonData().getNature().index != this.nature) {
         return false;
      } else if (this.ability != null && !pokemon.getPokemonData().getAbilityName().equalsIgnoreCase(this.ability)) {
         return false;
      } else if (this.gmaxFactor != null && pokemon.getPokemonData().hasGigantamaxFactor() != this.gmaxFactor) {
         return false;
      } else if (this.boss != null && pokemon.getBossMode().index != this.boss) {
         return false;
      } else if (this.shiny != null && pokemon.getPokemonData().isShiny() != this.shiny) {
         return false;
      } else {
         if (this.form != null) {
            if (this.formInvert != null && this.formInvert) {
               if (pokemon.getPokemonData().getForm() == this.form) {
                  return false;
               }
            } else if (pokemon.getPokemonData().getForm() != this.form) {
               return false;
            }
         }

         if (this.ball != null && pokemon.getPokemonData().getCaughtBall().getIndex() != this.ball) {
            return false;
         } else {
            Pokerus pkrs = (Pokerus)pokemon.getPokerus().orElse((Object)null);
            if (this.pokerusType == null || pkrs != null && (short)pkrs.type.ordinal() == this.pokerusType) {
               if (this.pokerusSpread != null && (pkrs == null || pkrs.secondsSinceInfection != this.pokerusSpread)) {
                  return false;
               } else {
                  if (this.egg != null) {
                     if (this.egg && !pokemon.getPokemonData().isEgg()) {
                        return false;
                     }

                     if (!this.egg && pokemon.getPokemonData().isEgg()) {
                        return false;
                     }
                  }

                  if (this.extraSpecs != null) {
                     Iterator var3 = this.extraSpecs.iterator();

                     while(var3.hasNext()) {
                        SpecValue spec = (SpecValue)var3.next();
                        if (spec.value == null) {
                           return false;
                        }

                        if (!spec.matches(pokemon)) {
                           return false;
                        }
                     }
                  }

                  return true;
               }
            } else {
               return false;
            }
         }
      }
   }

   public boolean matches(Pokemon pokemon) {
      if (pokemon == null) {
         return false;
      } else if (this.name != null && !pokemon.getSpecies().name.equals(this.name)) {
         return false;
      } else if (this.level != null && pokemon.getLevel() != this.level) {
         return false;
      } else if (this.gender != null && pokemon.getGender().ordinal() != this.gender) {
         return false;
      } else if (this.growth != null && pokemon.getGrowth().index != this.growth) {
         return false;
      } else if (this.nature != null && pokemon.getNature().index != this.nature) {
         return false;
      } else if (this.ability != null && !pokemon.getAbilityName().equalsIgnoreCase(this.ability)) {
         return false;
      } else if (this.gmaxFactor != null && pokemon.hasGigantamaxFactor() != this.gmaxFactor) {
         return false;
      } else if (this.shiny != null && pokemon.isShiny() != this.shiny) {
         return false;
      } else {
         if (this.form != null) {
            if (this.formInvert != null && this.formInvert) {
               if (pokemon.getForm() == this.form) {
                  return false;
               }
            } else if (pokemon.getForm() != this.form) {
               return false;
            }
         }

         if (this.formString != null) {
            String testValue = pokemon.getFormEnum().getFormSuffix().replace("-", "");
            if (this.formInvert != null && this.formInvert) {
               if (testValue.equalsIgnoreCase(this.formString)) {
                  return false;
               }
            } else if (!testValue.equalsIgnoreCase(this.formString)) {
               return false;
            }
         }

         if (this.ball != null && pokemon.caughtBall.getIndex() != this.ball) {
            return false;
         } else {
            Pokerus pkrs = pokemon.getPokerus();
            if (this.pokerusType != null && (pkrs == null || (short)pkrs.type.ordinal() != this.pokerusType)) {
               return false;
            } else if (this.pokerusSpread == null || pkrs != null && pkrs.secondsSinceInfection == this.pokerusSpread) {
               if (this.egg != null) {
                  if (this.egg && !pokemon.isEgg()) {
                     return false;
                  }

                  if (!this.egg && pokemon.isEgg()) {
                     return false;
                  }
               }

               if (this.extraSpecs != null) {
                  Iterator var3 = this.extraSpecs.iterator();

                  while(var3.hasNext()) {
                     SpecValue spec = (SpecValue)var3.next();
                     if (spec.value == null) {
                        return false;
                     }

                     if (!spec.matches(pokemon)) {
                        return false;
                     }
                  }
               }

               return true;
            } else {
               return false;
            }
         }
      }
   }

   public EntityPixelmon create(World world) {
      if (this.name == null) {
         return null;
      } else {
         EnumSpecies species = EnumSpecies.getFromNameAnyCaseNoTranslate(this.name);
         if (species == null) {
            Pixelmon.LOGGER.error("PokemonSpec tried to create a Pokémon with a bad name: " + this.name);
            return null;
         } else {
            EntityPixelmon pixelmon = new EntityPixelmon(world);
            pixelmon.setPokemon(this.create());
            pixelmon.getPokemonData().updateDimensionAndEntityID(world.field_73011_w.getDimension(), pixelmon.func_145782_y());
            if (this.boss != null) {
               pixelmon.setBoss(EnumBossMode.getMode(this.boss));
               pixelmon.resetDataWatchers();
            }

            return pixelmon;
         }
      }
   }

   public Pokemon create() {
      if (this.name == null) {
         Pixelmon.LOGGER.error("PokemonSpec tried to create a Pokémon with a null name.");
         return null;
      } else {
         EnumSpecies species = EnumSpecies.getFromNameAnyCaseNoTranslate(this.name);
         if (species == null) {
            Pixelmon.LOGGER.error("PokemonSpec tried to create a Pokémon with a bad name: " + this.name);
            return null;
         } else {
            Pokemon pokemon = Pixelmon.pokemonFactory.create(UUID.randomUUID());
            if (this.name != null) {
               pokemon.species = species;
            }

            if (this.form != null) {
               pokemon.form = this.form;
               pokemon.formEnum = pokemon.getSpecies().getFormEnum(this.form);
            }

            if (this.level != null) {
               pokemon.level = this.level;
            }

            if (this.gender != null) {
               pokemon.gender = Gender.getGender((short)this.gender);
               if (EnumSpecies.genderForm.contains(pokemon.species)) {
                  pokemon.form = pokemon.gender.getForm();
                  pokemon.formEnum = pokemon.getSpecies().getFormEnum(pokemon.form);
               }
            }

            if (this.growth != null) {
               pokemon.growth = EnumGrowth.getGrowthFromIndex(this.growth);
            }

            if (this.nature != null) {
               pokemon.nature = EnumNature.getNatureFromIndex(this.nature);
            }

            if (this.ability != null) {
               pokemon.setAbility(this.ability);
            }

            if (this.gmaxFactor != null) {
               pokemon.setGigantamaxFactor(this.gmaxFactor);
            }

            if (this.shiny != null) {
               pokemon.isShiny = this.shiny;
            }

            if (this.ball != null) {
               pokemon.caughtBall = EnumPokeballs.getFromIndex(this.ball);
            }

            if (this.pokerusType != null) {
               Pokerus p = new Pokerus(EnumPokerusType.values()[this.pokerusType]);
               p.secondsSinceInfection = this.pokerusSpread == null ? -1 : this.pokerusSpread;
               pokemon.setPokerus(p);
            }

            Iterator var5;
            if (this.ribbons != null && !this.ribbons.isEmpty()) {
               var5 = this.ribbons.iterator();

               while(var5.hasNext()) {
                  EnumRibbonType ribbon = (EnumRibbonType)var5.next();
                  pokemon.addRibbon(ribbon);
               }
            }

            pokemon.initialize();
            if (this.egg != null && this.egg) {
               pokemon.makeEgg();
            }

            if (this.extraSpecs != null && !this.extraSpecs.isEmpty()) {
               var5 = this.extraSpecs.iterator();

               while(var5.hasNext()) {
                  SpecValue specValue = (SpecValue)var5.next();
                  specValue.apply(pokemon);
               }
            }

            return pokemon;
         }
      }
   }

   /** @deprecated */
   @Deprecated
   public void apply(NBTTagCompound nbt) {
      if (this.level != null) {
         nbt.func_74768_a("Level", this.level);
      }

      if (this.gender != null) {
         nbt.func_74774_a("Gender", this.gender);
      }

      if (this.growth != null) {
         nbt.func_74774_a("Growth", this.growth);
      }

      if (this.nature != null) {
         nbt.func_74774_a("Nature", this.nature);
      }

      String name;
      EnumSpecies pokemon;
      BaseStats bs;
      if (this.ability != null) {
         nbt.func_74778_a("Ability", this.ability);
         name = nbt.func_74779_i("Name");
         if (name != null && !name.isEmpty()) {
            pokemon = (EnumSpecies)EnumSpecies.getFromName(name).orElse((Object)null);
            if (pokemon != null) {
               bs = pokemon.getBaseStats(pokemon.getFormEnum(nbt.func_74762_e("Variant")));
               if (bs != null) {
                  for(int i = 0; i < bs.getAbilitiesArray().length; ++i) {
                     if (bs.getAbilitiesArray()[i] != null && bs.getAbilitiesArray()[i].equals(this.ability)) {
                        nbt.func_74774_a("AbilitySlot", (byte)i);
                        break;
                     }
                  }
               }
            }
         }
      }

      if (this.gmaxFactor != null) {
         nbt.func_74757_a("GigantamaxFactor", this.gmaxFactor);
      }

      if (this.boss != null) {
         nbt.func_74774_a("BossMode", this.boss);
      }

      if (this.shiny != null) {
         nbt.func_74757_a("IsShiny", this.shiny);
      }

      if (this.form != null) {
         nbt.func_74774_a("Variant", this.form);
      }

      if (this.ball != null) {
         nbt.func_74774_a("CaughtBall", this.ball);
      }

      if (this.pokerusType != null) {
         NBTTagCompound pkrs = new NBTTagCompound();
         pkrs.func_74774_a("Type", this.pokerusType);
         if (this.pokerusSpread == null) {
            pkrs.func_82580_o("Spread");
         } else {
            pkrs.func_74774_a("Spread", this.pokerusSpread);
         }

         nbt.func_74782_a("Pokerus", pkrs);
      }

      if (this.egg != null && this.egg) {
         name = nbt.func_74779_i("Name");
         if (!name.isEmpty()) {
            pokemon = (EnumSpecies)EnumSpecies.getFromName(name).orElse((Object)null);
            if (pokemon != null) {
               bs = pokemon.getBaseStats(pokemon.getFormEnum(nbt.func_74762_e("Variant")));
               if (bs != null) {
                  nbt.func_74768_a("eggCycles", bs.getEggCycles());
               }
            }
         }
      }

      if (this.ribbons != null && !this.ribbons.isEmpty()) {
         NBTTagList ribbonList = new NBTTagList();
         Iterator var9 = this.ribbons.iterator();

         while(var9.hasNext()) {
            EnumRibbonType ribbon = (EnumRibbonType)var9.next();
            ribbonList.func_74742_a(new NBTTagString(ribbon.toString()));
         }

         nbt.func_74782_a("ribbons", ribbonList);
         nbt.func_74778_a("ribbon_display", ((EnumRibbonType)this.ribbons.get(0)).toString());
      }

      if (this.extraSpecs != null) {
         Iterator var8 = this.extraSpecs.iterator();

         while(var8.hasNext()) {
            SpecValue spec = (SpecValue)var8.next();
            spec.apply(nbt);
         }
      }

   }

   public void apply(EntityPixelmon pokemon) {
      if (this.level != null) {
         pokemon.getLvl().setLevel(this.level);
      }

      if (this.gender != null) {
         pokemon.getPokemonData().setGender(Gender.getGender(this.gender.shortValue()));
         if (pokemon.getBaseStats().getMalePercent() < 0.0) {
            pokemon.getPokemonData().setGender(Gender.None);
         } else if (pokemon.getBaseStats().getMalePercent() == 0.0) {
            pokemon.getPokemonData().setGender(Gender.Female);
         } else if (pokemon.getBaseStats().getMalePercent() >= 100.0) {
            pokemon.getPokemonData().setGender(Gender.Male);
         } else if (pokemon.getBaseStats().getMalePercent() > 0.0 && pokemon.getPokemonData().getGender() == Gender.None) {
            pokemon.getPokemonData().setGender(RandomHelper.rand.nextDouble() * 100.0 < pokemon.getBaseStats().getMalePercent() ? Gender.Male : Gender.Female);
         }
      }

      if (this.growth != null) {
         pokemon.getPokemonData().setGrowth(EnumGrowth.getGrowthFromIndex(this.growth));
      }

      if (this.nature != null) {
         pokemon.getPokemonData().setNature(EnumNature.getNatureFromIndex(this.nature));
      }

      if (this.form != null && (this.formInvert == null || !this.formInvert)) {
         pokemon.setForm(this.form);
      }

      if (this.ability != null) {
         pokemon.getPokemonData().setAbility(this.ability);
      }

      if (this.gmaxFactor != null) {
         pokemon.getPokemonData().setGigantamaxFactor(this.gmaxFactor);
      }

      if (this.shiny != null) {
         pokemon.getPokemonData().setShiny(this.shiny);
      }

      if (this.boss != null) {
         pokemon.setBoss(EnumBossMode.getMode(this.boss));
      }

      if (this.ball != null) {
         pokemon.getPokemonData().setCaughtBall(EnumPokeballs.getFromIndex(this.ball));
      }

      if (this.pokerusType != null) {
         Pokerus p = new Pokerus(EnumPokerusType.values()[this.pokerusType]);
         p.secondsSinceInfection = this.pokerusSpread == null ? -1 : this.pokerusSpread;
         pokemon.getPokemonData().setPokerus(p);
      }

      if (this.egg != null && this.egg) {
         pokemon.getPokemonData().makeEgg();
      }

      Iterator var4;
      if (this.extraSpecs != null) {
         var4 = this.extraSpecs.iterator();

         while(var4.hasNext()) {
            SpecValue spec = (SpecValue)var4.next();
            spec.apply(pokemon);
         }
      }

      if (this.ribbons != null && !this.ribbons.isEmpty()) {
         var4 = this.ribbons.iterator();

         while(var4.hasNext()) {
            EnumRibbonType ribbon = (EnumRibbonType)var4.next();
            pokemon.getPokemonData().addRibbon(ribbon);
         }
      }

      pokemon.update(new EnumUpdateType[]{EnumUpdateType.Stats, EnumUpdateType.Texture, EnumUpdateType.Ribbons});
   }

   public void apply(Pokemon pokemon) {
      if (this.name != null) {
         pokemon.setSpecies(EnumSpecies.getFromNameAnyCaseNoTranslate(this.name));
      }

      if (this.level != null) {
         pokemon.getLevelContainer().setLevel(this.level);
      }

      if (this.gender != null) {
         pokemon.setGender(Gender.getGender((short)this.gender));
      }

      if (this.growth != null) {
         pokemon.setGrowth(EnumGrowth.getGrowthFromIndex(this.growth));
      }

      if (this.nature != null) {
         pokemon.setNature(EnumNature.getNatureFromIndex(this.nature));
      }

      if (this.ability != null) {
         pokemon.setAbility((AbilityBase)AbilityBase.getAbility(this.ability).orElse(new ComingSoon(this.ability)));
      }

      if (this.gmaxFactor != null) {
         pokemon.setGigantamaxFactor(this.gmaxFactor);
      }

      if (this.shiny != null) {
         pokemon.setShiny(this.shiny);
      }

      if (this.form != null && (this.formInvert == null || !this.formInvert)) {
         pokemon.setForm(this.form);
      }

      if (this.ball != null) {
         pokemon.setCaughtBall(EnumPokeballs.getFromIndex(this.ball));
      }

      if (this.pokerusType != null) {
         Pokerus p = new Pokerus(EnumPokerusType.values()[this.pokerusType]);
         p.secondsSinceInfection = this.pokerusSpread == null ? -1 : this.pokerusSpread;
         pokemon.setPokerus(p);
      }

      if (this.egg != null && this.egg) {
         pokemon.makeEgg();
      }

      Iterator var4;
      if (this.ribbons != null && !this.ribbons.isEmpty()) {
         var4 = this.ribbons.iterator();

         while(var4.hasNext()) {
            EnumRibbonType ribbon = (EnumRibbonType)var4.next();
            pokemon.addRibbon(ribbon);
         }
      }

      if (this.extraSpecs != null && !this.extraSpecs.isEmpty()) {
         var4 = this.extraSpecs.iterator();

         while(var4.hasNext()) {
            SpecValue specValue = (SpecValue)var4.next();
            specValue.apply(pokemon);
         }
      }

   }

   public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
      if (this.args != null && this.args.length > 0) {
         nbt.func_74778_a("args", String.join(" ", this.args));
      }

      if (this.name != null) {
         nbt.func_74778_a("name", this.name);
      }

      if (this.level != null) {
         nbt.func_74768_a("level", this.level);
      }

      if (this.gender != null) {
         nbt.func_74768_a("gender", this.gender);
      }

      if (this.growth != null) {
         nbt.func_74768_a("growth", this.growth);
      }

      if (this.nature != null) {
         nbt.func_74768_a("nature", this.nature);
      }

      if (this.ability != null) {
         nbt.func_74778_a("ability", this.ability);
      }

      if (this.gmaxFactor != null) {
         nbt.func_74757_a("gmaxFactor", this.gmaxFactor);
      }

      if (this.boss != null) {
         nbt.func_74768_a("boss", this.boss);
      }

      if (this.shiny != null) {
         nbt.func_74757_a("shiny", this.shiny);
      }

      if (this.formInvert != null && this.formInvert) {
         nbt.func_74757_a("formInvert", true);
      }

      if (this.form != null) {
         nbt.func_74768_a("form", this.form);
      }

      if (this.ball != null) {
         nbt.func_74768_a("ball", this.ball);
      }

      if (this.pokerusType != null) {
         nbt.func_74768_a("pokerus", this.pokerusType);
         if (this.pokerusSpread != null) {
            nbt.func_74768_a("spread", this.pokerusSpread);
         }
      }

      if (this.egg != null) {
         nbt.func_74757_a("egg", this.egg);
      }

      if (this.extraSpecs != null && !this.extraSpecs.isEmpty()) {
         NBTTagCompound extraSpecsNBT = new NBTTagCompound();
         Iterator var3 = this.extraSpecs.iterator();

         while(var3.hasNext()) {
            SpecValue specValue = (SpecValue)var3.next();
            ISpecType specType = getSpecForKey(specValue.key);
            if (specType != null) {
               specType.writeToNBT(extraSpecsNBT, specValue);
            }
         }

         nbt.func_74782_a("extraSpecs", extraSpecsNBT);
      }

      return nbt;
   }

   public PokemonSpec readFromNBT(NBTTagCompound nbt) {
      if (nbt.func_74764_b("args")) {
         this.args = nbt.func_74779_i("args").split(" ");
      }

      if (nbt.func_74764_b("name")) {
         this.name = nbt.func_74779_i("name");
      }

      if (nbt.func_74764_b("level")) {
         this.level = nbt.func_74762_e("level");
      }

      if (nbt.func_74764_b("gender")) {
         this.gender = nbt.func_74771_c("gender");
      }

      if (nbt.func_74764_b("growth")) {
         this.growth = nbt.func_74771_c("growth");
      }

      if (nbt.func_74764_b("nature")) {
         this.nature = nbt.func_74771_c("nature");
      }

      if (nbt.func_74764_b("ability")) {
         this.ability = nbt.func_74779_i("ability");
      }

      if (nbt.func_74764_b("gmaxFactor")) {
         this.gmaxFactor = nbt.func_74767_n("gmaxFactor");
      }

      if (nbt.func_74764_b("boss")) {
         this.boss = nbt.func_74771_c("boss");
      }

      if (nbt.func_74764_b("shiny")) {
         this.shiny = nbt.func_74767_n("shiny");
      }

      if (nbt.func_74764_b("formInvert")) {
         this.formInvert = nbt.func_74767_n("formInvert");
      }

      if (nbt.func_74764_b("form")) {
         this.form = nbt.func_74771_c("form");
      }

      if (nbt.func_74764_b("ball")) {
         this.ball = nbt.func_74771_c("ball");
      }

      if (nbt.func_74764_b("pokerus")) {
         this.pokerusType = nbt.func_74771_c("pokerus");
         this.pokerusSpread = nbt.func_74764_b("spread") ? nbt.func_74771_c("spread") : null;
      }

      if (nbt.func_150297_b("egg", 1)) {
         this.egg = nbt.func_74767_n("egg");
      }

      if (nbt.func_74764_b("extraSpecs")) {
         if (this.extraSpecs == null) {
            this.extraSpecs = new ArrayList();
         }

         NBTTagCompound extraSpecsNBT = nbt.func_74775_l("extraSpecs");
         Iterator var3 = extraSpecsNBT.func_150296_c().iterator();

         while(var3.hasNext()) {
            String key = (String)var3.next();
            ISpecType type = getSpecForKey(key);
            if (type != null) {
               SpecValue value = type.readFromNBT(extraSpecsNBT);
               if (value != null) {
                  this.extraSpecs.add(value);
               }
            }
         }
      }

      return this;
   }

   public String toString() {
      return "PokemonSpec{" + this.writeToNBT(new NBTTagCompound()) + "}";
   }

   public static void registerDefaultExtraSpecs() {
      extraSpecTypes.add(new SpecFlag(new String[]{"untradeable"}));
      extraSpecTypes.add(new SpecFlag(new String[]{"unbreedable", "nobreed"}));
      UnbreedableFlag.init();
      extraSpecTypes.add(new SetAllIVsEVsFlag(Lists.newArrayList(new String[]{"minivs", "minimumivs"}), true, 0));
      extraSpecTypes.add(new SetAllIVsEVsFlag(Lists.newArrayList(new String[]{"maxivs", "maximumivs"}), true, 31));
      extraSpecTypes.add(new IVEVSpec(Lists.newArrayList(new String[]{"ivhp"}), "IVHP", StatsType.HP, true, 0, (IVEVSpec.Operation)null));
      extraSpecTypes.add(new IVEVSpec(Lists.newArrayList(new String[]{"ivattack", "ivatk"}), "IVAttack", StatsType.Attack, true, 0, (IVEVSpec.Operation)null));
      extraSpecTypes.add(new IVEVSpec(Lists.newArrayList(new String[]{"ivdefence", "ivdefense", "ivdef"}), "IVDefence", StatsType.Defence, true, 0, (IVEVSpec.Operation)null));
      extraSpecTypes.add(new IVEVSpec(Lists.newArrayList(new String[]{"ivspecialattack", "ivspatk"}), "IVSpAtt", StatsType.SpecialAttack, true, 0, (IVEVSpec.Operation)null));
      extraSpecTypes.add(new IVEVSpec(Lists.newArrayList(new String[]{"ivspecialdefence", "ivspecialdefense", "ivspdef"}), "IVSpDef", StatsType.SpecialDefence, true, 0, (IVEVSpec.Operation)null));
      extraSpecTypes.add(new IVEVSpec(Lists.newArrayList(new String[]{"ivspeed", "ivspd"}), "IVSpeed", StatsType.Speed, true, 0, (IVEVSpec.Operation)null));
      extraSpecTypes.add(new SetAllIVsEVsFlag(Lists.newArrayList(new String[]{"minevs", "minimumevs", "resetevs"}), false, 0));
      extraSpecTypes.add(new SetAllIVsEVsFlag(Lists.newArrayList(new String[]{"maxevs", "maximumevs"}), false, 31));
      extraSpecTypes.add(new IVEVSpec(Lists.newArrayList(new String[]{"evhp"}), "EVHP", StatsType.HP, false, 0, (IVEVSpec.Operation)null));
      extraSpecTypes.add(new IVEVSpec(Lists.newArrayList(new String[]{"evattack", "evatk"}), "EVAttack", StatsType.Attack, false, 0, (IVEVSpec.Operation)null));
      extraSpecTypes.add(new IVEVSpec(Lists.newArrayList(new String[]{"evdefence", "evdefense", "evdef"}), "EVDefence", StatsType.Defence, false, 0, (IVEVSpec.Operation)null));
      extraSpecTypes.add(new IVEVSpec(Lists.newArrayList(new String[]{"evspecialattack", "evspatk"}), "EVSpecialAttack", StatsType.SpecialAttack, false, 0, (IVEVSpec.Operation)null));
      extraSpecTypes.add(new IVEVSpec(Lists.newArrayList(new String[]{"evspecialdefence", "evspecialdefense", "evspdef"}), "EVSpecialDefence", StatsType.SpecialDefence, false, 0, (IVEVSpec.Operation)null));
      extraSpecTypes.add(new IVEVSpec(Lists.newArrayList(new String[]{"evspeed", "evspd"}), "EVSpeed", StatsType.Speed, false, 0, (IVEVSpec.Operation)null));
      extraSpecTypes.add(new StatusSpec((String)null));
      extraSpecTypes.add(new CanMegaEvoSpec(false));
      extraSpecTypes.add(new CanPrimalEvoSpec(false));
      extraSpecTypes.add(new CanUltraBurstSpec(false));
      extraSpecTypes.add(new CanCrownedSpec(false));
      extraSpecTypes.add(new GenerationSpec(1));
      extraSpecTypes.add(new IsLegendarySpec(false));
      extraSpecTypes.add(new CustomTextureSpec(Lists.newArrayList(new String[]{"ct", "CustomTexture", "custom-texture"}), (String)null));
      extraSpecTypes.add(new Type1Spec(Lists.newArrayList(new String[]{"type1", "t1"}), (String)null));
      extraSpecTypes.add(new Type2Spec(Lists.newArrayList(new String[]{"type2", "t2"}), (String)null));
      extraSpecTypes.add(new TypeSpec(Lists.newArrayList(new String[]{"type", "t"}), (String)null));
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof PokemonSpec)) {
         return false;
      } else {
         PokemonSpec spec = (PokemonSpec)o;
         return Arrays.equals(this.args, spec.args);
      }
   }

   public int hashCode() {
      return Arrays.hashCode(this.args);
   }

   public static class Adapter implements JsonDeserializer {
      public static Gson GSON;

      public PokemonSpec deserialize(JsonElement element, Type type, JsonDeserializationContext ctx) throws JsonParseException {
         return element.isJsonObject() ? (PokemonSpec)GSON.fromJson(element, PokemonSpec.class) : new PokemonSpec(element.getAsString());
      }

      static {
         GSON = (new GsonBuilder()).setPrettyPrinting().registerTypeAdapter(SpecValue.class, PokemonSpec.SPEC_VALUE_TYPE_ADAPTER).create();
      }
   }
}
