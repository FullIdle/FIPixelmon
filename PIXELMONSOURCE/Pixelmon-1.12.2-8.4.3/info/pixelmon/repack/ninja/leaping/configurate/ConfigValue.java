package info.pixelmon.repack.ninja.leaping.configurate;

import java.util.Iterator;

abstract class ConfigValue {
   protected final SimpleConfigurationNode holder;

   protected ConfigValue(SimpleConfigurationNode holder) {
      this.holder = holder;
   }

   abstract Object getValue();

   abstract void setValue(Object var1);

   abstract SimpleConfigurationNode putChild(Object var1, SimpleConfigurationNode var2);

   abstract SimpleConfigurationNode putChildIfAbsent(Object var1, SimpleConfigurationNode var2);

   abstract SimpleConfigurationNode getChild(Object var1);

   abstract Iterable iterateChildren();

   void clear() {
      Iterator it = this.iterateChildren().iterator();

      while(it.hasNext()) {
         SimpleConfigurationNode node = (SimpleConfigurationNode)it.next();
         node.attached = false;
         it.remove();
         if (node.getParentAttached().equals(this.holder)) {
            node.clear();
         }
      }

   }
}
