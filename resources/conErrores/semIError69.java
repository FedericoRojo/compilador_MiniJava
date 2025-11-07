///[Error:=|8]
/// El lado izquierdo de la asignación no es asignable

class A {
    A a1;

    void m1(int p1){
        a1.m2() = 5;
    }

    int m2(){

    }

}