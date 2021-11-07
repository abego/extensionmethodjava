package org.abego.lab.methodextension.withext;

// added, to dispatch to extension methods
class B_ext {
    static void m2(B self, StringBuilder sb) {
        if (self instanceof C)
            C_ext.m2((C) self, sb);
        else
            self.m2(sb);
    }
}
