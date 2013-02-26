package de.msg.xt.mdt.base;

import mp3manager.MP3MangerAppActivityAdapter;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;

public class TDslModule extends AbstractModule {

    public static final String TEST_ID_KEY = "TestID";
    private final String testID;

    public TDslModule(String testID) {
        this.testID = testID;
    }

    @Override
    protected void configure() {
        bind(Generator.class).to(SampleTestGenerator.class);
        bind(ActivityAdapter.class).to(SWTBotActivityAdapter.class);
        bind(ITestProtocol.class).toInstance(new SimpleTestProtocol(this.testID));
        // bind(de.msg.xt.mdt.ActivityAdapter.class).to(SWTBotGenActivityAdapter.class);
        bind(MP3MangerAppActivityAdapter.class).to(TextMP3ActivityAdapter.class);
        // bind(MP3MangerAppActivityAdapter.class).to(SWTBotMP3ActivityAdapter.class);

        bind(String.class).annotatedWith(Names.named(TEST_ID_KEY)).toInstance(this.testID);
    }
}
