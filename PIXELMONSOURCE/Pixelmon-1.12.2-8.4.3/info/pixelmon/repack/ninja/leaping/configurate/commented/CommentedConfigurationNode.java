package info.pixelmon.repack.ninja.leaping.configurate.commented;

import info.pixelmon.repack.ninja.leaping.configurate.ConfigurationNode;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CommentedConfigurationNode extends ConfigurationNode {
   Optional getComment();

   CommentedConfigurationNode setComment(String var1);

   CommentedConfigurationNode getParent();

   List getChildrenList();

   Map getChildrenMap();

   CommentedConfigurationNode setValue(Object var1);

   CommentedConfigurationNode mergeValuesFrom(ConfigurationNode var1);

   CommentedConfigurationNode getAppendedNode();

   CommentedConfigurationNode getNode(Object... var1);
}
