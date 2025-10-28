///[Error:m2|6]
//No se puede llamar a un metodo estatico sobre una instancia de una clase
class A {
     void m(){

         var a = this.m2();

    }

    static void m2(){}
}

