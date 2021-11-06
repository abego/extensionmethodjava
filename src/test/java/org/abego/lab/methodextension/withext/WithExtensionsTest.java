package org.abego.lab.methodextension.withext;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class WithExtensionsTest {
    @Test
    void B_m2() {
        StringBuilder sb = new StringBuilder();

        B b = new B();
        B_ext.m2(b, sb);

        assertEquals("" +
                "B.m2\n" +
                "A.m1\n", sb.toString());
    }

    @Test
    void D_as_B_m2() {
        StringBuilder sb = new StringBuilder();

        B b = new D();
        B_ext.m2(b, sb);

        assertEquals("" +
                "D.m2\n" +
                "C.m2\n" +
                "B.m2\n" +
                "D.m1\n" +
                "A.m1b\n", sb.toString());
    }

    @Test
    void G_as_B_m2() {
        StringBuilder sb = new StringBuilder();

        B b = new G();
        B_ext.m2(b, sb);

        assertEquals("" +
                "G.m2\n" +
                "B.m2\n" +
                "G.m1\n", sb.toString());
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

    @Test
    void F_m2() {
        StringBuilder sb = new StringBuilder();

        new F().m2(sb);

        assertEquals("" +
                "F.m2\n" +
                "C.m2\n" +
                "B.m2\n" +
                "F.m1\n" +
                "F.m1b\n", sb.toString());
    }

    @Test
    void F_m4() {
        StringBuilder sb = new StringBuilder();

        new F().m4(sb);

        assertEquals("" +
                "C.m4\n" +
                "F.m2\n" +
                "C.m2\n" +
                "B.m2\n" +
                "F.m1\n" +
                "F.m1b\n" +
                "F.m1b\n", sb.toString());
    }


    @Test
    void G_m2() {
        StringBuilder sb = new StringBuilder();

        new G().m2(sb);

        assertEquals("" +
                "G.m2\n" +
                "B.m2\n" +
                "G.m1\n", sb.toString());
    }
}