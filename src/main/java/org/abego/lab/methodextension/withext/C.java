package org.abego.lab.methodextension.withext;

class C extends B {

    void m4(StringBuilder sb) {
        sb.append("C.m4\n");
        C_ext.m2(this, sb);
        m1b(sb);
    }
}
