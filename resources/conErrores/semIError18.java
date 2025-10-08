///[Error:m1|9]
//Redefinicion de un metodo final

class A{
    final B m1(){}
}

class B extends A{
    B m1(){}
}


