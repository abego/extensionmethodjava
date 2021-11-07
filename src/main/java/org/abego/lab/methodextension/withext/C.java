package org.abego.lab.methodextension.withext;

class C extends B {

//moved to C_ext
//    void m2(StringBuilder sb) {
//        sb.append("C.m2\n");
//        super.m2(sb);
//        m1b(sb);
//    }

    void m4(StringBuilder sb) {
        sb.append("C.m4\n");
        C_ext.m2(this, sb);
        m1b(sb);
    }
}
