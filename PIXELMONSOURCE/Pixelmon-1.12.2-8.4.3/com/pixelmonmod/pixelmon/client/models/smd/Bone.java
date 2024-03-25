package com.pixelmonmod.pixelmon.client.models.smd;

import com.pixelmonmod.pixelmon.client.models.animations.EnumGeomData;
import com.pixelmonmod.pixelmon.client.models.animations.IModulized;
import com.pixelmonmod.pixelmon.util.helpers.VectorHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.lwjgl.util.vector.Matrix4f;

public class Bone implements IModulized {
   public Bone copy = null;
   public String name;
   public int ID;
   public Bone parent;
   public SmdModel owner;
   private Boolean isDummy;
   public Matrix4f rest;
   public Matrix4f restInverted;
   public Matrix4f modified = new Matrix4f();
   public Matrix4f difference = new Matrix4f();
   public Matrix4f prevInverted = new Matrix4f();
   public ArrayList children = new ArrayList();
   public HashMap verts = new HashMap();
   public HashMap animatedTransforms = new HashMap();

   public Bone(String name, int ID, Bone parent, SmdModel owner) {
      this.name = name;
      this.ID = ID;
      this.parent = parent;
      this.owner = owner;
   }

   public Bone(Bone b, Bone parent, SmdModel owner) {
      this.name = b.name;
      this.ID = b.ID;
      this.owner = owner;
      this.parent = parent;
      Iterator var4 = b.verts.entrySet().iterator();

      while(var4.hasNext()) {
         Map.Entry entry = (Map.Entry)var4.next();
         this.verts.put(owner.verts.get(((DeformVertex)entry.getKey()).ID), entry.getValue());
      }

      this.animatedTransforms = new HashMap(b.animatedTransforms);
      this.restInverted = b.restInverted;
      this.rest = b.rest;
      b.copy = this;
   }

   public void setChildren(Bone b, ArrayList bones) {
      for(int i = 0; i < b.children.size(); ++i) {
         Bone child = (Bone)b.children.get(i);
         this.children.add(bones.get(child.ID));
         ((Bone)bones.get(child.ID)).parent = this;
      }

   }

   public boolean isDummy() {
      return this.isDummy == null ? (this.isDummy = this.parent == null && this.children.isEmpty()) : this.isDummy;
   }

   public void setRest(Matrix4f resting) {
      this.rest = resting;
   }

   public void addChild(Bone child) {
      this.children.add(child);
   }

   public void addVertex(DeformVertex v, float weight) {
      if (this.name.equals("blender_implicit")) {
         throw new UnsupportedOperationException("NO.");
      } else {
         this.verts.put(v, weight);
      }
   }

   private void reform(Matrix4f parentMatrix) {
      this.rest = Matrix4f.mul(parentMatrix, this.rest, (Matrix4f)null);
      if (ValveStudioModel.debugModel) {
         VectorHelper.print((Object)(this.name + ' ' + this.rest));
      }

      this.reformChildren();
   }

   public void reformChildren() {
      Iterator var1 = this.children.iterator();

      while(var1.hasNext()) {
         Bone child = (Bone)var1.next();
         child.reform(this.rest);
      }

   }

   public void invertRestMatrix() {
      this.restInverted = Matrix4f.invert(this.rest, (Matrix4f)null);
   }

   public void reset() {
      this.modified.setIdentity();
   }

   public void preloadAnimation(AnimFrame key, Matrix4f animated) {
      HashMap precalcArray;
      if (this.animatedTransforms.containsKey(key.owner.animationName)) {
         precalcArray = (HashMap)this.animatedTransforms.get(key.owner.animationName);
      } else {
         precalcArray = new HashMap();
      }

      precalcArray.put(key.ID, animated);
      this.animatedTransforms.put(key.owner.animationName, precalcArray);
   }

   public void setModified() {
      Matrix4f realInverted;
      Matrix4f real;
      if (this.owner.owner.hasAnimations() && this.owner.currentAnim != null) {
         AnimFrame currentFrame = (AnimFrame)this.owner.currentAnim.frames.get(this.owner.currentAnim.currentFrameIndex);
         realInverted = (Matrix4f)currentFrame.transforms.get(this.ID);
         real = (Matrix4f)currentFrame.invertTransforms.get(this.ID);
      } else {
         realInverted = this.rest;
         real = this.restInverted;
      }

      Matrix4f delta = new Matrix4f();
      Matrix4f absolute = new Matrix4f();
      Matrix4f.mul(realInverted, real, delta);
      this.modified = this.parent != null ? Matrix4f.mul(this.parent.modified, delta, this.initModified()) : delta;
      Matrix4f.mul(real, this.modified, absolute);
      Matrix4f.invert(absolute, this.prevInverted);
      this.children.forEach(Bone::setModified);
   }

   protected Matrix4f initModified() {
      return this.modified == null ? (this.modified = new Matrix4f()) : this.modified;
   }

   public void applyModified() {
      AnimFrame currentFrame = this.owner.currentFrame();
      if (currentFrame != null) {
         HashMap precalcArray = (HashMap)this.animatedTransforms.get(currentFrame.owner.animationName);
         Matrix4f animated = (Matrix4f)precalcArray.get(currentFrame.ID);
         Matrix4f animatedChange = new Matrix4f();
         Matrix4f.mul(animated, this.restInverted, animatedChange);
         this.modified = this.modified == null ? animatedChange : Matrix4f.mul(this.modified, animatedChange, this.modified);
      }

      this.verts.entrySet().forEach((entry) -> {
         ((DeformVertex)entry.getKey()).applyModified(this, (Float)entry.getValue());
      });
      this.reset();
   }

   public float setValue(float value, EnumGeomData d) {
      return value;
   }

   public float getValue(EnumGeomData d) {
      return 0.0F;
   }
}
