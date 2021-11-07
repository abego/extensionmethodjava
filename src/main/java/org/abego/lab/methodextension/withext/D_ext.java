package org.abego.lab.methodextension.withext;

// added, to hold method extensions for class D
class D_ext {
    // was: D#m2(StringBuilder)
    static void m2$class_D(D self, StringBuilder sb) {
        sb.append("D.m2\n");
        // was: super.m2(sb);
        C_ext.m2$class_C(self, sb);
    }

    static void m2(D self, StringBuilder sb) {
        if (self instanceof E)
            self.m2(sb);
        else
            m2$class_D(self, sb);
    }
}
