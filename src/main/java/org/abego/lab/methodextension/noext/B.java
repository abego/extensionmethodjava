package org.abego.lab.methodextension.noext;

class B extends A {

    void m2(StringBuilder sb) {
        sb.append("B.m2\n");
        m1(sb);
    }
}
