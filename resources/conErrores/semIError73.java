///[Error:m3|10]
///La expresión es de un tipo que no tiene ni metodo ni atributo con nombre m3


class A {
    B a1;
    int v1;

    void m1(B p1){
        v1= (a1).m3();
    }
}

class B extends A{
    int m2() {
        return 10;
    }
}