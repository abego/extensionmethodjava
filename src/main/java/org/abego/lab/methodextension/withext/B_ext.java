package org.abego.lab.methodextension.withext;

final class B_ext {
    private B_ext() {}

    static void m2(B self, StringBuilder sb) {
        if (self instanceof C)
            C_ext.m2((C) self, sb);
        else
            self.m2(sb);
    }
}
