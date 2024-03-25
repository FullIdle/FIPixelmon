package com.pixelmonmod.pixelmon.client.gui.spawner;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.blocks.enums.EnumSpawnerAggression;
import com.pixelmonmod.pixelmon.blocks.machines.PokemonRarity;
import com.pixelmonmod.pixelmon.blocks.spawning.TileEntityPixelmonSpawner;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiTabCompleteTextField;
import com.pixelmonmod.pixelmon.comm.PixelmonSpawnerData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.UpdateSpawner;
import com.pixelmonmod.pixelmon.config.PixelmonServerConfig;
import com.pixelmonmod.pixelmon.entities.SpawnLocationType;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import java.io.IOException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;

public class GuiPixelmonSpawner extends GuiScreen {
   TileEntityPixelmonSpawner ps;
   int listTop;
   int listLeft;
   int listHeight;
   int listWidth;
   GuiTextField pokemonNameBox;
   GuiTextField rarityBox;
   GuiTextField spawnTickBox;
   GuiTextField spawnRadiusBox;
   GuiTextField maxSpawnsBox;
   GuiTextField levelMinBox;
   GuiTextField levelMaxBox;
   GuiTextField bossRatioBox;
   GuiPokemonList list;

   public GuiPixelmonSpawner(int x, int y, int z) {
      Keyboard.enableRepeatEvents(true);
      this.ps = (TileEntityPixelmonSpawner)Minecraft.func_71410_x().field_71441_e.func_175625_s(new BlockPos(x, y, z));
      this.listTop = 58;
      this.listLeft = 30;
      this.listHeight = 100;
      this.listWidth = 100;
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.list = new GuiPokemonList(this, 200, this.listHeight, this.listTop - 40, this.field_146297_k);
      this.field_146292_n.add(new GuiButton(0, this.listLeft + this.listWidth + 30, this.listTop + this.listHeight + 20, 30, 20, I18n.func_135052_a("gui.trainereditor.add", new Object[0])));
      this.field_146292_n.add(new GuiButton(1, 370, 190, 30, 20, I18n.func_135052_a("gui.guiItemDrops.ok", new Object[0])));
      this.field_146292_n.add(new GuiButton(2, 260, 30, 120, 20, this.ps.fireOnTick ? I18n.func_135052_a("gui.pixelmonspawner.fireontick", new Object[0]) : I18n.func_135052_a("gui.pixelmonspawner.fireonredstone", new Object[0])));
      this.field_146292_n.add(new GuiButton(3, 270, 180, 80, 20, this.ps.aggression.getLocalizedName()));
      this.field_146292_n.add(new GuiButton(4, 270, 200, 80, 20, this.ps.spawnLocation.getLocalizedName()));
      this.pokemonNameBox = (new GuiTabCompleteTextField(5, this.field_146297_k.field_71466_p, this.listLeft, this.listTop + this.listHeight + 20, 80, 20)).setCompletions(EnumSpecies.getNameList());
      this.pokemonNameBox.func_146180_a("");
      this.pokemonNameBox.func_146203_f(250);
      this.rarityBox = new GuiTextField(6, this.field_146297_k.field_71466_p, this.listLeft + 85, this.listTop + this.listHeight + 20, 40, 20);
      this.rarityBox.func_146180_a("");
      this.spawnTickBox = new GuiTextField(7, this.field_146297_k.field_71466_p, 350, this.listTop, 40, 20);
      this.spawnTickBox.func_146180_a("" + this.ps.spawnTick);
      this.spawnRadiusBox = new GuiTextField(8, this.field_146297_k.field_71466_p, 350, this.listTop + 20, 40, 20);
      this.spawnRadiusBox.func_146180_a("" + this.ps.spawnRadius);
      this.maxSpawnsBox = new GuiTextField(9, this.field_146297_k.field_71466_p, 350, this.listTop + 40, 40, 20);
      this.maxSpawnsBox.func_146180_a("" + this.ps.maxSpawns);
      this.levelMinBox = new GuiTextField(10, this.field_146297_k.field_71466_p, 350, this.listTop + 60, 40, 20);
      this.levelMinBox.func_146180_a("" + this.ps.levelMin);
      this.levelMaxBox = new GuiTextField(11, this.field_146297_k.field_71466_p, 350, this.listTop + 80, 40, 20);
      this.levelMaxBox.func_146180_a("" + this.ps.levelMax);
      this.bossRatioBox = new GuiTextField(12, this.field_146297_k.field_71466_p, 350, this.listTop + 100, 40, 20);
      this.bossRatioBox.func_146180_a("" + this.ps.bossRatio);
   }

   protected void func_73869_a(char par1, int par2) throws IOException {
      this.pokemonNameBox.func_146201_a(par1, par2);
      this.rarityBox.func_146201_a(par1, par2);
      this.spawnTickBox.func_146201_a(par1, par2);
      this.spawnRadiusBox.func_146201_a(par1, par2);
      this.maxSpawnsBox.func_146201_a(par1, par2);
      this.levelMinBox.func_146201_a(par1, par2);
      this.levelMaxBox.func_146201_a(par1, par2);
      this.bossRatioBox.func_146201_a(par1, par2);
      if (par2 == 1 || par2 == 28) {
         this.saveFields();
      }

   }

   protected void func_73864_a(int par1, int par2, int par3) throws IOException {
      super.func_73864_a(par1, par2, par3);
      this.pokemonNameBox.func_146192_a(par1, par2, par3);
      this.rarityBox.func_146192_a(par1, par2, par3);
      this.spawnTickBox.func_146192_a(par1, par2, par3);
      this.spawnRadiusBox.func_146192_a(par1, par2, par3);
      this.maxSpawnsBox.func_146192_a(par1, par2, par3);
      this.levelMinBox.func_146192_a(par1, par2, par3);
      this.levelMaxBox.func_146192_a(par1, par2, par3);
      this.bossRatioBox.func_146192_a(par1, par2, par3);
   }

   public void func_73863_a(int mouseX, int mouseY, float mfloat) {
      GlStateManager.func_179131_c(1.0F, 1.0F, 1.0F, 1.0F);
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.cwPanel);
      GuiHelper.drawImageQuad(0.0, 0.0, (double)this.field_146294_l, (float)this.field_146295_m, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      RenderHelper.func_74518_a();
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.pixelmonspawner.pcms", new Object[0]), 250, 10, 0);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.pixelmonspawner.pokemonlist", new Object[0]), 50, 3, 0);
      this.list.drawScreen(mouseX, mouseY, mfloat);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.pixelmonspawner.name", new Object[0]), this.listLeft + 2, this.listTop + this.listHeight + 10, 0);
      this.pokemonNameBox.func_146194_f();
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.pixelmonspawner.rarity", new Object[0]), this.listLeft + 87, this.listTop + this.listHeight + 10, 0);
      this.rarityBox.func_146194_f();
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.pixelmonspawner.spawntick", new Object[0]), 275, this.listTop + 6, 0);
      this.spawnTickBox.func_146194_f();
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.pixelmonspawner.radius", new Object[0]), 275, this.listTop + 26, 0);
      this.spawnRadiusBox.func_146194_f();
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.pixelmonspawner.maxspawns", new Object[0]), 275, this.listTop + 46, 0);
      this.maxSpawnsBox.func_146194_f();
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.pixelmonspawner.minlevel", new Object[0]), 275, this.listTop + 66, 0);
      this.levelMinBox.func_146194_f();
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.pixelmonspawner.maxlevel", new Object[0]), 275, this.listTop + 86, 0);
      this.levelMaxBox.func_146194_f();
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.pixelmonspawner.bossratio", new Object[0]), 275, this.listTop + 106, 0);
      this.bossRatioBox.func_146194_f();
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.pixelmonspawner.aggression", new Object[0]), 205, this.listTop + 128, 0);
      this.field_146297_k.field_71466_p.func_78276_b(I18n.func_135052_a("gui.pixelmonspawner.spawnlocation", new Object[0]), 185, this.listTop + 148, 0);
      super.func_73863_a(mouseX, mouseY, mfloat);
   }

   protected void func_146284_a(GuiButton button) throws IOException {
      super.func_146284_a(button);
      if (button.field_146124_l) {
         if (button.field_146127_k == 0) {
            PokemonSpec pokemon = new PokemonSpec(this.pokemonNameBox.func_146179_b().split(" "));

            int rarityString;
            try {
               rarityString = Integer.parseInt(this.rarityBox.func_146179_b());
            } catch (NumberFormatException var6) {
               return;
            }

            try {
               if (pokemon.name != null && rarityString > 0) {
                  this.ps.pokemonList.add(new PokemonRarity(pokemon, rarityString));
               }
            } catch (Exception var5) {
            }
         } else if (button.field_146127_k == 1) {
            this.saveFields();
         } else if (button.field_146127_k == 2) {
            this.ps.fireOnTick = !this.ps.fireOnTick;
            if (this.ps.fireOnTick) {
               button.field_146126_j = I18n.func_135052_a("gui.pixelmonspawner.fireontick", new Object[0]);
            } else {
               button.field_146126_j = I18n.func_135052_a("gui.pixelmonspawner.fireonredstone", new Object[0]);
            }
         } else if (button.field_146127_k == 3) {
            this.ps.aggression = EnumSpawnerAggression.nextAggression(this.ps.aggression);
            button.field_146126_j = this.ps.aggression.getLocalizedName();
         } else if (button.field_146127_k == 4) {
            this.ps.spawnLocation = SpawnLocationType.nextLocation(this.ps.spawnLocation);
            button.field_146126_j = this.ps.spawnLocation.getLocalizedName();
         }
      }

   }

   private void saveFields() {
      if (this.checkFields()) {
         GuiHelper.closeScreen();
      }

   }

   private boolean checkFields() {
      try {
         int spawnTicks = Integer.parseInt(this.spawnTickBox.func_146179_b());
         int spawnRadius = Integer.parseInt(this.spawnRadiusBox.func_146179_b());
         int maxSpawns = Integer.parseInt(this.maxSpawnsBox.func_146179_b());
         int levelMin = Integer.parseInt(this.levelMinBox.func_146179_b());
         int levelMax = Integer.parseInt(this.levelMaxBox.func_146179_b());
         int bossRatio = Integer.parseInt(this.bossRatioBox.func_146179_b());
         if (spawnTicks > 0 && spawnTicks < 1000 && spawnRadius > 0 && spawnRadius < 50 && maxSpawns > 0 && maxSpawns < 50 && levelMin > 0 && levelMin <= PixelmonServerConfig.maxLevel && levelMax >= levelMin && levelMax <= PixelmonServerConfig.maxLevel) {
            this.ps.spawnTick = spawnTicks;
            this.ps.spawnRadius = spawnRadius;
            this.ps.maxSpawns = maxSpawns;
            this.ps.levelMin = levelMin;
            this.ps.levelMax = levelMax;
            this.ps.bossRatio = bossRatio;
            NBTTagCompound nbt = new NBTTagCompound();
            this.ps.func_189515_b(nbt);
            PixelmonSpawnerData p = new PixelmonSpawnerData(this.ps.func_174877_v(), nbt);
            Pixelmon.network.sendToServer(new UpdateSpawner(p));
            return true;
         }
      } catch (Exception var9) {
      }

      return false;
   }

   public PokemonRarity getPokemonListEntry(int ind) {
      return ind < this.ps.pokemonList.size() && ind >= 0 ? (PokemonRarity)this.ps.pokemonList.get(ind) : null;
   }

   public int getPokemonListCount() {
      return this.ps.pokemonList.size();
   }

   public void removeFromList(int var1) {
      this.ps.pokemonList.remove(var1);
   }
}
