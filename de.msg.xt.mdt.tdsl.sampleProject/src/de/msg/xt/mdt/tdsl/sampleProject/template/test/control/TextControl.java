package de.msg.xt.mdt.tdsl.sampleProject.template.test.control;

public class TextControl {
    private final String id;

    private String str;

    public TextControl(final String id) {
        this.id = id;

    }

    public void setText(final String str) {
        this.str = str;
        System.out.println("Setting textControl " + this.id + " to " + str);
    }

    public String getText() {
        System.out.println("Getting textControl " + this.id + ": " + this.str);
        return this.str;
    }

    public void invokeAction() {
        System.out.println("Invoked textControl " + this.id + "'s invokeAction method!");
    }
}
