package com.pixelmonmod.pixelmon.client.models.obj;

public class Material {
   private Texture texture = null;
   private Vertex Ka = null;
   private Vertex Kd = null;
   private Vertex Ks = null;
   private float _shininess;
   private String name;
   public String texName;

   public Material(String name) {
      name = null;
      this.texName = null;
      this._shininess = 0.0F;
      this.name = name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public Texture getTexture() {
      return this.texture;
   }

   public void setTexture(Texture texture) {
      this.texture = texture;
   }

   public Vertex getKa() {
      return this.Ka;
   }

   public Vertex getKd() {
      return this.Kd;
   }

   public Vertex getKs() {
      return this.Ks;
   }

   public float getShininess() {
      return this._shininess;
   }

   public void setKa(Vertex ka) {
      this.Ka = ka;
   }

   public void setKd(Vertex kd) {
      this.Kd = kd;
   }

   public void setKs(Vertex ks) {
      this.Ks = ks;
   }

   public void setShininess(float s) {
      this._shininess = s;
   }
}
