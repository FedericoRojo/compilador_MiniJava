///[SinErrores]

class A {
    int a1;

    void m1(int p1){
        p1 = m2().m3();
    }

    int m3(){ return 10; }

    static A m2(){ return new A(); }
}