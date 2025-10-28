///[SinErrores]

class A {
    A x;
    int x1;

    void m1(int z){
        x1 = A.m2().m3();
        x = this;
    }

    int m3(){
        return 5;
    }

    static A m2(){
        return new A();
    }



}