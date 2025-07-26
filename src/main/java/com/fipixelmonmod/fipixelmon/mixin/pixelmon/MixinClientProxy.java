package com.fipixelmonmod.fipixelmon.mixin.pixelmon;

import com.fipixelmonmod.fipixelmon.FIPixelmon;
import com.fipixelmonmod.fipixelmon.bridge.GuiMegaItemBridge;
import com.fipixelmonmod.fipixelmon.helper.FileHelper;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.GuiMegaItem;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.util.helpers.RCFileHelper;
import lombok.SneakyThrows;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Mixin(value = ClientProxy.class, remap = false)
public class MixinClientProxy {
    /**
     * @see MixinGuiMegaItem
     */
    @Inject(method = "createScreen", at = @At("HEAD"), cancellable = true)
    private static void createScreen(EntityPlayer player, EnumGuiScreen gui, int[] data, CallbackInfoReturnable<GuiScreen> cir) {
        if (gui.equals(EnumGuiScreen.MegaItem)) {
            cir.setReturnValue(data.length == 0 ? new GuiMegaItem(false) : data[0] != 0 ? new GuiMegaItem(true) : (GuiScreen) ((GuiMegaItemBridge) new GuiMegaItem(true)).fIPixelmon$setTerastal(true));
            cir.cancel();
        }
    }

    @SneakyThrows
    @Inject(method = "loadSpritesToAtlas", at = @At("HEAD"), remap = false)
    private void loadSpritesToAtlas(TextureStitchEvent.Pre event, CallbackInfo ci) {
        {//FIPDataFile
            File file = new File(FIPixelmon.texturesFolder, "sprites");
            if (file.exists() && file.listFiles() != null) {
                Path path = file.toPath();
                List<Path> paths = RCFileHelper.listFilesRecursively(path, entry -> entry.getFileName().toString().endsWith(".png"), true);
                String fileName;
                StringBuilder folderName;
                for (Path pngPath : paths) {
                    fileName = pngPath.getFileName().toString();
                    folderName = new StringBuilder(pngPath.getParent().getFileName().toString());
                    if (folderName.length() > 0) folderName.append("/");
                    event.getMap().registerSprite(new ResourceLocation("pixelmon", "sprites/" + folderName + fileName.substring(0, fileName.length() - 4)));
                }
            }
        }
        {//zip
            ZipFile zipFile;
            String fileName;
            StringBuilder folderName;
            ZipEntry zipEntry;
            String path;
            int index;
            Enumeration<? extends ZipEntry> entries;
            for (File file : FileHelper.loadedZipFile) {
                zipFile = new ZipFile(file);
                entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    zipEntry = entries.nextElement();
                    path = zipEntry.getName();
                    if (path.startsWith("textures/sprites/") && path.endsWith(".png")) {
                        index = path.lastIndexOf('/');
                        folderName = new StringBuilder(path.substring(path.substring(0, index).lastIndexOf('/') + 1, index));
                        if (folderName.length() > 0) folderName.append("/");
                        fileName = path.substring(index + 1, path.length() - 4);
                        event.getMap().registerSprite(new ResourceLocation("pixelmon", "sprites/" + folderName + fileName));
                    }
                }
                zipFile.close();
            }
        }
    }
}
