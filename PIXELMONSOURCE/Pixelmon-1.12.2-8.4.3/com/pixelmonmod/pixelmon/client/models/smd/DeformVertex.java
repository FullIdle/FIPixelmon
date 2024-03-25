package com.pixelmonmod.pixelmon.client.models.smd;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

public class DeformVertex extends Vertex {
   public DeformVertex copy = null;
   protected final Vector4f baseLoc;
   public Vector4f currentLocMod = new Vector4f();
   protected final Vector4f baseNormal;
   public Vector4f currentNormalMod = new Vector4f();
   public final int ID;
   public float xn;
   public float yn;
   public float zn;

   public DeformVertex(DeformVertex vertex) {
      super(vertex.x, vertex.y, vertex.z);
      this.xn = vertex.xn;
      this.yn = vertex.yn;
      this.zn = vertex.zn;
      this.baseLoc = new Vector4f(vertex.baseLoc);
      this.baseNormal = new Vector4f(vertex.baseNormal);
      this.ID = vertex.ID;
      vertex.copy = this;
      this.currentLocMod = vertex.currentLocMod;
      this.currentNormalMod = vertex.currentNormalMod;
   }

   public DeformVertex(float x, float y, float z, float xn, float yn, float zn, int ID) {
      super(x, y, z);
      this.xn = xn;
      this.yn = yn;
      this.zn = zn;
      this.baseLoc = new Vector4f(x, y, z, 1.0F);
      this.baseNormal = new Vector4f(xn, yn, zn, 0.0F);
      this.ID = ID;
   }

   public void reset() {
      this.currentLocMod = null;
      this.currentNormalMod = null;
   }

   protected void initModVectors() {
      if (this.currentLocMod == null) {
         this.currentLocMod = new Vector4f();
      }

      if (this.currentNormalMod == null) {
         this.currentNormalMod = new Vector4f();
      }

   }

   public void applyModified(Bone bone, float weight) {
      Matrix4f modified = bone.modified;
      if (modified != null) {
         this.initModVectors();
         Vector4f locTemp = Matrix4f.transform(modified, this.baseLoc, (Vector4f)null);
         Vector4f normalTemp = Matrix4f.transform(modified, this.baseNormal, (Vector4f)null);
         locTemp.scale(weight);
         normalTemp.scale(weight);
         Vector4f.add(locTemp, this.currentLocMod, this.currentLocMod);
         Vector4f.add(normalTemp, this.currentNormalMod, this.currentNormalMod);
      }

   }

   public void applyChange() {
      if (this.currentLocMod == null) {
         this.x = this.baseLoc.x;
         this.y = this.baseLoc.y;
         this.z = this.baseLoc.z;
      } else {
         this.x = this.currentLocMod.x;
         this.y = this.currentLocMod.y;
         this.z = this.currentLocMod.z;
      }

      if (this.currentNormalMod == null) {
         this.xn = this.baseNormal.x;
         this.yn = this.baseNormal.y;
         this.zn = this.baseNormal.z;
      } else {
         this.xn = this.currentNormalMod.x;
         this.yn = this.currentNormalMod.y;
         this.zn = this.currentNormalMod.z;
      }

   }

   public boolean equals(float x, float y, float z) {
      return this.x == x && this.y == y && this.z == z;
   }

   public float getX(float partialTick) {
      return this.x;
   }

   public float getY(float partialTick) {
      return this.y;
   }

   public float getZ(float partialTick) {
      return this.z;
   }

   public float getXN(float partialTick) {
      return this.xn;
   }

   public float getYN(float partialTick) {
      return this.yn;
   }

   public float getZN(float partialTick) {
      return this.zn;
   }
}
