package com.pixelmonmod.pixelmon.client.models.smd;

import com.pixelmonmod.pixelmon.util.helpers.VectorHelper;
import java.util.ArrayList;
import org.lwjgl.util.vector.Matrix4f;

public class AnimFrame {
   public ArrayList transforms = new ArrayList();
   public ArrayList invertTransforms = new ArrayList();
   public SmdAnimation owner;
   public final int ID;

   public AnimFrame(AnimFrame anim, SmdAnimation parent) {
      this.owner = parent;
      this.ID = anim.ID;
      this.transforms = anim.transforms;
      this.invertTransforms = anim.invertTransforms;
   }

   public AnimFrame(SmdAnimation parent) {
      this.owner = parent;
      this.ID = parent.requestFrameID();
   }

   public void addTransforms(int index, Matrix4f invertedData) {
      this.transforms.add(index, invertedData);
      this.invertTransforms.add(index, Matrix4f.invert(invertedData, (Matrix4f)null));
   }

   public void fixUp(int id, float degrees) {
      float radians = (float)Math.toRadians((double)degrees);
      Matrix4f rotator = VectorHelper.matrix4FromLocRot(0.0F, 0.0F, 0.0F, radians, 0.0F, 0.0F);
      Matrix4f.mul(rotator, (Matrix4f)this.transforms.get(id), (Matrix4f)this.transforms.get(id));
      Matrix4f.mul(Matrix4f.invert(rotator, (Matrix4f)null), (Matrix4f)this.invertTransforms.get(id), (Matrix4f)this.invertTransforms.get(id));
   }

   public void reform() {
      for(int i = 0; i < this.transforms.size(); ++i) {
         Bone bone = (Bone)this.owner.bones.get(i);
         if (bone.parent != null) {
            Matrix4f temp = Matrix4f.mul((Matrix4f)this.transforms.get(bone.parent.ID), (Matrix4f)this.transforms.get(i), (Matrix4f)null);
            this.transforms.set(i, temp);
            this.invertTransforms.set(i, Matrix4f.invert(temp, (Matrix4f)null));
         }
      }

   }
}
