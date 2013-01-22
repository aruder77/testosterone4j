package de.msg.xt.mdt.base;

import mp3manager.TextControl;
import mp3manager.TreeControl;

public class TextMP3ActivityAdapter implements mp3manager.ActivityAdapter {

    @Override
    public Object findContext(String id, String type) {
        return id;
    }

    @Override
    public TextControl getTextControl(final Object contextObject, final String controlName) {
        return new TextControl() {
            @Override
            public void setText(final String str) {
                System.out.println(contextObject + ".TextControl[" + controlName + "].setText(" + "str = " + str + ")");
            }

            @Override
            public String getText() {
                System.out.println(contextObject + ".TextControl[" + controlName + "].getText");
                return null;
            }
        };
    }

    @Override
    public Object performOperation(String id, String type, Object contextObject, String operationName) {
        System.out.println(id + "." + operationName);
        return contextObject;
    }

    @Override
    public Object beforeTest() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public TreeControl getTreeControl(final Object contextObject, final String controlName) {
        return new TreeControl() {

            @Override
            public void doubleClickItem() {
                System.out.println(contextObject + ".TreeControl[" + controlName + "].doubleClickItem");
            }
        };
    }

}
