///[Error:=|12]
//Una expresión usada como sentencia debe ser una llamada o una asignación

class A {
    int x;
    boolean b;
    A nueva;
    int m1(){

        b = true && b;
        b = ++10 < x;
        var y = (nueva.m2().b = ((5 + 10) / x ));

    }

    A m2(){
        return new A();
    }
}


