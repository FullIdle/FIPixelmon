package com.pixelmonmod.tcg.duel.state;

import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.tcg.gui.duel.CardWithLocation;
import com.pixelmonmod.tcg.gui.enums.CardSelectorDisplay;
import io.netty.buffer.ByteBuf;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class CardSelectorState {
   private int id;
   private List cardList;
   private int minimumCount;
   private int maximumCount;
   private boolean countEnergy;
   private CardSelectorDisplay displayType;
   private boolean cancellable;
   private String customText;

   public CardSelectorState(int minimumCount, int maximumCount, CardSelectorDisplay displayType, boolean cancellable) {
      this(RandomHelper.rand.nextInt(10000), minimumCount, maximumCount, false, displayType, cancellable, (String)null, new ArrayList());
   }

   public CardSelectorState(int minimumCount, int maximumCount, CardSelectorDisplay displayType, boolean cancellable, String customText) {
      this(RandomHelper.rand.nextInt(10000), minimumCount, maximumCount, false, displayType, cancellable, customText, new ArrayList());
   }

   public CardSelectorState(int minimumCount, int maximumCount, boolean countEnergy, CardSelectorDisplay displayType, boolean cancellable) {
      this(RandomHelper.rand.nextInt(10000), minimumCount, maximumCount, countEnergy, displayType, cancellable, (String)null, new ArrayList());
   }

   public CardSelectorState(int id, int minimumCount, int maximumCount, boolean countEnergy, CardSelectorDisplay displayType, boolean cancellable, String customText, List cardList) {
      this.id = id;
      this.minimumCount = minimumCount;
      this.maximumCount = maximumCount;
      this.countEnergy = countEnergy;
      this.displayType = displayType;
      this.cancellable = cancellable;
      this.customText = customText;
      this.cardList = (List)(cardList == null ? new ArrayList() : cardList);
   }

   public CardSelectorState(ByteBuf buf) {
      this.id = buf.readInt();
      int count = buf.readInt();
      this.cardList = new ArrayList();

      for(int i = 0; i < count; ++i) {
         if (!buf.readBoolean()) {
            this.cardList.add(new CardWithLocation(buf));
         } else {
            this.cardList.add((Object)null);
         }
      }

      this.minimumCount = buf.readInt();
      this.maximumCount = buf.readInt();
      this.displayType = CardSelectorDisplay.values()[buf.readInt()];
      this.cancellable = buf.readBoolean();
      if (buf.readBoolean()) {
         this.customText = ByteBufUtils.readUTF8String(buf);
      }

   }

   public void write(ByteBuf buf) {
      buf.writeInt(this.id);
      buf.writeInt(this.cardList.size());
      Iterator var2 = this.cardList.iterator();

      while(var2.hasNext()) {
         CardWithLocation aCardList = (CardWithLocation)var2.next();
         if (aCardList == null) {
            buf.writeBoolean(true);
         } else {
            buf.writeBoolean(false);
            aCardList.write(buf);
         }
      }

      buf.writeInt(this.minimumCount);
      buf.writeInt(this.maximumCount);
      buf.writeInt(this.displayType.ordinal());
      buf.writeBoolean(this.cancellable);
      buf.writeBoolean(this.customText != null);
      if (this.customText != null) {
         ByteBufUtils.writeUTF8String(buf, this.customText);
      }

   }

   public int getId() {
      return this.id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public List getCardList() {
      return this.cardList;
   }

   public void setCardList(List cardList) {
      this.cardList = cardList;
   }

   public void addCard(CardWithLocation card) {
      this.cardList.add(card);
   }

   public int getMinimumCount() {
      return this.minimumCount;
   }

   public int getMaximumCount() {
      return this.maximumCount;
   }

   public boolean isCountEnergy() {
      return this.countEnergy;
   }

   public CardSelectorDisplay getDisplayType() {
      return this.displayType;
   }

   public void setDisplayType(CardSelectorDisplay displayType) {
      this.displayType = displayType;
   }

   public boolean isCancellable() {
      return this.cancellable;
   }

   public void setCancellable(boolean cancellable) {
      this.cancellable = cancellable;
   }

   public String getCustomText() {
      return this.customText;
   }

   public void setCustomText(String customText) {
      this.customText = customText;
   }
}
