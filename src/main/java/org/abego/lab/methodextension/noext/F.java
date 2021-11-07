package org.abego.lab.methodextension.noext;

class F extends C {
    void m1(StringBuilder sb) {
        sb.append("F.m1\n");
    }

    void m1b(StringBuilder sb) {
        sb.append("F.m1b\n");
    }

    void m2(StringBuilder sb) {
        sb.append("F.m2\n");
        super.m2(sb);
    }
}
