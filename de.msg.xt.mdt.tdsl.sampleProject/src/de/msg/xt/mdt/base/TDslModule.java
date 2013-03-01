package de.msg.xt.mdt.base;

import mp3manager.activities.LogicalViewAdapter;
import mp3manager.activities.MainWindowAdapter;
import mp3manager.activities.OpenViewDialogAdapter;
import mp3manager.activities.SongEditorAdapter;
import mp3manager.activitiyadapter.SWTBotLogicalViewAdapter;
import mp3manager.activitiyadapter.SWTBotMainWindowAdapter;
import mp3manager.activitiyadapter.SWTBotOpenViewDialogAdapter;
import mp3manager.activitiyadapter.SWTBotSongEditorAdapter;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

import de.msg.xt.mdt.tdsl.sampleProject.swtbot.SWTBotActivityLocator;

public class TDslModule extends AbstractModule {

    public static final String TEST_ID_KEY = "TestID";
    private final String testID;

    public TDslModule(String testID) {
        this.testID = testID;
    }

    @Override
    protected void configure() {
        bind(Generator.class).to(SampleTestGenerator.class);
        bind(ITestProtocol.class).toInstance(new SimpleTestProtocol(this.testID));

        // bind(MP3MangerAppActivityAdapter.class).to(TextMP3ActivityAdapter.class);
        // bind(MP3MangerAppActivityAdapter.class).to(SWTBotMP3ActivityAdapter.class);

        bind(String.class).annotatedWith(Names.named(TEST_ID_KEY)).toInstance(this.testID);

        install(new SWTBotMP3Module());
    }

    public static class SWTBotMP3Module extends AbstractModule {

        @Override
        protected void configure() {
            bind(ActivityLocator.class).to(SWTBotActivityLocator.class);

            bind(LogicalViewAdapter.class).to(SWTBotLogicalViewAdapter.class);
            bind(MainWindowAdapter.class).to(SWTBotMainWindowAdapter.class);
            bind(OpenViewDialogAdapter.class).to(SWTBotOpenViewDialogAdapter.class);
            bind(SongEditorAdapter.class).to(SWTBotSongEditorAdapter.class);
        }

    }
}
