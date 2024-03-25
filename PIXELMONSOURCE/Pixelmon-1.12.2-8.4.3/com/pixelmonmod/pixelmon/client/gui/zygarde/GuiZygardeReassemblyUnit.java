package com.pixelmonmod.pixelmon.client.gui.zygarde;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.api.storage.StoragePosition;
import com.pixelmonmod.pixelmon.blocks.tileEntities.TileEntityZygardeAssembly;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiPokemonUI;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiRoundButton;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.zygarde.ZygardeReassemblyPacket;
import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.PowerConstruct;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.EnumZygarde;
import com.pixelmonmod.pixelmon.items.ItemZygardeCube;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;

public class GuiZygardeReassemblyUnit extends GuiScreen {
   BlockPos pos;
   Mode mode = null;
   StoragePosition position = null;
   boolean requirementsMet = true;
   List buttons = Lists.newArrayList();
   List pokemon = Lists.newArrayList();
   List lines = Lists.newArrayList();
   int buttonOffset = 30;
   private int centerW;
   private int centerH;

   public GuiZygardeReassemblyUnit(int x, int y, int z) {
      this.field_146297_k = Minecraft.func_71410_x();
      this.pos = new BlockPos(x, y, z);
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.centerW = this.field_146294_l / 2;
      this.centerH = this.field_146295_m / 2;
      this.buttons.clear();
      this.pokemon.clear();
      this.lines.clear();
      if (this.mode == null) {
         this.buttonOffset = 30;
         this.lines.add(I18n.func_135052_a("pixelmon.gui.zygarde_reassembly.select", new Object[0]));
         this.buttons.add(new GuiRoundButton(0, 0, I18n.func_135052_a("pixelmon.gui.zygarde_reassembly.assembly", new Object[0]), 150, 20));
         this.buttons.add(new GuiRoundButton(0, 0, I18n.func_135052_a("pixelmon.gui.zygarde_reassembly.with_zygarde", new Object[0]), 150, 20));
         this.buttons.add(new GuiRoundButton(0, 0, I18n.func_135052_a("pixelmon.gui.zygarde_reassembly.separation", new Object[0]), 150, 20));
      } else {
         int i;
         if (this.mode == GuiZygardeReassemblyUnit.Mode.SEPARATION) {
            if (this.position == null) {
               this.lines.add(I18n.func_135052_a("pixelmon.gui.zygarde_reassembly.separation.select", new Object[0]));

               for(i = 0; i < 6; ++i) {
                  this.pokemon.add(new GuiPokemonUI(0, 0));
               }
            } else if (ClientStorageManager.party.get(this.position) != null && ClientStorageManager.party.get(this.position).getSpecies() == EnumSpecies.Zygarde) {
               if (ClientStorageManager.party.countPokemon() == 1) {
                  this.lines.add(I18n.func_135052_a("pixelmon.gui.zygarde_reassembly.last_pokemon", new Object[0]));
                  this.buttons.add(new GuiRoundButton(0, 0, I18n.func_135052_a("gui.button.okay", new Object[0]), 150, 20));
               } else if (ClientStorageManager.party.get(this.position).getAbility() instanceof PowerConstruct) {
                  this.lines.add(I18n.func_135052_a("pixelmon.gui.zygarde_reassembly.separation.cannot", new Object[0]));
                  this.buttons.add(new GuiRoundButton(0, 0, I18n.func_135052_a("gui.button.okay", new Object[0]), 150, 20));
               } else {
                  this.lines.add(I18n.func_135052_a("pixelmon.gui.zygarde_reassembly.separation.confirm", new Object[0]));
                  this.buttons.add(new GuiRoundButton(0, 0, I18n.func_135052_a("gui.button.yes", new Object[0]), 150, 20));
                  this.buttons.add(new GuiRoundButton(0, 0, I18n.func_135052_a("gui.button.cancel", new Object[0]), 150, 20));
               }
            } else {
               this.lines.add(I18n.func_135052_a("pixelmon.gui.zygarde_reassembly.not_zygarde", new Object[0]));
               this.buttons.add(new GuiRoundButton(0, 0, I18n.func_135052_a("gui.button.okay", new Object[0]), 150, 20));
            }
         } else {
            int count;
            if (this.mode == GuiZygardeReassemblyUnit.Mode.ASSEMBLY_WITH_ZYGARDE) {
               if (this.position == null) {
                  this.lines.add(I18n.func_135052_a("pixelmon.gui.zygarde_reassembly.with_zygarde.select", new Object[0]));

                  for(i = 0; i < 6; ++i) {
                     this.pokemon.add(new GuiPokemonUI(0, 0));
                  }
               } else if (ClientStorageManager.party.get(this.position) != null && ClientStorageManager.party.get(this.position).getSpecies() == EnumSpecies.Zygarde) {
                  if (ClientStorageManager.party.get(this.position).getAbility() instanceof PowerConstruct) {
                     this.lines.add(I18n.func_135052_a("pixelmon.gui.zygarde_reassembly.with_zygarde.cannot", new Object[0]));
                     this.buttons.add(new GuiRoundButton(0, 0, I18n.func_135052_a("gui.button.okay", new Object[0]), 150, 20));
                  } else {
                     Pokemon pokemon = ClientStorageManager.party.get(this.position);
                     ItemStack cube = this.getCube();
                     if (cube == null || pokemon == null) {
                        this.field_146297_k.func_147108_a((GuiScreen)null);
                        return;
                     }

                     count = ItemZygardeCube.getCellCount(cube);
                     int cores = ItemZygardeCube.getCoreCount(cube);
                     if (pokemon.getFormEnum() == EnumZygarde.TEN_PERCENT) {
                        if (count >= 90 && cores >= 1) {
                           this.lines.add(I18n.func_135052_a("pixelmon.gui.zygarde_reassembly.with_zygarde.10to100", new Object[0]));
                        } else if (count >= 40) {
                           this.lines.add(I18n.func_135052_a("pixelmon.gui.zygarde_reassembly.with_zygarde.10to50", new Object[0]));
                           this.lines.add("");
                        } else {
                           this.lines.add(I18n.func_135052_a("pixelmon.gui.zygarde_reassembly.missingcells", new Object[0]));
                           this.requirementsMet = false;
                        }
                     } else if (pokemon.getFormEnum() == EnumZygarde.FIFTY_PERCENT) {
                        if (count >= 50 && cores >= 1) {
                           this.lines.add(I18n.func_135052_a("pixelmon.gui.zygarde_reassembly.with_zygarde.50to100", new Object[0]));
                        } else if (count >= 50) {
                           this.lines.add(I18n.func_135052_a("pixelmon.gui.zygarde_reassembly.with_zygarde.missingcores", new Object[0]));
                           this.requirementsMet = false;
                        } else {
                           this.lines.add(I18n.func_135052_a("pixelmon.gui.zygarde_reassembly.missingcells", new Object[0]));
                           this.requirementsMet = false;
                        }
                     }

                     this.buttons.add(new GuiRoundButton(0, 0, I18n.func_135052_a("gui.button.okay", new Object[0]), 150, 20));
                     if (this.requirementsMet) {
                        this.buttons.add(new GuiRoundButton(0, 0, I18n.func_135052_a("gui.button.cancel", new Object[0]), 150, 20));
                     }
                  }
               } else {
                  this.lines.add(I18n.func_135052_a("pixelmon.gui.zygarde_reassembly.not_zygarde", new Object[0]));
                  this.buttons.add(new GuiRoundButton(0, 0, I18n.func_135052_a("gui.button.okay", new Object[0]), 150, 20));
               }
            } else if (this.mode == GuiZygardeReassemblyUnit.Mode.ASSEMBLY) {
               ItemStack cube = this.getCube();
               if (cube != null) {
                  int count = ItemZygardeCube.getCellCount(cube);
                  count = ItemZygardeCube.getCoreCount(cube);
                  if (count >= 100 && count >= 2) {
                     this.lines.add(I18n.func_135052_a("pixelmon.gui.zygarde_reassembly.assembly.100", new Object[0]));
                  } else if (count >= 50 && count >= 1) {
                     this.lines.add(I18n.func_135052_a("pixelmon.gui.zygarde_reassembly.assembly.50", new Object[0]));
                  } else if (count >= 10 && count >= 1) {
                     this.lines.add(I18n.func_135052_a("pixelmon.gui.zygarde_reassembly.assembly.10", new Object[0]));
                  } else if (count >= 10) {
                     this.lines.add(I18n.func_135052_a("pixelmon.gui.zygarde_reassembly.assembly.missingcore", new Object[0]));
                     this.requirementsMet = false;
                  } else {
                     this.lines.add(I18n.func_135052_a("pixelmon.gui.zygarde_reassembly.missingcells", new Object[0]));
                     this.requirementsMet = false;
                  }
               } else {
                  this.field_146297_k.func_147108_a((GuiScreen)null);
               }

               this.buttonOffset = 30;
               this.buttons.add(new GuiRoundButton(0, 0, I18n.func_135052_a("gui.button.okay", new Object[0]), 150, 20));
               if (this.requirementsMet) {
                  this.buttons.add(new GuiRoundButton(0, 0, I18n.func_135052_a("gui.button.cancel", new Object[0]), 150, 20));
               }
            }
         }
      }

   }

   public void func_73863_a(int mouseX, int mouseY, float partialTicks) {
      super.func_73863_a(mouseX, mouseY, partialTicks);
      if (!this.lines.isEmpty()) {
         GuiHelper.drawDialogueBox(this, (String)"", (List)this.lines, 0.0F);
      }

      PlayerPartyStorage party = ClientStorageManager.party;

      int i;
      for(i = 0; i < this.pokemon.size(); ++i) {
         boolean highlight = ((GuiPokemonUI)this.pokemon.get(i)).isMouseOver(i < 3 ? this.centerW - 110 : this.centerW + 5, this.field_146295_m / 4 + 20 + this.buttonOffset * (i % 3 + 1), mouseX, mouseY);
         ((GuiPokemonUI)this.pokemon.get(i)).drawPokemon(party.get(i), i < 3 ? this.centerW - 110 : this.centerW + 5, this.field_146295_m / 4 + 20 + this.buttonOffset * (i % 3 + 1), mouseX, mouseY, 0.0F, highlight);
      }

      for(i = 0; i < this.buttons.size(); ++i) {
         GuiRoundButton button = (GuiRoundButton)this.buttons.get(i);
         button.drawButton(this.centerW - 70, this.field_146295_m / 4 + 20 + this.buttonOffset * (i + 1), mouseX, mouseY, 0.0F);
      }

   }

   protected void func_73864_a(int mouseX, int mouseY, int mouseButton) {
      int i = 0;

      while(true) {
         if (i < this.pokemon.size()) {
            GuiPokemonUI pokemonUI = (GuiPokemonUI)this.pokemon.get(i);
            if (!pokemonUI.isMouseOver(i < 3 ? this.centerW - 110 : this.centerW + 5, this.field_146295_m / 4 + 20 + this.buttonOffset * (i % 3 + 1), mouseX, mouseY) || this.mode != GuiZygardeReassemblyUnit.Mode.SEPARATION && (this.mode != GuiZygardeReassemblyUnit.Mode.ASSEMBLY_WITH_ZYGARDE || this.position != null)) {
               ++i;
               continue;
            }

            if (ClientStorageManager.party.get(new StoragePosition(-1, i)) == null) {
               return;
            }

            this.position = new StoragePosition(-1, i);
            this.func_73866_w_();
            return;
         }

         for(i = 0; i < this.buttons.size(); ++i) {
            GuiRoundButton button = (GuiRoundButton)this.buttons.get(i);
            if (button.isMouseOver(this.centerW - 70, this.field_146295_m / 4 + 20 + this.buttonOffset * (i + 1), mouseX, mouseY)) {
               if (this.mode == null) {
                  if (i == 0) {
                     this.mode = GuiZygardeReassemblyUnit.Mode.ASSEMBLY;
                  } else if (i == 1) {
                     this.mode = GuiZygardeReassemblyUnit.Mode.ASSEMBLY_WITH_ZYGARDE;
                  } else {
                     this.mode = GuiZygardeReassemblyUnit.Mode.SEPARATION;
                  }

                  this.func_73866_w_();
               } else if (this.mode != GuiZygardeReassemblyUnit.Mode.SEPARATION && this.mode != GuiZygardeReassemblyUnit.Mode.ASSEMBLY_WITH_ZYGARDE) {
                  if (this.mode == GuiZygardeReassemblyUnit.Mode.ASSEMBLY) {
                     if (i == 0 && this.requirementsMet) {
                        ItemStack cube = this.getCube();
                        if (cube != null) {
                           Pixelmon.network.sendToServer(new ZygardeReassemblyPacket(this.pos, TileEntityZygardeAssembly.Mode.ASSEMBLING, (StoragePosition)null, this.field_146297_k.field_71439_g.field_71071_by.func_184429_b(cube)));
                        }
                     }

                     this.field_146297_k.func_147108_a((GuiScreen)null);
                  }
               } else if (this.position != null) {
                  Pokemon pokemon = ClientStorageManager.party.get(this.position);
                  if (this.requirementsMet && pokemon != null && ClientStorageManager.party.countPokemon() != 1 && pokemon.getSpecies() == EnumSpecies.Zygarde && !(pokemon.getAbility() instanceof PowerConstruct) && i == 0) {
                     ItemStack cube = this.getCube();
                     if (cube != null) {
                        Pixelmon.network.sendToServer(new ZygardeReassemblyPacket(this.pos, this.mode == GuiZygardeReassemblyUnit.Mode.SEPARATION ? TileEntityZygardeAssembly.Mode.SEPARATING : TileEntityZygardeAssembly.Mode.ASSEMBLING, this.position, this.field_146297_k.field_71439_g.field_71071_by.func_184429_b(cube)));
                     }
                  }

                  this.field_146297_k.func_147108_a((GuiScreen)null);
               }
            }
         }

         return;
      }
   }

   public boolean func_73868_f() {
      return false;
   }

   private ItemStack getCube() {
      InventoryPlayer inv = this.field_146297_k.field_71439_g.field_71071_by;
      Predicate check = (itemStack) -> {
         return itemStack.func_77973_b() == PixelmonItems.zygardeCube;
      };
      Optional opt = inv.field_70462_a.stream().filter(check).findFirst();
      if (!opt.isPresent()) {
         opt = inv.field_184439_c.stream().filter(check).findFirst();
      }

      return (ItemStack)opt.orElse((Object)null);
   }

   public static enum Mode {
      ASSEMBLY,
      ASSEMBLY_WITH_ZYGARDE,
      SEPARATION;
   }
}
