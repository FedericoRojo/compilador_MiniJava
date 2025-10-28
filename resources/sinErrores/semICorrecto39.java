///[SinErrores]

class A {
    A x;
    int x1;
    int x2;

    void m1(int z){
        x2 = A.m2().x.x1;
    }

    int m3(){
        return 5;
    }

    static A m2(){
        return new A();
    }



}