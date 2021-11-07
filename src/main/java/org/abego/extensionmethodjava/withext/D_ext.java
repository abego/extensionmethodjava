package org.abego.extensionmethodjava.withext;

final class D_ext {
    private D_ext() {}

    static void m2$class_D(D self, StringBuilder sb) {
        sb.append("D.m2\n");
        C_ext.m2$class_C(self, sb);
    }

    static void m2(D self, StringBuilder sb) {
        if (self instanceof E)
            self.m2(sb);
        else
            m2$class_D(self, sb);
    }
}
