///[SinErrores]

class A {

    char xa;
    char xb;
    boolean xba;

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

    void m2(){
        this.m1(10);
        xa = 'a';
        xb = 'b';
        xba = xa == xb;
        var xEsta = 10;
        var yEsta = (xEsta = 5);
    }

}