package com.rcpky.blackjack2;

import java.util.*;

public class BlackjackController {
    public boolean validateAction(String action) {
        return "hit".equalsIgnoreCase(action) || "stand".equalsIgnoreCase(action);
    }
}
