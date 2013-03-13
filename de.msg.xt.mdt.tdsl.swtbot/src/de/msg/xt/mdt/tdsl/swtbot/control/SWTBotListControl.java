package de.msg.xt.mdt.tdsl.swtbot.control;

import java.util.Arrays;

import org.eclipse.swtbot.swt.finder.widgets.SWTBotList;

import de.msg.xt.mdt.tdsl.swtbot.ActivityContext;
import de.msg.xt.mdt.tdsl.swtbot.List;

public class SWTBotListControl implements List {

    SWTBotList swtBotList;
    String id;

    public static SWTBotListControl findControl(ActivityContext context, String id) {
        return new SWTBotListControl(id, context.getBot().listWithId(id));
    }

    public SWTBotListControl(String id, SWTBotList swtBotText) {
        this.id = id;
        this.swtBotList = swtBotText;
    }

    @Override
    public Boolean isEnabled() {
        return this.swtBotList.isEnabled();
    }

    @Override
    public void addSelection(String entry) {
        String[] currentSelection = this.swtBotList.selection();
        String[] newSelection = Arrays.copyOf(currentSelection, currentSelection.length + 1);
        this.swtBotList.select(newSelection);
    }

    @Override
    public void clearSelection() {
        this.swtBotList.unselect();
    }

    @Override
    public Boolean isSelected(String entry) {
        return null;
    }

    @Override
    public void doubleClick(String entry) {
        // to be implemented
    }
}