package de.msg.xt.mdt.tdsl.sampleProject.template.test.test;

import org.eclipse.swtbot.eclipse.finder.widgets.SWTBotView;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotTree;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Guice;

import de.msg.xt.mdt.base.TDslModule;
import de.msg.xt.mdt.tdsl.sampleProject.swtbot.TestUtil;
import de.msg.xt.mdt.tdsl.swtbot.MainMenu;

public class SWTBotTest {

    public SWTBotTest() {
        Guice.createInjector(new TDslModule("Test")).injectMembers(this);
    }

    @Before
    public void setUp() throws Exception {
        TestUtil.resetWorkbench();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test() {
        MainMenu.window().click().menu("Open View").click().menu("Other...").click();
        SWTBot bot = new SWTBot();
        SWTBotShell shell = bot.shell("Show View");
        SWTBotTree tree = shell.bot().tree();
        tree.expandNode("MP3 Manager (Virtual)", false).getNode("Logical View").select();
        shell.bot().button("OK").click();

        SWTBotView logicalView = TestUtil.findActiveView("com.siemens.ct.mp3m.ui.views.logical.LogicalView");
        SWTBotTree logicalTree = logicalView.bot().tree();
        logicalTree.expandNode("Bob Marley").expandNode("Legend").getNode("Stir It Up").doubleClick();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
