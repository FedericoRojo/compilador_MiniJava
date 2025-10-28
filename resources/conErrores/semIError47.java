///[Error:=|15]
//El tipo ReferenceType Animal no es compatible con ReferenceType Perro

class Animal {
    boolean tieneCorazon(){}
}

class Perro extends Animal {
    void ladrar(){}
}

class Init {
    Perro a;
    void main(){
        a = new Animal();
    }
}