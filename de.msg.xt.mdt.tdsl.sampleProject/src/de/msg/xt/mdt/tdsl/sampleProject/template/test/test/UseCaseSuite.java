package de.msg.xt.mdt.tdsl.sampleProject.template.test.test;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class UseCaseSuite<T> {

    @XmlElement
    List<T> testCases;

    public UseCaseSuite() {
    }

    public UseCaseSuite(List<T> testCases) {
        this.testCases = testCases;
    }

    public void setTestCases(List<T> testCases) {
        this.testCases = testCases;
    }

    public List<T> getTestCases() {
        return this.testCases;
    }
}