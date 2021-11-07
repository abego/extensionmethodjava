package org.abego.extensionmethodjava.withext;

class B extends A {

    void m2(StringBuilder sb) {
        m2$class_B(sb);
    }

    void m2$class_B(StringBuilder sb) {
        sb.append("B.m2\n");
        m1(sb);
    }
}
