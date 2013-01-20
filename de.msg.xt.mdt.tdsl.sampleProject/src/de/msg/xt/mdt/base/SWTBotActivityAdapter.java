package de.msg.xt.mdt.base;

import de.msg.xt.mdt.tdsl.sampleProject.template.test.control.TextControl;

public class SWTBotActivityAdapter implements ActivityAdapter {

    @Override
    public Object findContext(String id, String type) {
        return type + "[" + id + "]";
    }

    @Override
    public TextControl getTextControl(final Object contextObject, final String controlName) {
        return new TextControl() {
            @Override
            public void setText(final String str) {
                System.out.println(contextObject + " TextControl[" + controlName + "].setText(" + "str = " + str + ") called.");

            }

            @Override
            public String getText() {
                System.out.println(contextObject + " TextControl[" + controlName + "].getText(" + ") called.");
                return null;
            }

            @Override
            public void invokeAction() {
                System.out.println(contextObject + " TextControl[" + controlName + "].invokeAction(" + ") called.");

            }
        };
    }

    @Override
    public Object performOperation(Object contextObject, String operationName) {
        System.out.println(contextObject + "." + operationName + " called.");
        return contextObject;
    }

}
