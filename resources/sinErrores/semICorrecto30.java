///[SinErrores]

class A {

}

class B extends A {

}

class C {

}

class Init {
    int i;
    char c;
    boolean b;
    String s;
    Object o;
    A classA;
    B classB;
    C classC;

    void m(){
        var integer = i % 5;
        integer = i + 5 - 5 / 5 % 10;
        b = b && true || false;
        b = classA != classB;
        b = classB != classA;
    }
}