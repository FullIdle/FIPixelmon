package info.pixelmon.repack.com.typesafe.config.impl;

import info.pixelmon.repack.com.typesafe.config.ConfigException;
import info.pixelmon.repack.com.typesafe.config.ConfigOrigin;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

final class SimpleConfigOrigin implements ConfigOrigin {
   private final String description;
   private final int lineNumber;
   private final int endLineNumber;
   private final OriginType originType;
   private final String urlOrNull;
   private final String resourceOrNull;
   private final List commentsOrNull;
   static final String MERGE_OF_PREFIX = "merge of ";

   protected SimpleConfigOrigin(String description, int lineNumber, int endLineNumber, OriginType originType, String urlOrNull, String resourceOrNull, List commentsOrNull) {
      if (description == null) {
         throw new ConfigException.BugOrBroken("description may not be null");
      } else {
         this.description = description;
         this.lineNumber = lineNumber;
         this.endLineNumber = endLineNumber;
         this.originType = originType;
         this.urlOrNull = urlOrNull;
         this.resourceOrNull = resourceOrNull;
         this.commentsOrNull = commentsOrNull;
      }
   }

   static SimpleConfigOrigin newSimple(String description) {
      return new SimpleConfigOrigin(description, -1, -1, OriginType.GENERIC, (String)null, (String)null, (List)null);
   }

   static SimpleConfigOrigin newFile(String filename) {
      String url;
      try {
         url = (new File(filename)).toURI().toURL().toExternalForm();
      } catch (MalformedURLException var3) {
         url = null;
      }

      return new SimpleConfigOrigin(filename, -1, -1, OriginType.FILE, url, (String)null, (List)null);
   }

   static SimpleConfigOrigin newURL(URL url) {
      String u = url.toExternalForm();
      return new SimpleConfigOrigin(u, -1, -1, OriginType.URL, u, (String)null, (List)null);
   }

   static SimpleConfigOrigin newResource(String resource, URL url) {
      String desc;
      if (url != null) {
         desc = resource + " @ " + url.toExternalForm();
      } else {
         desc = resource;
      }

      return new SimpleConfigOrigin(desc, -1, -1, OriginType.RESOURCE, url != null ? url.toExternalForm() : null, resource, (List)null);
   }

   static SimpleConfigOrigin newResource(String resource) {
      return newResource(resource, (URL)null);
   }

   public SimpleConfigOrigin withLineNumber(int lineNumber) {
      return lineNumber == this.lineNumber && lineNumber == this.endLineNumber ? this : new SimpleConfigOrigin(this.description, lineNumber, lineNumber, this.originType, this.urlOrNull, this.resourceOrNull, this.commentsOrNull);
   }

   SimpleConfigOrigin addURL(URL url) {
      return new SimpleConfigOrigin(this.description, this.lineNumber, this.endLineNumber, this.originType, url != null ? url.toExternalForm() : null, this.resourceOrNull, this.commentsOrNull);
   }

   public SimpleConfigOrigin withComments(List comments) {
      return ConfigImplUtil.equalsHandlingNull(comments, this.commentsOrNull) ? this : new SimpleConfigOrigin(this.description, this.lineNumber, this.endLineNumber, this.originType, this.urlOrNull, this.resourceOrNull, comments);
   }

   SimpleConfigOrigin prependComments(List comments) {
      if (!ConfigImplUtil.equalsHandlingNull(comments, this.commentsOrNull) && comments != null) {
         if (this.commentsOrNull == null) {
            return this.withComments(comments);
         } else {
            List merged = new ArrayList(comments.size() + this.commentsOrNull.size());
            merged.addAll(comments);
            merged.addAll(this.commentsOrNull);
            return this.withComments(merged);
         }
      } else {
         return this;
      }
   }

   SimpleConfigOrigin appendComments(List comments) {
      if (!ConfigImplUtil.equalsHandlingNull(comments, this.commentsOrNull) && comments != null) {
         if (this.commentsOrNull == null) {
            return this.withComments(comments);
         } else {
            List merged = new ArrayList(comments.size() + this.commentsOrNull.size());
            merged.addAll(this.commentsOrNull);
            merged.addAll(comments);
            return this.withComments(merged);
         }
      } else {
         return this;
      }
   }

   public String description() {
      if (this.lineNumber < 0) {
         return this.description;
      } else {
         return this.endLineNumber == this.lineNumber ? this.description + ": " + this.lineNumber : this.description + ": " + this.lineNumber + "-" + this.endLineNumber;
      }
   }

   public boolean equals(Object other) {
      if (!(other instanceof SimpleConfigOrigin)) {
         return false;
      } else {
         SimpleConfigOrigin otherOrigin = (SimpleConfigOrigin)other;
         return this.description.equals(otherOrigin.description) && this.lineNumber == otherOrigin.lineNumber && this.endLineNumber == otherOrigin.endLineNumber && this.originType == otherOrigin.originType && ConfigImplUtil.equalsHandlingNull(this.urlOrNull, otherOrigin.urlOrNull) && ConfigImplUtil.equalsHandlingNull(this.resourceOrNull, otherOrigin.resourceOrNull);
      }
   }

   public int hashCode() {
      int h = 41 * (41 + this.description.hashCode());
      h = 41 * (h + this.lineNumber);
      h = 41 * (h + this.endLineNumber);
      h = 41 * (h + this.originType.hashCode());
      if (this.urlOrNull != null) {
         h = 41 * (h + this.urlOrNull.hashCode());
      }

      if (this.resourceOrNull != null) {
         h = 41 * (h + this.resourceOrNull.hashCode());
      }

      return h;
   }

   public String toString() {
      return "ConfigOrigin(" + this.description + ")";
   }

   public String filename() {
      if (this.originType == OriginType.FILE) {
         return this.description;
      } else if (this.urlOrNull != null) {
         URL url;
         try {
            url = new URL(this.urlOrNull);
         } catch (MalformedURLException var3) {
            return null;
         }

         return url.getProtocol().equals("file") ? url.getFile() : null;
      } else {
         return null;
      }
   }

   public URL url() {
      if (this.urlOrNull == null) {
         return null;
      } else {
         try {
            return new URL(this.urlOrNull);
         } catch (MalformedURLException var2) {
            return null;
         }
      }
   }

   public String resource() {
      return this.resourceOrNull;
   }

   public int lineNumber() {
      return this.lineNumber;
   }

   public List comments() {
      return this.commentsOrNull != null ? Collections.unmodifiableList(this.commentsOrNull) : Collections.emptyList();
   }

   private static SimpleConfigOrigin mergeTwo(SimpleConfigOrigin a, SimpleConfigOrigin b) {
      OriginType mergedType;
      if (a.originType == b.originType) {
         mergedType = a.originType;
      } else {
         mergedType = OriginType.GENERIC;
      }

      String aDesc = a.description;
      String bDesc = b.description;
      if (aDesc.startsWith("merge of ")) {
         aDesc = aDesc.substring("merge of ".length());
      }

      if (bDesc.startsWith("merge of ")) {
         bDesc = bDesc.substring("merge of ".length());
      }

      String mergedDesc;
      int mergedStartLine;
      int mergedEndLine;
      String mergedURL;
      String mergedResource;
      if (aDesc.equals(bDesc)) {
         mergedDesc = aDesc;
         if (a.lineNumber < 0) {
            mergedStartLine = b.lineNumber;
         } else if (b.lineNumber < 0) {
            mergedStartLine = a.lineNumber;
         } else {
            mergedStartLine = Math.min(a.lineNumber, b.lineNumber);
         }

         mergedEndLine = Math.max(a.endLineNumber, b.endLineNumber);
      } else {
         mergedURL = a.description();
         mergedResource = b.description();
         if (mergedURL.startsWith("merge of ")) {
            mergedURL = mergedURL.substring("merge of ".length());
         }

         if (mergedResource.startsWith("merge of ")) {
            mergedResource = mergedResource.substring("merge of ".length());
         }

         mergedDesc = "merge of " + mergedURL + "," + mergedResource;
         mergedStartLine = -1;
         mergedEndLine = -1;
      }

      if (ConfigImplUtil.equalsHandlingNull(a.urlOrNull, b.urlOrNull)) {
         mergedURL = a.urlOrNull;
      } else {
         mergedURL = null;
      }

      if (ConfigImplUtil.equalsHandlingNull(a.resourceOrNull, b.resourceOrNull)) {
         mergedResource = a.resourceOrNull;
      } else {
         mergedResource = null;
      }

      Object mergedComments;
      if (ConfigImplUtil.equalsHandlingNull(a.commentsOrNull, b.commentsOrNull)) {
         mergedComments = a.commentsOrNull;
      } else {
         mergedComments = new ArrayList();
         if (a.commentsOrNull != null) {
            ((List)mergedComments).addAll(a.commentsOrNull);
         }

         if (b.commentsOrNull != null) {
            ((List)mergedComments).addAll(b.commentsOrNull);
         }
      }

      return new SimpleConfigOrigin(mergedDesc, mergedStartLine, mergedEndLine, mergedType, mergedURL, mergedResource, (List)mergedComments);
   }

   private static int similarity(SimpleConfigOrigin a, SimpleConfigOrigin b) {
      int count = 0;
      if (a.originType == b.originType) {
         ++count;
      }

      if (a.description.equals(b.description)) {
         ++count;
         if (a.lineNumber == b.lineNumber) {
            ++count;
         }

         if (a.endLineNumber == b.endLineNumber) {
            ++count;
         }

         if (ConfigImplUtil.equalsHandlingNull(a.urlOrNull, b.urlOrNull)) {
            ++count;
         }

         if (ConfigImplUtil.equalsHandlingNull(a.resourceOrNull, b.resourceOrNull)) {
            ++count;
         }
      }

      return count;
   }

   private static SimpleConfigOrigin mergeThree(SimpleConfigOrigin a, SimpleConfigOrigin b, SimpleConfigOrigin c) {
      return similarity(a, b) >= similarity(b, c) ? mergeTwo(mergeTwo(a, b), c) : mergeTwo(a, mergeTwo(b, c));
   }

   static ConfigOrigin mergeOrigins(ConfigOrigin a, ConfigOrigin b) {
      return mergeTwo((SimpleConfigOrigin)a, (SimpleConfigOrigin)b);
   }

   static ConfigOrigin mergeOrigins(List stack) {
      List origins = new ArrayList(stack.size());
      Iterator var2 = stack.iterator();

      while(var2.hasNext()) {
         AbstractConfigValue v = (AbstractConfigValue)var2.next();
         origins.add(v.origin());
      }

      return mergeOrigins((Collection)origins);
   }

   static ConfigOrigin mergeOrigins(Collection stack) {
      if (stack.isEmpty()) {
         throw new ConfigException.BugOrBroken("can't merge empty list of origins");
      } else if (stack.size() == 1) {
         return (ConfigOrigin)stack.iterator().next();
      } else if (stack.size() == 2) {
         Iterator i = stack.iterator();
         return mergeTwo((SimpleConfigOrigin)i.next(), (SimpleConfigOrigin)i.next());
      } else {
         List remaining = new ArrayList();
         Iterator var2 = stack.iterator();

         while(var2.hasNext()) {
            ConfigOrigin o = (ConfigOrigin)var2.next();
            remaining.add((SimpleConfigOrigin)o);
         }

         while(remaining.size() > 2) {
            SimpleConfigOrigin c = (SimpleConfigOrigin)remaining.get(remaining.size() - 1);
            remaining.remove(remaining.size() - 1);
            SimpleConfigOrigin b = (SimpleConfigOrigin)remaining.get(remaining.size() - 1);
            remaining.remove(remaining.size() - 1);
            SimpleConfigOrigin a = (SimpleConfigOrigin)remaining.get(remaining.size() - 1);
            remaining.remove(remaining.size() - 1);
            SimpleConfigOrigin merged = mergeThree(a, b, c);
            remaining.add(merged);
         }

         return mergeOrigins((Collection)remaining);
      }
   }

   Map toFields() {
      Map m = new EnumMap(SerializedConfigValue.SerializedField.class);
      m.put(SerializedConfigValue.SerializedField.ORIGIN_DESCRIPTION, this.description);
      if (this.lineNumber >= 0) {
         m.put(SerializedConfigValue.SerializedField.ORIGIN_LINE_NUMBER, this.lineNumber);
      }

      if (this.endLineNumber >= 0) {
         m.put(SerializedConfigValue.SerializedField.ORIGIN_END_LINE_NUMBER, this.endLineNumber);
      }

      m.put(SerializedConfigValue.SerializedField.ORIGIN_TYPE, this.originType.ordinal());
      if (this.urlOrNull != null) {
         m.put(SerializedConfigValue.SerializedField.ORIGIN_URL, this.urlOrNull);
      }

      if (this.resourceOrNull != null) {
         m.put(SerializedConfigValue.SerializedField.ORIGIN_RESOURCE, this.resourceOrNull);
      }

      if (this.commentsOrNull != null) {
         m.put(SerializedConfigValue.SerializedField.ORIGIN_COMMENTS, this.commentsOrNull);
      }

      return m;
   }

   Map toFieldsDelta(SimpleConfigOrigin baseOrigin) {
      Map baseFields;
      if (baseOrigin != null) {
         baseFields = baseOrigin.toFields();
      } else {
         baseFields = Collections.emptyMap();
      }

      return fieldsDelta(baseFields, this.toFields());
   }

   static Map fieldsDelta(Map base, Map child) {
      Map m = new EnumMap(child);
      Iterator var3 = base.entrySet().iterator();

      while(true) {
         while(var3.hasNext()) {
            Map.Entry baseEntry = (Map.Entry)var3.next();
            SerializedConfigValue.SerializedField f = (SerializedConfigValue.SerializedField)baseEntry.getKey();
            if (m.containsKey(f) && ConfigImplUtil.equalsHandlingNull(baseEntry.getValue(), m.get(f))) {
               m.remove(f);
            } else if (!m.containsKey(f)) {
               switch (f) {
                  case ORIGIN_DESCRIPTION:
                     throw new ConfigException.BugOrBroken("origin missing description field? " + child);
                  case ORIGIN_LINE_NUMBER:
                     m.put(SerializedConfigValue.SerializedField.ORIGIN_LINE_NUMBER, -1);
                     break;
                  case ORIGIN_END_LINE_NUMBER:
                     m.put(SerializedConfigValue.SerializedField.ORIGIN_END_LINE_NUMBER, -1);
                     break;
                  case ORIGIN_TYPE:
                     throw new ConfigException.BugOrBroken("should always be an ORIGIN_TYPE field");
                  case ORIGIN_URL:
                     m.put(SerializedConfigValue.SerializedField.ORIGIN_NULL_URL, "");
                     break;
                  case ORIGIN_RESOURCE:
                     m.put(SerializedConfigValue.SerializedField.ORIGIN_NULL_RESOURCE, "");
                     break;
                  case ORIGIN_COMMENTS:
                     m.put(SerializedConfigValue.SerializedField.ORIGIN_NULL_COMMENTS, "");
                     break;
                  case ORIGIN_NULL_URL:
                  case ORIGIN_NULL_RESOURCE:
                  case ORIGIN_NULL_COMMENTS:
                     throw new ConfigException.BugOrBroken("computing delta, base object should not contain " + f + " " + base);
                  case END_MARKER:
                  case ROOT_VALUE:
                  case ROOT_WAS_CONFIG:
                  case UNKNOWN:
                  case VALUE_DATA:
                  case VALUE_ORIGIN:
                     throw new ConfigException.BugOrBroken("should not appear here: " + f);
               }
            }
         }

         return m;
      }
   }

   static SimpleConfigOrigin fromFields(Map m) throws IOException {
      if (m.isEmpty()) {
         return null;
      } else {
         String description = (String)m.get(SerializedConfigValue.SerializedField.ORIGIN_DESCRIPTION);
         Integer lineNumber = (Integer)m.get(SerializedConfigValue.SerializedField.ORIGIN_LINE_NUMBER);
         Integer endLineNumber = (Integer)m.get(SerializedConfigValue.SerializedField.ORIGIN_END_LINE_NUMBER);
         Number originTypeOrdinal = (Number)m.get(SerializedConfigValue.SerializedField.ORIGIN_TYPE);
         if (originTypeOrdinal == null) {
            throw new IOException("Missing ORIGIN_TYPE field");
         } else {
            OriginType originType = OriginType.values()[originTypeOrdinal.byteValue()];
            String urlOrNull = (String)m.get(SerializedConfigValue.SerializedField.ORIGIN_URL);
            String resourceOrNull = (String)m.get(SerializedConfigValue.SerializedField.ORIGIN_RESOURCE);
            List commentsOrNull = (List)m.get(SerializedConfigValue.SerializedField.ORIGIN_COMMENTS);
            if (originType == OriginType.RESOURCE && resourceOrNull == null) {
               resourceOrNull = description;
            }

            return new SimpleConfigOrigin(description, lineNumber != null ? lineNumber : -1, endLineNumber != null ? endLineNumber : -1, originType, urlOrNull, resourceOrNull, commentsOrNull);
         }
      }
   }

   static Map applyFieldsDelta(Map base, Map delta) throws IOException {
      Map m = new EnumMap(delta);
      Iterator var3 = base.entrySet().iterator();

      while(var3.hasNext()) {
         Map.Entry baseEntry = (Map.Entry)var3.next();
         SerializedConfigValue.SerializedField f = (SerializedConfigValue.SerializedField)baseEntry.getKey();
         if (!delta.containsKey(f)) {
            switch (f) {
               case ORIGIN_DESCRIPTION:
                  m.put(f, base.get(f));
                  break;
               case ORIGIN_LINE_NUMBER:
               case ORIGIN_END_LINE_NUMBER:
               case ORIGIN_TYPE:
                  m.put(f, base.get(f));
                  break;
               case ORIGIN_URL:
                  if (delta.containsKey(SerializedConfigValue.SerializedField.ORIGIN_NULL_URL)) {
                     m.remove(SerializedConfigValue.SerializedField.ORIGIN_NULL_URL);
                  } else {
                     m.put(f, base.get(f));
                  }
                  break;
               case ORIGIN_RESOURCE:
                  if (delta.containsKey(SerializedConfigValue.SerializedField.ORIGIN_NULL_RESOURCE)) {
                     m.remove(SerializedConfigValue.SerializedField.ORIGIN_NULL_RESOURCE);
                  } else {
                     m.put(f, base.get(f));
                  }
                  break;
               case ORIGIN_COMMENTS:
                  if (delta.containsKey(SerializedConfigValue.SerializedField.ORIGIN_NULL_COMMENTS)) {
                     m.remove(SerializedConfigValue.SerializedField.ORIGIN_NULL_COMMENTS);
                  } else {
                     m.put(f, base.get(f));
                  }
                  break;
               case ORIGIN_NULL_URL:
               case ORIGIN_NULL_RESOURCE:
               case ORIGIN_NULL_COMMENTS:
                  throw new ConfigException.BugOrBroken("applying fields, base object should not contain " + f + " " + base);
               case END_MARKER:
               case ROOT_VALUE:
               case ROOT_WAS_CONFIG:
               case UNKNOWN:
               case VALUE_DATA:
               case VALUE_ORIGIN:
                  throw new ConfigException.BugOrBroken("should not appear here: " + f);
            }
         }
      }

      return m;
   }

   static SimpleConfigOrigin fromBase(SimpleConfigOrigin baseOrigin, Map delta) throws IOException {
      Map baseFields;
      if (baseOrigin != null) {
         baseFields = baseOrigin.toFields();
      } else {
         baseFields = Collections.emptyMap();
      }

      Map fields = applyFieldsDelta(baseFields, delta);
      return fromFields(fields);
   }
}
