package com.pixelmonmod.pixelmon.AI;

import java.util.Comparator;
import net.minecraft.entity.Entity;

public class TargetSorter implements Comparator {
   private Entity theEntity;
   final AITargetNearest targetNearest;

   public TargetSorter(AITargetNearest targetNearest, Entity entity) {
      this.targetNearest = targetNearest;
      this.theEntity = entity;
   }

   public int compare(Entity firstEntity, Entity secondEntity) {
      double distanceFirst = this.theEntity.func_70068_e(firstEntity);
      double distanceSecond = this.theEntity.func_70068_e(secondEntity);
      return distanceFirst < distanceSecond ? -1 : (distanceFirst > distanceSecond ? 1 : 0);
   }
}
