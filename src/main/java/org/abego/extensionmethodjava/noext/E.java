package org.abego.extensionmethodjava.noext;

class E extends D {
    @Override
    void m1(StringBuilder sb) {
        sb.append("E.m1\n");
    }

    @Override
    void m1b(StringBuilder sb) {
        sb.append("E.m1b\n");
    }

    @Override
    void m2(StringBuilder sb) {
        sb.append("E.m2\n");
        super.m2(sb);
    }
}
