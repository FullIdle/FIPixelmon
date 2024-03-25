package com.pixelmonmod.pixelmon.client.models.blocks;

import com.pixelmonmod.pixelmon.entities.EntityPokestop;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPokestop extends ModelBase {
   private final ModelRenderer bottom_parts;
   private final ModelRenderer octagon_r1;
   private final ModelRenderer upper_parts;
   private final ModelRenderer inner_loose_parts2;
   private final ModelRenderer hexadecagon_r1;
   private final ModelRenderer inner_loose_parts;
   private final ModelRenderer hexadecagon_r2;
   private final ModelRenderer hexadecagon_r3;
   private final ModelRenderer hexadecagon_r4;
   private final ModelRenderer outer_loose_parts;
   private final ModelRenderer hexadecagon_r5;
   private final ModelRenderer hexadecagon_r6;
   private final ModelRenderer inner_circle;
   private final ModelRenderer hexadecagon_r7;
   private final ModelRenderer hexadecagon_r8;
   private final ModelRenderer hexadecagon_r9;
   private final ModelRenderer hexadecagon_r10;
   private final ModelRenderer hexadecagon_r11;
   private final ModelRenderer hexadecagon_r12;
   private final ModelRenderer hexadecagon_r13;
   private final ModelRenderer hexadecagon_r14;
   private final ModelRenderer hexadecagon_r15;
   private final ModelRenderer hexadecagon_r16;
   private final ModelRenderer lower_inner_ring;
   private final ModelRenderer hexadecagon_r17;
   private final ModelRenderer hexadecagon_r18;
   private final ModelRenderer hexadecagon_r19;
   private final ModelRenderer hexadecagon_r20;
   private final ModelRenderer upper_inner_ring;
   private final ModelRenderer hexadecagon_r21;
   private final ModelRenderer hexadecagon_r22;
   private final ModelRenderer hexadecagon_r23;
   private final ModelRenderer hexadecagon_r24;
   private final ModelRenderer outer_ring;
   private final ModelRenderer hexadecagon_r25;
   private final ModelRenderer hexadecagon_r26;
   private final ModelRenderer hexadecagon_r27;
   private final ModelRenderer hexadecagon_r28;
   private final ModelRenderer cube;
   private final ModelRenderer floating_plate;
   private boolean hideBase = false;
   private boolean isCube = false;
   protected float center_circle_index = 0.0F;
   protected float inner_lp2_index = 0.0F;
   protected float inner_lp_index = 0.0F;

   public ModelPokestop() {
      this.field_78090_t = 64;
      this.field_78089_u = 64;
      this.cube = new ModelRenderer(this);
      this.cube.func_78793_a(0.0F, -3.0F, 0.0F);
      this.setRotationAngle(this.cube, 0.7853982F, 0.0F, 0.3926991F);
      this.cube.field_78804_l.add(new ModelBox(this.cube, 0, 0, -6.0F, -6.0F, -6.0F, 12, 12, 12, 0.0F, false));
      this.bottom_parts = new ModelRenderer(this);
      this.bottom_parts.func_78793_a(0.0F, 24.0F, 0.0F);
      this.bottom_parts.field_78804_l.add(new ModelBox(this.bottom_parts, 0, 0, -1.6569F, -0.5F, -4.0F, 2, 1, 8, 0.0F, false));
      this.bottom_parts.field_78804_l.add(new ModelBox(this.bottom_parts, 0, 0, -0.3431F, -0.5F, -4.0F, 2, 1, 8, 0.0F, false));
      this.bottom_parts.field_78804_l.add(new ModelBox(this.bottom_parts, 0, 0, -4.0F, -0.5F, -1.6569F, 8, 1, 2, 0.0F, false));
      this.bottom_parts.field_78804_l.add(new ModelBox(this.bottom_parts, 0, 0, -4.0F, -0.5F, -0.3431F, 8, 1, 2, 0.0F, false));
      this.bottom_parts.field_78804_l.add(new ModelBox(this.bottom_parts, 0, 1, -0.5F, -10.0F, -0.5F, 1, 10, 1, 0.0F, false));
      this.octagon_r1 = new ModelRenderer(this);
      this.octagon_r1.func_78793_a(0.0F, 0.0F, 0.0F);
      this.bottom_parts.func_78792_a(this.octagon_r1);
      this.setRotationAngle(this.octagon_r1, 0.0F, -0.7854F, 0.0F);
      this.octagon_r1.field_78804_l.add(new ModelBox(this.octagon_r1, 0, 0, -4.0F, -0.5F, -0.3431F, 8, 1, 2, 0.0F, false));
      this.octagon_r1.field_78804_l.add(new ModelBox(this.octagon_r1, 0, 0, -4.0F, -0.5F, -1.6569F, 8, 1, 2, 0.0F, false));
      this.octagon_r1.field_78804_l.add(new ModelBox(this.octagon_r1, 0, 0, -1.6569F, -0.5F, -4.0F, 2, 1, 8, 0.0F, false));
      this.octagon_r1.field_78804_l.add(new ModelBox(this.octagon_r1, 0, 0, -0.3431F, -0.5F, -4.0F, 2, 1, 8, 0.0F, false));
      this.upper_parts = new ModelRenderer(this);
      this.upper_parts.func_78793_a(0.0F, -4.0F, 0.0F);
      this.inner_loose_parts2 = new ModelRenderer(this);
      this.inner_loose_parts2.func_78793_a(0.0F, 0.0F, 0.0F);
      this.upper_parts.func_78792_a(this.inner_loose_parts2);
      this.inner_loose_parts2.field_78804_l.add(new ModelBox(this.inner_loose_parts2, 0, 0, -1.9611F, 9.25F, -0.5F, 4, 1, 1, 0.0F, false));
      this.hexadecagon_r1 = new ModelRenderer(this);
      this.hexadecagon_r1.func_78793_a(0.0F, 0.0F, 0.0F);
      this.inner_loose_parts2.func_78792_a(this.hexadecagon_r1);
      this.setRotationAngle(this.hexadecagon_r1, 0.0F, 0.0F, -0.3927F);
      this.hexadecagon_r1.field_78804_l.add(new ModelBox(this.hexadecagon_r1, 0, 0, -2.0389F, 9.25F, -0.5F, 2, 1, 1, 0.0F, false));
      this.hexadecagon_r1.field_78804_l.add(new ModelBox(this.hexadecagon_r1, 0, 0, -0.9611F, 9.25F, -0.5F, 3, 1, 1, 0.0F, false));
      this.inner_loose_parts = new ModelRenderer(this);
      this.inner_loose_parts.func_78793_a(0.0F, 0.0F, 0.0F);
      this.upper_parts.func_78792_a(this.inner_loose_parts);
      this.setRotationAngle(this.inner_loose_parts, 0.0F, 0.0F, -0.2182F);
      this.inner_loose_parts.field_78804_l.add(new ModelBox(this.inner_loose_parts, 0, 0, -10.25F, -0.9611F, -0.5F, 1, 3, 1, 0.0F, false));
      this.inner_loose_parts.field_78804_l.add(new ModelBox(this.inner_loose_parts, 0, 0, -10.25F, -2.0389F, -0.5F, 1, 2, 1, 0.0F, false));
      this.hexadecagon_r2 = new ModelRenderer(this);
      this.hexadecagon_r2.func_78793_a(0.0F, 0.0F, 0.0F);
      this.inner_loose_parts.func_78792_a(this.hexadecagon_r2);
      this.setRotationAngle(this.hexadecagon_r2, 0.0F, 0.0F, -0.3927F);
      this.hexadecagon_r2.field_78804_l.add(new ModelBox(this.hexadecagon_r2, 0, 0, -2.0389F, -10.25F, -0.5F, 2, 1, 1, 0.0F, false));
      this.hexadecagon_r2.field_78804_l.add(new ModelBox(this.hexadecagon_r2, 0, 0, -0.9611F, -10.25F, -0.5F, 3, 1, 1, 0.0F, false));
      this.hexadecagon_r3 = new ModelRenderer(this);
      this.hexadecagon_r3.func_78793_a(0.0F, 0.0F, 0.0F);
      this.inner_loose_parts.func_78792_a(this.hexadecagon_r3);
      this.setRotationAngle(this.hexadecagon_r3, 0.0F, 0.0F, -0.7854F);
      this.hexadecagon_r3.field_78804_l.add(new ModelBox(this.hexadecagon_r3, 0, 0, -2.0389F, -10.25F, -0.5F, 2, 1, 1, 0.0F, false));
      this.hexadecagon_r3.field_78804_l.add(new ModelBox(this.hexadecagon_r3, 0, 0, -0.9611F, -10.25F, -0.5F, 3, 1, 1, 0.0F, false));
      this.hexadecagon_r4 = new ModelRenderer(this);
      this.hexadecagon_r4.func_78793_a(0.0F, 0.0F, 0.0F);
      this.inner_loose_parts.func_78792_a(this.hexadecagon_r4);
      this.setRotationAngle(this.hexadecagon_r4, 0.0F, 0.0F, 0.3927F);
      this.hexadecagon_r4.field_78804_l.add(new ModelBox(this.hexadecagon_r4, 0, 0, -10.25F, -2.0389F, -0.5F, 1, 2, 1, 0.0F, false));
      this.hexadecagon_r4.field_78804_l.add(new ModelBox(this.hexadecagon_r4, 0, 0, -10.25F, -0.9611F, -0.5F, 1, 3, 1, 0.0F, false));
      this.outer_loose_parts = new ModelRenderer(this);
      this.outer_loose_parts.func_78793_a(0.0F, 0.0F, 0.0F);
      this.upper_parts.func_78792_a(this.outer_loose_parts);
      this.hexadecagon_r5 = new ModelRenderer(this);
      this.hexadecagon_r5.func_78793_a(0.0F, 0.0F, 0.0F);
      this.outer_loose_parts.func_78792_a(this.hexadecagon_r5);
      this.setRotationAngle(this.hexadecagon_r5, 0.0F, 0.0F, -0.3927F);
      this.hexadecagon_r5.field_78804_l.add(new ModelBox(this.hexadecagon_r5, 0, 0, 14.75F, -3.1329F, -0.5F, 1, 5, 1, 0.0F, false));
      this.hexadecagon_r5.field_78804_l.add(new ModelBox(this.hexadecagon_r5, 0, 0, -15.75F, -1.8671F, -0.5F, 1, 5, 1, 0.0F, false));
      this.hexadecagon_r6 = new ModelRenderer(this);
      this.hexadecagon_r6.func_78793_a(0.0F, 0.0F, 0.0F);
      this.outer_loose_parts.func_78792_a(this.hexadecagon_r6);
      this.setRotationAngle(this.hexadecagon_r6, 0.0F, 0.0F, 0.7854F);
      this.hexadecagon_r6.field_78804_l.add(new ModelBox(this.hexadecagon_r6, 0, 0, -1.8671F, -15.75F, -0.5F, 5, 1, 1, 0.0F, false));
      this.hexadecagon_r6.field_78804_l.add(new ModelBox(this.hexadecagon_r6, 0, 0, -3.1329F, 14.75F, -0.5F, 5, 1, 1, 0.0F, false));
      this.inner_circle = new ModelRenderer(this);
      this.inner_circle.func_78793_a(0.0F, 0.0F, 0.0F);
      this.upper_parts.func_78792_a(this.inner_circle);
      this.inner_circle.field_78804_l.add(new ModelBox(this.inner_circle, 0, 0, -0.3038F, -3.5F, -0.5F, 1, 7, 1, 0.0F, false));
      this.inner_circle.field_78804_l.add(new ModelBox(this.inner_circle, 0, 0, -0.6962F, -3.5F, -0.5F, 1, 7, 1, 0.0F, false));
      this.inner_circle.field_78804_l.add(new ModelBox(this.inner_circle, 0, 0, -3.5F, -0.6962F, -0.5F, 7, 1, 1, 0.0F, false));
      this.inner_circle.field_78804_l.add(new ModelBox(this.inner_circle, 0, 0, -3.5F, -0.3038F, -0.5F, 7, 1, 1, 0.0F, false));
      this.hexadecagon_r7 = new ModelRenderer(this);
      this.hexadecagon_r7.func_78793_a(0.1502F, -0.3625F, 0.0F);
      this.inner_circle.func_78792_a(this.hexadecagon_r7);
      this.setRotationAngle(this.hexadecagon_r7, 0.0F, 0.0F, 0.3927F);
      this.hexadecagon_r7.field_78804_l.add(new ModelBox(this.hexadecagon_r7, 0, 0, -3.5F, -0.3038F, -0.5F, 7, 1, 1, 0.0F, false));
      this.hexadecagon_r8 = new ModelRenderer(this);
      this.hexadecagon_r8.func_78793_a(0.0F, 0.0F, 0.0F);
      this.inner_circle.func_78792_a(this.hexadecagon_r8);
      this.setRotationAngle(this.hexadecagon_r8, 0.0F, 0.0F, 0.3927F);
      this.hexadecagon_r8.field_78804_l.add(new ModelBox(this.hexadecagon_r8, 0, 0, -3.5F, -0.3038F, -0.5F, 7, 1, 1, 0.0F, false));
      this.hexadecagon_r8.field_78804_l.add(new ModelBox(this.hexadecagon_r8, 0, 0, -0.3038F, -3.5F, -0.5F, 1, 7, 1, 0.0F, false));
      this.hexadecagon_r9 = new ModelRenderer(this);
      this.hexadecagon_r9.func_78793_a(-0.1502F, -0.3625F, 0.0F);
      this.inner_circle.func_78792_a(this.hexadecagon_r9);
      this.setRotationAngle(this.hexadecagon_r9, 0.0F, 0.0F, -0.3927F);
      this.hexadecagon_r9.field_78804_l.add(new ModelBox(this.hexadecagon_r9, 0, 0, -3.5F, -0.3038F, -0.5F, 7, 1, 1, 0.0F, false));
      this.hexadecagon_r10 = new ModelRenderer(this);
      this.hexadecagon_r10.func_78793_a(0.0F, 0.0F, 0.0F);
      this.inner_circle.func_78792_a(this.hexadecagon_r10);
      this.setRotationAngle(this.hexadecagon_r10, 0.0F, 0.0F, -0.3927F);
      this.hexadecagon_r10.field_78804_l.add(new ModelBox(this.hexadecagon_r10, 0, 0, -3.5F, -0.3038F, -0.5F, 7, 1, 1, 0.0F, false));
      this.hexadecagon_r10.field_78804_l.add(new ModelBox(this.hexadecagon_r10, 0, 0, -0.3038F, -3.5F, -0.5F, 1, 7, 1, 0.0F, false));
      this.hexadecagon_r11 = new ModelRenderer(this);
      this.hexadecagon_r11.func_78793_a(-0.2775F, -0.2775F, 0.0F);
      this.inner_circle.func_78792_a(this.hexadecagon_r11);
      this.setRotationAngle(this.hexadecagon_r11, 0.0F, 0.0F, 0.7854F);
      this.hexadecagon_r11.field_78804_l.add(new ModelBox(this.hexadecagon_r11, 0, 0, -0.3038F, -3.5F, -0.5F, 1, 7, 1, 0.0F, false));
      this.hexadecagon_r12 = new ModelRenderer(this);
      this.hexadecagon_r12.func_78793_a(0.0F, 0.0F, 0.0F);
      this.inner_circle.func_78792_a(this.hexadecagon_r12);
      this.setRotationAngle(this.hexadecagon_r12, 0.0F, 0.0F, 0.7854F);
      this.hexadecagon_r12.field_78804_l.add(new ModelBox(this.hexadecagon_r12, 0, 0, -0.3038F, -3.5F, -0.5F, 1, 7, 1, 0.0F, false));
      this.hexadecagon_r13 = new ModelRenderer(this);
      this.hexadecagon_r13.func_78793_a(-0.3625F, -0.1502F, 0.0F);
      this.inner_circle.func_78792_a(this.hexadecagon_r13);
      this.setRotationAngle(this.hexadecagon_r13, 0.0F, 0.0F, 0.3927F);
      this.hexadecagon_r13.field_78804_l.add(new ModelBox(this.hexadecagon_r13, 0, 0, -0.3038F, -3.5F, -0.5F, 1, 7, 1, 0.0F, false));
      this.hexadecagon_r14 = new ModelRenderer(this);
      this.hexadecagon_r14.func_78793_a(-0.3625F, 0.1502F, 0.0F);
      this.inner_circle.func_78792_a(this.hexadecagon_r14);
      this.setRotationAngle(this.hexadecagon_r14, 0.0F, 0.0F, -0.3927F);
      this.hexadecagon_r14.field_78804_l.add(new ModelBox(this.hexadecagon_r14, 0, 0, -0.3038F, -3.5F, -0.5F, 1, 7, 1, 0.0F, false));
      this.hexadecagon_r15 = new ModelRenderer(this);
      this.hexadecagon_r15.func_78793_a(-0.2775F, 0.2775F, 0.0F);
      this.inner_circle.func_78792_a(this.hexadecagon_r15);
      this.setRotationAngle(this.hexadecagon_r15, 0.0F, 0.0F, -0.7854F);
      this.hexadecagon_r15.field_78804_l.add(new ModelBox(this.hexadecagon_r15, 0, 0, -0.3038F, -3.5F, -0.5F, 1, 7, 1, 0.0F, false));
      this.hexadecagon_r16 = new ModelRenderer(this);
      this.hexadecagon_r16.func_78793_a(0.0F, 0.0F, 0.0F);
      this.inner_circle.func_78792_a(this.hexadecagon_r16);
      this.setRotationAngle(this.hexadecagon_r16, 0.0F, 0.0F, -0.7854F);
      this.hexadecagon_r16.field_78804_l.add(new ModelBox(this.hexadecagon_r16, 0, 0, -0.3038F, -3.5F, -0.5F, 1, 7, 1, 0.0F, false));
      this.lower_inner_ring = new ModelRenderer(this);
      this.lower_inner_ring.func_78793_a(0.0F, 0.0F, 0.0F);
      this.upper_parts.func_78792_a(this.lower_inner_ring);
      this.lower_inner_ring.field_78804_l.add(new ModelBox(this.lower_inner_ring, 0, 0, -6.75F, 0.6908F, -0.5F, 2, 1, 1, 0.0F, true));
      this.lower_inner_ring.field_78804_l.add(new ModelBox(this.lower_inner_ring, 0, 0, -1.5F, 4.75F, -0.5F, 3, 2, 1, 0.0F, false));
      this.lower_inner_ring.field_78804_l.add(new ModelBox(this.lower_inner_ring, 0, 0, 4.75F, 0.6908F, -0.5F, 2, 1, 1, 0.0F, false));
      this.lower_inner_ring.field_78804_l.add(new ModelBox(this.lower_inner_ring, 0, 0, 6.5F, 0.6908F, -0.5F, 2, 1, 1, 0.0F, false));
      this.lower_inner_ring.field_78804_l.add(new ModelBox(this.lower_inner_ring, 0, 0, -0.3092F, 6.5F, -0.5F, 2, 2, 1, 0.0F, false));
      this.lower_inner_ring.field_78804_l.add(new ModelBox(this.lower_inner_ring, 0, 0, -1.6908F, 6.5F, -0.5F, 2, 2, 1, 0.0F, false));
      this.lower_inner_ring.field_78804_l.add(new ModelBox(this.lower_inner_ring, 0, 0, -8.5F, 0.6908F, -0.5F, 2, 1, 1, 0.0F, true));
      this.hexadecagon_r17 = new ModelRenderer(this);
      this.hexadecagon_r17.func_78793_a(0.0F, 0.0F, 0.0F);
      this.lower_inner_ring.func_78792_a(this.hexadecagon_r17);
      this.setRotationAngle(this.hexadecagon_r17, 0.0F, 0.0F, -0.3927F);
      this.hexadecagon_r17.field_78804_l.add(new ModelBox(this.hexadecagon_r17, 0, 0, -8.5F, -0.3092F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r17.field_78804_l.add(new ModelBox(this.hexadecagon_r17, 0, 0, -8.5F, -1.6908F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r17.field_78804_l.add(new ModelBox(this.hexadecagon_r17, 0, 0, -1.6908F, 6.5F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r17.field_78804_l.add(new ModelBox(this.hexadecagon_r17, 0, 0, -0.3092F, 6.5F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r17.field_78804_l.add(new ModelBox(this.hexadecagon_r17, 0, 0, -1.5F, 4.75F, -0.5F, 3, 2, 1, 0.0F, false));
      this.hexadecagon_r17.field_78804_l.add(new ModelBox(this.hexadecagon_r17, 0, 0, -6.75F, -1.0F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r18 = new ModelRenderer(this);
      this.hexadecagon_r18.func_78793_a(0.0F, 0.0F, 0.0F);
      this.lower_inner_ring.func_78792_a(this.hexadecagon_r18);
      this.setRotationAngle(this.hexadecagon_r18, 0.0F, 0.0F, 0.7854F);
      this.hexadecagon_r18.field_78804_l.add(new ModelBox(this.hexadecagon_r18, 0, 0, -1.6908F, 6.5F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r18.field_78804_l.add(new ModelBox(this.hexadecagon_r18, 0, 0, -0.3092F, 6.5F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r18.field_78804_l.add(new ModelBox(this.hexadecagon_r18, 0, 0, -1.75F, 4.75F, -0.5F, 3, 2, 1, 0.0F, false));
      this.hexadecagon_r19 = new ModelRenderer(this);
      this.hexadecagon_r19.func_78793_a(0.0F, 0.0F, 0.0F);
      this.lower_inner_ring.func_78792_a(this.hexadecagon_r19);
      this.setRotationAngle(this.hexadecagon_r19, 0.0F, 0.0F, 0.3927F);
      this.hexadecagon_r19.field_78804_l.add(new ModelBox(this.hexadecagon_r19, 0, 0, -1.6908F, 6.5F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r19.field_78804_l.add(new ModelBox(this.hexadecagon_r19, 0, 0, -0.3092F, 6.5F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r19.field_78804_l.add(new ModelBox(this.hexadecagon_r19, 0, 0, 6.5F, -0.3092F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r19.field_78804_l.add(new ModelBox(this.hexadecagon_r19, 0, 0, 6.5F, -1.6908F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r19.field_78804_l.add(new ModelBox(this.hexadecagon_r19, 0, 0, 4.75F, -1.0F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r19.field_78804_l.add(new ModelBox(this.hexadecagon_r19, 0, 0, -1.5F, 4.75F, -0.5F, 3, 2, 1, 0.0F, false));
      this.hexadecagon_r20 = new ModelRenderer(this);
      this.hexadecagon_r20.func_78793_a(0.0F, 0.0F, 0.0F);
      this.lower_inner_ring.func_78792_a(this.hexadecagon_r20);
      this.setRotationAngle(this.hexadecagon_r20, 0.0F, 0.0F, -0.7854F);
      this.hexadecagon_r20.field_78804_l.add(new ModelBox(this.hexadecagon_r20, 0, 0, -0.3092F, 6.5F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r20.field_78804_l.add(new ModelBox(this.hexadecagon_r20, 0, 0, -1.6908F, 6.5F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r20.field_78804_l.add(new ModelBox(this.hexadecagon_r20, 0, 0, -1.25F, 4.75F, -0.5F, 3, 2, 1, 0.0F, false));
      this.upper_inner_ring = new ModelRenderer(this);
      this.upper_inner_ring.func_78793_a(0.0F, 0.0F, 0.0F);
      this.upper_parts.func_78792_a(this.upper_inner_ring);
      this.upper_inner_ring.field_78804_l.add(new ModelBox(this.upper_inner_ring, 0, 0, -0.3092F, -8.5F, -0.5F, 2, 2, 1, 0.0F, false));
      this.upper_inner_ring.field_78804_l.add(new ModelBox(this.upper_inner_ring, 0, 0, -1.6908F, -8.5F, -0.5F, 2, 2, 1, 0.0F, false));
      this.upper_inner_ring.field_78804_l.add(new ModelBox(this.upper_inner_ring, 0, 0, -8.5F, -1.6908F, -0.5F, 2, 1, 1, 0.0F, true));
      this.upper_inner_ring.field_78804_l.add(new ModelBox(this.upper_inner_ring, 0, 0, 6.5F, -1.6908F, -0.5F, 2, 1, 1, 0.0F, false));
      this.upper_inner_ring.field_78804_l.add(new ModelBox(this.upper_inner_ring, 0, 0, 4.75F, -1.6907F, -0.5F, 2, 1, 1, 0.0F, false));
      this.upper_inner_ring.field_78804_l.add(new ModelBox(this.upper_inner_ring, 0, 0, -6.75F, -1.6907F, -0.5F, 2, 1, 1, 0.0F, true));
      this.upper_inner_ring.field_78804_l.add(new ModelBox(this.upper_inner_ring, 0, 0, -1.5F, -6.75F, -0.5F, 3, 2, 1, 0.0F, false));
      this.hexadecagon_r21 = new ModelRenderer(this);
      this.hexadecagon_r21.func_78793_a(0.0F, 0.0F, 0.0F);
      this.upper_inner_ring.func_78792_a(this.hexadecagon_r21);
      this.setRotationAngle(this.hexadecagon_r21, 0.0F, 0.0F, 0.7854F);
      this.hexadecagon_r21.field_78804_l.add(new ModelBox(this.hexadecagon_r21, 0, 0, -1.25F, -6.75F, -0.5F, 3, 2, 1, 0.0F, false));
      this.hexadecagon_r21.field_78804_l.add(new ModelBox(this.hexadecagon_r21, 0, 0, -1.6908F, -8.5F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r21.field_78804_l.add(new ModelBox(this.hexadecagon_r21, 0, 0, -0.3092F, -8.5F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r22 = new ModelRenderer(this);
      this.hexadecagon_r22.func_78793_a(0.0F, 0.0F, 0.0F);
      this.upper_inner_ring.func_78792_a(this.hexadecagon_r22);
      this.setRotationAngle(this.hexadecagon_r22, 0.0F, 0.0F, -0.7854F);
      this.hexadecagon_r22.field_78804_l.add(new ModelBox(this.hexadecagon_r22, 0, 0, -1.75F, -6.75F, -0.5F, 3, 2, 1, 0.0F, false));
      this.hexadecagon_r22.field_78804_l.add(new ModelBox(this.hexadecagon_r22, 0, 0, -1.6908F, -8.5F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r22.field_78804_l.add(new ModelBox(this.hexadecagon_r22, 0, 0, -0.3092F, -8.5F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r23 = new ModelRenderer(this);
      this.hexadecagon_r23.func_78793_a(0.0F, 0.0F, 0.0F);
      this.upper_inner_ring.func_78792_a(this.hexadecagon_r23);
      this.setRotationAngle(this.hexadecagon_r23, 0.0F, 0.0F, 0.3927F);
      this.hexadecagon_r23.field_78804_l.add(new ModelBox(this.hexadecagon_r23, 0, 0, -1.5F, -6.75F, -0.5F, 3, 2, 1, 0.0F, false));
      this.hexadecagon_r23.field_78804_l.add(new ModelBox(this.hexadecagon_r23, 0, 0, -6.75F, -1.0F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r23.field_78804_l.add(new ModelBox(this.hexadecagon_r23, 0, 0, -8.5F, -0.3092F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r23.field_78804_l.add(new ModelBox(this.hexadecagon_r23, 0, 0, -8.5F, -1.6907F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r23.field_78804_l.add(new ModelBox(this.hexadecagon_r23, 0, 0, -1.6908F, -8.5F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r23.field_78804_l.add(new ModelBox(this.hexadecagon_r23, 0, 0, -0.3092F, -8.5F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r24 = new ModelRenderer(this);
      this.hexadecagon_r24.func_78793_a(0.0F, 0.0F, 0.0F);
      this.upper_inner_ring.func_78792_a(this.hexadecagon_r24);
      this.setRotationAngle(this.hexadecagon_r24, 0.0F, 0.0F, -0.3927F);
      this.hexadecagon_r24.field_78804_l.add(new ModelBox(this.hexadecagon_r24, 0, 0, -1.5F, -6.75F, -0.5F, 3, 2, 1, 0.0F, false));
      this.hexadecagon_r24.field_78804_l.add(new ModelBox(this.hexadecagon_r24, 0, 0, 4.75F, -1.0F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r24.field_78804_l.add(new ModelBox(this.hexadecagon_r24, 0, 0, 6.5F, -1.6908F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r24.field_78804_l.add(new ModelBox(this.hexadecagon_r24, 0, 0, 6.5F, -0.3092F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r24.field_78804_l.add(new ModelBox(this.hexadecagon_r24, 0, 0, -1.6908F, -8.5F, -0.5F, 2, 2, 1, 0.0F, false));
      this.hexadecagon_r24.field_78804_l.add(new ModelBox(this.hexadecagon_r24, 0, 0, -0.3092F, -8.5F, -0.5F, 2, 2, 1, 0.0F, false));
      this.outer_ring = new ModelRenderer(this);
      this.outer_ring.func_78793_a(0.0F, 0.0F, 0.0F);
      this.upper_parts.func_78792_a(this.outer_ring);
      this.outer_ring.field_78804_l.add(new ModelBox(this.outer_ring, 0, 0, -0.2152F, 11.0F, -1.0F, 3, 3, 2, 0.0F, false));
      this.outer_ring.field_78804_l.add(new ModelBox(this.outer_ring, 0, 0, -2.7848F, 11.0F, -1.0F, 3, 3, 2, 0.0F, false));
      this.outer_ring.field_78804_l.add(new ModelBox(this.outer_ring, 0, 0, -0.2152F, -14.0F, -1.0F, 3, 3, 2, 0.0F, false));
      this.outer_ring.field_78804_l.add(new ModelBox(this.outer_ring, 0, 0, -2.7848F, -14.0F, -1.0F, 3, 3, 2, 0.0F, false));
      this.outer_ring.field_78804_l.add(new ModelBox(this.outer_ring, 0, 0, 11.0F, -0.2152F, -1.0F, 3, 3, 2, 0.0F, false));
      this.outer_ring.field_78804_l.add(new ModelBox(this.outer_ring, 0, 0, 11.0F, -2.7848F, -1.0F, 3, 3, 2, 0.0F, false));
      this.outer_ring.field_78804_l.add(new ModelBox(this.outer_ring, 0, 0, -14.0F, -0.2152F, -1.0F, 3, 3, 2, 0.0F, false));
      this.outer_ring.field_78804_l.add(new ModelBox(this.outer_ring, 0, 0, -14.0F, -2.7848F, -1.0F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r25 = new ModelRenderer(this);
      this.hexadecagon_r25.func_78793_a(0.0F, 0.0F, -0.5F);
      this.outer_ring.func_78792_a(this.hexadecagon_r25);
      this.setRotationAngle(this.hexadecagon_r25, 0.0F, 0.0F, 0.3927F);
      this.hexadecagon_r25.field_78804_l.add(new ModelBox(this.hexadecagon_r25, 0, 0, -14.0F, -2.7848F, -0.5F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r25.field_78804_l.add(new ModelBox(this.hexadecagon_r25, 0, 0, -14.0F, -0.2152F, -0.5F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r25.field_78804_l.add(new ModelBox(this.hexadecagon_r25, 0, 0, 11.0F, -2.7848F, -0.5F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r25.field_78804_l.add(new ModelBox(this.hexadecagon_r25, 0, 0, 11.0F, -0.2152F, -0.5F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r25.field_78804_l.add(new ModelBox(this.hexadecagon_r25, 0, 0, -2.7848F, -14.0F, -0.5F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r25.field_78804_l.add(new ModelBox(this.hexadecagon_r25, 0, 0, -0.2152F, -14.0F, -0.5F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r25.field_78804_l.add(new ModelBox(this.hexadecagon_r25, 0, 0, -2.7848F, 11.0F, -0.5F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r25.field_78804_l.add(new ModelBox(this.hexadecagon_r25, 0, 0, -0.2152F, 11.0F, -0.5F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r26 = new ModelRenderer(this);
      this.hexadecagon_r26.func_78793_a(0.0F, 0.0F, -0.5F);
      this.outer_ring.func_78792_a(this.hexadecagon_r26);
      this.setRotationAngle(this.hexadecagon_r26, 0.0F, 0.0F, -0.3927F);
      this.hexadecagon_r26.field_78804_l.add(new ModelBox(this.hexadecagon_r26, 0, 0, -14.0F, -2.7848F, -0.5F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r26.field_78804_l.add(new ModelBox(this.hexadecagon_r26, 0, 0, -14.0F, -0.2152F, -0.5F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r26.field_78804_l.add(new ModelBox(this.hexadecagon_r26, 0, 0, 11.0F, -2.7848F, -0.5F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r26.field_78804_l.add(new ModelBox(this.hexadecagon_r26, 0, 0, 11.0F, -0.2152F, -0.5F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r26.field_78804_l.add(new ModelBox(this.hexadecagon_r26, 0, 0, -2.7848F, -14.0F, -0.5F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r26.field_78804_l.add(new ModelBox(this.hexadecagon_r26, 0, 0, -0.2152F, -14.0F, -0.5F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r26.field_78804_l.add(new ModelBox(this.hexadecagon_r26, 0, 0, -2.7848F, 11.0F, -0.5F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r26.field_78804_l.add(new ModelBox(this.hexadecagon_r26, 0, 0, -0.2152F, 11.0F, -0.5F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r27 = new ModelRenderer(this);
      this.hexadecagon_r27.func_78793_a(0.0F, 0.0F, -0.5F);
      this.outer_ring.func_78792_a(this.hexadecagon_r27);
      this.setRotationAngle(this.hexadecagon_r27, 0.0F, 0.0F, 0.7854F);
      this.hexadecagon_r27.field_78804_l.add(new ModelBox(this.hexadecagon_r27, 0, 0, -2.7848F, -14.0F, -0.5F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r27.field_78804_l.add(new ModelBox(this.hexadecagon_r27, 0, 0, -0.2152F, -14.0F, -0.5F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r27.field_78804_l.add(new ModelBox(this.hexadecagon_r27, 0, 0, -2.7848F, 11.0F, -0.5F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r27.field_78804_l.add(new ModelBox(this.hexadecagon_r27, 0, 0, -0.2152F, 11.0F, -0.5F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r28 = new ModelRenderer(this);
      this.hexadecagon_r28.func_78793_a(0.0F, 0.0F, -0.5F);
      this.outer_ring.func_78792_a(this.hexadecagon_r28);
      this.setRotationAngle(this.hexadecagon_r28, 0.0F, 0.0F, -0.7854F);
      this.hexadecagon_r28.field_78804_l.add(new ModelBox(this.hexadecagon_r28, 0, 0, -2.7848F, -14.0F, -0.5F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r28.field_78804_l.add(new ModelBox(this.hexadecagon_r28, 0, 0, -0.2152F, -14.0F, -0.5F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r28.field_78804_l.add(new ModelBox(this.hexadecagon_r28, 0, 0, -2.7848F, 11.0F, -0.5F, 3, 3, 2, 0.0F, false));
      this.hexadecagon_r28.field_78804_l.add(new ModelBox(this.hexadecagon_r28, 0, 0, -0.2152F, 11.0F, -0.5F, 3, 3, 2, 0.0F, false));
      this.floating_plate = new ModelRenderer(this);
      this.floating_plate.func_78793_a(0.0F, 14.0F, 0.0F);
      this.floating_plate.field_78804_l.add(new ModelBox(this.floating_plate, 0, 0, -5.0F, -1.0F, -5.0F, 10, 1, 10, 0.0F, false));
      this.floating_plate.field_78804_l.add(new ModelBox(this.floating_plate, 0, 0, -5.0F, -1.5F, -5.0F, 10, 1, 10, 0.0F, false));
   }

   public void func_78088_a(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
      if (this.isCube) {
         this.cube.func_78785_a(f5);
      } else {
         this.upper_parts.func_78785_a(f5);
      }

      if (!this.hideBase) {
         this.bottom_parts.func_78785_a(f5);
         this.floating_plate.func_78785_a(f5);
      }

   }

   public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
      modelRenderer.field_78795_f = x;
      modelRenderer.field_78796_g = y;
      modelRenderer.field_78808_h = z;
   }

   public void applyValuesFromEntity(EntityPokestop entity) {
      if (entity.isCube(Minecraft.func_71410_x().field_71439_g)) {
         this.cube.field_78796_g = (float)entity.getAnimationTickCube() / 150.0F;
         this.isCube = true;
      } else {
         entity.isAnimating();
         this.isCube = false;
      }

      this.func_78087_a(0.0F, 0.0F, (float)entity.getAnimationTicksTotal(), 0.0F, 0.0F, 0.0F, entity);
      this.hideBase = entity.hasNoBasePlate();
   }

   public void func_78087_a(float f, float f1, float f2, float f3, float f4, float f5, Entity parEntity) {
      this.floating_plate.field_82908_p = ((float)Math.sin((double)(f2 / 20.0F)) * 0.5F + 0.5F) * f5;
      float center_circle_index = f2 % 78.0F;
      if (center_circle_index <= 17.0F) {
         this.inner_circle.field_78796_g = (float)(Math.sin((double)center_circle_index * Math.PI / 34.0) * Math.PI);
      } else if (center_circle_index <= 22.0F) {
         this.inner_circle.field_78796_g = 3.1415927F;
      } else if (center_circle_index <= 39.0F) {
         this.inner_circle.field_78796_g = (float)((Math.sin((double)(center_circle_index - 22.0F) * Math.PI / 34.0) + 1.0) * Math.PI);
      } else if (center_circle_index <= 44.0F) {
         this.inner_circle.field_78796_g = 6.2831855F;
      } else if (center_circle_index <= 68.0F) {
         this.inner_circle.field_78796_g = (float)(Math.sin((double)(center_circle_index - 20.0F) * Math.PI / 48.0) * 2.0 * Math.PI);
      } else {
         this.inner_circle.field_78796_g = 0.0F;
      }

      float inner_lp2_index = f2 % 72.0F;
      if (inner_lp2_index <= 5.0F) {
         this.inner_loose_parts2.field_78808_h = (float)Math.sin((double)inner_lp2_index * Math.PI / 10.0) * -0.1309F;
      } else if (inner_lp2_index <= 24.0F) {
         this.inner_loose_parts2.field_78808_h = -0.1309F;
      } else if (inner_lp2_index <= 40.0F) {
         this.inner_loose_parts2.field_78808_h = (float)Math.sin((double)(inner_lp2_index - 24.0F) * Math.PI / 32.0) * -0.829F - 0.1309F;
      } else if (inner_lp2_index <= 48.0F) {
         this.inner_loose_parts2.field_78808_h = -0.9599F;
      } else if (inner_lp2_index <= 64.0F) {
         this.inner_loose_parts2.field_78808_h = (float)Math.cos((double)(inner_lp2_index - 48.0F) * Math.PI / 32.0) * -0.9599F;
      } else {
         this.inner_loose_parts2.field_78808_h = 0.0F;
      }

      float inner_lp_index = f2 % 83.0F;
      if (inner_lp_index <= 24.0F) {
         this.inner_loose_parts.field_78808_h = (float)(Math.sin((double)inner_lp_index * Math.PI / 48.0) * 1.2654000520706177);
      } else if (inner_lp_index <= 60.0F) {
         this.inner_loose_parts.field_78808_h = (float)(Math.cos((double)(inner_lp_index - 20.0F) * Math.PI / 80.0) * 1.2654000520706177);
      } else {
         this.inner_loose_parts.field_78808_h = 0.0F;
      }

      if (f2 % 78.0F <= 60.0F) {
         this.outer_loose_parts.field_78808_h = (float)(Math.sin((double)(f2 % 78.0F) * Math.PI / 60.0) * -1.1780999898910522);
      } else {
         this.outer_loose_parts.field_78808_h = 0.0F;
      }

   }
}
