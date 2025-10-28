///[SinErrores]
class B {
    int x;
}

class A {
    B y;

    void test() {
        var z = y.x;
    }
}
