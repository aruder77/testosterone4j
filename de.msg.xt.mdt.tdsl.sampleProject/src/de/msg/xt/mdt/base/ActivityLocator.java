package de.msg.xt.mdt.base;

public interface ActivityLocator {

    Object beforeTest();

    <T extends ActivityAdapter> T find(String id, Class<T> class1);

}
