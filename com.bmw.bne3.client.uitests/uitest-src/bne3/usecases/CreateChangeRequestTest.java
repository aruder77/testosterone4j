package bne3.usecases;

import bne3.usecases.CreateChangeRequest;
import com.google.inject.Injector;
import de.msg.xt.mdt.base.ActivityLocator;
import de.msg.xt.mdt.base.GenerationHelper;
import de.msg.xt.mdt.base.Generator;
import de.msg.xt.mdt.base.ITestProtocol;
import de.msg.xt.mdt.base.Parameters;
import de.msg.xt.mdt.base.TDslInjector;
import de.msg.xt.mdt.base.TDslParameterized;
import de.msg.xt.mdt.base.TestDescriptor;
import java.util.Collection;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(TDslParameterized.class)
public class CreateChangeRequestTest {
  private final static String TEST_CASES_SERIALIZATION = "./CREATECHANGEREQUESTTEST_1369638543046";
  
  private final static Injector INJECTOR = TDslInjector.createInjector(TEST_CASES_SERIALIZATION);
  
  private final static ActivityLocator LOCATOR = INJECTOR.getInstance(ActivityLocator.class);
  
  private final ITestProtocol protocol = INJECTOR.getInstance(ITestProtocol.class);
  
  private int testNumber;
  
  private final CreateChangeRequest useCase;
  
  @Parameters
  public static Collection<Object[]> config() {
    GenerationHelper testHelper = INJECTOR.getInstance(GenerationHelper.class);
    Generator generator = INJECTOR.getInstance(Generator.class);
    LOCATOR.beforeTest();
    return testHelper.readOrGenerateTestCases(TEST_CASES_SERIALIZATION, generator, CreateChangeRequest.class);
  }
  
  public CreateChangeRequestTest(final TestDescriptor<CreateChangeRequest> testDescriptor) {
    this.testNumber = testDescriptor.getTestNumber();
    this.useCase = testDescriptor.getTestCase();
    INJECTOR.injectMembers(this);
  }
  
  @Before
  public void setup() {
    LOCATOR.beforeTest();
  }
  
  @After
  public void cleanup() {
    LOCATOR.afterTest();
  }
  
  @Test
  public void test() {
    this.protocol.newTest(String.valueOf(this.testNumber));
    try {
    	this.useCase.run();
    	this.protocol.append("Test OK");
    } catch (java.lang.RuntimeException ex) {
    	throw ex;
    } finally {
    	this.protocol.appendSummary();
    }
  }
}
