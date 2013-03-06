package de.msg.xt.mdt.tdsl.swtbot.activityadapter;

public class SWTBotShellActivityAdapter extends SWTBotActivityAdapter {

    public Object ok() {
        this.contextObject.getBot().button("OK").click();
        return null;
    }

    public Object cancel() {
        this.contextObject.getBot().button("Cancel").click();
        return null;
    }

}
