package de.msg.xt.mdt.tdsl.basictypes;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class IntegerDTEquivalenceClassTest {

    @Before
    public void setUp() throws Exception {
    }

    // @Test
    // public void testMinusMinus() {
    // IntegerDTEquivalenceClass clazz = IntegerDTEquivalenceClass.minusMinus;
    // Integer i = clazz.getValue();
    // System.out.println("MinusMinus value: " + i);
    // assertTrue(i == -1000 || i == -1500 || i == -100);
    //
    // IntegerDTEquivalenceClass clazz1 =
    // IntegerDTEquivalenceClass.getByValue(-1000);
    // assertEquals(IntegerDTEquivalenceClass.minusMinus, clazz1);
    // IntegerDTEquivalenceClass clazz2 =
    // IntegerDTEquivalenceClass.getByValue(-1500);
    // assertEquals(IntegerDTEquivalenceClass.minusMinus, clazz2);
    // IntegerDTEquivalenceClass clazz3 =
    // IntegerDTEquivalenceClass.getByValue(-100);
    // assertEquals(IntegerDTEquivalenceClass.minusMinus, clazz3);
    // }
    //
    // @Test
    // public void testMinus() {
    // IntegerDTEquivalenceClass clazz = IntegerDTEquivalenceClass.minus;
    // Integer i = clazz.getValue();
    // System.out.println("Minus value: " + i);
    // assertTrue(i >= -99 && i < -1);
    //
    // IntegerDTEquivalenceClass clazz1 =
    // IntegerDTEquivalenceClass.getByValue(-99);
    // assertEquals(IntegerDTEquivalenceClass.minus, clazz1);
    // IntegerDTEquivalenceClass clazz2 =
    // IntegerDTEquivalenceClass.getByValue(-50);
    // assertEquals(IntegerDTEquivalenceClass.minus, clazz2);
    // IntegerDTEquivalenceClass clazz3 =
    // IntegerDTEquivalenceClass.getByValue(-2);
    // assertEquals(IntegerDTEquivalenceClass.minus, clazz3);
    // }

    @Test
    public void testZero() {
        IntegerDTEquivalenceClass clazz = IntegerDTEquivalenceClass.zero;
        Integer i = clazz.getValue();
        assertEquals(Integer.valueOf(0), i);

        IntegerDTEquivalenceClass clazz1 = IntegerDTEquivalenceClass.getByValue(0);
        assertEquals(IntegerDTEquivalenceClass.zero, clazz1);
    }

    // @Test
    // public void testSmall() {
    // IntegerDTEquivalenceClass clazz = IntegerDTEquivalenceClass.small;
    // Integer i = clazz.getValue();
    // System.out.println("Small value: " + i);
    // assertTrue(i > 0 && i < 100);
    //
    // IntegerDTEquivalenceClass clazz1 =
    // IntegerDTEquivalenceClass.getByValue(1);
    // assertEquals(IntegerDTEquivalenceClass.small, clazz1);
    // IntegerDTEquivalenceClass clazz2 =
    // IntegerDTEquivalenceClass.getByValue(50);
    // assertEquals(IntegerDTEquivalenceClass.small, clazz2);
    // IntegerDTEquivalenceClass clazz3 =
    // IntegerDTEquivalenceClass.getByValue(99);
    // assertEquals(IntegerDTEquivalenceClass.small, clazz3);
    // }

    @Test
    public void testBig() {
        IntegerDTEquivalenceClass clazz = IntegerDTEquivalenceClass.big;
        Integer i = clazz.getValue();
        assertEquals(Integer.valueOf(310052345), i);

        IntegerDTEquivalenceClass clazz1 = IntegerDTEquivalenceClass.getByValue(310052345);
        assertEquals(IntegerDTEquivalenceClass.big, clazz1);
    }
}
