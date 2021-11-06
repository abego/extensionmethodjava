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

    void m2(StringBuilder sb) {
        m2_class_B(sb);
    }

    // extracted from `void m2(StringBuilder sb)`
    // to directly access the specific method
    void m2_class_B(StringBuilder sb) {
        sb.append("B.m2\n");
        m1(sb);
    }
}

class C extends B {

//moved to C_ext
//    void m2(StringBuilder sb) {
//        sb.append("C.m2\n");
//        super.m2(sb);
//        m1b(sb);
//    }
}

// added, to hold method extensions for class C
class C_ext {
    private final C self;

    C_ext(C self) {
        this.self = self;
    }

    // was: C#m2(StringBuilder)
    void m2(StringBuilder sb) {
        sb.append("C.m2\n");
        // was: super.m2(sb);
        self.m2_class_B(sb);
        // was: m1b(sb);
        self.m1b(sb);
    }
}

class D extends C {
    void m1(StringBuilder sb) {
        sb.append("D.m1\n");
    }

    void m2(StringBuilder sb) {
        sb.append("D.m2\n");
        // was: super.m2(sb);
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
