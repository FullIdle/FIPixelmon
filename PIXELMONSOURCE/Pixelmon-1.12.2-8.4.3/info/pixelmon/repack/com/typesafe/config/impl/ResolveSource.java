package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;

final class ResolveSource {
   final AbstractConfigObject root;
   final Node pathFromRoot;

   ResolveSource(AbstractConfigObject root, Node pathFromRoot) {
      this.root = root;
      this.pathFromRoot = pathFromRoot;
   }

   ResolveSource(AbstractConfigObject root) {
      this.root = root;
      this.pathFromRoot = null;
   }

   private AbstractConfigObject rootMustBeObj(Container value) {
      return (AbstractConfigObject)(value instanceof AbstractConfigObject ? (AbstractConfigObject)value : SimpleConfigObject.empty());
   }

   private static ResultWithPath findInObject(AbstractConfigObject obj, ResolveContext context, Path path) throws AbstractConfigValue.NotPossibleToResolve {
      if (ConfigImpl.traceSubstitutionsEnabled()) {
         ConfigImpl.trace("*** finding '" + path + "' in " + obj);
      }

      Path restriction = context.restrictToChild();
      ResolveResult partiallyResolved = context.restrict(path).resolve(obj, new ResolveSource(obj));
      ResolveContext newContext = partiallyResolved.context.restrict(restriction);
      if (partiallyResolved.value instanceof AbstractConfigObject) {
         ValueWithPath pair = findInObject((AbstractConfigObject)partiallyResolved.value, path);
         return new ResultWithPath(ResolveResult.make(newContext, pair.value), pair.pathFromRoot);
      } else {
         throw new ConfigException.BugOrBroken("resolved object to non-object " + obj + " to " + partiallyResolved);
      }
   }

   private static ValueWithPath findInObject(AbstractConfigObject obj, Path path) {
      try {
         return findInObject(obj, (Path)path, (Node)null);
      } catch (ConfigException.NotResolved var3) {
         throw ConfigImpl.improveNotResolved(path, var3);
      }
   }

   private static ValueWithPath findInObject(AbstractConfigObject obj, Path path, Node parents) {
      String key = path.first();
      Path next = path.remainder();
      if (ConfigImpl.traceSubstitutionsEnabled()) {
         ConfigImpl.trace("*** looking up '" + key + "' in " + obj);
      }

      AbstractConfigValue v = obj.attemptPeekWithPartialResolve(key);
      Node newParents = parents == null ? new Node(obj) : parents.prepend(obj);
      if (next == null) {
         return new ValueWithPath(v, newParents);
      } else {
         return v instanceof AbstractConfigObject ? findInObject((AbstractConfigObject)v, next, newParents) : new ValueWithPath((AbstractConfigValue)null, newParents);
      }
   }

   ResultWithPath lookupSubst(ResolveContext context, SubstitutionExpression subst, int prefixLength) throws AbstractConfigValue.NotPossibleToResolve {
      if (ConfigImpl.traceSubstitutionsEnabled()) {
         ConfigImpl.trace(context.depth(), "searching for " + subst);
      }

      if (ConfigImpl.traceSubstitutionsEnabled()) {
         ConfigImpl.trace(context.depth(), subst + " - looking up relative to file it occurred in");
      }

      ResultWithPath result = findInObject(this.root, context, subst.path());
      if (result.result.value == null) {
         Path unprefixed = subst.path().subPath(prefixLength);
         if (prefixLength > 0) {
            if (ConfigImpl.traceSubstitutionsEnabled()) {
               ConfigImpl.trace(result.result.context.depth(), unprefixed + " - looking up relative to parent file");
            }

            result = findInObject(this.root, result.result.context, unprefixed);
         }

         if (result.result.value == null && result.result.context.options().getUseSystemEnvironment()) {
            if (ConfigImpl.traceSubstitutionsEnabled()) {
               ConfigImpl.trace(result.result.context.depth(), unprefixed + " - looking up in system environment");
            }

            result = findInObject(ConfigImpl.envVariablesAsConfigObject(), context, unprefixed);
         }
      }

      if (ConfigImpl.traceSubstitutionsEnabled()) {
         ConfigImpl.trace(result.result.context.depth(), "resolved to " + result);
      }

      return result;
   }

   ResolveSource pushParent(Container parent) {
      if (parent == null) {
         throw new ConfigException.BugOrBroken("can't push null parent");
      } else {
         if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace("pushing parent " + parent + " ==root " + (parent == this.root) + " onto " + this);
         }

         if (this.pathFromRoot == null) {
            if (parent == this.root) {
               return new ResolveSource(this.root, new Node(parent));
            } else {
               if (ConfigImpl.traceSubstitutionsEnabled() && this.root.hasDescendant((AbstractConfigValue)parent)) {
                  ConfigImpl.trace("***** BUG ***** tried to push parent " + parent + " without having a path to it in " + this);
               }

               return this;
            }
         } else {
            Container parentParent = (Container)this.pathFromRoot.head();
            if (ConfigImpl.traceSubstitutionsEnabled() && parentParent != null && !parentParent.hasDescendant((AbstractConfigValue)parent)) {
               ConfigImpl.trace("***** BUG ***** trying to push non-child of " + parentParent + ", non-child was " + parent);
            }

            return new ResolveSource(this.root, this.pathFromRoot.prepend(parent));
         }
      }
   }

   ResolveSource resetParents() {
      return this.pathFromRoot == null ? this : new ResolveSource(this.root);
   }

   private static Node replace(Node list, Container old, AbstractConfigValue replacement) {
      Container child = (Container)list.head();
      if (child != old) {
         throw new ConfigException.BugOrBroken("Can only replace() the top node we're resolving; had " + child + " on top and tried to replace " + old + " overall list was " + list);
      } else {
         Container parent = list.tail() == null ? null : (Container)list.tail().head();
         AbstractConfigValue newParent;
         if (replacement != null && replacement instanceof Container) {
            if (parent == null) {
               return new Node((Container)replacement);
            } else {
               newParent = parent.replaceChild((AbstractConfigValue)old, replacement);
               Node newTail = replace(list.tail(), parent, newParent);
               return newTail != null ? newTail.prepend((Container)replacement) : new Node((Container)replacement);
            }
         } else if (parent == null) {
            return null;
         } else {
            newParent = parent.replaceChild((AbstractConfigValue)old, (AbstractConfigValue)null);
            return replace(list.tail(), parent, newParent);
         }
      }
   }

   ResolveSource replaceCurrentParent(Container old, Container replacement) {
      if (ConfigImpl.traceSubstitutionsEnabled()) {
         ConfigImpl.trace("replaceCurrentParent old " + old + "@" + System.identityHashCode(old) + " replacement " + replacement + "@" + System.identityHashCode(old) + " in " + this);
      }

      if (old == replacement) {
         return this;
      } else if (this.pathFromRoot != null) {
         Node newPath = replace(this.pathFromRoot, old, (AbstractConfigValue)replacement);
         if (ConfigImpl.traceSubstitutionsEnabled()) {
            ConfigImpl.trace("replaced " + old + " with " + replacement + " in " + this);
            ConfigImpl.trace("path was: " + this.pathFromRoot + " is now " + newPath);
         }

         return newPath != null ? new ResolveSource((AbstractConfigObject)newPath.last(), newPath) : new ResolveSource(SimpleConfigObject.empty());
      } else if (old == this.root) {
         return new ResolveSource(this.rootMustBeObj(replacement));
      } else {
         throw new ConfigException.BugOrBroken("attempt to replace root " + this.root + " with " + replacement);
      }
   }

   ResolveSource replaceWithinCurrentParent(AbstractConfigValue old, AbstractConfigValue replacement) {
      if (ConfigImpl.traceSubstitutionsEnabled()) {
         ConfigImpl.trace("replaceWithinCurrentParent old " + old + "@" + System.identityHashCode(old) + " replacement " + replacement + "@" + System.identityHashCode(old) + " in " + this);
      }

      if (old == replacement) {
         return this;
      } else if (this.pathFromRoot != null) {
         Container parent = (Container)this.pathFromRoot.head();
         AbstractConfigValue newParent = parent.replaceChild(old, replacement);
         return this.replaceCurrentParent(parent, newParent instanceof Container ? (Container)newParent : null);
      } else if (old == this.root && replacement instanceof Container) {
         return new ResolveSource(this.rootMustBeObj((Container)replacement));
      } else {
         throw new ConfigException.BugOrBroken("replace in parent not possible " + old + " with " + replacement + " in " + this);
      }
   }

   public String toString() {
      return "ResolveSource(root=" + this.root + ", pathFromRoot=" + this.pathFromRoot + ")";
   }

   static final class ResultWithPath {
      final ResolveResult result;
      final Node pathFromRoot;

      ResultWithPath(ResolveResult result, Node pathFromRoot) {
         this.result = result;
         this.pathFromRoot = pathFromRoot;
      }

      public String toString() {
         return "ResultWithPath(result=" + this.result + ", pathFromRoot=" + this.pathFromRoot + ")";
      }
   }

   static final class ValueWithPath {
      final AbstractConfigValue value;
      final Node pathFromRoot;

      ValueWithPath(AbstractConfigValue value, Node pathFromRoot) {
         this.value = value;
         this.pathFromRoot = pathFromRoot;
      }

      public String toString() {
         return "ValueWithPath(value=" + this.value + ", pathFromRoot=" + this.pathFromRoot + ")";
      }
   }

   static final class Node {
      final Object value;
      final Node next;

      Node(Object value, Node next) {
         this.value = value;
         this.next = next;
      }

      Node(Object value) {
         this(value, (Node)null);
      }

      Node prepend(Object value) {
         return new Node(value, this);
      }

      Object head() {
         return this.value;
      }

      Node tail() {
         return this.next;
      }

      Object last() {
         Node i;
         for(i = this; i.next != null; i = i.next) {
         }

         return i.value;
      }

      Node reverse() {
         if (this.next == null) {
            return this;
         } else {
            Node reversed = new Node(this.value);

            for(Node i = this.next; i != null; i = i.next) {
               reversed = reversed.prepend(i.value);
            }

            return reversed;
         }
      }

      public String toString() {
         StringBuffer sb = new StringBuffer();
         sb.append("[");

         for(Node toAppendValue = this.reverse(); toAppendValue != null; toAppendValue = toAppendValue.next) {
            sb.append(toAppendValue.value.toString());
            if (toAppendValue.next != null) {
               sb.append(" <= ");
            }
         }

         sb.append("]");
         return sb.toString();
      }
   }
}
