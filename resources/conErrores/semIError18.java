///[Error:m1|9]
//Redefinición incorrecta de metodo

class A{
    void m1(int a){}
}

class B extends A{
    void m1(A b){}
}

