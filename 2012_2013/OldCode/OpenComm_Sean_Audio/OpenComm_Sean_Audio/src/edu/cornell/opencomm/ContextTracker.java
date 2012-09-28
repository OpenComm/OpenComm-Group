package edu.cornell.opencomm;

import android.content.Context;

public class ContextTracker {
    private static Context currentContext;

    public static void setContext(Context newContext) {
        currentContext = newContext;
    }

    public static Context getContext() {
        return currentContext;
    }
}
