package com.vladmarica.betterpingdisplay.hud;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ColorUtilTest {

  @Test
  public void testExtractComponents() {
    int color = 0xD6C130;
    assertEquals(0xD6, ColorUtil.getRed(color));
    assertEquals(0xC1, ColorUtil.getGreen(color));
    assertEquals(0x30, ColorUtil.getBlue(color));
  }

  private static final int COLOR_START = 0xFF00E676;
  private static final int COLOR_MID = 0xFFD6CD30;

  @Test
  public void testInterpolation_zero() {
    int color = ColorUtil.interpolate(COLOR_START, COLOR_MID, 0F);
    assertEquals(COLOR_START, color);
  }

  @Test
  public void testInterpolation_middle() {
    int color = ColorUtil.interpolate(COLOR_START, COLOR_MID, 0.5F);
    assertEquals(0x6b, ColorUtil.getRed(color));
    assertEquals(0xda, ColorUtil.getGreen(color));
    assertEquals(0x53, ColorUtil.getBlue(color));
  }

  @Test
  public void testInterpolation_one() {
    int color = ColorUtil.interpolate(COLOR_START, COLOR_MID, 1F);
    assertEquals(COLOR_MID, color);
  }

  @Test
  public void testInterpolation_invalidOffset() {
    assertThrows(IllegalArgumentException.class, () -> ColorUtil.interpolate(0, 1, -0.1F));
    assertThrows(IllegalArgumentException.class, () -> ColorUtil.interpolate(0, 1, 1.1F));
  }
}
