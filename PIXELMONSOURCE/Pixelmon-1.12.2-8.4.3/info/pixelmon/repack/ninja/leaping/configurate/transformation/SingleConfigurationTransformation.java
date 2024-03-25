package info.pixelmon.repack.ninja.leaping.configurate.transformation;

import info.pixelmon.repack.ninja.leaping.configurate.ConfigurationNode;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

class SingleConfigurationTransformation extends ConfigurationTransformation {
   private final MoveStrategy strategy;
   private final Map actions;
   private final ThreadLocal sharedPath = new ThreadLocal() {
      protected ConfigurationTransformation.NodePath initialValue() {
         return new ConfigurationTransformation.NodePath();
      }
   };

   protected SingleConfigurationTransformation(Map actions, MoveStrategy strategy) {
      this.actions = actions;
      this.strategy = strategy;
   }

   public static ConfigurationTransformation.Builder builder() {
      return new ConfigurationTransformation.Builder();
   }

   public void apply(ConfigurationNode node) {
      Iterator var2 = this.actions.entrySet().iterator();

      while(var2.hasNext()) {
         Map.Entry ent = (Map.Entry)var2.next();
         this.applySingleAction(node, (Object[])ent.getKey(), 0, node, (TransformAction)ent.getValue());
      }

   }

   protected void applySingleAction(ConfigurationNode start, Object[] path, int startIdx, ConfigurationNode node, TransformAction action) {
      for(int i = startIdx; i < path.length; ++i) {
         if (path[i] == WILDCARD_OBJECT) {
            if (node.hasListChildren()) {
               List children = node.getChildrenList();

               for(int cI = 0; cI < children.size(); ++cI) {
                  path[i] = cI;
                  this.applySingleAction(start, path, i + 1, (ConfigurationNode)children.get(cI), action);
               }

               path[i] = WILDCARD_OBJECT;
            } else {
               if (!node.hasMapChildren()) {
                  return;
               }

               Iterator var10 = node.getChildrenMap().entrySet().iterator();

               while(var10.hasNext()) {
                  Map.Entry ent = (Map.Entry)var10.next();
                  path[i] = ent.getKey();
                  this.applySingleAction(start, path, i + 1, (ConfigurationNode)ent.getValue(), action);
               }

               path[i] = WILDCARD_OBJECT;
            }

            return;
         }

         node = node.getNode(path[i]);
         if (node.isVirtual()) {
            return;
         }
      }

      ConfigurationTransformation.NodePath immutablePath = (ConfigurationTransformation.NodePath)this.sharedPath.get();
      immutablePath.arr = path;
      Object[] transformedPath = action.visitPath(immutablePath, node);
      if (transformedPath != null && !Arrays.equals(path, transformedPath)) {
         this.strategy.move(node, start.getNode(transformedPath));
         node.setValue((Object)null);
      }

   }
}
