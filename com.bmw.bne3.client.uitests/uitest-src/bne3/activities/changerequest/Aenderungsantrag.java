package bne3.activities.changerequest;

import bne3.activities.EditorActivity;
import bne3.activities.changerequest.AenderungsantragAdapter;
import bne3.datatypes.ChangeRequestTitle;
import com.google.inject.Injector;
import de.msg.xt.mdt.base.ActivityLocator;
import de.msg.xt.mdt.base.ITestProtocol;
import de.msg.xt.mdt.base.TDslInjector;
import de.msg.xt.mdt.tdsl.basictypes.BooleanDT;
import de.msg.xt.mdt.tdsl.swtbot.TextControl;

public class Aenderungsantrag extends EditorActivity {
  private final static String ID = "Änderungsantrag";
  
  private final Injector injector = TDslInjector.getInjector();
  
  private final static ActivityLocator activityLocator = TDslInjector.getInjector().getInstance(ActivityLocator.class);
  
  private final ITestProtocol protocol = this.injector.getInstance(ITestProtocol.class);
  
  private AenderungsantragAdapter contextAdapter;
  
  public static Aenderungsantrag find() {
    return new Aenderungsantrag(activityLocator.find(ID, AenderungsantragAdapter.class));
  }
  
  public Aenderungsantrag() {
    super();
  }
  
  public Aenderungsantrag(final AenderungsantragAdapter contextAdapter) {
    super(contextAdapter);
    this.contextAdapter = contextAdapter;
  }
  
  public TextControl getTitelTextControl() {
    return this.contextAdapter.getTextControl("ChangeRequest.title");
  }
  
  public Aenderungsantrag titel_setText(final ChangeRequestTitle str) {
    this.protocol.appendControlOperationCall(this.getClass().getName(), "titel", TextControl.class.getName(), "setText", null, str.getValue().toString());
    getTitelTextControl().setText(str.getValue());
    return this;
  }
  
  public ChangeRequestTitle titel_getText() {
    String value = getTitelTextControl().getText(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "titel", TextControl.class.getName(), "getText", value != null ? value.toString() : "null" );
    return new bne3.datatypes.ChangeRequestTitle(value, bne3.datatypes.ChangeRequestTitleEquivalenceClass.getByValue(value));
  }
  
  public BooleanDT titel_isEnabled() {
    Boolean value = getTitelTextControl().isEnabled(); 
    this.protocol.appendControlOperationCall(this.getClass().getName(), "titel", TextControl.class.getName(), "isEnabled", value != null ? value.toString() : "null" );
    return new de.msg.xt.mdt.tdsl.basictypes.BooleanDT(value, de.msg.xt.mdt.tdsl.basictypes.BooleanDTEquivalenceClass.getByValue(value));
  }
}
