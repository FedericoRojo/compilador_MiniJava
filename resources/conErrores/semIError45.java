///[Error:x|11]
//No se puede acceder a miembros de un tipo primitivo

class A {
    int g() {
        return 5;
    }

    void test() {

        g().x(2);
    }
}









