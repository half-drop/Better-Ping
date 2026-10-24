package com.vladmarica.betterpingdisplay.hud;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

public class PingVisibilityTest {
  @Test
  public void hidesSharedPlaceholderPing() {
    assertTrue(shouldHide(10000, 10000, 10000));
  }

  @Test
  public void detectsAnyIdenticalValue() {
    assertTrue(shouldHide(42, 42));
    assertTrue(shouldHide(0, 0, 0));
    assertTrue(shouldHide(-1, -1));
  }

  @Test
  public void keepsPingWhenAnyPlayerDiffers() {
    assertFalse(shouldHide(10000, 42, 10000));
    assertFalse(shouldHide(42, 42, 43));
  }

  @Test
  public void doesNotClassifyEmptyOrSinglePlayerLists() {
    assertFalse(shouldHide());
    assertFalse(shouldHide(10000));
  }

  @Test
  public void checksPlayersBeyondTheFirstEightyEntries() {
    List<Integer> pings = new ArrayList<>(Collections.nCopies(80, 10000));
    pings.add(42);
    assertFalse(PingVisibility.shouldHidePing(pings, Integer::intValue));
  }

  @Test
  public void reevaluatesAfterLatencyAndPlayerListChanges() {
    List<Integer> pings = new ArrayList<>(List.of(10000, 10000));
    assertTrue(PingVisibility.shouldHidePing(pings, Integer::intValue));
    pings.set(1, 42);
    assertFalse(PingVisibility.shouldHidePing(pings, Integer::intValue));
    pings.set(0, 42);
    assertTrue(PingVisibility.shouldHidePing(pings, Integer::intValue));
    pings.remove(1);
    assertFalse(PingVisibility.shouldHidePing(pings, Integer::intValue));
    pings.clear();
    assertFalse(PingVisibility.shouldHidePing(pings, Integer::intValue));
  }

  private static boolean shouldHide(Integer... pings) {
    return PingVisibility.shouldHidePing(List.of(pings), Integer::intValue);
  }
}
