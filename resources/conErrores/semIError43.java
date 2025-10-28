///[Error:b2|12]
//Una expresión usada como sentencia debe ser una llamada o una asignación


abstract class A {
    B x1;
    void m1(int x){

    }

    void m2(){
        m3().x1.b2;
    }

    A m3(){

    }
}

class B {
    B b2;
    void b1(){}
}








