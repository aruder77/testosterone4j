package de.msg.xt.mdt.base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import de.msg.xt.mdt.tdsl.sampleProject.template.test.test.UseCaseSuite;

public class GenerationHelper {

    public List<Object[]> readOrGenerateTestCases(String fileName, Generator generator, Class<? extends Runnable> testClass) {
        List<?> testCases = null;

        File f = new File(fileName);
        /*
         * temporarily disabled until UseCases are serializable again... if
         * (f.exists()) { try { testCases = readXML(testClass, f); } catch
         * (JAXBException e) { throw new
         * RuntimeException("Cannot read test case data from xml!", e); } } else
         * { testCases = generate(generator, testClass); try {
         * writeXML(testCases, testClass, f); } catch (JAXBException e) { throw
         * new RuntimeException("Cannot write generated test data to xml!", e);
         * } }
         */
        testCases = generate(generator, testClass);

        List<Object[]> testCaseConfig = new ArrayList<Object[]>();
        for (Object testCase : testCases) {
            testCaseConfig.add(new Object[] { testCase });
        }
        return testCaseConfig;
    }

    public List<?> generate(Generator generator, Class<? extends Runnable> clazz) {
        return generator.generate(clazz);
    }

    private List<?> readXML(Class<?> useCaseClass, File f) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(UseCaseSuite.class, useCaseClass);
        Unmarshaller um = context.createUnmarshaller();
        UseCaseSuite<?> suite = (UseCaseSuite<?>) um.unmarshal(f);
        return suite.getTestCases();
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void writeXML(List<?> testCases, Class<?> useCaseClass, File f) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(UseCaseSuite.class, useCaseClass);
        Marshaller m = context.createMarshaller();
        m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        m.marshal(new UseCaseSuite(testCases), f);
    }

}
