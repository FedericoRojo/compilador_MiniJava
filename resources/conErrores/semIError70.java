///[Error:m2|7]
///Se intenta llamar a un metodo estatico en un contexto dinamico

class A {

    static void m1(int p1){
        m2();
    }

    void m2(){

    }

}