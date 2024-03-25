package com.pixelmonmod.pixelmon.client.models.smd;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.opengl.GL11;

public class NormalizedFace {
   public DeformVertex[] vertices;
   public TextureCoordinate[] textureCoordinates;
   public Vertex faceNormal;

   public NormalizedFace(DeformVertex[] xyz, TextureCoordinate[] uvs) {
      this.vertices = xyz;
      this.textureCoordinates = uvs;
   }

   public NormalizedFace(NormalizedFace face, ArrayList verts) {
      this.vertices = new DeformVertex[face.vertices.length];

      for(int i = 0; i < this.vertices.length; ++i) {
         this.vertices[i] = (DeformVertex)verts.get(face.vertices[i].ID);
      }

      this.textureCoordinates = new TextureCoordinate[face.textureCoordinates.length];
      System.arraycopy(face.textureCoordinates, 0, this.textureCoordinates, 0, this.textureCoordinates.length);
      if (face.faceNormal != null) {
         this.faceNormal = face.faceNormal;
      }

   }

   public void addFaceForRender(boolean smoothShading) {
      if (!smoothShading && this.faceNormal == null) {
         this.faceNormal = this.calculateFaceNormal();
      }

      for(int i = 0; i < 3; ++i) {
         GL11.glTexCoord2f(this.textureCoordinates[i].u, this.textureCoordinates[i].v);
         if (!smoothShading) {
            GL11.glNormal3f(this.faceNormal.x, this.faceNormal.y, this.faceNormal.z);
         } else {
            GL11.glNormal3f(this.vertices[i].xn, this.vertices[i].yn, this.vertices[i].zn);
         }

         GL11.glVertex3d((double)this.vertices[i].x, (double)this.vertices[i].y, (double)this.vertices[i].z);
      }

   }

   public Vertex calculateFaceNormal() {
      Vec3d v1 = new Vec3d((double)(this.vertices[1].x - this.vertices[0].x), (double)(this.vertices[1].y - this.vertices[0].y), (double)(this.vertices[1].z - this.vertices[0].z));
      Vec3d v2 = new Vec3d((double)(this.vertices[2].x - this.vertices[0].x), (double)(this.vertices[2].y - this.vertices[0].y), (double)(this.vertices[2].z - this.vertices[0].z));
      Vec3d normalVector = null;
      normalVector = v1.func_72431_c(v2).func_72432_b();
      return new Vertex((float)normalVector.field_72450_a, (float)normalVector.field_72448_b, (float)normalVector.field_72449_c);
   }

   public void addFaceForRender(FloatBuffer vertexBuffer, FloatBuffer textureBuffer, FloatBuffer normalBuffer, boolean smoothShading, float partialTick) {
      if (!smoothShading && this.faceNormal == null) {
         this.faceNormal = this.calculateFaceNormal();
      }

      for(int i = 0; i < 3; ++i) {
         textureBuffer.put(this.textureCoordinates[i].u);
         textureBuffer.put(this.textureCoordinates[i].v);
         if (!smoothShading) {
            normalBuffer.put(this.faceNormal.x);
            normalBuffer.put(this.faceNormal.y);
            normalBuffer.put(this.faceNormal.z);
         } else {
            normalBuffer.put(this.vertices[i].getXN(partialTick));
            normalBuffer.put(this.vertices[i].getYN(partialTick));
            normalBuffer.put(this.vertices[i].getZN(partialTick));
         }

         vertexBuffer.put(this.vertices[i].getX(partialTick));
         vertexBuffer.put(this.vertices[i].getY(partialTick));
         vertexBuffer.put(this.vertices[i].getZ(partialTick));
      }

   }

   public void addFaceForRender(FloatBuffer vertexBuffer, FloatBuffer normalBuffer, boolean smoothShading, float partialTick) {
      if (!smoothShading && this.faceNormal == null) {
         this.faceNormal = this.calculateFaceNormal();
      }

      for(int i = 0; i < 3; ++i) {
         if (!smoothShading) {
            normalBuffer.put(this.faceNormal.x);
            normalBuffer.put(this.faceNormal.y);
            normalBuffer.put(this.faceNormal.z);
         } else {
            normalBuffer.put(this.vertices[i].getXN(partialTick));
            normalBuffer.put(this.vertices[i].getYN(partialTick));
            normalBuffer.put(this.vertices[i].getZN(partialTick));
         }

         vertexBuffer.put(this.vertices[i].getX(partialTick));
         vertexBuffer.put(this.vertices[i].getY(partialTick));
         vertexBuffer.put(this.vertices[i].getZ(partialTick));
      }

   }
}
