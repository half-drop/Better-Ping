package com.vladmarica.betterpingdisplay.hud;

import java.util.HashMap;
import java.util.Map;
import java.util.function.ToIntFunction;

public final class PingVisibility {
  private static final int PLACEHOLDER_PING = 10000;
  private static final int MIN_MIXED_ENTRIES = 8;

  private PingVisibility() {
  }

  /** Detect shared latency values and large lists dominated by repeated placeholder values. */
  public static <T> boolean shouldHidePing(Iterable<T> players, ToIntFunction<? super T> latency) {
    Map<Integer, Integer> counts = new HashMap<>();
    int total = 0;
    for (T player : players) {
      counts.merge(latency.applyAsInt(player), 1, Integer::sum);
      total++;
      if (counts.size() > 2) {
        return false;
      }
    }

    // A single player cannot establish that a latency value is shared.
    if (total < 2) {
      return false;
    }
    if (counts.size() == 1) {
      return true;
    }

    // Custom tab layouts can mix 10000ms filler entries with another fixed value,
    // such as 500ms. Require a large majority and a repeated second value so a
    // single unusual player, or a varied set of real pings, keeps normal display.
    int placeholderCount = counts.getOrDefault(PLACEHOLDER_PING, 0);
    return total >= MIN_MIXED_ENTRIES
        && placeholderCount * 4 >= total * 3
        && total - placeholderCount >= 2;
  }
}
