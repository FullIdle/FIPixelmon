package info.pixelmon.repack.com.typesafe.config.impl;

import java.util.HashMap;
import java.util.Map;

final class ResolveMemos {
   private final Map memos;

   private ResolveMemos(Map memos) {
      this.memos = memos;
   }

   ResolveMemos() {
      this(new HashMap());
   }

   AbstractConfigValue get(MemoKey key) {
      return (AbstractConfigValue)this.memos.get(key);
   }

   ResolveMemos put(MemoKey key, AbstractConfigValue value) {
      Map copy = new HashMap(this.memos);
      copy.put(key, value);
      return new ResolveMemos(copy);
   }
}
