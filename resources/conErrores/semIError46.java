///[Error:x|10]
//Se quiere llamar al metodo estatico x pero no es estatico

class A {
    void x() { }
}

class B {
    void test() {
        A.x();
    }
}








