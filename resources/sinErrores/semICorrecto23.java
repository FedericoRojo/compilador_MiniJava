///[SinErrores]
//


abstract class A {
    B x1;
    void m1(int x){

    }

    void m2(){
        m3().x1.b2.b1();
    }

    A m3(){

    }
}

class B {
    B b2;
    void b1(){}
}

