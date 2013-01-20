package de.msg.xt.mdt.base;

public class ActivityAdapterFactory {

    public static ActivityAdapter getActivityAdapter() {
        return new SWTBotActivityAdapter();
    }
}
