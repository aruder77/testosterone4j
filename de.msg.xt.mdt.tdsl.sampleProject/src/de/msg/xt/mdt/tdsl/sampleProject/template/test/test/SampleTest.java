package de.msg.xt.mdt.tdsl.sampleProject.template.test.test;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Guice;
import com.google.inject.Injector;

import de.msg.xt.mdt.base.GenerationHelper;
import de.msg.xt.mdt.base.Generator;
import de.msg.xt.mdt.base.Parameters;
import de.msg.xt.mdt.base.TDslModule;
import de.msg.xt.mdt.base.TDslParameterized;
import de.msg.xt.mdt.tdsl.sampleProject.template.test.usecase.SampleUseCase;

@RunWith(TDslParameterized.class)
public class SampleTest {

    private static final String TEST_CASES_XML = "./SampleTest_2234234.xml";

    private static final Injector INJECTOR = Guice.createInjector(new TDslModule());

    private final SampleUseCase useCase;

    @Parameters
    public static Collection<Object[]> config() {
        GenerationHelper testHelper = INJECTOR.getInstance(GenerationHelper.class);
        Generator generator = INJECTOR.getInstance(Generator.class);
        return testHelper.readOrGenerateTestCases(TEST_CASES_XML, generator, SampleUseCase.class);
    }

    public SampleTest(SampleUseCase useCase) {
        this.useCase = useCase;
        INJECTOR.injectMembers(this);
    }

    @Test
    public void test() {
        this.useCase.run();
    }
}
