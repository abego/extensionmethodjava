package org.abego.lab.methodextension.withext;

class B extends A {

    void m2(StringBuilder sb) {
        m2$class_B(sb);
    }

    // extracted from `void m2(StringBuilder sb)`
    // to directly access the specific method
    void m2$class_B(StringBuilder sb) {
        sb.append("B.m2\n");
        m1(sb);
    }
}
