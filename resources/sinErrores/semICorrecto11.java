///[SinErrores]
// 

abstract class A { abstract int m(); }
abstract class B extends A { int k() { return 1; } }
class C extends B { int m() { return 0; } }





