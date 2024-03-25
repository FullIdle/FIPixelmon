package com.pixelmonmod.pixelmon.client.keybindings;

import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.util.helpers.ArrayHelper;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

public class WikiKey extends KeyBinding {
   public static final String PIXELMON_WIKI = "https://pixelmonmod.com/wiki/index.php?title=";
   private static final String MINECRAFT_WIKI_START = "http://minecraft";
   private static final String MINECRAFT_WIKI_END = ".gamepedia.com/";
   private static final String[] TRANSLATED_MINECRAFT_WIKI = new String[]{"de", "el", "es", "fr", "hu", "it", "ja", "ko", "nl", "pl", "pt", "ru", "zh"};

   public WikiKey() {
      super("key.wiki", 37, "key.categories.pixelmon");
   }

   @SubscribeEvent
   public void keyDown(InputEvent.KeyInputEvent event) {
      if (this.func_151468_f()) {
         Minecraft mc = Minecraft.func_71410_x();
         if (mc.field_71441_e == null) {
            return;
         }

         try {
            RayTraceResult objectMouseOver = TargetKeyBinding.getTarget(false);
            ItemStack itemStack = mc.field_71439_g.field_71071_by.func_70448_g();
            ResourceLocation id = null;
            String name = null;
            if (itemStack != null && !itemStack.func_190926_b()) {
               Item item = itemStack.func_77973_b();
               id = item.getRegistryName();
               if (item == PixelmonItems.itemPixelmonSprite) {
                  name = I18n.func_135052_a("item.pixelmon_sprite.name", new Object[0]);
               } else {
                  name = item.func_77653_i(itemStack);
               }
            } else if (objectMouseOver.field_72313_a == Type.BLOCK) {
               Block block = mc.field_71441_e.func_180495_p(objectMouseOver.func_178782_a()).func_177230_c();
               id = block.getRegistryName();
               name = block.func_149732_F();
            }

            if (id != null && name != null) {
               name = name.trim().replace(":", "%3A").replace(" ", "_").replace("é", "e");
               String resourceDomain = id.func_110624_b();
               String baseURL = null;
               if (resourceDomain.equalsIgnoreCase("pixelmon")) {
                  baseURL = "https://pixelmonmod.com/wiki/index.php?title=";
               } else if (resourceDomain.equalsIgnoreCase("minecraft")) {
                  baseURL = "http://minecraft";
                  String langCode = mc.func_135016_M().func_135041_c().func_135034_a();
                  langCode = langCode.substring(0, 2);
                  if (!langCode.equals("en") && ArrayHelper.contains(TRANSLATED_MINECRAFT_WIKI, langCode)) {
                     if (langCode.equals("pt")) {
                        langCode = "br";
                     }

                     baseURL = baseURL + '-' + langCode;
                  }

                  baseURL = baseURL + ".gamepedia.com/";
               }

               if (baseURL != null) {
                  Desktop.getDesktop().browse(new URI(baseURL + name));
               }

               return;
            }
         } catch (UnsupportedOperationException var10) {
         } catch (URISyntaxException | RuntimeException | IOException var11) {
            var11.printStackTrace();
         }
      }

   }
}
