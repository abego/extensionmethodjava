package org.abego.lab.methodextension.withext;

class A {
    void m1(StringBuilder sb) {
        sb.append("A.m1\n");
    }

    void m1b(StringBuilder sb) {
        sb.append("A.m1b\n");
    }
}

class B extends A {

    void m2_class_B(StringBuilder sb) {
        sb.append("B.m2\n");
        m1(sb);
    }

    void m2(StringBuilder sb) {
        m2_class_B(sb);
    }
}

class C extends B {
}

class C_ext {
    private final C self;

    C_ext(C self) {
        this.self = self;
    }

    void m2(StringBuilder sb) {
        sb.append("C.m2\n");
        self.m2_class_B(sb);
        self.m1b(sb);
    }
}

class D extends C {
    void m1(StringBuilder sb) {
        sb.append("D.m1\n");
    }

    void m2(StringBuilder sb) {
        sb.append("D.m2\n");
        new C_ext(this).m2(sb);
    }

    void m3(StringBuilder sb) {
        sb.append("D.m3\n");
        m2(sb);
    }
}

class E extends D {
    void m1(StringBuilder sb) {
        sb.append("E.m1\n");
    }

    void m1b(StringBuilder sb) {
        sb.append("E.m1b\n");
    }

    void m2(StringBuilder sb) {
        sb.append("E.m2\n");
        super.m2(sb);
    }
}
