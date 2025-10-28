///[Error:x|10]
//La llamada a metodo g retorna un tipo que no tiene ni metodo ni atributo con nombre x

class B { }

class A {
    B g() { return new B(); }

    void test() {
        g().x(2);
    }
}









