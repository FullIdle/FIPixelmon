package com.pixelmonmod.pixelmon.client.gui;

import com.pixelmonmod.pixelmon.api.moveskills.MoveSkill;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiKeybindIcon;
import com.pixelmonmod.pixelmon.client.listener.SendoutListener;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.config.PixelmonConfig;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.quests.client.ui.GuiKeybindIconQuest;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import com.pixelmonmod.pixelmon.util.helpers.CollectionHelper;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.util.vector.Vector4f;

public class GuiPixelmonOverlay extends Gui {
   public static boolean isGuiMinimized = false;
   public FontRenderer fontRenderer;
   public static int selectedPixelmon;
   public static String selectedMoveSkill;
   public static int targetId = -1;
   public static boolean isVisible = true;
   private static int sideBarHeight = 200;
   public static List icons;
   protected static GuiKeybindIcon pokedexIcon;
   protected static GuiKeybindIcon wikiIcon;
   protected static GuiKeybindIcon trainerCardIcon;
   protected static GuiKeybindIcon questIcon;
   static int count = 100;
   private static String messageName = null;
   private static String oldMessageName = null;
   private static UUID uuid;
   private static UUID lastUuid;
   private static int fadeInTicks = 0;
   private static int fadeOutTicks = 0;

   public GuiPixelmonOverlay() {
      this.fontRenderer = Minecraft.func_71410_x().field_71466_p;
      icons = new ArrayList();
      icons.add(pokedexIcon = new GuiKeybindIcon(ClientProxy.pokedexKeyBind, GuiResources.pokedexItemIcon));
      icons.add(wikiIcon = new GuiKeybindIcon(ClientProxy.wikiKeyBind, GuiResources.wikiItemIcon));
      icons.add(trainerCardIcon = new GuiKeybindIcon(ClientProxy.trainerCardKeyBind, GuiResources.trainerCardItemIcon));
      icons.add(questIcon = new GuiKeybindIconQuest(ClientProxy.questJournalKeyBind, ClientProxy.questCycleKeyBind));
   }

   @SubscribeEvent
   public void onRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
      Minecraft mc = Minecraft.func_71410_x();
      if (GuiPixelmonOverlay.count++ >= 100) {
         GuiPixelmonOverlay.count = 0;
      }

      if (event.getType() == ElementType.ALL) {
         if (mc.field_71462_r == null || isVisible) {
            if (!(mc.field_71462_r instanceof GuiInventory) && !mc.field_71474_y.field_74319_N && !(mc.field_71462_r instanceof GuiItemDrops)) {
               if (ClientStorageManager.party != null && ClientStorageManager.party.countAll() != 0) {
                  GuiCameraOverlay.checkCameraOn();
                  ScaledResolution var5 = new ScaledResolution(mc);
                  int screenWidth = var5.func_78326_a();
                  int screenHeight = var5.func_78328_b();
                  mc.field_71460_t.func_78478_c();
                  GlStateManager.func_179147_l();
                  GlStateManager.func_179112_b(770, 771);
                  GlStateManager.func_179131_c(0.5F, 0.5F, 0.5F, 1.0F);
                  boolean priorUniFlag = this.fontRenderer.func_82883_a();
                  if (GuiCameraOverlay.isCameraGuiOn) {
                     GuiCameraOverlay.renderCamera(screenWidth, screenHeight, mc);
                  } else {
                     int topSideBar = screenHeight / 2 - sideBarHeight / 2;
                     int topOffset = topSideBar + 5;
                     float slotHeight = 30.0F;
                     mc.field_71446_o.func_110577_a(GuiResources.dock);
                     this.field_73735_i = -90.0F;
                     GuiHelper.drawImageQuad(0.0, (double)topSideBar, 22.0, 203.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
                     int leftText = 30;
                     this.fontRenderer.func_78264_a(true);
                     int i = -1;
                     GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
                     checkSelection();
                     PlayerPartyStorage party = ClientStorageManager.party;
                     Pokemon[] var13 = party.getAll();
                     int scaledWidth = var13.length;

                     int scaledHeight;
                     int yPos;
                     String levelString;
                     for(int var15 = 0; var15 < scaledWidth; ++var15) {
                        Pokemon pokemon = var13[var15];
                        scaledHeight = 0;
                        ++i;
                        yPos = topOffset + (int)((float)i * slotHeight) + 9 + scaledHeight;
                        if (pokemon != null) {
                           boolean isSentOut = SendoutListener.isInWorld(pokemon.getUUID(), mc.field_71441_e);
                           if (!isGuiMinimized) {
                              mc.field_71446_o.func_110577_a(GuiResources.textbox);
                              GuiHelper.drawImageQuad((double)(leftText - 28), (double)(yPos - 10), 123.0, 34.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
                              float textureX = -1.0F;
                              float textureY = -1.0F;
                              float[] texturePair = StatusType.getTexturePos(pokemon.getStatus().type);
                              textureX = texturePair[0];
                              textureY = texturePair[1];
                              if (textureX != -1.0F && pokemon.getHealth() > 0) {
                                 mc.field_71446_o.func_110577_a(GuiResources.status);
                                 GuiHelper.drawImageQuad((double)(leftText + 56), (double)(yPos + 1), 8.0, 8.0F, (double)(textureX / 768.0F), (double)(textureY / 512.0F), (double)((textureX + 240.0F) / 768.0F), (double)((textureY + 240.0F) / 512.0F), this.field_73735_i);
                              }
                           }

                           String displayName = pokemon.getDisplayName();
                           if (!isGuiMinimized) {
                              this.fontRenderer.func_78276_b(displayName, leftText - 2, yPos, 16777215);
                              GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
                              if (pokemon.getGender() == Gender.Male && !pokemon.isEgg()) {
                                 mc.field_71446_o.func_110577_a(GuiResources.male);
                                 GuiHelper.drawImageQuad((double)(this.fontRenderer.func_78256_a(displayName) + leftText - 1), (double)yPos, 5.0, 8.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
                              } else if (pokemon.getGender() == Gender.Female && !pokemon.isEgg()) {
                                 mc.field_71446_o.func_110577_a(GuiResources.female);
                                 GuiHelper.drawImageQuad((double)(this.fontRenderer.func_78256_a(displayName) + leftText - 1), (double)yPos, 5.0, 8.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
                              }
                           }

                           GuiHelper.bindPokeballTexture(pokemon.getCaughtBall());
                           GuiHelper.drawImageQuad(-3.0, (double)(yPos - 7), 32.0, 32.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
                           if (i == selectedPixelmon) {
                              if (pokemon.getHealth() <= 0) {
                                 mc.field_71446_o.func_110577_a(GuiResources.faintedSelected);
                              } else if (isSentOut) {
                                 mc.field_71446_o.func_110577_a(GuiResources.releasedSelected);
                              } else {
                                 mc.field_71446_o.func_110577_a(GuiResources.selected);
                              }
                           } else if (pokemon.getHealth() <= 0) {
                              mc.field_71446_o.func_110577_a(GuiResources.fainted);
                           } else if (isSentOut) {
                              mc.field_71446_o.func_110577_a(GuiResources.released);
                           } else {
                              mc.field_71446_o.func_110577_a(GuiResources.normal);
                           }

                           GuiHelper.drawImageQuad(-3.0, (double)(yPos - 7), 32.0, 32.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
                           this.drawPokemonSprite(pokemon, yPos, mc);
                           if (!pokemon.getHeldItem().func_190926_b()) {
                              mc.field_71446_o.func_110577_a(GuiResources.heldItem);
                              GuiHelper.drawImageQuad(18.0, (double)(yPos + 13), 6.0, 6.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
                           }

                           if (!isGuiMinimized && !pokemon.isEgg()) {
                              levelString = I18n.func_135052_a("gui.screenpokechecker.lvl", new Object[0]) + " " + pokemon.getLevel();
                              this.fontRenderer.func_78276_b(levelString, leftText - 1, yPos + 1 + this.fontRenderer.field_78288_b, 16777215);
                              if (pokemon.getHealth() <= 0) {
                                 this.fontRenderer.func_78276_b(I18n.func_135052_a("gui.creativeinv.fainted", new Object[0]), leftText + 1 + this.fontRenderer.func_78256_a(levelString), yPos + 1 + this.fontRenderer.field_78288_b, 16777215);
                              } else {
                                 this.fontRenderer.func_78276_b(I18n.func_135052_a("nbt.hp.name", new Object[0]) + " " + pokemon.getHealth() + "/" + pokemon.getMaxHealth(), leftText + 2 + this.fontRenderer.func_78256_a(levelString), yPos + 1 + this.fontRenderer.field_78288_b, 16777215);
                              }
                           }
                        } else {
                           mc.field_71446_o.func_110577_a(GuiResources.available);
                           GuiHelper.drawImageQuad(5.0, (double)(yPos + 1), 16.0, 16.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
                        }
                     }

                     ScaledResolution eventResolution = event.getResolution();
                     scaledWidth = eventResolution.func_78326_a();
                     Pokemon pokemon = party.get(selectedPixelmon);
                     int pixels;
                     if (pokemon != null) {
                        boolean isSentOut = SendoutListener.isInWorld(pokemon.getUUID(), mc.field_71441_e);
                        if (PixelmonConfig.showTarget && isSentOut) {
                           mc.field_71446_o.func_110577_a(GuiResources.targetArea);
                           GuiHelper.drawImageQuad((double)(scaledWidth - 76), 2.0, 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
                           this.fontRenderer.func_78276_b(GameSettings.func_74298_c(ClientProxy.actionKeyBind.func_151463_i()), scaledWidth - 50, 0, 16777215);
                           if (targetId != -1) {
                              EntityLivingBase entity = (EntityLivingBase)mc.field_71441_e.func_73045_a(targetId);
                              if (entity instanceof EntityPixelmon) {
                                 Pokemon target = ((EntityPixelmon)entity).getPokemonData();
                                 GuiHelper.bindPokemonSprite(target, mc);
                                 GuiHelper.drawImageQuad((double)(scaledWidth - 75), 3.0, 20.0, 20.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
                              } else if (entity instanceof EntityPlayer) {
                                 mc.func_175598_ae().field_78724_e.func_110577_a(((AbstractClientPlayer)entity).func_110306_p());
                                 GuiHelper.drawImageQuad((double)(scaledWidth - 73), 5.0, 18.0, 18.0F, 0.125, 0.125, 0.25, 0.25, this.field_73735_i);
                              } else if (entity instanceof NPCTrainer) {
                                 NPCTrainer trainer = (NPCTrainer)entity;
                                 if (trainer.getCustomSteveTexture() != "" && trainer.getCustomSteveTexture().contains(";")) {
                                    trainer.bindTexture();
                                 } else {
                                    mc.func_175598_ae().field_78724_e.func_110577_a(new ResourceLocation(((NPCTrainer)entity).getTexture()));
                                 }

                                 GuiHelper.drawImageQuad((double)(scaledWidth - 73), 5.0, 18.0, 18.0F, 0.125, 0.125, 0.25, 0.25, this.field_73735_i);
                              }
                           } else {
                              mc.field_71446_o.func_110577_a(GuiResources.notarget);
                              GuiHelper.drawImageQuad((double)(scaledWidth - 76), 2.0, 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
                           }

                           mc.field_71446_o.func_110577_a(GuiResources.targetAreaOver);
                           GuiHelper.drawImageQuad((double)(scaledWidth - 76), 2.0, 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
                        } else {
                           targetId = -1;
                        }

                        if (isSentOut && PixelmonServerConfig.allowExternalMoves) {
                           ArrayList moveSkills = ClientProxy.getMoveSkills(ClientStorageManager.party.get(selectedPixelmon));
                           MoveSkill selected = selectedMoveSkill == null ? null : (MoveSkill)CollectionHelper.find(moveSkills, (ms) -> {
                              return ms.id.equals(selectedMoveSkill);
                           });
                           if (selected == null && !moveSkills.isEmpty()) {
                              selected = (MoveSkill)moveSkills.get(0);
                              selectedMoveSkill = selected.id;
                           }

                           if (selected != null) {
                              mc.field_71446_o.func_110577_a(selected.sprite);
                              GuiHelper.drawImageQuad((double)(scaledWidth - 42), 2.0, 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
                              double cooldownRatio = pokemon.getMoveSkillCooldownRatio(selected);
                              if (cooldownRatio != 1.0) {
                                 mc.field_71446_o.func_110577_a(GuiResources.cooldown);
                                 pixels = (int)((1.0 - cooldownRatio) * 24.0);
                                 GuiHelper.drawImageQuad((double)(scaledWidth - 43), (double)(25 - pixels), 26.0, (float)(pixels + 2), 0.0, (double)(1.0F - (float)pixels / 24.0F), 1.0, 1.0, this.field_73735_i);
                              }

                              this.fontRenderer.func_78276_b(GameSettings.func_74298_c(ClientProxy.useMoveSkillKeyBind.func_151463_i()), scaledWidth - 17, 0, 16777215);
                              this.fontRenderer.func_78276_b(GameSettings.func_74298_c(ClientProxy.chooseMoveSkillKey.func_151463_i()), scaledWidth - 17, 18, 16777215);
                              levelString = I18n.func_135052_a(selected.name, new Object[0]);
                              this.fontRenderer.func_78276_b(levelString, scaledWidth - 30 - this.fontRenderer.func_78256_a(levelString) / 2, 26, 16777215);
                           }
                        }
                     }

                     EntityPlayer player = Minecraft.func_71410_x().field_71439_g;
                     if (player.func_184187_bx() != null && player.func_184187_bx() instanceof EntityPixelmon) {
                        EntityPixelmon ridingEntity = (EntityPixelmon)player.func_184187_bx();
                        if (ridingEntity.getIsFlying()) {
                           int height = 126;
                           int width = 15;
                           int topPos = screenHeight / 2 - height / 2;
                           pixels = screenWidth - 25;
                           Vector4f lightGray = new Vector4f(0.1F, 0.1F, 0.1F, 0.2F);
                           Vector4f darkGray = new Vector4f(0.05F, 0.05F, 0.05F, 0.6F);
                           GuiHelper.drawGradientRect(pixels, topPos, 0.0F, pixels + width, topPos + height, lightGray, lightGray, true);
                           GuiHelper.drawGradientRect(pixels + 1, topPos + 1, 0.0F, pixels + width - 1, topPos + height - 1, darkGray, darkGray, true);
                           float speed = ridingEntity.moveMultiplier;
                           float percent = speed / EntityPixelmon.maxMoveMultiplier;
                           new Vector4f(1.0F - percent, percent, 0.0F, 1.0F);
                           int barheight = (int)(percent * (float)(height - 6));
                           int segmentHeight = 30;
                           int whiteBarHeight = Math.min(barheight - segmentHeight * 3, segmentHeight);
                           Vector4f colour = new Vector4f(1.0F, 1.0F, 0.4F, 1.0F);
                           GuiHelper.drawGradientRect(pixels + 3, topPos + height - 3 - whiteBarHeight - segmentHeight * 3, 0.0F, pixels + width - 3, topPos + height - 3, colour, colour, true);
                           int yellowBarHeight = Math.min(barheight - segmentHeight * 2, segmentHeight);
                           colour = new Vector4f(1.0F, 0.75F, 0.3F, 1.0F);
                           GuiHelper.drawGradientRect(pixels + 3, topPos + height - 3 - yellowBarHeight - segmentHeight * 2, 0.0F, pixels + width - 3, topPos + height - 3, colour, colour, true);
                           int orangeBarHeight = Math.min(barheight - segmentHeight, segmentHeight);
                           colour = new Vector4f(1.0F, 0.25F, 0.2F, 1.0F);
                           GuiHelper.drawGradientRect(pixels + 3, topPos + height - 3 - orangeBarHeight - segmentHeight, 0.0F, pixels + width - 3, topPos + height - 3, colour, colour, true);
                           int redBarHeight = Math.min(barheight, segmentHeight);
                           colour = new Vector4f(1.0F, 0.0F, 0.1F, 1.0F);
                           GuiHelper.drawGradientRect(pixels + 3, topPos + height - 3 - redBarHeight, 0.0F, pixels + width - 3, topPos + height - 3, colour, colour, true);
                        }
                     }

                     scaledHeight = eventResolution.func_78328_b();
                     yPos = 0;
                     Iterator var45 = icons.iterator();

                     while(var45.hasNext()) {
                        GuiKeybindIcon icon = (GuiKeybindIcon)var45.next();
                        if (icon instanceof GuiKeybindIconQuest) {
                           icon.draw(scaledWidth - 30, scaledHeight - 60, this.field_73735_i);
                        } else {
                           icon.draw(scaledWidth - 30 - yPos++ * 25, scaledHeight - 30, this.field_73735_i);
                        }
                     }

                     this.renderSpectateMessage(screenWidth, screenHeight);
                  }

                  this.fontRenderer.func_78264_a(priorUniFlag);
                  RenderHelper.func_74518_a();
                  GlStateManager.func_179140_f();
                  GlStateManager.func_179132_a(true);
                  GlStateManager.func_179126_j();
               }
            }
         }
      }
   }

   private void drawPokemonSprite(Pokemon pokemon, int yPos, Minecraft mc) {
      GuiHelper.bindPokemonSprite(pokemon, mc);
      GuiHelper.drawImageQuad(1.0, (double)(yPos - 6), 24.0, 24.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
   }

   public static void selectNextPixelmon() {
      if (ClientStorageManager.party.countAll() != 0 && ClientStorageManager.party.countPokemon() != 0) {
         ++selectedPixelmon;
         if (selectedPixelmon >= 6) {
            selectedPixelmon = 0;
         }

         while(ClientStorageManager.party.get(selectedPixelmon) == null || ClientStorageManager.party.get(selectedPixelmon).isEgg()) {
            ++selectedPixelmon;
            if (selectedPixelmon >= 6) {
               selectedPixelmon = 0;
            }
         }

      }
   }

   public static void selectPreviousPixelmon() {
      if (ClientStorageManager.party.countAll() != 0 && ClientStorageManager.party.countPokemon() != 0) {
         --selectedPixelmon;
         if (selectedPixelmon < 0) {
            selectedPixelmon = 5;
         }

         while(ClientStorageManager.party.get(selectedPixelmon) == null || ClientStorageManager.party.get(selectedPixelmon).isEgg()) {
            --selectedPixelmon;
            if (selectedPixelmon < 0) {
               selectedPixelmon = 5;
            }
         }

      }
   }

   public static void checkSelection() {
      Pokemon current = ClientStorageManager.party.get(selectedPixelmon);
      if ((current == null || current.isEgg()) && !ClientProxy.battleManager.isBattling()) {
         selectPreviousPixelmon();
      }

   }

   public static void showSpectateMessage(UUID playerUuid) {
      EntityPlayer player = Minecraft.func_71410_x().field_71441_e.func_152378_a(playerUuid);
      if (player != null) {
         messageName = player.getDisplayNameString();
         uuid = playerUuid;
         fadeInTicks = 20;
      }
   }

   public static void hideSpectateMessage(UUID playerUuid) {
      if (uuid == null) {
         fadeOutTicks = 0;
         messageName = null;
      } else {
         EntityPlayer player = Minecraft.func_71410_x().field_71441_e.func_152378_a(uuid);
         if (player == null) {
            fadeOutTicks = 0;
            uuid = null;
            messageName = null;
         } else {
            fadeOutTicks = 15;
            oldMessageName = player.getDisplayNameString();
            lastUuid = uuid;
            if (uuid.equals(playerUuid)) {
               messageName = null;
               uuid = null;
            }

         }
      }
   }

   private void renderSpectateMessage(int screenWidth, int screenHeight) {
      String spectateString;
      int width;
      float alpha;
      int c;
      if (messageName != null) {
         spectateString = I18n.func_135052_a("gui.spectate.spectateMessage", new Object[]{GameSettings.func_74298_c(ClientProxy.spectateKeyBind.func_151463_i()), messageName});
         width = this.fontRenderer.func_78256_a(spectateString);
         alpha = 1.0F - (float)fadeInTicks / 20.0F * 0.7F;
         c = GuiHelper.toColourValue(0.7F, 0.7F, 0.7F, alpha);
         this.fontRenderer.func_78276_b(spectateString, screenWidth / 2 - width / 2, screenHeight / 2 + 30, c);
      }

      if (oldMessageName != null && fadeOutTicks > 0) {
         spectateString = I18n.func_135052_a("gui.spectate.spectateMessage", new Object[]{GameSettings.func_74298_c(ClientProxy.spectateKeyBind.func_151463_i()), oldMessageName});
         width = this.fontRenderer.func_78256_a(spectateString);
         alpha = (float)fadeOutTicks / 15.0F * 0.7F;
         c = GuiHelper.toColourValue(0.7F, 0.7F, 0.7F, alpha);
         this.fontRenderer.func_78276_b(spectateString, screenWidth / 2 - width / 2, screenHeight / 2 + 30, c);
      }

   }

   public static void onPlayerTick() {
      if (fadeInTicks > 0) {
         --fadeInTicks;
      }

      if (fadeOutTicks > 0) {
         --fadeOutTicks;
      }

   }

   public static UUID getCurrentSpectatingUUID() {
      if (uuid != null) {
         return uuid;
      } else {
         return lastUuid != null && fadeOutTicks > 0 ? lastUuid : null;
      }
   }
}
