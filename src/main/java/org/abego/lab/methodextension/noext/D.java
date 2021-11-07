package org.abego.lab.methodextension.noext;

class D extends C {

    @Override
    void m1(StringBuilder sb) {
        sb.append("D.m1\n");
    }

    @Override
    void m2(StringBuilder sb) {
        sb.append("D.m2\n");
        super.m2(sb);
    }

    void m3(StringBuilder sb) {
        sb.append("D.m3\n");
        m2(sb);
    }
}
