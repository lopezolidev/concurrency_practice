class Empacadora {
    private int almacen_proc ;
    private int almacen_cajas ;
    private int unidades_producidas ;

    public Empacadora() {
        almacen_proc = 10 ;
        almacen_cajas = 100 ;
    }

    public synchronized void consultar_procesadores() {
        System.out.println("En este momento, hay " + (10 - almacen_proc) + " procesadores sin empacar.");
    }

    public synchronized void consultar_cajas() {
        System.out.println("En este momento hay " + (100 - almacen_cajas) + " cajas sin empacar.");
    }

    public synchronized void consultar_empaques() {
        System.out.println("Se han producido " + unidades_producidas + " procesadores empacados.");
    }

    public synchronized void almacenar_procesador(){
        while(almacen_proc == 0) {
            try{
                System.out.println("Se han producido " + unidades_producidas +" unidades. Almacén de procesadores lleno, espere...");
                wait();
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        almacen_proc -= 1 ;
        System.out.println("Se ha agregado un procesador más al almacén.");
        notifyAll() ;
    }

    public synchronized void almacenar_caja(){
        while(almacen_cajas == 0) {
            try {
                System.out.println("Se han producido " + unidades_producidas +" unidades. Almacén de cajas lleno, espere...");
                wait();
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        almacen_cajas -= 1 ;
        System.out.println("Se ha agregado una caja más al almacén.");
        notifyAll() ;
    }

    public synchronized void empacar() {
        while(almacen_cajas == 100 || almacen_proc == 10) {
            try {
                System.out.println("No hay procesadores o cajas suficientes para empacar, espere...");
                wait() ;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        almacen_cajas += 1 ;
        almacen_proc += 1 ;
        unidades_producidas += 1 ;
        // se plantea que a un procesador va en una caja

        System.out.println("Despachado un procesador empacado.");
        notifyAll() ;
    }
}

class Maquina_procesador implements Runnable{
    private Empacadora empacadora ;
    private int tiempo_descanso ;
    private int procesadores ;

    public Maquina_procesador (Empacadora e, int td, int proc) {
        empacadora = e ;
        tiempo_descanso = td ;
        procesadores = proc ;
    }

    public void run() {
        for(int i = 0 ; i < procesadores ; i++) {
            empacadora.almacenar_procesador();

            try{
               Thread.currentThread().sleep((int)(Math.random() * tiempo_descanso)) ; 

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

class Maquina_cajas implements Runnable {
    private Empacadora empacadora ;
    private int cajas ;
    private int tiempo_descanso ;

    public Maquina_cajas (Empacadora e, int c, int td) {
        empacadora = e ;
        cajas = c;
        tiempo_descanso = td ;
    }

    public void run() {
        for(int i = 0 ; i < cajas ; i++) {
            empacadora.almacenar_caja();

            try{
               Thread.currentThread().sleep((int)(Math.random() * tiempo_descanso)) ; 

            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

class UnidadControl implements Runnable {
    private Empacadora empacadora ;
    private int unidades_a_producir ;
    private int tiempo_descanso ;

    public UnidadControl (Empacadora e ,int uap, int td) {
        empacadora = e ;
        unidades_a_producir = uap ;
        tiempo_descanso = td ;
    }

    public void run() {
        for(int i = 0 ; i < unidades_a_producir ; i++) {
            empacadora.empacar();
            
            try {
                Thread.currentThread().sleep((int)(Math.random() * tiempo_descanso)) ;
            }catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        empacadora.consultar_procesadores();
        empacadora.consultar_cajas();
        empacadora.consultar_empaques();
    }
}

public class FabricaProcesadores {
    public static void main(String args[]){
        int unidades_a_producir = 40 ;
        int tiempo_descanso1 = 1000 ;
        int tiempo_descanso2 = 2000 ;
        int tiempo_descanso3 = 3000 ;
        int tiempo_descanso4 = 4000 ;
        int tiempo_descanso5 = 5000 ;
        
        
        int procesadores1 =  15 ;
        int procesadores2 =  15 ;
        int procesadores3 =  15 ; 
        
        int cajas = 47 ;

        Empacadora emp = new Empacadora() ;
        Maquina_procesador mp1 = new Maquina_procesador(emp, tiempo_descanso1, procesadores1) ;
        Maquina_procesador mp2 = new Maquina_procesador(emp, tiempo_descanso2, procesadores2) ;
        Maquina_procesador mp3 = new Maquina_procesador(emp, tiempo_descanso3, procesadores3) ;

        Maquina_cajas mc = new Maquina_cajas(emp, cajas, tiempo_descanso4) ;

        UnidadControl uc = new UnidadControl(emp, unidades_a_producir, tiempo_descanso5) ;
        
        Thread maquina_procesadores_1 = new Thread(mp1) ;

        Thread maquina_procesadores_2 = new Thread(mp2) ;

        Thread maquina_procesadores_3 = new Thread(mp3) ;

        Thread maquina_cajas = new Thread(mc) ;

        Thread maquina_unidad_control = new Thread(uc) ;

        maquina_procesadores_1.start() ; 
        maquina_procesadores_2.start() ;
        maquina_procesadores_3.start() ;
        maquina_cajas.start() ;
        maquina_unidad_control.start() ;
    }    
}
