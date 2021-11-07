package org.abego.lab.methodextension.withext;

class F extends C {
    @Override
    void m1(StringBuilder sb) {
        sb.append("F.m1\n");
    }

    @Override
    void m1b(StringBuilder sb) {
        sb.append("F.m1b\n");
    }

    @Override
    void m2(StringBuilder sb) {
        sb.append("F.m2\n");
        C_ext.m2$class_C(this, sb);
    }
}
