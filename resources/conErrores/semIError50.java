///[Error:==|18]
//Tipos incompatibles en comparación
class Animal {

}

class Perro extends Animal{
}

class Silla {}

class Init {
    Animal a;
    Perro p;
    Silla s;
    void main(){
        var b = a == p;
        b = s == a;
    }
}
