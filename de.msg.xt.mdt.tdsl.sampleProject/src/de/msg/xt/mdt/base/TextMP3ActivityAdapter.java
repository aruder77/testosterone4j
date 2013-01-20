package de.msg.xt.mdt.base;

public class TextMP3ActivityAdapter implements mp3manager.ActivityAdapter {

    @Override
    public Object findContext(String id, String type) {
        return type + "[" + id + "]";
    }

    // @Override
    // public TextControl getTextControl(final Object contextObject, final
    // String controlName) {
    // return new TextControl() {
    // @Override
    // public void setText(final String str) {
    // System.out.println(contextObject + " TextControl[" + controlName +
    // "].setText(" + "str = " + str + ") called.");
    //
    // }
    //
    // @Override
    // public String getText() {
    // System.out.println(contextObject + " TextControl[" + controlName +
    // "].getText(" + ") called.");
    // return null;
    // }
    //
    // @Override
    // public void invokeAction() {
    // System.out.println(contextObject + " TextControl[" + controlName +
    // "].invokeAction(" + ") called.");
    //
    // }
    // };
    // }

    @Override
    public Object performOperation(String id, String type, Object contextObject, String operationName) {
        System.out.println(type + "[" + id + "]." + operationName);
        return contextObject;
    }

    @Override
    public Object beforeTest() {
        // TODO Auto-generated method stub
        return null;
    }

}
