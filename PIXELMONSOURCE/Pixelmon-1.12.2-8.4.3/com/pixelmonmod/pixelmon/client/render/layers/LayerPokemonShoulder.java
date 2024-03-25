package com.pixelmonmod.pixelmon.client.render.layers;

import com.pixelmonmod.pixelmon.client.models.PixelmonModelRegistry;
import com.pixelmonmod.pixelmon.client.render.RenderPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity2Client;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity3HasStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.enums.forms.IEnumForm;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerEntityOnShoulder;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerPokemonShoulder extends LayerEntityOnShoulder {
   public LayerPokemonShoulder(RenderManager p_i47370_1_) {
      super(p_i47370_1_);
   }

   public void func_177141_a(EntityPlayer player, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
      EnumSpecies species;
      Gender gender;
      IEnumForm form;
      boolean shiny;
      String customTexture;
      if (player.func_192023_dk() != null) {
         if (player.func_192023_dk().func_74779_i("id").endsWith("pixelmon")) {
            if (this.field_192865_a == null || !(this.field_192865_a instanceof RenderPixelmon)) {
               species = EnumSpecies.getFromNameAnyCaseNoTranslate(player.func_192023_dk().func_74779_i("Name"));
               gender = Gender.getGender((short)player.func_192023_dk().func_74771_c("Gender"));
               form = Entity3HasStats.getFormEnum(species, gender.getForm(), player.func_192023_dk().func_74762_e("Variant"));
               shiny = player.func_192023_dk().func_74767_n("Shiny");
               customTexture = player.func_192025_dl().func_74779_i("CustomTexture");
               this.field_192865_a = new RenderPixelmon(this.field_192867_c);
               this.field_192868_d = PixelmonModelRegistry.getModel(species, form);
               this.field_192869_e = Entity2Client.getTextureFor(species, form, gender, customTexture, shiny);
            }
         } else if (this.field_192865_a instanceof RenderPixelmon) {
            this.field_192865_a = null;
            this.field_192868_d = null;
            this.field_192869_e = null;
         }
      }

      if (player.func_192025_dl() != null) {
         if (player.func_192025_dl().func_74779_i("id").endsWith("pixelmon")) {
            if (this.field_192866_b == null || !(this.field_192866_b instanceof RenderPixelmon)) {
               species = EnumSpecies.getFromNameAnyCaseNoTranslate(player.func_192025_dl().func_74779_i("Name"));
               gender = Gender.getGender((short)player.func_192025_dl().func_74771_c("Gender"));
               form = Entity3HasStats.getFormEnum(species, gender.getForm(), player.func_192025_dl().func_74762_e("Variant"));
               shiny = player.func_192025_dl().func_74767_n("Shiny");
               customTexture = player.func_192025_dl().func_74779_i("CustomTexture");
               this.field_192866_b = new RenderPixelmon(this.field_192867_c);
               this.field_192872_h = PixelmonModelRegistry.getModel(species, form);
               this.field_192873_i = Entity2Client.getTextureFor(species, form, gender, customTexture, shiny);
            }
         } else if (this.field_192865_a instanceof RenderPixelmon) {
            this.field_192866_b = null;
            this.field_192872_h = null;
            this.field_192873_i = null;
         }
      }

      super.func_177141_a(player, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, scale);
   }
}
