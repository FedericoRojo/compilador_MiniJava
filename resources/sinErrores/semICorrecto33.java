///[SinErrores]

class A {

    void m1(int z){
        var x = new A();
        var i = 1;
        while(1 > i){
            i = ++i;
            if(i > 10){
                return;
            }
        }
        var y = new A();
    }

}