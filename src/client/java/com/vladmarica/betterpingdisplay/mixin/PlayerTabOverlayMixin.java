package com.vladmarica.betterpingdisplay.mixin;

import com.vladmarica.betterpingdisplay.hud.CustomPlayerListHud;
import com.vladmarica.betterpingdisplay.hud.PingVisibility;
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
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerTabOverlay.class)
public abstract class PlayerTabOverlayMixin {
  @Unique
  @Final
  private static final int PLAYER_SLOT_EXTRA_WIDTH = 35;

  @Shadow
  @Final
  private Minecraft minecraft;

  @Unique
  private boolean betterpingdisplay$hidePing;

  @Inject(method = "extractRenderState", at = @At("HEAD"))
  private void betterpingdisplay$updatePingVisibility(CallbackInfo ci) {
    var connection = minecraft.getConnection();
    betterpingdisplay$hidePing = connection != null
        && PingVisibility.shouldHidePing(connection.getListedOnlinePlayers(), PlayerInfo::getLatency);
  }

  @ModifyConstant(method = "extractRenderState", constant = @Constant(intValue = 13))
  private int modifySlotWidthConstant(int original) {
    return betterpingdisplay$hidePing ? original : original + PLAYER_SLOT_EXTRA_WIDTH;
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
    // Skip the complete rendering path, including optional vanilla signal bars.
    if (!betterpingdisplay$hidePing) {
      CustomPlayerListHud.renderPingDisplay(minecraft, instance, graphics, slotWidth, xo, yo, entry);
    }
  }
}
