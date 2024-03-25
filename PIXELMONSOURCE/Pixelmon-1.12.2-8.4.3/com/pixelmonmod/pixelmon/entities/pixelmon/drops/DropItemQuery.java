package com.pixelmonmod.pixelmon.entities.pixelmon.drops;

import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.util.math.Vec3d;

public class DropItemQuery {
   public Vec3d position;
   public UUID playerUUID;
   public ArrayList droppedItemList;

   public DropItemQuery(Vec3d position, UUID playerUUID, ArrayList droppedItemList) {
      this.position = position;
      this.playerUUID = playerUUID;
      this.droppedItemList = droppedItemList;
   }
}
