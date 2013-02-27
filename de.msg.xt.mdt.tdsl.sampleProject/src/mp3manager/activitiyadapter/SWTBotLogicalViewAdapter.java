package mp3manager.activitiyadapter;

import mp3manager.activities.LogicalViewAdapter;

public class SWTBotLogicalViewAdapter extends SWTBotBaseAdapter implements LogicalViewAdapter {

    @Override
    public String getType() {
        return "View";
    }

}
