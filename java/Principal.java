// Ejercicio 2: prueba de código sin synchronize
// recurso crítico: contador

public class C implements Runnable {
    private int contador = 0;

    public synchronized void incrementar() {
        contador++;
    }

    public void run() {
        for(int j = 1; j <= 1000; j++) {
            incrementar(); 
        }
    }

    public int getContador() {
        return contador;
    }
}


public class Principal {
  public static void main (String[] args) {

    C clase = new C() ;
    // creación de la clase con el incremento
    
    Thread hiloA = new Thread(clase) ;
    Thread hiloB = new Thread(clase) ;
    // creación de hilos con la "tarea" de la clase C
    
      hiloA.start() ;
      hiloB.start() ;
      
      hiloA.join();
      hiloB.join();

      System.out.printLn("El valor del contador es: " + clase.getContador()) ;
    }  
}
