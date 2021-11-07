package org.abego.extensionmethodjava.noext;

abstract class A {
    void m1(StringBuilder sb) {
        sb.append("A.m1\n");
    }

    void m1b(StringBuilder sb) {
        sb.append("A.m1b\n");
    }

    abstract void m2(StringBuilder sb);
}
