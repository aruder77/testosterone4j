package de.msg.xt.mdt.base;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public class BaseUseCase {

    @XmlTransient
    protected Generator generator;

    @XmlElement
    protected Map<String, DataType> generatedData = new HashMap<String, DataType>();

    @XmlElement
    protected Map<String, BaseUseCase> generatedSubUseCases = new HashMap<String, BaseUseCase>();

    public <T extends DataType> T getOrGenerateValue(Class<T> clazz, String key) {
        return getOrGenerateValue(clazz, key, null);
    }

    public <T extends DataType> T getOrGenerateValue(Class<T> clazz, String key, Tag[] tags) {
        T value = null;
        if (this.generatedData.containsKey(key)) {
            value = (T) this.generatedData.get(key);
        } else {
            value = this.generator.generateDataTypeValue(clazz, key, tags);
            this.generatedData.put(key, value);
        }
        return value;
    }

    protected <E extends BaseUseCase> E getOrGenerateSubUseCase(Class<E> clazz, String key) {
        return this.getOrGenerateSubUseCase(clazz, key, null);
    }

    protected <E extends BaseUseCase> E getOrGenerateSubUseCase(Class<E> clazz, String key, Tag[] tags) {
        E value = null;
        if (this.generatedSubUseCases.containsKey(key)) {
            value = (E) this.generatedSubUseCases.get(key);
        } else {
            try {
                value = clazz.getConstructor(Generator.class).newInstance(this.generator);
                this.generatedSubUseCases.put(key, value);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                    | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        }
        return value;
    }
}
