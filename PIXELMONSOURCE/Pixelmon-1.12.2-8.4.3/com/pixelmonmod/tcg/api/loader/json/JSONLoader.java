package com.pixelmonmod.tcg.api.loader.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixelmonmod.pixelmon.util.helpers.RCFileHelper;
import com.pixelmonmod.tcg.api.card.CardType;
import com.pixelmonmod.tcg.api.card.ImmutableCard;
import com.pixelmonmod.tcg.api.card.ThemeDeck;
import com.pixelmonmod.tcg.api.card.ability.AbilityCard;
import com.pixelmonmod.tcg.api.card.attack.AttackCard;
import com.pixelmonmod.tcg.api.card.attack.BaseAttackEffectTypeAdapter;
import com.pixelmonmod.tcg.api.card.set.CardSet;
import com.pixelmonmod.tcg.api.loader.TCGLoader;
import com.pixelmonmod.tcg.api.registries.CardRegistry;
import com.pixelmonmod.tcg.duel.ability.BaseAbilityEffect;
import com.pixelmonmod.tcg.duel.attack.effects.BaseAttackEffect;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import net.minecraft.util.ResourceLocation;

public class JSONLoader implements TCGLoader {
   private static final Gson GSON = (new GsonBuilder()).registerTypeAdapter(AbilityCard.class, new AbilityTypeAdapter()).registerTypeAdapter(ResourceLocation.class, new ResourceLocationTypeAdapter()).registerTypeAdapter(BaseAbilityEffect.class, new AbilityEffectTypeAdapter()).registerTypeAdapter(BaseAttackEffect.class, new BaseAttackEffectTypeAdapter()).create();
   private static final Gson ABILITY_GSON = (new GsonBuilder()).registerTypeAdapter(BaseAbilityEffect.class, new AbilityEffectTypeAdapter()).create();
   private static final Gson THEME_DECK_GSON = (new GsonBuilder()).registerTypeAdapter(ImmutableCard.class, new ImmutableCardTypeAdapter()).create();

   public List loadCards() throws Exception {
      List cards = new ArrayList();
      Path rootPath = RCFileHelper.pathFromResourceLocation(new ResourceLocation("tcg", "cards"));
      List ls = RCFileHelper.listFilesRecursively(rootPath, (p) -> {
         return true;
      }, true);
      Iterator var4 = ls.iterator();

      while(var4.hasNext()) {
         Path path = (Path)var4.next();

         try {
            InputStream inputStream = this.getInputPath(ImmutableCard.class, path);
            Throwable var7 = null;

            try {
               Scanner scanner = new Scanner(inputStream);
               Throwable var9 = null;

               try {
                  scanner.useDelimiter("\\A");
                  String json = scanner.hasNext() ? scanner.next() : "";
                  ImmutableCard card = (ImmutableCard)GSON.fromJson(json, ImmutableCard.class);
                  if (card.getCardType() == CardType.TRAINER) {
                     card.calculateTrainerEffect();
                  }

                  cards.add(card);
               } catch (Throwable var35) {
                  var9 = var35;
                  throw var35;
               } finally {
                  if (scanner != null) {
                     if (var9 != null) {
                        try {
                           scanner.close();
                        } catch (Throwable var34) {
                           var9.addSuppressed(var34);
                        }
                     } else {
                        scanner.close();
                     }
                  }

               }
            } catch (Throwable var37) {
               var7 = var37;
               throw var37;
            } finally {
               if (inputStream != null) {
                  if (var7 != null) {
                     try {
                        inputStream.close();
                     } catch (Throwable var33) {
                        var7.addSuppressed(var33);
                     }
                  } else {
                     inputStream.close();
                  }
               }

            }
         } catch (IOException var39) {
            var39.printStackTrace();
         }
      }

      return cards;
   }

   public List loadAttacks() throws Exception {
      List cardAttacks = new ArrayList();
      Path rootPath = RCFileHelper.pathFromResourceLocation(new ResourceLocation("tcg", "attacks"));
      List ls = RCFileHelper.listFilesRecursively(rootPath, (p) -> {
         return true;
      }, true);
      Iterator var4 = ls.iterator();

      while(var4.hasNext()) {
         Path path = (Path)var4.next();

         try {
            InputStream inputStream = this.getInputPath(AttackCard.class, path);
            Throwable var7 = null;

            try {
               Scanner scanner = new Scanner(inputStream);
               Throwable var9 = null;

               try {
                  scanner.useDelimiter("\\A");
                  String json = scanner.hasNext() ? scanner.next() : "";
                  AttackCard cardAttack = (AttackCard)GSON.fromJson(json, AttackCard.class);
                  cardAttacks.add(cardAttack);
               } catch (Throwable var35) {
                  var9 = var35;
                  throw var35;
               } finally {
                  if (scanner != null) {
                     if (var9 != null) {
                        try {
                           scanner.close();
                        } catch (Throwable var34) {
                           var9.addSuppressed(var34);
                        }
                     } else {
                        scanner.close();
                     }
                  }

               }
            } catch (Throwable var37) {
               var7 = var37;
               throw var37;
            } finally {
               if (inputStream != null) {
                  if (var7 != null) {
                     try {
                        inputStream.close();
                     } catch (Throwable var33) {
                        var7.addSuppressed(var33);
                     }
                  } else {
                     inputStream.close();
                  }
               }

            }
         } catch (IOException var39) {
            var39.printStackTrace();
         }
      }

      return cardAttacks;
   }

   public List loadAbilities() throws Exception {
      List abilities = new ArrayList();
      Path rootPath = RCFileHelper.pathFromResourceLocation(new ResourceLocation("tcg", "abilities"));
      List ls = RCFileHelper.listFilesRecursively(rootPath, (p) -> {
         return true;
      }, true);
      Iterator var4 = ls.iterator();

      while(var4.hasNext()) {
         Path path = (Path)var4.next();

         try {
            InputStream inputStream = this.getInputPath(AbilityCard.class, path);
            Throwable var7 = null;

            try {
               Scanner scanner = new Scanner(inputStream);
               Throwable var9 = null;

               try {
                  scanner.useDelimiter("\\A");
                  String json = scanner.hasNext() ? scanner.next() : "";
                  AbilityCard cardAbility = (AbilityCard)ABILITY_GSON.fromJson(json, AbilityCard.class);
                  abilities.add(cardAbility);
               } catch (Throwable var35) {
                  var9 = var35;
                  throw var35;
               } finally {
                  if (scanner != null) {
                     if (var9 != null) {
                        try {
                           scanner.close();
                        } catch (Throwable var34) {
                           var9.addSuppressed(var34);
                        }
                     } else {
                        scanner.close();
                     }
                  }

               }
            } catch (Throwable var37) {
               var7 = var37;
               throw var37;
            } finally {
               if (inputStream != null) {
                  if (var7 != null) {
                     try {
                        inputStream.close();
                     } catch (Throwable var33) {
                        var7.addSuppressed(var33);
                     }
                  } else {
                     inputStream.close();
                  }
               }

            }
         } catch (IOException var39) {
            var39.printStackTrace();
         }
      }

      return abilities;
   }

   public List loadThemeDecks() throws Exception {
      List themeDecks = new ArrayList();
      Path rootPath = RCFileHelper.pathFromResourceLocation(new ResourceLocation("tcg", "themedecks"));
      List ls = RCFileHelper.listFilesRecursively(rootPath, (p) -> {
         return true;
      }, true);
      Iterator var4 = ls.iterator();

      while(var4.hasNext()) {
         Path path = (Path)var4.next();

         try {
            InputStream inputStream = this.getInputPath(ThemeDeck.class, path);
            Throwable var7 = null;

            try {
               Scanner scanner = new Scanner(inputStream);
               Throwable var9 = null;

               try {
                  scanner.useDelimiter("\\A");
                  String json = scanner.hasNext() ? scanner.next() : "";
                  ThemeDeck themeDeck = (ThemeDeck)THEME_DECK_GSON.fromJson(json, ThemeDeck.class);
                  themeDecks.add(themeDeck);
               } catch (Throwable var35) {
                  var9 = var35;
                  throw var35;
               } finally {
                  if (scanner != null) {
                     if (var9 != null) {
                        try {
                           scanner.close();
                        } catch (Throwable var34) {
                           var9.addSuppressed(var34);
                        }
                     } else {
                        scanner.close();
                     }
                  }

               }
            } catch (Throwable var37) {
               var7 = var37;
               throw var37;
            } finally {
               if (inputStream != null) {
                  if (var7 != null) {
                     try {
                        inputStream.close();
                     } catch (Throwable var33) {
                        var7.addSuppressed(var33);
                     }
                  } else {
                     inputStream.close();
                  }
               }

            }
         } catch (IOException var39) {
            var39.printStackTrace();
         }
      }

      return themeDecks;
   }

   public List loadCardSets() throws Exception {
      List cardSets = new ArrayList();
      Path rootPath = RCFileHelper.pathFromResourceLocation(new ResourceLocation("tcg", "cardsets"));
      List ls = RCFileHelper.listFilesRecursively(rootPath, (p) -> {
         return true;
      }, true);
      Iterator var4 = ls.iterator();

      while(var4.hasNext()) {
         Path path = (Path)var4.next();

         try {
            InputStream inputStream = this.getInputPath(CardSet.class, path);
            Throwable var7 = null;

            try {
               Scanner scanner = new Scanner(inputStream);
               Throwable var9 = null;

               try {
                  scanner.useDelimiter("\\A");
                  String json = scanner.hasNext() ? scanner.next() : "";
                  CardSet cardSet = (CardSet)GSON.fromJson(json, CardSet.class);
                  int counter = 0;
                  Iterator var13 = CardRegistry.getAll().iterator();

                  while(var13.hasNext()) {
                     ImmutableCard value = (ImmutableCard)var13.next();
                     if (value.getSetID() == cardSet.getID()) {
                        ++counter;
                     }
                  }

                  if (counter >= 10) {
                     cardSets.add(cardSet);
                  }
               } catch (Throwable var38) {
                  var9 = var38;
                  throw var38;
               } finally {
                  if (scanner != null) {
                     if (var9 != null) {
                        try {
                           scanner.close();
                        } catch (Throwable var37) {
                           var9.addSuppressed(var37);
                        }
                     } else {
                        scanner.close();
                     }
                  }

               }
            } catch (Throwable var40) {
               var7 = var40;
               throw var40;
            } finally {
               if (inputStream != null) {
                  if (var7 != null) {
                     try {
                        inputStream.close();
                     } catch (Throwable var36) {
                        var7.addSuppressed(var36);
                     }
                  } else {
                     inputStream.close();
                  }
               }

            }
         } catch (IOException var42) {
            var42.printStackTrace();
         }
      }

      return cardSets;
   }

   private InputStream getInputPath(Class clazz, Path path) {
      return clazz.getResourceAsStream(path.toUri().toString().substring(path.toUri().toString().indexOf("/assets/tcg")).replace("%20", " "));
   }
}
