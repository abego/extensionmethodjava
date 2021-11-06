package org.abego.lab.methodextension.withext;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WithExtensionsTest {
    @Test
    void B_m2() {
        StringBuilder sb = new StringBuilder();

        new B().m2(sb);

        assertEquals("" +
                "B.m2\n" +
                "A.m1\n", sb.toString());
    }

    @Test
    void C_m2() {
        StringBuilder sb = new StringBuilder();

        // was: new C().m2(sb);
        C_ext.m2(new C(), sb);

        assertEquals("" +
                "C.m2\n" +
                "B.m2\n" +
                "A.m1\n" +
                "A.m1b\n", sb.toString());
    }

    @Test
    void D_m2() {
        StringBuilder sb = new StringBuilder();

        // was: new D().m2(sb);
        D_ext.m2(new D(), sb);

        assertEquals("" +
                "D.m2\n" +
                "C.m2\n" +
                "B.m2\n" +
                "D.m1\n" +
                "A.m1b\n", sb.toString());
    }

    @Test
    void E_m2() {
        StringBuilder sb = new StringBuilder();

        new E().m2(sb);

        assertEquals("" +
                "E.m2\n" +
                "D.m2\n" +
                "C.m2\n" +
                "B.m2\n" +
                "E.m1\n" +
                "E.m1b\n", sb.toString());
    }
    @Test
    void D_m3() {
        StringBuilder sb = new StringBuilder();

        new D().m3(sb);

        assertEquals("" +
                "D.m3\n" +
                "D.m2\n" +
                "C.m2\n" +
                "B.m2\n" +
                "D.m1\n" +
                "A.m1b\n", sb.toString());
    }

    @Test
    void E_m3() {
        StringBuilder sb = new StringBuilder();

        new E().m3(sb);

        assertEquals("" +
                "D.m3\n" +
                "E.m2\n" +
                "D.m2\n" +
                "C.m2\n" +
                "B.m2\n" +
                "E.m1\n" +
                "E.m1b\n", sb.toString());
    }
}