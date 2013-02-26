package de.msg.xt.mdt.base;

import org.eclipse.swtbot.swt.finder.SWTBot;

public class ActivityContext {

    Object context;
    String id;
    SWTBot bot;

    public ActivityContext(Object context, String id, SWTBot bot) {
        this.context = context;
        this.id = id;
        this.bot = bot;
    }

    public Object getContext() {
        return this.context;
    }

    public SWTBot getBot() {
        return this.bot;
    }

    public void setBot(SWTBot bot) {
        this.bot = bot;
    }

    public String getId() {
        return this.id;
    }
}