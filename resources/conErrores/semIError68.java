///[Error:m1|15]

class B {

    static int m1(){
        return 10;
    }

}

class A {
    B b;
    void m1(){
        b = new B();
        b.m1();
    }
}


