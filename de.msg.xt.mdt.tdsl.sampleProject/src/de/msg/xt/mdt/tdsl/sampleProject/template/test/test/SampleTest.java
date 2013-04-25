package de.msg.xt.mdt.tdsl.sampleProject.template.test.test;

import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.inject.Injector;

import de.msg.xt.mdt.base.GenerationHelper;
import de.msg.xt.mdt.base.Generator;
import de.msg.xt.mdt.base.ITestProtocol;
import de.msg.xt.mdt.base.Parameters;
import de.msg.xt.mdt.base.TDslInjector;
import de.msg.xt.mdt.base.TDslParameterized;
import de.msg.xt.mdt.base.TestDescriptor;
import de.msg.xt.mdt.tdsl.sampleProject.template.test.usecase.SampleUseCase;

@RunWith(TDslParameterized.class)
public class SampleTest {

    private static final String TEST_CASES_XML = "./SampleTest_2234234.xml";

    private static final Injector INJECTOR = TDslInjector.createInjector(TEST_CASES_XML);

    private final ITestProtocol protocol = INJECTOR.getInstance(ITestProtocol.class);

    private final SampleUseCase useCase;

    private final int testNumber;

    @Parameters
    public static Collection<Object[]> config() {
        GenerationHelper testHelper = INJECTOR.getInstance(GenerationHelper.class);
        Generator generator = INJECTOR.getInstance(Generator.class);
        return testHelper.readOrGenerateTestCases(TEST_CASES_XML, generator, SampleUseCase.class);
    }

    public SampleTest(TestDescriptor<SampleUseCase> testDescriptor) {
        this.testNumber = testDescriptor.getTestNumber();
        this.useCase = testDescriptor.getTestCase();
        INJECTOR.injectMembers(this);
    }

    @Test
    public void test() {
        this.protocol.newTest(String.valueOf(this.testNumber));
        this.useCase.run();
    }
}
