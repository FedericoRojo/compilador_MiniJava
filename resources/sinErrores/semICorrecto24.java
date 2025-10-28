///[SinErrores]
//

class B {
    void x(int a) { }
}

class A {
    B g() { return new B(); }

    void test() {
        g().x(2);
    }
}


