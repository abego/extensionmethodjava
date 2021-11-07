package org.abego.lab.methodextension.withext;

class F extends C {
    void m1(StringBuilder sb) {
        sb.append("F.m1\n");
    }

    void m1b(StringBuilder sb) {
        sb.append("F.m1b\n");
    }

    void m2(StringBuilder sb) {
        sb.append("F.m2\n");
        // was: super.m2(sb);
        C_ext.m2$class_C(this, sb);
    }
}
