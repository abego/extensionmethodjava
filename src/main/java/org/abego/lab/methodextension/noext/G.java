package org.abego.lab.methodextension.noext;

class G extends B {
    @Override
    void m1(StringBuilder sb) {
        sb.append("G.m1\n");
    }

    @Override
    void m1b(StringBuilder sb) {
        sb.append("G.m1b\n");
    }

    @Override
    void m2(StringBuilder sb) {
        sb.append("G.m2\n");
        super.m2(sb);
    }
}
