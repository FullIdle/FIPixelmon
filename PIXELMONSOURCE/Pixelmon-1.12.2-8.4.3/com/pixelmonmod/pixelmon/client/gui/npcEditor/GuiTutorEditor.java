package com.pixelmonmod.pixelmon.client.gui.npcEditor;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.attacks.AttackBase;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.client.gui.GuiResources;
import com.pixelmonmod.pixelmon.client.gui.chooseMoveset.GuiMoveList;
import com.pixelmonmod.pixelmon.client.gui.chooseMoveset.IElementClicked;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiScreenDropDown;
import com.pixelmonmod.pixelmon.client.gui.elements.GuiTabCompleteTextField;
import com.pixelmonmod.pixelmon.client.gui.npc.GuiTutor;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.DeleteNPC;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.StoreTutorData;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTutor;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStatsLearnType;
import com.pixelmonmod.pixelmon.util.ITranslatable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import org.lwjgl.input.Keyboard;

public class GuiTutorEditor extends GuiScreenDropDown implements IElementClicked {
   public static EnumSet learnTypes = EnumSet.noneOf(BaseStatsLearnType.class);
   private final Map itemMap = new HashMap();
   private final Map idMap = new HashMap();
   private final Map modAndIdMap = new HashMap();
   NPCTutor tutor;
   List cost;
   int listTop;
   int listLeft;
   int listHeight;
   int listWidth;
   GuiMoveList guiMoveList;
   int selectedMove = -1;
   GuiTextField newMove;
   GuiTextField newItem;
   GuiTextField newItemAmount;
   GuiTextField newItemDamage;
   GuiButton okayButton;
   GuiButton addMove;
   GuiButton deleteMove;
   GuiButton addItem;
   GuiButton[] deleteCost = new GuiButton[4];
   GuiButton[] learnTypeButtons;
   GuiButton deleteTutor;
   private TextureEditorNPC textureEditor;

   public GuiTutorEditor(int tutorID) {
      this.learnTypeButtons = new GuiButton[BaseStatsLearnType.GEN8_DEFAULT.length];
      Keyboard.enableRepeatEvents(true);
      Optional entityNPCOptional = EntityNPC.locateNPCClient(Minecraft.func_71410_x().field_71441_e, tutorID, NPCTutor.class);
      if (!entityNPCOptional.isPresent()) {
         GuiHelper.closeScreen();
      } else {
         this.tutor = (NPCTutor)entityNPCOptional.get();
         this.listHeight = 150;
         this.listWidth = 90;
         ForgeRegistries.ITEMS.getValuesCollection().stream().map(ItemStack::new).forEach((itemStack) -> {
            Item item = itemStack.func_77973_b();
            this.itemMap.put(itemStack.func_82833_r(), item);
            ResourceLocation location = itemStack.func_77973_b().getRegistryName();
            this.idMap.put(location.func_110623_a().toLowerCase(), item);
            this.modAndIdMap.put((location.func_110624_b() + ":" + location.func_110623_a()).toLowerCase(), item);
         });
      }
   }

   public void func_73866_w_() {
      super.func_73866_w_();
      this.listTop = this.field_146295_m / 2 - 62;
      this.listLeft = this.field_146294_l / 2 - 68;
      this.guiMoveList = new GuiMoveList(this, GuiTutor.moveList, this.listWidth, this.listHeight, this.listTop, this.listLeft, this.field_146297_k);
      this.field_146292_n.add(new GuiButton(1, this.field_146294_l / 2 + 155, this.field_146295_m / 2 + 90, 50, 20, I18n.func_135052_a("gui.pokemoneditor.save", new Object[0])));
      this.okayButton = new GuiButton(10, this.field_146294_l / 2 + 20, this.field_146295_m / 2 + 90, 50, 20, I18n.func_135052_a("gui.guiItemDrops.ok", new Object[0]));
      this.newMove = (new GuiTabCompleteTextField(1, this.field_146297_k.field_71466_p, this.field_146294_l / 2 + 54, this.field_146295_m / 2 - 100, 90, 16)).setCompletions((Collection)AttackBase.ATTACKS.stream().map(ITranslatable::getLocalizedName).collect(Collectors.toSet()));
      this.newItem = (new GuiTabCompleteTextField(2, this.field_146297_k.field_71466_p, this.field_146294_l / 2 + 80, this.field_146295_m / 2 + 30, 70, 16)).setCompletions(this.itemMap.keySet());
      this.newItemAmount = new GuiTextField(3, this.field_146297_k.field_71466_p, this.field_146294_l / 2 + 80, this.field_146295_m / 2 + 50, 70, 16);
      this.newItemDamage = new GuiTextField(4, this.field_146297_k.field_71466_p, this.field_146294_l / 2 + 80, this.field_146295_m / 2 + 70, 70, 16);
      this.addMove = new GuiButton(2, this.field_146294_l / 2 + 69, this.field_146295_m / 2 - 80, 60, 20, I18n.func_135052_a("gui.tutor.addmove", new Object[0]));
      this.deleteMove = new GuiButton(3, this.field_146294_l / 2 + 64, this.field_146295_m / 2 - 45, 70, 20, I18n.func_135052_a("gui.tutor.deletemove", new Object[0]));
      this.addItem = new GuiButton(4, this.field_146294_l / 2 + 80, this.field_146295_m / 2 + 90, 60, 20, I18n.func_135052_a("gui.tutor.addcost", new Object[0]));
      int i;
      if (this.selectedMove > -1) {
         this.field_146292_n.add(this.addItem);
         this.field_146292_n.add(this.deleteMove);
         this.field_146292_n.add(this.okayButton);
      } else {
         i = 0;
         BaseStatsLearnType[] var2 = BaseStatsLearnType.GEN8_DEFAULT;
         int var3 = var2.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            BaseStatsLearnType type = var2[var4];
            this.field_146292_n.add(this.learnTypeButtons[i] = new GuiButton(11 + i, this.field_146294_l / 2 + 150, this.field_146295_m / 2 - 34 + 16 * i, 20, 16, learnTypes.contains(type) ? "O" : "X"));
            ++i;
         }
      }

      for(i = 0; i < this.deleteCost.length; ++i) {
         this.deleteCost[i] = new GuiButton(5 + i, this.field_146294_l / 2 + 80 + 20 * i, this.field_146295_m / 2 + 8, 20, 20, "X");
      }

      this.deleteTutor = new GuiButton(9, this.field_146294_l / 2 - 193, this.field_146295_m / 2 - 100, 80, 20, I18n.func_135052_a("gui.tutor.deletetutor", new Object[0]));
      this.textureEditor = new TextureEditorNPC(this, this.tutor, this.field_146294_l / 2 - 190, this.field_146295_m / 2 + 55, 100, -28);
      this.field_146292_n.add(this.addMove);
   }

   protected void drawBackgroundUnderMenus(float mFloat, int mouseX, int mouseY) {
      this.func_146276_q_();
      this.field_146297_k.field_71446_o.func_110577_a(GuiResources.cwPanel);
      GlStateManager.func_179124_c(0.9F, 0.9F, 0.9F);
      float sixteenthWidth = 40.0F;
      float sixteenthHeight = 20.0F;
      GuiHelper.drawImageQuad((double)(0.0F + sixteenthWidth), (double)(0.0F + sixteenthHeight), (double)((float)this.field_146294_l - sixteenthWidth * 2.0F), (float)this.field_146295_m - sixteenthHeight * 2.0F, 0.0, 0.0, 1.0, 1.0, this.field_73735_i);
      RenderHelper.func_74518_a();
      String text = I18n.func_135052_a("pixelmon.npc.tutorname", new Object[0]);
      this.field_146297_k.field_71466_p.func_78276_b(text, this.field_146294_l / 2 - this.field_146289_q.func_78256_a(text) / 2, this.field_146295_m / 2 - 100, 0);
      text = I18n.func_135052_a("gui.choosemoveset.choosemove", new Object[0]);
      this.field_146297_k.field_71466_p.func_78276_b(text, this.field_146294_l / 2 - 33 - this.field_146289_q.func_78256_a(text) / 2, this.field_146295_m / 2 - 76, 0);
      if (!GuiTutor.moveList.isEmpty()) {
         this.guiMoveList.drawScreen(mouseX, mouseY, mFloat);
      }

      GuiHelper.drawEntity(this.tutor, this.field_146294_l / 2 - 133, this.field_146295_m / 2 + 50, 50.0F, 0.0F, 0.0F);
      this.newMove.func_146194_f();
      int i;
      if (this.selectedMove >= 0) {
         text = ((NPCTutor.LearnableMove)GuiTutor.moveList.get(this.selectedMove)).attack().getLocalizedName();
         this.field_146297_k.field_71466_p.func_78276_b(text, this.field_146294_l / 2 + 99 - this.field_146289_q.func_78256_a(text) / 2, this.field_146295_m / 2 - 20, 0);
         text = I18n.func_135052_a("gui.choosemoveset.cost", new Object[0]);
         this.field_146297_k.field_71466_p.func_78276_b(text, this.field_146294_l / 2 + 74 - this.field_146289_q.func_78256_a(text), this.field_146295_m / 2 - 5, 0);
         if (this.cost.size() < 4) {
            this.newItem.func_146194_f();
            this.newItemAmount.func_146194_f();
            this.newItemDamage.func_146194_f();
            text = I18n.func_135052_a("gui.tutor.itemname", new Object[0]);
            this.field_146297_k.field_71466_p.func_78276_b(text, this.field_146294_l / 2 + 75 - this.field_146289_q.func_78256_a(text), this.field_146295_m / 2 + 35, 0);
            text = I18n.func_135052_a("gui.tutor.itemamount", new Object[0]);
            this.field_146297_k.field_71466_p.func_78276_b(text, this.field_146294_l / 2 + 75 - this.field_146289_q.func_78256_a(text), this.field_146295_m / 2 + 55, 0);
            text = I18n.func_135052_a("gui.tutor.itemdamage", new Object[0]);
            this.field_146297_k.field_71466_p.func_78276_b(text, this.field_146294_l / 2 + 75 - this.field_146289_q.func_78256_a(text), this.field_146295_m / 2 + 75, 0);
         }
      } else {
         text = I18n.func_135052_a("gui.tutor.learntypes", new Object[0]);
         this.field_146297_k.field_71466_p.func_78276_b(text, this.field_146294_l / 2 + 160 - this.field_146289_q.func_78256_a(text), this.field_146295_m / 2 - 45, 0);
         i = 0;
         BaseStatsLearnType[] var8 = BaseStatsLearnType.GEN8_DEFAULT;
         int var9 = var8.length;

         for(int var10 = 0; var10 < var9; ++var10) {
            BaseStatsLearnType type = var8[var10];
            text = I18n.func_135052_a("gui.tutor.learntype." + type.name().toLowerCase(), new Object[0]);
            this.field_146297_k.field_71466_p.func_78276_b(text, this.field_146294_l / 2 + 140 - this.field_146289_q.func_78256_a(text), this.field_146295_m / 2 - 30 + 16 * i, 0);
            ++i;
         }
      }

      this.textureEditor.drawCustomTextBox();
      if (this.cost != null && !this.cost.isEmpty()) {
         i = 0;

         for(Iterator var12 = this.cost.iterator(); var12.hasNext(); ++i) {
            ItemStack item = (ItemStack)var12.next();
            this.field_146296_j.func_180450_b(item, this.field_146294_l / 2 + 80 + i * 21, this.field_146295_m / 2 - 10);
            this.field_146296_j.func_180453_a(this.field_146297_k.field_71466_p, item, this.field_146294_l / 2 + 80 + i * 21, this.field_146295_m / 2 - 10, (String)null);
            if (i < this.deleteCost.length && !this.field_146292_n.contains(this.deleteCost[i])) {
               this.field_146292_n.add(this.deleteCost[i]);
            }
         }
      }

   }

   protected void mouseClickedUnderMenus(int x, int y, int mouseButton) throws IOException {
      this.newMove.func_146192_a(x, y, mouseButton);
      this.newItem.func_146192_a(x, y, mouseButton);
      this.newItemAmount.func_146192_a(x, y, mouseButton);
      this.newItemDamage.func_146192_a(x, y, mouseButton);
      this.textureEditor.mouseClicked(x, y, mouseButton);
   }

   public void elementClicked(List list, int index) {
      if (!GuiTutor.moveList.isEmpty() && index < GuiTutor.moveList.size()) {
         this.selectedMove = index;
         this.cost = ((NPCTutor.LearnableMove)GuiTutor.moveList.get(index)).costs();
         if (!this.field_146292_n.contains(this.deleteMove)) {
            this.field_146292_n.add(this.deleteMove);
         }

         if (!this.field_146292_n.contains(this.addItem) && this.cost.size() < 4) {
            this.field_146292_n.add(this.addItem);
            this.field_146292_n.add(this.okayButton);
         }

         GuiButton[] var3 = this.deleteCost;
         int var4 = var3.length;

         for(int var5 = 0; var5 < var4; ++var5) {
            GuiButton deleteCostButton = var3[var5];
            this.field_146292_n.remove(deleteCostButton);
         }

         this.field_146292_n.removeAll(Arrays.asList(this.learnTypeButtons));
      }

   }

   protected void func_73869_a(char key, int keyCode) {
      this.newMove.func_146201_a(key, keyCode);
      List textFields = new ArrayList();
      textFields.add(this.newMove);
      if (this.selectedMove >= 0 && this.cost.size() < 4) {
         this.newItem.func_146201_a(key, keyCode);
         this.newItemAmount.func_146201_a(key, keyCode);
         this.newItemDamage.func_146201_a(key, keyCode);
         textFields.addAll(Arrays.asList(this.newItem, this.newItemAmount, this.newItemDamage));
      }

      if (keyCode != 15) {
         this.textureEditor.keyTyped(key, keyCode, (GuiTextField[])textFields.toArray(new GuiTextField[0]));
      }

      if (keyCode == 1 || keyCode == 28) {
         this.saveFields();
      }

   }

   protected void func_146284_a(GuiButton button) {
      if (button.field_146127_k == 1) {
         this.saveFields();
      } else if (button.field_146127_k == 2) {
         Optional optional = AttackBase.getAttackBase(this.newMove.func_146179_b());
         if (optional.isPresent()) {
            AttackBase move = (AttackBase)optional.get();
            if (GuiTutor.moveList.stream().noneMatch((it) -> {
               return it.attack() == move;
            })) {
               GuiTutor.moveList.add(new NPCTutor.LearnableMove(move, Lists.newArrayList(), true));
            }
         }
      } else {
         int amount;
         GuiButton deleteCostButton;
         GuiButton[] var10;
         int var12;
         if (button.field_146127_k == 3) {
            if (this.selectedMove >= 0 && this.selectedMove < GuiTutor.moveList.size()) {
               GuiTutor.moveList.remove(this.selectedMove);
               this.cost = null;
               this.selectedMove = -1;
               this.field_146292_n.remove(this.addItem);
               this.field_146292_n.remove(this.okayButton);
               this.field_146292_n.remove(this.deleteMove);
               var10 = this.deleteCost;
               var12 = var10.length;

               for(amount = 0; amount < var12; ++amount) {
                  deleteCostButton = var10[amount];
                  this.field_146292_n.remove(deleteCostButton);
               }

               this.field_146292_n.addAll(Arrays.asList(this.learnTypeButtons));
            }
         } else {
            int damage;
            if (button.field_146127_k == 4) {
               if (this.cost.size() < 4) {
                  String itemString = this.newItem.func_146179_b();
                  Item item = null;
                  if (this.itemMap.containsKey(itemString)) {
                     item = (Item)this.itemMap.get(itemString);
                  } else if (this.idMap.containsKey(itemString.toLowerCase())) {
                     item = (Item)this.idMap.get(itemString.toLowerCase());
                  } else if (this.modAndIdMap.containsKey(itemString.toLowerCase())) {
                     item = (Item)this.modAndIdMap.get(itemString.toLowerCase());
                  } else {
                     try {
                        amount = Integer.parseInt(itemString);
                        if (Item.func_150899_d(amount) != null) {
                           item = Item.func_150899_d(amount);
                        }
                     } catch (Exception var9) {
                     }
                  }

                  if (item != null) {
                     amount = 1;
                     damage = 0;

                     try {
                        amount = Math.max(amount, Integer.parseInt(this.newItemAmount.func_146179_b()));
                     } catch (NumberFormatException var8) {
                     }

                     try {
                        damage = Math.max(damage, Integer.parseInt(this.newItemDamage.func_146179_b()));
                     } catch (NumberFormatException var7) {
                     }

                     ItemStack newItem = new ItemStack(item, amount);
                     if (amount > newItem.func_77976_d()) {
                        newItem.func_190920_e(newItem.func_77976_d());
                     }

                     newItem.func_77964_b(damage);
                     this.cost.add(newItem);
                     if (this.cost.size() >= 4) {
                        this.field_146292_n.remove(this.addItem);
                     }
                  }
               }
            } else {
               int index;
               if (button.field_146127_k >= 5 && button.field_146127_k <= 8) {
                  index = button.field_146127_k - 5;
                  if (this.cost.size() > index) {
                     this.cost.remove(index);
                     GuiButton[] var16 = this.deleteCost;
                     amount = var16.length;

                     for(damage = 0; damage < amount; ++damage) {
                        GuiButton deleteCostButton = var16[damage];
                        this.field_146292_n.remove(deleteCostButton);
                     }

                     if (!this.field_146292_n.contains(this.addItem)) {
                        this.field_146292_n.add(this.addItem);
                     }
                  }
               } else if (button.field_146127_k == 9) {
                  Pixelmon.network.sendToServer(new DeleteNPC(this.tutor.getId()));
                  GuiHelper.closeScreen();
               } else if (button.field_146127_k == 10) {
                  this.cost = null;
                  this.selectedMove = -1;
                  this.field_146292_n.remove(this.addItem);
                  this.field_146292_n.remove(this.okayButton);
                  this.field_146292_n.remove(this.deleteMove);
                  var10 = this.deleteCost;
                  var12 = var10.length;

                  for(amount = 0; amount < var12; ++amount) {
                     deleteCostButton = var10[amount];
                     this.field_146292_n.remove(deleteCostButton);
                  }

                  this.field_146292_n.addAll(Arrays.asList(this.learnTypeButtons));
               } else if (button.field_146127_k >= 11 && button.field_146127_k <= 11 + this.learnTypeButtons.length) {
                  index = button.field_146127_k - 11;
                  if (this.learnTypeButtons[index].field_146126_j.equals("X")) {
                     this.learnTypeButtons[index].field_146126_j = "O";
                     learnTypes.add(BaseStatsLearnType.GEN8_DEFAULT[index]);
                  } else {
                     this.learnTypeButtons[index].field_146126_j = "X";
                     learnTypes.remove(BaseStatsLearnType.GEN8_DEFAULT[index]);
                  }
               }
            }
         }
      }

   }

   public boolean func_73868_f() {
      return false;
   }

   private void saveFields() {
      if (!GuiTutor.moveList.isEmpty()) {
         if (this.tutor != null) {
            Pixelmon.network.sendToServer(new StoreTutorData(this.tutor.getId(), GuiTutor.moveList, learnTypes));
            this.textureEditor.saveCustomTexture();
         }

         GuiHelper.closeScreen();
      }

   }
}
