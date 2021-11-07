package org.abego.lab.methodextension.withext;

final class C_ext {
    private C_ext() {}

    static void m2$class_C(C self, StringBuilder sb) {
        sb.append("C.m2\n");
        self.m2$class_B(sb);
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
