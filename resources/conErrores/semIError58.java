///[Error:=|9]
//Una expresión usada como sentencia debe ser una llamada o una asignación

class A {
    int x;
    A nueva;

    int m1(){
       var y = (nueva.m2() = ((5 + 10) / x ));
    }

    A m2(){
        return new A();
    }
}


