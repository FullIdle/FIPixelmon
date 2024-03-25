package com.pixelmonmod.pixelmon.client.gui.raids;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.raids.RaidData;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.SoundHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiParticleEngine;
import com.pixelmonmod.pixelmon.client.gui.battles.GuiBattle;
import com.pixelmonmod.pixelmon.client.models.smd.AnimationType;
import com.pixelmonmod.pixelmon.client.render.RenderPixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.raids.RaidAction;
import com.pixelmonmod.pixelmon.config.PixelmonItemsPokeballs;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.items.ItemPokeball;
import com.pixelmonmod.pixelmon.sounds.PixelSounds;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.input.Mouse;

public class GuiRaidCatch extends GuiScreen {
   private static final ResourceLocation WATERMARK = new ResourceLocation("pixelmon", "textures/gui/raids/watermark.png");
   private static final ResourceLocation STAR = new ResourceLocation("pixelmon", "textures/gui/raids/star.png");
   private static final ResourceLocation SHINY = new ResourceLocation("pixelmon", "textures/gui/battle/shiny.png");
   private final ArrayList buttons = new ArrayList();
   private RaidData raid;
   private EntityPixelmon pixelmon;
   private boolean shiny;
   private ItemStack[] drops;
   private ArrayList balls = new ArrayList();
   private boolean canCatch;
   private boolean sentToPC;
   private boolean catchSuccess = false;
   private boolean pcFull = false;
   private boolean inCatchAnimation = false;
   private int shakes = 0;
   private int ticker = 0;
   private int delay = -1;
   private boolean shakeSound = false;
   private boolean shouldRefresh = false;
   private int ballIndex = 0;
   private int scrollIndex = 0;
   private boolean closing = false;
   protected GuiParticleEngine particleEngine = new GuiParticleEngine();
   private boolean scrolling = false;
   private boolean click = false;
   private long lastTime = -1L;

   public GuiRaidCatch(RaidData raid, boolean shiny, ItemStack[] drops, boolean canCatch) {
      this.field_146297_k = Minecraft.func_71410_x();
      SoundHelper.playSound(SoundEvents.field_194228_if, 0.5F, 1.0F);
      this.drops = drops;
      this.canCatch = canCatch;
      if (this.canCatch) {
         for(int i = 0; i < this.field_146297_k.field_71439_g.field_71071_by.func_70302_i_(); ++i) {
            ItemStack stack = this.field_146297_k.field_71439_g.field_71071_by.func_70301_a(i);
            if (!stack.func_190926_b() && stack.func_77973_b() instanceof ItemPokeball) {
               this.balls.add(new Tuple(i, stack));
            }
         }
      }

      this.shiny = shiny;
      this.setRaid(raid);
   }

   public void setRaid(RaidData raid) {
      this.setRaid(raid, (GuiRaidCatch)null);
   }

   public void setRaid(RaidData raid, GuiRaidCatch old) {
      this.raid = raid;
      if (old != null) {
         this.pixelmon = old.pixelmon;
      } else {
         Pokemon pokemon = Pixelmon.pokemonFactory.create(raid.getSpecies());
         if (raid.getForm() != null) {
            pokemon.setForm(raid.getForm());
         }

         pokemon.setGrowth(EnumGrowth.Ordinary);
         pokemon.setShiny(this.shiny);
         this.pixelmon = new EntityPixelmon(this.field_146297_k.field_71441_e);
         this.pixelmon.setPokemon(pokemon);
         this.pixelmon.setAnimation(AnimationType.IDLE);
         this.pixelmon.checkAnimation();
         this.pixelmon.initAnimation();
      }

      this.shouldRefresh = true;
   }

   public void setShakes(int shakes, boolean sentToPC) {
      if (!this.inCatchAnimation && this.canCatch) {
         if (shakes < 0) {
            this.pcFull = true;
         } else {
            SoundHelper.playSound(PixelSounds.pokeballCapture, 0.4F, 1.0F);
            this.inCatchAnimation = true;
            this.ticker = -500;
            this.shakes = MathHelper.func_76125_a(shakes, 1, 3);
            this.catchSuccess = this.shakes == 3;
            this.canCatch = false;
            this.sentToPC = sentToPC;
         }

         this.shouldRefresh = true;
      }

   }

   private void initButtons() {
      this.buttons.clear();
      if (this.canCatch) {
         this.buttons.add((new RaidButton((double)this.field_146294_l * 0.5, (double)this.field_146295_m / 1.06 - (double)((int)((double)this.field_146295_m * 0.08 * 2.0)), 1.0, 0.0, (double)((float)this.field_146295_m / 17.0F), "<", this.raid, (button) -> {
            boolean prev = this.canUseCurrentBall();
            --this.ballIndex;
            if (this.ballIndex < 0) {
               this.ballIndex = this.balls.size() - 1;
            }

            if (this.canUseCurrentBall() != prev) {
               this.shouldRefresh = true;
            }

         })).setEnabled(this.balls.size() > 1).setVisible(this.canCatch));
         this.buttons.add((new RaidButton((double)this.field_146294_l * 0.6, (double)this.field_146295_m / 1.06 - (double)((int)((double)this.field_146295_m * 0.08 * 2.0)), 1.0, 0.0, (double)((float)this.field_146295_m / 17.0F), ">", this.raid, (button) -> {
            boolean prev = this.canUseCurrentBall();
            ++this.ballIndex;
            if (this.ballIndex >= this.balls.size()) {
               this.ballIndex = 0;
            }

            if (this.canUseCurrentBall() != prev) {
               this.shouldRefresh = true;
            }

         })).setEnabled(this.balls.size() > 1).setVisible(this.canCatch));
         this.buttons.add((new RaidButton((double)this.field_146294_l * 0.643, (double)this.field_146295_m / 1.06 - (double)((int)((double)this.field_146295_m * 0.08 * 2.0)), 1.0, (double)((float)this.field_146294_l / 7.0F), (double)((float)this.field_146295_m / 17.0F), this.pcFull ? I18n.func_135052_a("raid.button.pcfull", new Object[0]) : (this.balls.isEmpty() ? I18n.func_135052_a("raid.button.noballs", new Object[0]) : (this.canUseCurrentBall() ? I18n.func_135052_a("raid.button.catch", new Object[0]) : I18n.func_135052_a("raid.button.cantuse", new Object[0]))), this.raid, (button) -> {
            Pixelmon.network.sendToServer(new RaidAction(this.raid.getDen(), 10, (Integer)((Tuple)this.balls.get(this.ballIndex)).func_76341_a()));
         })).setEnabled(this.canCatch && !this.balls.isEmpty() && !this.pcFull && this.canUseCurrentBall()).setVisible(this.canCatch));
         this.buttons.add((new RaidButton((double)this.field_146294_l * 0.83, (double)this.field_146295_m / 1.06 - (double)((int)((double)this.field_146295_m * 0.08 * 2.0)), 1.0, (double)((float)this.field_146294_l / 7.0F), (double)((float)this.field_146295_m / 17.0F), I18n.func_135052_a("raid.button.dontcatch", new Object[0]), this.raid, (button) -> {
            this.closing = true;
            GuiBattle.restoreSettingsAndCloseStatic(ClientProxy.battleManager);
         })).setEnabled(this.canCatch).setVisible(this.canCatch));
      } else {
         this.buttons.add((new RaidButton((double)this.field_146294_l * 0.55, (double)this.field_146295_m / 1.06 - (double)((int)((double)this.field_146295_m * 0.08 * 2.0)), 1.0, (double)((float)this.field_146294_l / 2.365F), (double)((float)this.field_146295_m / 17.0F), I18n.func_135052_a("raid.button.done", new Object[0]), this.raid, (button) -> {
            this.closing = true;
            GuiBattle.restoreSettingsAndCloseStatic(ClientProxy.battleManager);
         })).setEnabled(!this.inCatchAnimation && !this.canCatch).setVisible(!this.canCatch));
      }

   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      if (this.buttons.isEmpty() || this.shouldRefresh) {
         this.shouldRefresh = false;
         this.initButtons();
      }

      this.func_146276_q_();
      GlStateManager.func_179141_d();
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(770, 771);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      Iterator var4 = this.buttons.iterator();

      while(var4.hasNext()) {
         RaidButton button = (RaidButton)var4.next();
         button.draw((double)mouseX, (double)mouseY, partialTicks);
      }

      this.field_146297_k.field_71446_o.func_110577_a(STAR);
      int starSize = (int)((double)this.field_146294_l * 0.05);
      double sw = (double)(this.raid.getStars() * starSize);

      for(int i = 0; i < this.raid.getStars(); ++i) {
         GuiHelper.drawImage((double)((float)this.field_146294_l * 0.75F + (float)(starSize * i)) - sw / 2.0, (double)((float)this.field_146295_m * 0.185F), (double)starSize, (double)starSize, 1.0F);
      }

      float fs = (float)this.field_146294_l * 0.045F;
      GuiHelper.drawScaledCenteredString(I18n.func_135052_a(this.catchSuccess && !this.inCatchAnimation ? "raid.caught" : "raid.defeated", new Object[]{this.raid.getSpecies().getPokemonName()}), (float)this.field_146294_l * 0.755F, (float)this.field_146295_m * 0.125F - fs / 4.0F, -1, fs);
      this.drawDrops(mouseX, mouseY);
      float scale;
      if (!this.balls.isEmpty() && this.canCatch) {
         scale = (float)this.field_146294_l / 960.0F * 3.0F;
         GlStateManager.func_179094_E();
         GlStateManager.func_179137_b((double)((float)this.field_146294_l * 0.525F), (double)this.field_146295_m * 0.7675, 0.0);
         GlStateManager.func_179152_a(scale, scale, scale);
         this.drawItem((ItemStack)((Tuple)this.balls.get(this.ballIndex)).func_76340_b(), 0.0F, 0.0F, 770, 771);
         GlStateManager.func_179121_F();
      }

      if (this.canCatch || this.ticker < 0 || !this.raid.canAllCatch && !this.raid.isOwner(this.field_146297_k.field_71439_g.func_110124_au())) {
         GlStateManager.func_179094_E();
         GlStateManager.func_179142_g();
         GlStateManager.func_179126_j();
         GlStateManager.func_179132_a(true);
         GlStateManager.func_179109_b((float)this.field_146294_l / 4.0F, (float)this.field_146295_m / 4.0F * 3.0F, 300.0F);
         double referenceW = (double)((float)this.field_146295_m / 3.0F);
         double referenceH = (double)((float)this.field_146295_m / 4.0F);
         double dh = this.pixelmon.getBaseStats().getBoundsData().getHeight() - referenceH;
         double dw = this.pixelmon.getBaseStats().getBoundsData().getWidth() - referenceW;
         double scalar = 1.0;
         if (dh > dw) {
            scalar = referenceH / this.pixelmon.getBaseStats().getBoundsData().getHeight();
         } else {
            scalar = referenceW / this.pixelmon.getBaseStats().getBoundsData().getWidth();
         }

         GlStateManager.func_179139_a(scalar, scalar, scalar);
         if (this.ticker < 0) {
            float shrink = (float)Math.max(0, -this.ticker) / 500.0F;
            GlStateManager.func_179152_a(shrink, shrink, shrink);
         }

         GlStateManager.func_179114_b(180.0F, 0.0F, 0.0F, 1.0F);
         RenderHelper.func_74519_b();

         try {
            RenderManager renderManager = Minecraft.func_71410_x().func_175598_ae();
            Render entityClassRenderObject = renderManager.func_78715_a(EntityPixelmon.class);
            RenderPixelmon rp = (RenderPixelmon)entityClassRenderObject;
            rp.renderPixelmon(this.pixelmon, 0.0, 0.0, 0.0, partialTicks, true);
            renderManager.field_78735_i = 180.0F;
         } catch (Exception var23) {
            var23.printStackTrace();
         }

         GlStateManager.func_179121_F();
         if (this.shiny && RandomHelper.rand.nextInt(80) == 0) {
            double dx = (RandomHelper.rand.nextDouble() - 0.5) * (double)this.field_146295_m * 0.5;
            double dy = (RandomHelper.rand.nextDouble() - 0.5) * (double)this.field_146295_m * 0.5;
            int size = 15 + RandomHelper.rand.nextInt(15);
            this.particleEngine.addParticle(new GuiParticleEngine.GuiParticle(SHINY, (double)this.field_146294_l * 0.225 + dx, (double)this.field_146295_m * 0.5 + dy, 1000.0, 0.0, 0.0, 0.0, 1.0F, 0.8F, 0.3F, 0.0F, (float)size, (float)size, 120, (particle) -> {
               int x = particle.age;
               int m = particle.maxAge;
               int h = m / 2;
               particle.a = (float)(x <= h ? x : h - (x - h)) / (float)h;
            }));
         }
      }

      if (!this.canCatch) {
         GlStateManager.func_179094_E();
         scale = (float)this.field_146294_l / 960.0F * 6.0F;
         GlStateManager.func_179137_b((double)((float)this.field_146294_l * 0.201F), (double)this.field_146295_m * 0.58, 200.0);
         GlStateManager.func_179152_a(scale, scale, scale);
         if (this.inCatchAnimation) {
            long ms = this.getMillis();
            if (this.shakes > 0) {
               boolean tick = true;
               float scalar = 0.5F;
               float speed = 1.0F;
               float angle = 0.0F;
               float shrink = 1.0F;
               if (this.ticker >= 0) {
                  if (this.delay == -1) {
                     this.delay = 500;
                  }

                  if (this.delay > 0) {
                     this.delay = (int)((long)this.delay - ms);
                     tick = false;
                  }

                  if ((float)this.ticker < 100.0F * speed) {
                     angle = (float)(-this.ticker) * scalar;
                  } else if ((float)this.ticker < 300.0F * speed) {
                     angle = ((float)this.ticker - 200.0F * speed) * scalar;
                     if ((float)this.ticker < 200.0F * speed && !this.shakeSound) {
                        SoundHelper.playSound(SoundEvents.field_187676_dE, 1.0F, 1.0F);
                        this.shakeSound = true;
                     }
                  } else if ((float)this.ticker < 400.0F * speed) {
                     angle = (400.0F * speed - (float)this.ticker) * scalar;
                  } else if ((float)this.ticker > 500.0F * speed) {
                     this.ticker = 0;
                     --this.shakes;
                     this.shakeSound = false;
                  }
               } else {
                  shrink = 1.0F - (float)Math.max(0, -this.ticker) / 500.0F;
               }

               GlStateManager.func_179109_b(8.0F, 16.0F, 0.0F);
               GlStateManager.func_179152_a(shrink, shrink, shrink);
               GlStateManager.func_179114_b(angle, 0.0F, 0.0F, 1.0F);
               GlStateManager.func_179109_b(-8.0F, -16.0F, 0.0F);
               if (tick) {
                  this.ticker = (int)((double)this.ticker + (double)ms / 2.5);
               }
            } else {
               this.inCatchAnimation = false;
               this.shouldRefresh = true;
               if (this.catchSuccess) {
                  SoundHelper.playSound(PixelSounds.pokeballCaptureSuccess, 0.2F, 1.0F);

                  for(int i = 0; i < 15; ++i) {
                     int r = RandomHelper.rand.nextInt(360);
                     double dx = (RandomHelper.rand.nextDouble() * 2.0 - 1.0) * (double)this.field_146294_l * 0.05;
                     double dy = (RandomHelper.rand.nextDouble() * 2.0 - 1.0) * (double)this.field_146294_l * 0.05;
                     this.particleEngine.addParticle(new GuiParticleEngine.GuiParticle(STAR, (double)((float)this.field_146294_l * 0.251F) + dx, (double)this.field_146295_m * 0.675 + dy, 0.0, 0.0, 0.0, 0.0, 0.96F, 0.78F, 0.0F, 0.0F, 0.0F, 0.0F, RandomHelper.rand.nextInt(100) + 100, (particle) -> {
                        int x = particle.age;
                        int m = particle.maxAge;
                        int h = m / 2;
                        float s = (float)(x <= h ? x : h - (x - h)) / (float)h;
                        GlStateManager.func_179137_b(particle.x, particle.y, 0.0);
                        GlStateManager.func_179114_b((float)(r + x), 0.0F, 0.0F, 1.0F);
                        GlStateManager.func_179137_b(-particle.x, -particle.y, 0.0);
                        GlStateManager.func_179109_b(-particle.w / 2.0F, -particle.h / 2.0F, 0.0F);
                        particle.a = s;
                        particle.w = s * (float)this.field_146294_l * 0.0525F;
                        particle.h = s * (float)this.field_146294_l * 0.0525F;
                     }));
                  }
               } else {
                  SoundHelper.playSound(SoundEvents.field_187635_cQ, 0.8F, 0.8F + this.field_146297_k.field_71441_e.field_73012_v.nextFloat() * 0.4F);
                  ItemPokeball ball = (ItemPokeball)((ItemStack)((Tuple)this.balls.get(this.ballIndex)).func_76340_b()).func_77973_b();
                  ItemStack[] stacks = new ItemStack[]{new ItemStack(ball.type.getLid()), new ItemStack(PixelmonItemsPokeballs.ironBase), new ItemStack(Blocks.field_150430_aB)};
                  ItemStack[] var13 = stacks;
                  int var35 = stacks.length;

                  for(int var15 = 0; var15 < var35; ++var15) {
                     ItemStack stack = var13[var15];
                     double mY = 0.1 + (RandomHelper.rand.nextDouble() * 0.1 - 0.05);
                     this.particleEngine.addParticle(new GuiParticleEngine.GuiParticle((ResourceLocation)null, (double)((float)this.field_146294_l * 0.201F), (double)this.field_146295_m * 0.58, 0.0, RandomHelper.rand.nextDouble() * 2.0 - 1.0, -2.0, 0.0, 1.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 300, (particle) -> {
                        GlStateManager.func_179094_E();
                        GlStateManager.func_179137_b(particle.x, particle.y, 200.0);
                        GlStateManager.func_179152_a(scale, scale, scale);
                        this.drawItem(stack, 0.0F, 0.0F, 770, 771);
                        GlStateManager.func_179121_F();
                        particle.mY += mY;
                     }));
                  }
               }
            }
         }

         if (this.shakes > 0 || this.catchSuccess) {
            this.drawItem((ItemStack)((Tuple)this.balls.get(this.ballIndex)).func_76340_b(), 0.0F, 0.0F, 770, 771, this.shakes == 0);
         }

         GlStateManager.func_179121_F();
      }

      this.checkMouseWheel(mouseX, mouseY);
      this.particleEngine.draw();
   }

   private void drawDrops(int mouseX, int mouseY) {
      float fs = (float)this.field_146294_l * 0.045F;

      int top;
      int bottom;
      float dy;
      for(top = 0; top < 6; ++top) {
         bottom = this.scrollIndex + top;
         if (bottom >= this.drops.length) {
            break;
         }

         ItemStack stack = this.drops[bottom];
         float h = (float)this.field_146295_m * (0.3F + 0.076F * (float)top);
         this.field_146297_k.field_71446_o.func_110577_a(STAR);
         dy = (float)this.field_146294_l / 960.0F * 2.0F;
         GlStateManager.func_179094_E();
         GlStateManager.func_179137_b((double)((float)this.field_146294_l * 0.55F), (double)h - (double)this.field_146295_m * 0.015, 0.0);
         GlStateManager.func_179152_a(dy, dy, dy);
         this.drawItem(stack, 0.0F, 0.0F, 770, 771);
         GlStateManager.func_179121_F();
         GuiHelper.drawScaledSquashedString(stack.func_82833_r(), (float)this.field_146294_l * 0.595F, h, -1, fs, 100.0);
         GuiHelper.drawScaledString("x " + stack.func_190916_E(), (float)this.field_146294_l * 0.89F, h - (float)this.field_146295_m * 0.005F, -1, fs);
      }

      if (this.drops.length > 6) {
         top = (int)((double)this.field_146295_m * 0.29);
         bottom = (int)((double)this.field_146295_m * 0.735);
         int diff = bottom - top;
         int div = this.drops.length - 5;
         dy = (float)diff / (float)div;
         Gui.func_73734_a((int)((double)this.field_146294_l * 0.972), top, (int)((double)this.field_146294_l * 0.9825), bottom, -298897617);
         int bX1 = (int)((double)this.field_146294_l * 0.972);
         int bY1 = (int)((float)top + dy * (float)this.scrollIndex);
         int bX2 = (int)((double)this.field_146294_l * 0.9825);
         int bY2 = (int)((float)top + dy * (float)(this.scrollIndex + 1));
         boolean mouseOver = mouseX >= bX1 && mouseY >= bY1 && mouseX <= bX2 && mouseY <= bY2;
         if (this.click && mouseOver) {
            this.scrolling = true;
         }

         if (!Mouse.isButtonDown(0)) {
            this.scrolling = false;
         }

         if (this.scrolling) {
            if (mouseY > bY2) {
               ++this.scrollIndex;
               if (this.scrollIndex > this.drops.length - 6) {
                  this.scrollIndex = Math.max(0, this.drops.length - 6);
               }
            } else if (mouseY < bY1) {
               --this.scrollIndex;
               if (this.scrollIndex < 0) {
                  this.scrollIndex = 0;
               }
            }
         }

         Gui.func_73734_a(bX1, bY1, bX2, bY2, !mouseOver && !this.scrolling ? -292318317 : -289686597);
      }

      this.click = false;
   }

   private void drawItem(ItemStack stack, float x, float y, int srcFactor, int destFactor) {
      this.drawItem(stack, x, y, srcFactor, destFactor, false);
   }

   private void drawItem(ItemStack stack, float x, float y, int srcFactor, int destFactor, boolean darken) {
      IBakedModel bakedmodel = this.field_146296_j.func_184393_a(stack, (World)null, (EntityLivingBase)null);
      GlStateManager.func_179094_E();
      this.field_146297_k.field_71446_o.func_110577_a(TextureMap.field_110575_b);
      this.field_146297_k.field_71446_o.func_110581_b(TextureMap.field_110575_b).func_174936_b(false, false);
      GlStateManager.func_179091_B();
      GlStateManager.func_179141_d();
      GlStateManager.func_179132_a(true);
      GlStateManager.func_179126_j();
      GlStateManager.func_179092_a(516, 0.1F);
      GlStateManager.func_179147_l();
      GlStateManager.func_179112_b(srcFactor, destFactor);
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.func_179109_b(x, y, 100.0F + this.field_73735_i);
      GlStateManager.func_179109_b(8.0F, 8.0F, 0.0F);
      GlStateManager.func_179152_a(1.0F, -1.0F, 1.0F);
      GlStateManager.func_179152_a(16.0F, 16.0F, 16.0F);
      if (darken) {
         GlStateManager.func_179145_e();
      } else {
         GlStateManager.func_179140_f();
      }

      bakedmodel = ForgeHooksClient.handleCameraTransforms(bakedmodel, TransformType.GUI, false);
      this.field_146296_j.func_180454_a(stack, bakedmodel);
      GlStateManager.func_179118_c();
      GlStateManager.func_179101_C();
      GlStateManager.func_179140_f();
      GlStateManager.func_179121_F();
      GlStateManager.func_179132_a(false);
      GlStateManager.func_179097_i();
      this.field_146297_k.field_71446_o.func_110577_a(TextureMap.field_110575_b);
      this.field_146297_k.field_71446_o.func_110581_b(TextureMap.field_110575_b).func_174935_a();
   }

   private void checkMouseWheel(int mouseX, int mouseY) {
      int mousewheelDirection = Mouse.getDWheel();
      if ((double)mouseX >= (double)this.field_146294_l * 0.525 && (double)mouseY >= (double)this.field_146295_m * 0.25 && (double)mouseY <= (double)this.field_146295_m * 0.75) {
         if (mousewheelDirection == -120) {
            ++this.scrollIndex;
            if (this.scrollIndex > this.drops.length - 6) {
               this.scrollIndex = Math.max(0, this.drops.length - 6);
            }
         } else if (mousewheelDirection == 120) {
            --this.scrollIndex;
            if (this.scrollIndex < 0) {
               this.scrollIndex = 0;
            }
         }
      }

   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) throws IOException {
      super.func_73864_a(mouseX, mouseY, mouseButton);
      Iterator var4 = this.buttons.iterator();

      while(var4.hasNext()) {
         RaidButton button = (RaidButton)var4.next();
         button.onClick((double)mouseX, (double)mouseY, mouseButton);
      }

      this.click = true;
   }

   public void func_146276_q_() {
      this.func_73733_a(0, 0, this.field_146294_l, this.field_146295_m, -1596523265, -1866285374);
      int bgBar = (int)((double)this.field_146295_m * 0.125);
      GlStateManager.func_179094_E();
      GlStateManager.func_179109_b((float)this.field_146294_l, 0.0F, 0.0F);
      GlStateManager.func_179114_b(90.0F, 0.0F, 0.0F, 1.0F);
      this.raid.getDenEntity(this.field_146297_k.field_71441_e).map((den) -> {
         GuiHelper.drawGradientRect(bgBar, 0, 0.0F, this.field_146295_m - bgBar, this.field_146294_l, den.getColorUIA().getRGB(), den.getColorUIB().getRGB());
         Color c3 = den.getColorUIC();
         GlStateManager.func_179131_c((float)c3.getRed() / 255.0F, (float)c3.getGreen() / 255.0F, (float)c3.getBlue() / 255.0F, 0.4F);
         return true;
      }).orElseGet(() -> {
         GuiHelper.drawGradientRect(bgBar, 0, 0.0F, this.field_146295_m - bgBar, this.field_146294_l, -37796, -5752007);
         GlStateManager.func_179131_c(0.8F, 0.0F, 0.0F, 0.4F);
         return true;
      });
      GlStateManager.func_179121_F();
      this.field_146297_k.field_71446_o.func_110577_a(WATERMARK);
      GlStateManager.func_179147_l();
      GlStateManager.func_179141_d();
      GlStateManager.func_179112_b(770, 771);
      GlStateManager.func_179094_E();
      GlStateManager.func_179114_b(-35.0F, 0.0F, 0.0F, 1.0F);
      GuiHelper.drawImage((double)((float)this.field_146294_l / 2.8F), (double)((float)this.field_146295_m / 1.2F), (double)((float)this.field_146294_l / 3.75F), (double)((float)this.field_146294_l / 3.75F) * 1.65591398, 2.0F);
      GlStateManager.func_179121_F();
      GlStateManager.func_179094_E();
      GuiHelper.drawQuad((double)this.field_146294_l, (double)bgBar * 0.5, 0.0, (double)this.field_146294_l - (double)this.field_146294_l * 0.475, (double)bgBar * 0.5, 0.0, (double)this.field_146294_l - (double)this.field_146294_l * 0.5, (double)bgBar * 1.5, 0.0, (double)this.field_146294_l, (double)bgBar * 1.5, 0.0, Color.BLACK);
      GlStateManager.func_179121_F();
   }

   public void func_73876_c() {
      this.pixelmon.getAnimationVariables().tick();
   }

   public void func_175273_b(Minecraft mc, int nw, int nh) {
      super.func_175273_b(mc, nw, nh);
      this.initButtons();
   }

   public void func_146281_b() {
      super.func_146281_b();
      if (!this.closing) {
         this.field_146297_k.func_152344_a(() -> {
            this.field_146297_k.func_147108_a(this);
         });
      } else if (this.sentToPC) {
         this.field_146297_k.field_71439_g.func_145747_a(new TextComponentTranslation("pixelmon.storage.partyfull", new Object[]{this.pixelmon.func_145748_c_()}));
      }

   }

   public void func_146282_l() throws IOException {
   }

   public boolean func_73868_f() {
      return false;
   }

   public long getMillis() {
      if (this.lastTime < 0L) {
         this.lastTime = System.currentTimeMillis();
         return 0L;
      } else {
         long time = System.currentTimeMillis();
         long diff = time - this.lastTime;
         this.lastTime = time;
         return diff;
      }
   }

   private boolean canUseCurrentBall() {
      if (this.balls.isEmpty()) {
         return false;
      } else {
         ItemPokeball ball = (ItemPokeball)((ItemStack)((Tuple)this.balls.get(this.ballIndex)).func_76340_b()).func_77973_b();
         return this.raid.canUseMaster || ball.type.getBallBonus() < 255.0;
      }
   }
}
