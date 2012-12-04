package de.msg.xt.mdt.tdsl.sampleProject.template.test.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import de.msg.xt.mdt.base.SampleTestGenerator;
import de.msg.xt.mdt.tdsl.sampleProject.template.test.usecase.SampleUseCase;

@RunWith(Parameterized.class)
public class SampleTest {

    private static final String TEST_CASES_XML = "./SampleTest_2234234.xml";

    @Parameters
    public static Collection<Object[]> config() {
        List<SampleUseCase> testCases = null;

        File f = new File(TEST_CASES_XML);
        if (f.exists()) {
            try {
                testCases = readXML(f);
            } catch (JAXBException e) {
                throw new RuntimeException("Cannot read test case data from xml!", e);
            }
        } else {
            testCases = generate();
            try {
                writeXML(testCases, f);
            } catch (JAXBException e) {
                throw new RuntimeException("Cannot write generated test data to xml!", e);
            }
        }

        List<Object[]> testCaseConfig = new ArrayList<Object[]>();
        for (SampleUseCase testCase : testCases) {
            testCaseConfig.add(new Object[] { testCase });
        }

        return testCaseConfig;
    }

    public static List<SampleUseCase> generate() {
        SampleTestGenerator generator = new SampleTestGenerator();
        List<SampleUseCase> testCases = generator.generate(SampleUseCase.class);

        // generate the test cases
        // testCases.add(new SampleUseCase(new StringDT("a",
        // StringDTEquivalenceClass.shortString), new Sachnummer("b",
        // SachnummerEquivalenceClass.snrString), new SampleSubUseCase(new
        // StringDT("c", StringDTEquivalenceClass.shortString))));
        // testCases.add(new SampleUseCase(new
        // StringDT("abaclkavjsödlfkjasödfljkasö",
        // StringDTEquivalenceClass.longString), new Sachnummer("b",
        // SachnummerEquivalenceClass.snrString), new SampleSubUseCase(new
        // StringDT("c", StringDTEquivalenceClass.shortString))));

        return testCases;
    }

    private static List<SampleUseCase> readXML(File f) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(SampleUseCaseSuite.class);
        Unmarshaller um = context.createUnmarshaller();
        SampleUseCaseSuite suite = (SampleUseCaseSuite) um.unmarshal(f);
        return suite.getTestCases();
    }

    private static void writeXML(List<SampleUseCase> testCases, File f) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(SampleUseCaseSuite.class);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.marshal(new SampleUseCaseSuite(testCases), f);
    }

    private final SampleUseCase useCase;

    public SampleTest(SampleUseCase useCase) {
        this.useCase = useCase;
    }

    @Test
    public void test() {
        this.useCase.run();
    }

    @XmlRootElement
    private static class SampleUseCaseSuite {

        @XmlElement
        List<SampleUseCase> testCases;

        public SampleUseCaseSuite() {
            super();
        }

        public SampleUseCaseSuite(List<SampleUseCase> testCases) {
            this.testCases = testCases;
        }

        public void setTestCasees(List<SampleUseCase> testCases) {
            this.testCases = testCases;
        }

        public List<SampleUseCase> getTestCases() {
            return this.testCases;
        }
    }
}
