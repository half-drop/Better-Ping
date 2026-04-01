package com.vladmarica.betterpingdisplay.mixin;

import com.vladmarica.betterpingdisplay.hud.CustomPlayerListHud;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerTabOverlay.class)
public abstract class PlayerTabOverlayMixin {
  @Unique
  @Final
  private static final int PLAYER_SLOT_EXTRA_WIDTH = 35;

  @Shadow
  @Final
  private Minecraft minecraft;

  @ModifyConstant(method = "extractRenderState", constant = @Constant(intValue = 13))
  private int modifySlotWidthConstant(int original) {
    return original + PLAYER_SLOT_EXTRA_WIDTH;
  }

  @Redirect(
      method = "extractRenderState",
      at = @At(
          value = "INVOKE",
          target = "Lnet/minecraft/client/gui/components/PlayerTabOverlay;extractPingIcon(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIILnet/minecraft/client/multiplayer/PlayerInfo;)V"))
  private void redirectExtractPingIcon(
      PlayerTabOverlay instance,
      GuiGraphicsExtractor graphics,
      int slotWidth,
      int xo,
      int yo,
      @NotNull PlayerInfo entry) {
    CustomPlayerListHud.renderPingDisplay(minecraft, instance, graphics, slotWidth, xo, yo, entry);
  }
}
