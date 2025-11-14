///2&exitosamente

class A {
    void m1(){
        var x = 1;
        x = --x;
        debugPrint(x);
    }

    static void main() {
        var x = 10;
        B.m1(10);
    }
}

class B {
    static int m1(int x){
        m2();
        return x*2;
    }

    static int m2(){
        debugPrint(10);
        return 10;
    }
}


