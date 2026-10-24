package com.vladmarica.betterpingdisplay.hud;

import com.vladmarica.betterpingdisplay.BetterPingDisplayMod;
import com.vladmarica.betterpingdisplay.Config;
import com.vladmarica.betterpingdisplay.mixin.PlayerTabOverlayInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.client.multiplayer.PlayerInfo;

public final class CustomPlayerListHud {
  private static final int PING_BAR_OFFSET = 11;

  private static Config config() {
    return BetterPingDisplayMod.instance().getConfig();
  }

  public static void renderPingDisplay(
      Minecraft client,
      PlayerTabOverlay overlay,
      GuiGraphicsExtractor context,
      int width,
      int x,
      int y,
      PlayerInfo player) {
    Config cfg = config();
    String pingString = String.format(cfg.getTextFormatString(), player.getLatency());
    Font font = client.font;
    int pingStringWidth = font.width(pingString);
    int pingTextColor = cfg.shouldAutoColorPingText()
        ? PingColors.getColor(player.getLatency()) : cfg.getTextColor();

    boolean renderPingBars = cfg.shouldRenderPingBars();
    int pingBarOffset = renderPingBars ? PING_BAR_OFFSET : 0;
    int textX = x + width - pingBarOffset - pingStringWidth - 2;

    context.text(font, pingString, textX, y, pingTextColor, true);

    if (renderPingBars) {
      ((PlayerTabOverlayInvoker) overlay).invokeExtractPingIcon(context, width, x, y, player);
    }
  }
}
