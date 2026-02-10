// Monitor
class Tuberia {
    private char buffer[] = new char[6] ;
    private int siguiente = 0 ;
    private boolean estaLlena = false ;
    private boolean estaVacia = true ;

    public synchronized char recoger() {
        char c ;

        while(estaVacia) {
            try {
                wait() ;
            } catch (InterruptedException e ) { ; }
        }

        siguiente-- ;

        if(siguiente == 0) {
            estaVacia = true ;
        } 

        estaLlena = false ;

        notify() ;

        c = buffer[siguiente] ;
        return c ;
    }

    public synchronized void lanzar(char c) {
        while(estaLlena) {
            try {
                wait() ;
            } catch (InterruptedException e ) { ; }
        }

        buffer[siguiente] = c ;
        siguiente ++ ;

        if(siguiente == 6) {
            estaLlena = true ;
        }
        
        estaVacia = false ;

        notify() ;
    }
}

class Productor extends Thread {
    private Tuberia tuberia ;
    private String alfabeto = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" ;

    public Productor (Tuberia t) {
        tuberia = t ;
    }

    public void run() {
        char c ;

        for(int i = 0 ; i < 10 ; i++) {
            c = alfabeto.charAt( (int)(Math.random() * 26) ) ;

            tuberia.lanzar(c) ;

            System.out.println("lanzado " + c + " al final de la tuberia. ") ;

            try{
                sleep( (int) (Math.random() * 1000)) ;
            } catch(InterruptedException e) {
                ;
            }
        }
    }
}

class Consumidor extends Thread{
    private Tuberia tuberia ;

    public Consumidor(Tuberia t) {
        tuberia = t ;
    }

    public void run() {
        char c ;

        for(int i = 0 ; i < 10 ; i++){
            c = tuberia.recoger() ;

            System.out.println("Recogido el caracter " + c) ;
            
            try {
                sleep((int) (Math.random() * 2000)) ;
            } catch(InterruptedException e ) { 
                ;
            }
        }
    }
}

class TubeTest {
    public static void main(String args[]) {
        Tuberia t = new Tuberia() ;
        Consumidor c = new Consumidor(t) ;
        Productor p = new Productor(t) ;
        c.start() ;
        p.start() ;
    }
}