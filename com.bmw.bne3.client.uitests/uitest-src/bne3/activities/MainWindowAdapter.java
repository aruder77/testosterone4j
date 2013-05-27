package bne3.activities;

import bne3.activities.StdtoolkitActivityAdapter;

public interface MainWindowAdapter extends StdtoolkitActivityAdapter {
  public abstract Object openView();
  
  public abstract Object openBne3DevPerspective();
  
  public abstract Object openBne3Perspective();
  
  public abstract Object resetPerspective();
  
  public abstract Object openPerspective(final String perspectiveId);
  
  public abstract Object openPerspectiveByName(final String perspectiveName);
  
  public abstract Object findEthernetNavigator();
  
  public abstract Object findNewCodingEditor();
  
  public abstract Object findNewAenderungsantragEditor();
  
  public abstract Object findNewBasisdatentypEditor();
  
  public abstract Object findNewAufzaehlungstypEditor();
  
  public abstract Object findNewStrukturtypEditor();
  
  public abstract Object findNewUniontypEditor();
}
