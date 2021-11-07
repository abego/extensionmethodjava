package org.abego.lab.methodextension.noext;

class G extends B {
    void m1(StringBuilder sb) {
        sb.append("G.m1\n");
    }

    void m1b(StringBuilder sb) {
        sb.append("G.m1b\n");
    }

    void m2(StringBuilder sb) {
        sb.append("G.m2\n");
        super.m2(sb);
    }
}
