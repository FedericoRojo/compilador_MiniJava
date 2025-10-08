///[Error:m|5]
//Static no puede sobreescribir static

class P { static int m() { return 0; } }
class C extends P { static int m() { return 1; } }






