///[Error:A|10]
//Una expresión usada como sentencia debe ser una llamada o una asignación

class A {
    public A(int x){

    }
    int m1(){
        var nuevaInst = new A(10);
        nuevaInst = new A(10, 'a');
    }
}


