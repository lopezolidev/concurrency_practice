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

class Productor implements Runnable {
    private Tuberia tuberia ;
    private String alf = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" ;

    public Productor (Tuberia t) {
        tuberia = t ;
    }

    public void run(){
        char c ;

        for(int i = 0 ; i < 10 ; i++) {
            c = alf.charAt( (int)(Math.random() * 26) ) ;

            tuberia.lanzar(c) ;

            System.out.println("lanzado " + c + " al final de la tuberia. ") ;

            try{
                Thread.currentThread().sleep( (int) (Math.random() * 1000)) ;
                // para referenciar métodos de los threads debemos indicar que es el hilo actual
                // ya que estamos en una clase Runnable
            } catch(InterruptedException e) {
                ;
            }
        }
    }
}

class Consumidor implements Runnable {
    private Tuberia tuberia ;
    
    public Consumidor (Tuberia t) {
        tuberia = t ;
    }

    public void run(){
        char c ;

        for(int i = 0 ; i < 10 ; i++){
            c = tuberia.recoger() ;

            System.out.println("Recogido el caracter " + c) ;
            
            try {
                Thread.currentThread().sleep((int) (Math.random() * 2000)) ; 
                // para referenciar métodos de los threads debemos indicar que es el hilo actual
                // ya que estamos en una clase Runnable
            } catch(InterruptedException e ) { 
                ;
            }
        }
    }
}

class TubeTestRunnable {
    public static void main(String args[]) {
        Tuberia t = new Tuberia() ;
        Productor p = new Productor(t) ;
        Consumidor c = new Consumidor(t) ;
        Thread writer = new Thread(p) ;
        Thread reader = new Thread(c) ;

        writer.start() ;
        reader.start() ;
        try {
            reader.join(); 
            // todos los demás objetos esperarán hasta que este método termine para que los otros terminen de forma segura
            writer.interrupt();
        } catch (InterruptedException e) {;}
        
    }
}