package com.pixelmonmod.pixelmon.entities.pixelmon.stats;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;

@JsonAdapter(Adapter.class)
public class EntityBoundsData {
   private double width;
   private double height;
   private float eye_height;

   public EntityBoundsData(double width, double height, float eye_height) {
      this.width = width;
      this.height = height;
      this.eye_height = eye_height;
   }

   public double getWidth() {
      return this.width;
   }

   public double getHeight() {
      return this.height;
   }

   public float getEyeHeight() {
      return this.eye_height > 0.0F ? this.eye_height : (float)(this.height * 0.8500000238418579);
   }

   public AxisAlignedBB createBoundingBox() {
      double diameter = this.width / 2.0;
      return new AxisAlignedBB(diameter, 0.0, diameter, diameter, this.height, diameter);
   }

   public AxisAlignedBB createBoundingBox(Entity entity) {
      double diameter = this.width / 2.0;
      return new AxisAlignedBB(entity.field_70165_t - diameter, entity.field_70163_u, entity.field_70161_v - diameter, entity.field_70165_t + diameter, entity.field_70163_u + this.height, entity.field_70161_v + diameter);
   }

   public AxisAlignedBB createBoundingBox(Entity entity, double scale) {
      double diameter = this.width * scale / 2.0;
      return new AxisAlignedBB(entity.field_70165_t - diameter, entity.field_70163_u, entity.field_70161_v - diameter, entity.field_70165_t + diameter, entity.field_70163_u + this.height * scale, entity.field_70161_v + diameter);
   }

   public static class Adapter extends TypeAdapter {
      public void write(JsonWriter out, EntityBoundsData value) throws IOException {
         if (value == null) {
            value = new EntityBoundsData(1.0, 1.0, 0.85F);
         }

         out.beginObject();
         out.name("width").value(value.width);
         out.name("height").value(value.height);
         out.name("eye_height").value(Double.parseDouble(Float.toString(value.eye_height)));
         out.endObject();
      }

      public EntityBoundsData read(JsonReader in) throws IOException {
         if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
         } else {
            in.beginObject();
            double width = 0.0;
            double height = 0.0;
            float eye_height = 0.0F;

            while(in.hasNext()) {
               switch (in.nextName()) {
                  case "width":
                     width = in.nextDouble();
                     break;
                  case "height":
                     height = in.nextDouble();
                     break;
                  case "eye_height":
                     eye_height = (float)in.nextDouble();
               }
            }

            in.endObject();
            return new EntityBoundsData(width, height, eye_height);
         }
      }
   }
}
