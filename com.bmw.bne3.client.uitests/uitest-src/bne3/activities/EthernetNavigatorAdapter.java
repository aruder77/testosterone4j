package bne3.activities;

import bne3.activities.ViewActivityAdapter;

public interface EthernetNavigatorAdapter extends ViewActivityAdapter {
  public abstract Object activateChangeRequest(final String title);
  
  public abstract Object openCoding(final String title);
  
  public abstract Object openChangeRequest(final String title);
  
  public abstract Object openBasisDatentyp(final String title);
  
  public abstract Object openAufzaehlungstyp(final String title);
  
  public abstract Object openStrukturtyp(final String title);
  
  public abstract Object openUniontyp(final String title);
  
  public abstract Object openPaket(final String title);
}
