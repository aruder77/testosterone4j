package de.msg.xt.mdt.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

public class GenerationHelper {

	public List<Object[]> readOrGenerateTestCases(final String fileName, final Generator generator,
			final Class<? extends Runnable> testClass) {
		List<?> testCases = null;

		final File f = new File(fileName);

		if (!f.exists()) {
			testCases = generate(generator, testClass);
			try {
				writeSerialization(testCases, testClass, f);
			} catch (final IOException e) {
				throw new RuntimeException("Cannot write generated test data to file!", e);
			}
		}

		try {
			testCases = readSerialization(testClass, f);
		} catch (final ClassNotFoundException e) {
			throw new RuntimeException("Cannot read test case data from file!", e);
		} catch (final IOException e) {
			throw new RuntimeException("Cannot read test case data from file!", e);
		}
		final List<Object[]> testCaseConfig = new ArrayList<Object[]>();
		int i = 1;
		for (final Object testCase : testCases) {
			testCaseConfig.add(new Object[] { new TestDescriptor(i++, testCase) });
		}
		return testCaseConfig;
	}

	public List<?> generate(final Generator generator, final Class<? extends Runnable> clazz) {
		return generator.generate(clazz);
	}

	private List<Object> readSerialization(final Class<?> useCaseClass, final File f) throws IOException, ClassNotFoundException {
		final ObjectInputStream oin = new ObjectInputStream(new FileInputStream(f));
		Object o = null;
		final List<Object> list = new ArrayList<Object>();
		while ((o = oin.readObject()) != null) {
			list.add(o);
		}
		return list;

		// JAXBContext context = JAXBContext.newInstance(UseCaseSuite.class,
		// useCaseClass);
		// Unmarshaller um = context.createUnmarshaller();
		// UseCaseSuite<?> suite = (UseCaseSuite<?>) um.unmarshal(f);
		// return suite.getTestCases();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void writeSerialization(final List<?> testCases, final Class<?> useCaseClass, final File f) throws IOException {
		final ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(f));
		for (final Object testCase : testCases) {
			oout.writeObject(testCase);
		}
		oout.writeObject(null);
	}

}
