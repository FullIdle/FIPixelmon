package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;
import info.pixelmon.repack.com.typesafe.config.ConfigList;
import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;
import info.pixelmon.repack.com.typesafe.config.ConfigRenderOptions;
import info.pixelmon.repack.com.typesafe.config.ConfigValue;
import info.pixelmon.repack.com.typesafe.config.ConfigValueType;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

final class SimpleConfigList extends AbstractConfigValue implements ConfigList, Container, Serializable {
   private static final long serialVersionUID = 2L;
   private final List value;
   private final boolean resolved;

   SimpleConfigList(ConfigOrigin origin, List value) {
      this(origin, value, ResolveStatus.fromValues(value));
   }

   SimpleConfigList(ConfigOrigin origin, List value, ResolveStatus status) {
      super(origin);
      this.value = value;
      this.resolved = status == ResolveStatus.RESOLVED;
      if (status != ResolveStatus.fromValues(value)) {
         throw new ConfigException.BugOrBroken("SimpleConfigList created with wrong resolve status: " + this);
      }
   }

   public ConfigValueType valueType() {
      return ConfigValueType.LIST;
   }

   public List unwrapped() {
      List list = new ArrayList();
      Iterator var2 = this.value.iterator();

      while(var2.hasNext()) {
         AbstractConfigValue v = (AbstractConfigValue)var2.next();
         list.add(v.unwrapped());
      }

      return list;
   }

   ResolveStatus resolveStatus() {
      return ResolveStatus.fromBoolean(this.resolved);
   }

   public SimpleConfigList replaceChild(AbstractConfigValue child, AbstractConfigValue replacement) {
      List newList = replaceChildInList(this.value, child, replacement);
      return newList == null ? null : new SimpleConfigList(this.origin(), newList);
   }

   public boolean hasDescendant(AbstractConfigValue descendant) {
      return hasDescendantInList(this.value, descendant);
   }

   private SimpleConfigList modify(AbstractConfigValue.NoExceptionsModifier modifier, ResolveStatus newResolveStatus) {
      try {
         return this.modifyMayThrow(modifier, newResolveStatus);
      } catch (RuntimeException var4) {
         throw var4;
      } catch (Exception var5) {
         throw new ConfigException.BugOrBroken("unexpected checked exception", var5);
      }
   }

   private SimpleConfigList modifyMayThrow(AbstractConfigValue.Modifier modifier, ResolveStatus newResolveStatus) throws Exception {
      List changed = null;
      int i = 0;

      for(Iterator var5 = this.value.iterator(); var5.hasNext(); ++i) {
         AbstractConfigValue v = (AbstractConfigValue)var5.next();
         AbstractConfigValue modified = modifier.modifyChildMayThrow((String)null, v);
         if (changed == null && modified != v) {
            changed = new ArrayList();

            for(int j = 0; j < i; ++j) {
               changed.add(this.value.get(j));
            }
         }

         if (changed != null && modified != null) {
            changed.add(modified);
         }
      }

      if (changed != null) {
         if (newResolveStatus != null) {
            return new SimpleConfigList(this.origin(), changed, newResolveStatus);
         } else {
            return new SimpleConfigList(this.origin(), changed);
         }
      } else {
         return this;
      }
   }

   ResolveResult resolveSubstitutions(ResolveContext context, ResolveSource source) throws AbstractConfigValue.NotPossibleToResolve {
      if (this.resolved) {
         return ResolveResult.make(context, this);
      } else if (context.isRestrictedToChild()) {
         return ResolveResult.make(context, this);
      } else {
         try {
            ResolveModifier modifier = new ResolveModifier(context, source.pushParent(this));
            SimpleConfigList value = this.modifyMayThrow(modifier, context.options().getAllowUnresolved() ? null : ResolveStatus.RESOLVED);
            return ResolveResult.make(modifier.context, value);
         } catch (AbstractConfigValue.NotPossibleToResolve var5) {
            throw var5;
         } catch (RuntimeException var6) {
            throw var6;
         } catch (Exception var7) {
            throw new ConfigException.BugOrBroken("unexpected checked exception", var7);
         }
      }
   }

   SimpleConfigList relativized(final Path prefix) {
      return this.modify(new AbstractConfigValue.NoExceptionsModifier() {
         public AbstractConfigValue modifyChild(String key, AbstractConfigValue v) {
            return v.relativized(prefix);
         }
      }, this.resolveStatus());
   }

   protected boolean canEqual(Object other) {
      return other instanceof SimpleConfigList;
   }

   public boolean equals(Object other) {
      if (!(other instanceof SimpleConfigList)) {
         return false;
      } else {
         return this.canEqual(other) && (this.value == ((SimpleConfigList)other).value || this.value.equals(((SimpleConfigList)other).value));
      }
   }

   public int hashCode() {
      return this.value.hashCode();
   }

   protected void render(StringBuilder sb, int indent, boolean atRoot, ConfigRenderOptions options) {
      if (this.value.isEmpty()) {
         sb.append("[]");
      } else {
         sb.append("[");
         if (options.getFormatted()) {
            sb.append('\n');
         }

         Iterator var5 = this.value.iterator();

         while(var5.hasNext()) {
            AbstractConfigValue v = (AbstractConfigValue)var5.next();
            if (options.getOriginComments()) {
               String[] lines = v.origin().description().split("\n");
               String[] var8 = lines;
               int var9 = lines.length;

               for(int var10 = 0; var10 < var9; ++var10) {
                  String l = var8[var10];
                  indent(sb, indent + 1, options);
                  sb.append('#');
                  if (!l.isEmpty()) {
                     sb.append(' ');
                  }

                  sb.append(l);
                  sb.append("\n");
               }
            }

            if (options.getComments()) {
               Iterator var12 = v.origin().comments().iterator();

               while(var12.hasNext()) {
                  String comment = (String)var12.next();
                  indent(sb, indent + 1, options);
                  sb.append("# ");
                  sb.append(comment);
                  sb.append("\n");
               }
            }

            indent(sb, indent + 1, options);
            v.render(sb, indent + 1, atRoot, options);
            sb.append(",");
            if (options.getFormatted()) {
               sb.append('\n');
            }
         }

         sb.setLength(sb.length() - 1);
         if (options.getFormatted()) {
            sb.setLength(sb.length() - 1);
            sb.append('\n');
            indent(sb, indent, options);
         }

         sb.append("]");
      }

   }

   public boolean contains(Object o) {
      return this.value.contains(o);
   }

   public boolean containsAll(Collection c) {
      return this.value.containsAll(c);
   }

   public AbstractConfigValue get(int index) {
      return (AbstractConfigValue)this.value.get(index);
   }

   public int indexOf(Object o) {
      return this.value.indexOf(o);
   }

   public boolean isEmpty() {
      return this.value.isEmpty();
   }

   public Iterator iterator() {
      final Iterator i = this.value.iterator();
      return new Iterator() {
         public boolean hasNext() {
            return i.hasNext();
         }

         public ConfigValue next() {
            return (ConfigValue)i.next();
         }

         public void remove() {
            throw SimpleConfigList.weAreImmutable("iterator().remove");
         }
      };
   }

   public int lastIndexOf(Object o) {
      return this.value.lastIndexOf(o);
   }

   private static ListIterator wrapListIterator(final ListIterator i) {
      return new ListIterator() {
         public boolean hasNext() {
            return i.hasNext();
         }

         public ConfigValue next() {
            return (ConfigValue)i.next();
         }

         public void remove() {
            throw SimpleConfigList.weAreImmutable("listIterator().remove");
         }

         public void add(ConfigValue arg0) {
            throw SimpleConfigList.weAreImmutable("listIterator().add");
         }

         public boolean hasPrevious() {
            return i.hasPrevious();
         }

         public int nextIndex() {
            return i.nextIndex();
         }

         public ConfigValue previous() {
            return (ConfigValue)i.previous();
         }

         public int previousIndex() {
            return i.previousIndex();
         }

         public void set(ConfigValue arg0) {
            throw SimpleConfigList.weAreImmutable("listIterator().set");
         }
      };
   }

   public ListIterator listIterator() {
      return wrapListIterator(this.value.listIterator());
   }

   public ListIterator listIterator(int index) {
      return wrapListIterator(this.value.listIterator(index));
   }

   public int size() {
      return this.value.size();
   }

   public List subList(int fromIndex, int toIndex) {
      List list = new ArrayList();
      Iterator var4 = this.value.subList(fromIndex, toIndex).iterator();

      while(var4.hasNext()) {
         AbstractConfigValue v = (AbstractConfigValue)var4.next();
         list.add(v);
      }

      return list;
   }

   public Object[] toArray() {
      return this.value.toArray();
   }

   public Object[] toArray(Object[] a) {
      return this.value.toArray(a);
   }

   private static UnsupportedOperationException weAreImmutable(String method) {
      return new UnsupportedOperationException("ConfigList is immutable, you can't call List.'" + method + "'");
   }

   public boolean add(ConfigValue e) {
      throw weAreImmutable("add");
   }

   public void add(int index, ConfigValue element) {
      throw weAreImmutable("add");
   }

   public boolean addAll(Collection c) {
      throw weAreImmutable("addAll");
   }

   public boolean addAll(int index, Collection c) {
      throw weAreImmutable("addAll");
   }

   public void clear() {
      throw weAreImmutable("clear");
   }

   public boolean remove(Object o) {
      throw weAreImmutable("remove");
   }

   public ConfigValue remove(int index) {
      throw weAreImmutable("remove");
   }

   public boolean removeAll(Collection c) {
      throw weAreImmutable("removeAll");
   }

   public boolean retainAll(Collection c) {
      throw weAreImmutable("retainAll");
   }

   public ConfigValue set(int index, ConfigValue element) {
      throw weAreImmutable("set");
   }

   protected SimpleConfigList newCopy(ConfigOrigin newOrigin) {
      return new SimpleConfigList(newOrigin, this.value);
   }

   final SimpleConfigList concatenate(SimpleConfigList other) {
      ConfigOrigin combinedOrigin = SimpleConfigOrigin.mergeOrigins(this.origin(), other.origin());
      List combined = new ArrayList(this.value.size() + other.value.size());
      combined.addAll(this.value);
      combined.addAll(other.value);
      return new SimpleConfigList(combinedOrigin, combined);
   }

   private Object writeReplace() throws ObjectStreamException {
      return new SerializedConfigValue(this);
   }

   public SimpleConfigList withOrigin(ConfigOrigin origin) {
      return (SimpleConfigList)super.withOrigin(origin);
   }

   private static class ResolveModifier implements AbstractConfigValue.Modifier {
      ResolveContext context;
      final ResolveSource source;

      ResolveModifier(ResolveContext context, ResolveSource source) {
         this.context = context;
         this.source = source;
      }

      public AbstractConfigValue modifyChildMayThrow(String key, AbstractConfigValue v) throws AbstractConfigValue.NotPossibleToResolve {
         ResolveResult result = this.context.resolve(v, this.source);
         this.context = result.context;
         return result.value;
      }
   }
}
