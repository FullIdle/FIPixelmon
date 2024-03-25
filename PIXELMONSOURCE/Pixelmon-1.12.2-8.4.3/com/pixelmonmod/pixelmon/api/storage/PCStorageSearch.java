package com.pixelmonmod.pixelmon.api.storage;

import com.google.common.base.Preconditions;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.client.storage.ClientStorageManager;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.newStorage.pc.ServerQueryPC;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public class PCStorageSearch extends PCStorage {
   private final int area;
   public int searchId = -1;

   public PCStorageSearch(int width, int height) {
      super(ClientStorageManager.openPC.uuid, 1);
      Preconditions.checkArgument(width > 0, "Width must be higher than 0");
      Preconditions.checkArgument(height > 0, "Height must be higher than 0");
      this.area = width * height;
      this.query("");
   }

   public void query(String query) {
      query = query.replaceAll(" +", " ").toLowerCase();
      if (StringUtils.isBlank(query)) {
         this.rearrangeBoxes((List)null);
      } else {
         this.rearrangeBoxes(ServerQueryPC.search(ClientStorageManager.openPC, query));
         Pixelmon.network.sendToServer(new ServerQueryPC(++this.searchId, query));
      }

   }

   public void rearrangeBoxes(@Nullable List pokemon) {
      if (pokemon != null && !pokemon.isEmpty()) {
         this.boxes = new PCBox[1 + pokemon.size() / this.area];

         for(int i = 0; i < this.boxes.length; ++i) {
            this.boxes[i] = new PCBoxSearch(this, i);
            int box = i * this.area;

            for(int j = 0; j < this.area && box + j < pokemon.size(); ++j) {
               this.boxes[i].set(j, (Pokemon)pokemon.get(box + j));
            }
         }
      } else {
         this.boxes = new PCBox[]{new PCBoxSearch(this, 0)};
      }

   }

   public int getLastBox() {
      return 0;
   }

   public boolean canTransfer(StoragePosition from, StoragePosition to) {
      return super.canTransfer(from, to) && (this.isBoxFullAndPartyEmpty(from, to) || this.isBoxFullAndPartyEmpty(to, from));
   }

   private boolean isBoxFullAndPartyEmpty(StoragePosition first, StoragePosition second) {
      return first.box >= 0 && first.box < this.getBoxCount() && first.order >= 0 && first.order < this.area && this.get(first) != null && second.box == -1 && second.order >= 0 && second.order <= 6 && ClientStorageManager.party.get(second) == null;
   }
}
