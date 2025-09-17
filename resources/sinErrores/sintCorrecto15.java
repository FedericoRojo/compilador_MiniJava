///[SinErrores]

class Vehiculo {
    int velocidad;
    boolean encendido;

    public Vehiculo() {
        var a = 10;
        this.velocidad = 0;
        this.encendido = false;
    }

    void arrancar() {
        if (!this.encendido) {
            this.encendido = true;
        }
    }
}

final class Auto extends Vehiculo {
    int puertas;
    Motor motor;

    public Auto(int p) {
        this.puertas = p;
        this.motor = new Motor();
    }

    static int obtenerMaxVelocidad() {
        return 180;
    }

    abstract void metodoAbstracto();

    void acelerar(int cantidad) {
        if (this.motor.estaEncendido()) {
            while (cantidad > 0) {
                this.velocidad = 5;
                cantidad = cantidad - 1;
            }
        } else {
            this.arrancar();
        }

        var max = Auto.obtenerMaxVelocidad();
        if (this.velocidad > max) {
            this.velocidad = max;
        }
    }

    Vehiculo obtenerVehiculo() {
        return this;
    }
}






