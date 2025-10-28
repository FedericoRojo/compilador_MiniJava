///[SinErrores]

class Animal {
    boolean tieneCorazon(){}
}

class Perro extends Animal {
    void ladrar(){}
}

class Init {
    Animal a;
    void main(){
        a = new Perro();
        a = new Animal();
    }
}