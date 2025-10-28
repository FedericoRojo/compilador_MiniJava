///[Error:m1|9]
//Redefinicion de un metodo static

class A{
    static B m1(){}
}

class B extends A{
    B m1(){}
}


