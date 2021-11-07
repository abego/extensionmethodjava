package org.abego.lab.methodextension.withext;

class E extends D {
    void m1(StringBuilder sb) {
        sb.append("E.m1\n");
    }

    void m1b(StringBuilder sb) {
        sb.append("E.m1b\n");
    }

    void m2(StringBuilder sb) {
        sb.append("E.m2\n");
        // was: super.m2(sb);
        D_ext.m2$class_D(this, sb);
    }
}
