package org.abego.extensionmethodjava.withext;

class D extends C {
    @Override
    void m1(StringBuilder sb) {
        sb.append("D.m1\n");
    }

    void m3(StringBuilder sb) {
        sb.append("D.m3\n");
        D_ext.m2(this, sb);
    }
}
