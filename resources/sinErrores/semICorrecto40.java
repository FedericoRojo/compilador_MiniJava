///[SinErrores]

class A {
    int a;
    public A(int x){

    }
    void m1(){
        if( a < 10 ){
            var localIf = 'a';
        }else{
            var localElse = 10;
        }
    }
}