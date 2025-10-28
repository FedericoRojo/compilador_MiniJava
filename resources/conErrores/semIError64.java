///[Error:m2|10]

class A {
    public A(int x){

    }
    void m1(){

        A.m2(10, 'a');
        A.m2('a', 10);

    }

    static void m2(int x, char c){

    }
}


