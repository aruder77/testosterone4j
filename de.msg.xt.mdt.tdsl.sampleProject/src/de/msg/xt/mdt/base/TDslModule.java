package de.msg.xt.mdt.base;

import com.google.inject.AbstractModule;

public class TDslModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Generator.class).to(SampleTestGenerator.class);
        bind(ActivityAdapter.class).to(SWTBotActivityAdapter.class);
        // bind(de.msg.xt.mdt.ActivityAdapter.class).to(SWTBotGenActivityAdapter.class);
         bind(mp3manager.ActivityAdapter.class).to(TextMP3ActivityAdapter.class);
//        bind(mp3manager.ActivityAdapter.class).to(SWTBotMP3ActivityAdapter.class);
    }

}
