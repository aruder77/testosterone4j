package de.msg.xt.mdt.base;

import java.util.List;

public interface Generator {

    <E extends Runnable> List<E> generate(Class<E> clazz);

    <T extends DataType> T generateDataTypeValue(Class<T> clazz, String id);
}
