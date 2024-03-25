package com.pixelmonmod.pixelmon.client.models.obj;

public class Vertex {
   private float x;
   private float y;
   private float z;

   public Vertex() {
   }

   public Vertex(float x, float y, float z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public Vertex(int i, int j) {
      this.setX((float)i);
      this.setY((float)j);
   }

   public Vertex(Vertex position) {
      this.setX(position.getX());
      this.setY(position.getY());
      this.setZ(position.getZ());
   }

   public float getX() {
      return this.x;
   }

   public void setX(float x) {
      this.x = x;
   }

   public float getY() {
      return this.y;
   }

   public void setY(float y) {
      this.y = y;
   }

   public float getZ() {
      return this.z;
   }

   public void setZ(float z) {
      this.z = z;
   }

   public double norm() {
      return Math.sqrt(Math.pow((double)this.x, 2.0) + Math.pow((double)this.y, 2.0) + Math.pow((double)this.z, 2.0));
   }

   public void normalize() {
      double norm = this.norm();
      this.setX(this.getX() / (float)norm);
      this.setY(this.getY() / (float)norm);
      this.setZ(this.getZ() / (float)norm);
   }

   public double distanceFrom(Vertex to) {
      return Math.sqrt((double)(this.getX() * to.getX() + this.getY() + to.getY() + this.getZ() * to.getZ()));
   }

   public Vertex rotateZ(double angle) {
      float savedX = this.getX();
      this.x = (float)((double)this.x * Math.cos(angle) + (double)this.y * Math.sin(angle));
      this.y = (float)((double)savedX * -Math.sin(angle) + (double)this.y * Math.cos(angle));
      return this;
   }

   public Vertex rotateX(double angle) {
      float savedY = this.y;
      this.y = (float)((double)this.y * Math.cos(angle) + (double)this.z * -Math.sin(angle));
      this.z = (float)((double)savedY * Math.sin(angle) + (double)this.z * Math.cos(angle));
      return this;
   }

   public Vertex copyAndRotateZ(float angle) {
      float newX = (float)((double)this.x * Math.cos((double)angle) + (double)this.y * Math.sin((double)angle));
      float newY = (float)((double)this.x * -Math.sin((double)angle) + (double)this.y * Math.cos((double)angle));
      return new Vertex(newX, newY, this.z);
   }

   public void add(Vertex offSet) {
      this.x += offSet.getX();
      this.y += offSet.getY();
      this.z += offSet.getZ();
   }

   public Vertex copyAndAdd(Vertex offSet) {
      return new Vertex(this.getX() + offSet.getX(), this.getY() + offSet.getY(), this.getZ() + offSet.getZ());
   }

   public Vertex mult(Vertex offSet) {
      return new Vertex(this.getX() * offSet.getX(), this.getY() * offSet.getY(), this.getZ() * offSet.getZ());
   }

   public Vertex mult(double factor) {
      return this.mult((float)factor);
   }

   public Vertex mult(float factor) {
      return new Vertex(this.getX() * factor, this.getY() * factor, this.getZ() * factor);
   }

   public Vertex copyAndSub(Vertex v) {
      return new Vertex(this.getX() - v.getX(), this.getY() - v.getY(), this.getZ() - v.getZ());
   }

   public Vertex copyAndMult(float coef) {
      return new Vertex(this.getX() * coef, this.getY() * coef, this.getZ() * coef);
   }

   public float dot(Vertex v) {
      return v.x * this.x + v.y * this.y;
   }

   public float perpDot(Vertex v) {
      return this.x * v.y - this.y * v.x;
   }

   public void subFrom(Vertex position) {
      this.setX(position.getX() - this.getX());
      this.setY(position.getY() - this.getY());
      this.setZ(position.getZ() - this.getZ());
   }

   public String toString() {
      return "x=" + this.x + ",y=" + this.y + ",z=" + this.z;
   }
}
