///[Error:return|22]
//El tipo ReferenceType String no es comptabile con PrimitiveType int

class A {

    int m1(){
        if(true){
            return 1;
        }
        {
            var x = 10;
            {
                var y = "hola";
            }
            var y = "Hola como andas";
            {
                var z = true;
                {
                    var z1 = false;
                    {
                        var x1 = 1000;
                        return y;
                    }
                }
            }
        }
        return 5;
    }
}


