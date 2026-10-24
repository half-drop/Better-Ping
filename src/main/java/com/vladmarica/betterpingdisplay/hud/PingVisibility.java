package com.vladmarica.betterpingdisplay.hud;

import java.util.Iterator;
import java.util.function.ToIntFunction;

public final class PingVisibility {
  private PingVisibility() {
  }

  /** A single player does not provide enough information to detect a shared placeholder ping. */
  public static <T> boolean shouldHidePing(Iterable<T> players, ToIntFunction<? super T> latency) {
    Iterator<T> iterator = players.iterator();
    if (!iterator.hasNext()) {
      return false;
    }

    int firstPing = latency.applyAsInt(iterator.next());
    if (!iterator.hasNext()) {
      return false;
    }

    while (iterator.hasNext()) {
      if (latency.applyAsInt(iterator.next()) != firstPing) {
        return false;
      }
    }
    return true;
  }
}
