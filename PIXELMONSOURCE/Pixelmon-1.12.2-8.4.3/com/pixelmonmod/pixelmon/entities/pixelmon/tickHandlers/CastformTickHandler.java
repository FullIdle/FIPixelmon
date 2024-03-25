package com.pixelmonmod.pixelmon.entities.pixelmon.tickHandlers;

import com.pixelmonmod.pixelmon.api.world.WorldTime;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity1Base;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.forms.EnumCastform;
import net.minecraft.world.World;

public class CastformTickHandler extends TickHandlerBase {
   int formIndex = -1;

   public CastformTickHandler(Entity1Base pixelmon) {
      super(pixelmon, 100);
   }

   public void onTick(World world) {
      if (!world.field_72995_K) {
         if (!(this.pixelmon instanceof EntityPixelmon) || ((EntityPixelmon)this.pixelmon).battleController == null) {
            if (world.func_72896_J() && world.func_180494_b(this.pixelmon.func_180425_c()).func_76738_d()) {
               int j2 = world.func_175725_q(this.pixelmon.func_180425_c()).func_177956_o();
               float f2 = world.func_180494_b(this.pixelmon.func_180425_c()).func_180626_a(this.pixelmon.func_180425_c());
               if (world.func_72959_q().func_76939_a(f2, j2) >= 0.15F) {
                  this.formIndex = EnumCastform.Rain.ordinal();
                  if (this.pixelmon.getPokemonData().getForm() != this.formIndex) {
                     this.pixelmon.getPokemonData().setForm(EnumCastform.Rain);
                  }
               } else {
                  this.formIndex = EnumCastform.Ice.ordinal();
                  if (this.pixelmon.getPokemonData().getForm() != this.formIndex) {
                     this.pixelmon.getPokemonData().setForm(EnumCastform.Ice);
                  }
               }
            } else if (WorldTime.getCurrent(world).contains(WorldTime.DAY) && !world.func_72896_J()) {
               this.formIndex = EnumCastform.Sun.ordinal();
               if (this.pixelmon.getPokemonData().getForm() != this.formIndex) {
                  this.pixelmon.getPokemonData().setForm(EnumCastform.Sun);
               }
            } else {
               this.formIndex = EnumCastform.Normal.ordinal();
               if (this.pixelmon.getPokemonData().getForm() != this.formIndex) {
                  this.pixelmon.getPokemonData().setForm(EnumCastform.Normal);
               }
            }

         }
      }
   }
}
