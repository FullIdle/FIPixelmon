package com.pixelmonmod.pixelmon.client.gui.raids;

import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.raids.RaidData;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.entities.EntityDen;
import java.awt.Color;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;

public class PokemonSlot {
   private RaidData raid;
   private Pokemon pokemon;
   private int index;
   private RaidData.RaidPlayer player;
   private NetworkPlayerInfo npi;
   private final Consumer onClick;
   private final double x;
   private final double y;
   private final double z;
   private double w;
   private double h;
   private boolean visible;
   private boolean enabled;

   public PokemonSlot(RaidData raid, double x, double y, double z, RaidData.RaidPlayer player, Consumer onClick) {
      this(raid, x, y, z, onClick);
      this.pokemon = null;
      this.player = player;
      if (this.player != null && this.player.player != null) {
         this.npi = Minecraft.func_71410_x().field_71439_g.field_71174_a.func_175102_a(this.player.player);
      } else {
         this.npi = null;
      }

   }

   public PokemonSlot(RaidData raid, double x, double y, double z, Pokemon pokemon, Consumer onClick) {
      this(raid, x, y, z, onClick);
      this.pokemon = pokemon;
      this.player = null;
      this.npi = null;
   }

   private PokemonSlot(RaidData raid, double x, double y, double z, Consumer onClick) {
      this.index = -1;
      this.visible = true;
      this.enabled = true;
      this.raid = raid;
      this.x = x;
      this.y = y;
      this.z = z;
      this.onClick = onClick;
   }

   public void draw(double mouseX, double mouseY, double width, double height, boolean selected, float partialTicks) {
      this.w = width / 20.0;
      this.h = height / 40.0;
      boolean disabled = this.isInteractable() && (this.pokemon == null || !this.pokemon.canBattle());
      boolean hover = this.isWithin(mouseX, mouseY);
      Color color;
      if (disabled) {
         color = new Color(100, 100, 100);
      } else {
         Optional den = this.raid.getDenEntity(Minecraft.func_71410_x().field_71441_e);
         if (selected && !hover) {
            if (den.isPresent()) {
               color = ((EntityDen)den.get()).getColorUIA();
            } else {
               color = new Color(255, 108, 92);
            }
         } else if (den.isPresent()) {
            color = ((EntityDen)den.get()).getColorUIB();
         } else {
            color = new Color(168, 59, 57);
         }
      }

      GuiHelper.drawEllipse(this.x, this.y, this.z - 1.0, this.w, height / 40.0, 40, disabled ? color : (!hover && !selected ? Color.WHITE : color));
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      if (this.player != null || this.pokemon != null) {
         if (this.player != null) {
            GuiHelper.bindPokemonSprite(this.player.species, this.player.form.getForm(), this.player.gender, "", this.player.shiny, 0, Minecraft.func_71410_x());
         } else {
            GuiHelper.bindPokemonSprite(this.pokemon, Minecraft.func_71410_x());
         }

         if (disabled) {
            GlStateManager.func_179131_c(0.4F, 0.4F, 0.4F, 1.0F);
         }

         GuiHelper.drawImage(this.x + width * 0.005, this.y - height * 0.06, width / 24.0, width / 24.0, (float)this.z);
         if (this.npi != null) {
            GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
            Minecraft.func_71410_x().func_110434_K().func_110577_a(this.npi.func_178837_g());
            GuiHelper.drawImageQuad(this.x + width * 0.04, this.y + height * 0.01, width / 64.0, (float)(width / 64.0), 0.125, 0.125, 0.25, 0.25, (float)this.z);
         }
      }

   }

   public boolean isWithin(double x, double y) {
      return this.enabled && this.pokemon != null && x >= this.x && y >= this.y - this.w + this.h && x <= this.x + this.w && y <= this.y + this.h;
   }

   public void onClick(double mouseX, double mouseY, int button) {
      if (this.isInteractable() && this.isWithin(mouseX, mouseY)) {
         this.onClick.accept(button);
      }

   }

   public boolean isInteractable() {
      return this.enabled && this.visible;
   }

   public PokemonSlot setVisible(boolean visible) {
      this.visible = visible;
      return this;
   }

   public PokemonSlot setEnabled(boolean enabled) {
      this.enabled = enabled;
      return this;
   }

   public PokemonSlot setPokemon(Pokemon pokemon) {
      this.pokemon = pokemon;
      return this;
   }

   public PokemonSlot setIndex(int index) {
      this.index = index;
      return this;
   }

   public PokemonSlot setPlayer(NetworkPlayerInfo npi) {
      this.npi = npi;
      return this;
   }

   public int getIndex() {
      return this.index;
   }

   public boolean isPlayer(UUID uuid) {
      return this.npi != null && this.npi.func_178845_a() != null && this.npi.func_178845_a().getId().equals(uuid);
   }
}
