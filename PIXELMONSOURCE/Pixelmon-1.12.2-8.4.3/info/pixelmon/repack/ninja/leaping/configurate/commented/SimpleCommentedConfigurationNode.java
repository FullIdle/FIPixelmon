package info.pixelmon.repack.ninja.leaping.configurate.commented;

import info.pixelmon.repack.ninja.leaping.configurate.ConfigurationNode;
import info.pixelmon.repack.ninja.leaping.configurate.ConfigurationOptions;
import info.pixelmon.repack.ninja.leaping.configurate.SimpleConfigurationNode;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

public class SimpleCommentedConfigurationNode extends SimpleConfigurationNode implements CommentedConfigurationNode {
   private final AtomicReference comment = new AtomicReference();

   public static SimpleCommentedConfigurationNode root() {
      return root(ConfigurationOptions.defaults());
   }

   public static SimpleCommentedConfigurationNode root(ConfigurationOptions options) {
      return new SimpleCommentedConfigurationNode((Object)null, (SimpleConfigurationNode)null, options);
   }

   protected SimpleCommentedConfigurationNode(Object path, SimpleConfigurationNode parent, ConfigurationOptions options) {
      super(path, parent, options);
   }

   public Optional getComment() {
      return Optional.ofNullable(this.comment.get());
   }

   public SimpleCommentedConfigurationNode setComment(String comment) {
      this.attachIfNecessary();
      this.comment.set(comment);
      return this;
   }

   public SimpleCommentedConfigurationNode getParent() {
      return (SimpleCommentedConfigurationNode)super.getParent();
   }

   protected SimpleCommentedConfigurationNode createNode(Object path) {
      return new SimpleCommentedConfigurationNode(path, this, this.getOptions());
   }

   public SimpleCommentedConfigurationNode setValue(Object value) {
      if (value instanceof CommentedConfigurationNode && ((CommentedConfigurationNode)value).getComment().isPresent()) {
         this.setComment((String)((CommentedConfigurationNode)value).getComment().get());
      }

      return (SimpleCommentedConfigurationNode)super.setValue(value);
   }

   public SimpleCommentedConfigurationNode mergeValuesFrom(ConfigurationNode other) {
      if (other instanceof CommentedConfigurationNode) {
         Optional otherComment = ((CommentedConfigurationNode)other).getComment();
         if (otherComment.isPresent()) {
            this.comment.compareAndSet((Object)null, otherComment.get());
         }
      }

      return (SimpleCommentedConfigurationNode)super.mergeValuesFrom(other);
   }

   public SimpleCommentedConfigurationNode getNode(Object... path) {
      return (SimpleCommentedConfigurationNode)super.getNode(path);
   }

   public List getChildrenList() {
      return super.getChildrenList();
   }

   public Map getChildrenMap() {
      return super.getChildrenMap();
   }

   public SimpleCommentedConfigurationNode getAppendedNode() {
      return (SimpleCommentedConfigurationNode)super.getAppendedNode();
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (!(o instanceof SimpleCommentedConfigurationNode)) {
         return false;
      } else if (!super.equals(o)) {
         return false;
      } else {
         SimpleCommentedConfigurationNode that = (SimpleCommentedConfigurationNode)o;
         return this.comment.equals(that.comment);
      }
   }

   public int hashCode() {
      int result = super.hashCode();
      result = 31 * result + this.comment.hashCode();
      return result;
   }

   public String toString() {
      return "SimpleCommentedConfigurationNode{super=" + super.toString() + "comment=" + this.comment + '}';
   }
}
