package info.pixelmon.repack.ninja.leaping.configurate.transformation;

import com.google.common.collect.Iterators;
import info.pixelmon.repack.ninja.leaping.configurate.ConfigurationNode;
import java.util.Arrays;
import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;

public abstract class ConfigurationTransformation {
   public static final Object WILDCARD_OBJECT = new Object();

   public abstract void apply(ConfigurationNode var1);

   public static Builder builder() {
      return new Builder();
   }

   public static VersionedBuilder versionedBuilder() {
      return new VersionedBuilder();
   }

   public static ConfigurationTransformation chain(ConfigurationTransformation... transformations) {
      return new ChainedConfigurationTransformation(transformations);
   }

   public static final class VersionedBuilder {
      private Object[] versionKey = new Object[]{"version"};
      private final SortedMap versions = new TreeMap();

      protected VersionedBuilder() {
      }

      public VersionedBuilder setVersionKey(Object... versionKey) {
         this.versionKey = Arrays.copyOf(versionKey, versionKey.length, Object[].class);
         return this;
      }

      public VersionedBuilder addVersion(int version, ConfigurationTransformation transformation) {
         this.versions.put(version, transformation);
         return this;
      }

      public ConfigurationTransformation build() {
         return new VersionedTransformation(this.versionKey, this.versions);
      }
   }

   public static final class Builder {
      private MoveStrategy strategy;
      private final SortedMap actions;

      protected Builder() {
         this.strategy = MoveStrategy.OVERWRITE;
         this.actions = new TreeMap(new NodePathComparator());
      }

      public Builder addAction(Object[] path, TransformAction action) {
         this.actions.put(path, action);
         return this;
      }

      public MoveStrategy getMoveStrategy() {
         return this.strategy;
      }

      public Builder setMoveStrategy(MoveStrategy strategy) {
         this.strategy = strategy;
         return this;
      }

      public ConfigurationTransformation build() {
         return new SingleConfigurationTransformation(this.actions, this.strategy);
      }
   }

   public static class NodePath implements Iterable {
      Object[] arr;

      NodePath() {
      }

      public Object get(int i) {
         return this.arr[i];
      }

      public int size() {
         return this.arr.length;
      }

      public Object[] getArray() {
         return Arrays.copyOf(this.arr, this.arr.length);
      }

      public Iterator iterator() {
         return Iterators.forArray(this.arr);
      }
   }
}
