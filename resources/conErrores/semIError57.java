///[Error:x|9]
//Una expresión usada como sentencia debe ser una llamada o una asignación

class A {
    int x;

    int m1(){
       var y = 10;
       m2().x;
    }

    A m2(){
        return new A();
    }
}


