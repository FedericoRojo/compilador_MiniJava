///[Error:m3|7]
///Se quiere llamar al metodo estatico m3 pero no fue declarado


class A {
    void m1(int p1){
        C.m3();
    }
}

class C {
    void m2(){}
}