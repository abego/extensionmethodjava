package org.abego.lab.methodextension.withext;

// added, to hold method extensions for class C
class C_ext {
    // was: C#m2(StringBuilder)
    static void m2$class_C(C self, StringBuilder sb) {
        sb.append("C.m2\n");
        // was: super.m2(sb);
        self.m2$class_B(sb);
        // was: m1b(sb);
        self.m1b(sb);
    }

    static void m2(C self, StringBuilder sb) {
        if (self instanceof D)
            D_ext.m2$class_D((D) self, sb);
        else if (self instanceof F)
            self.m2(sb);
        else
            m2$class_C(self, sb);
    }
}
