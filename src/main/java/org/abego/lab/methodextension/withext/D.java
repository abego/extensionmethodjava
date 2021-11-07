package org.abego.lab.methodextension.withext;

class D extends C {
    void m1(StringBuilder sb) {
        sb.append("D.m1\n");
    }

//moved to D_ext
//    void m2(StringBuilder sb) {
//        sb.append("D.m2\n");
//        // was: super.m2(sb);
//        new C_ext(this).m2(sb);
//    }

    void m3(StringBuilder sb) {
        sb.append("D.m3\n");
        // was: m2(sb);
        D_ext.m2(this, sb);
    }
}
