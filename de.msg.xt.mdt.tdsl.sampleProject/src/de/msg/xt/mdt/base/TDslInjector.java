package de.msg.xt.mdt.base;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class TDslInjector {

    private static Injector instance = null;

    public static Injector getInjector() {
        return instance;
    }

    public static Injector createInjector(String testID) {
        instance = Guice.createInjector(new TDslModule(testID));
        return instance;
    }

}
