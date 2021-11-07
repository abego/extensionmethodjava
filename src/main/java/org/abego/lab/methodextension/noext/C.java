package org.abego.lab.methodextension.noext;

class C extends B {

    @Override
    void m2(StringBuilder sb) {
        sb.append("C.m2\n");
        super.m2(sb);
        m1b(sb);
    }

    void m4(StringBuilder sb) {
        sb.append("C.m4\n");
        m2(sb);
        m1b(sb);
    }
}
