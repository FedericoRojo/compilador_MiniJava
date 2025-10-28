///[SinErrores]

class A {

    void m1(int z){
        var x = new A();
        {
            var y = 10;
        }
        var y = new A();
    }

}