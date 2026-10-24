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
  public void hidesMixedPlaceholderLayoutLikeTheScreenshot() {
    List<Integer> pings = mixedPings(68, 10000, 12, 500);
    assertTrue(PingVisibility.shouldHidePing(pings, Integer::intValue));
    Collections.reverse(pings);
    assertTrue(PingVisibility.shouldHidePing(pings, Integer::intValue));
  }

  @Test
  public void doesNotRequireTheSecondaryFixedValueToBeFiveHundred() {
    assertTrue(PingVisibility.shouldHidePing(mixedPings(68, 10000, 12, 0), Integer::intValue));
  }

  @Test
  public void requiresEnoughEntriesAndADominantPlaceholderGroup() {
    assertTrue(PingVisibility.shouldHidePing(mixedPings(6, 10000, 2, 500), Integer::intValue));
    assertFalse(PingVisibility.shouldHidePing(mixedPings(5, 10000, 2, 500), Integer::intValue));
    assertFalse(PingVisibility.shouldHidePing(mixedPings(59, 10000, 21, 500), Integer::intValue));
  }

  @Test
  public void keepsRealPingsEvenWhenManyPlayersShareTwoValues() {
    assertFalse(PingVisibility.shouldHidePing(mixedPings(68, 42, 12, 43), Integer::intValue));
    assertFalse(PingVisibility.shouldHidePing(mixedPings(68, 9000, 12, 9500), Integer::intValue));
  }

  @Test
  public void keepsASingleDifferentPlayerVisible() {
    assertFalse(PingVisibility.shouldHidePing(mixedPings(79, 10000, 1, 500), Integer::intValue));
  }

  @Test
  public void keepsDisplayWhenRemainingPlayersHaveVariedRealPings() {
    List<Integer> pings = mixedPings(68, 10000, 10, 500);
    pings.addAll(List.of(42, 78));
    assertFalse(PingVisibility.shouldHidePing(pings, Integer::intValue));
  }

  @Test
  public void restoresDisplayWhenMixedPlaceholdersBecomeRealMeasurements() {
    List<Integer> pings = mixedPings(68, 10000, 12, 500);
    assertTrue(PingVisibility.shouldHidePing(pings, Integer::intValue));
    pings.set(68, 42);
    assertFalse(PingVisibility.shouldHidePing(pings, Integer::intValue));
    pings.set(68, 500);
    assertTrue(PingVisibility.shouldHidePing(pings, Integer::intValue));
    pings.clear();
    pings.addAll(List.of(30, 45, 60));
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

  private static List<Integer> mixedPings(int firstCount, int firstPing, int secondCount, int secondPing) {
    List<Integer> pings = new ArrayList<>(Collections.nCopies(firstCount, firstPing));
    pings.addAll(Collections.nCopies(secondCount, secondPing));
    return pings;
  }
}
