class ParkingLot {
    private int puestos_totales ;
    private int puestos_ocupados ;


    public ParkingLot (int p) {
        puestos_totales = p ;
        puestos_ocupados = 0 ;
    }

    public synchronized void consultar() {
        System.out.println("Hay en este momento: " + (puestos_totales - puestos_ocupados) + " puestos disponibles") ;
    }

    public synchronized void estacionar() {
        while(puestos_ocupados == puestos_totales){
            try{
                System.out.println("Estacionamiento lleno, vuelva más tarde...") ;
                wait() ;
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt(); // Restaurar estado de interrupción
                return;
            }
        }

        puestos_ocupados += 1 ;
        System.out.println(Thread.currentThread().getName() + ". Estacionamiento con: " + puestos_ocupados) ;
        notify() ; 
        /* 
        No usamos notifyAll() ya que solo hay 2 taquillas de pago (hilos).

        Si una taquilla estaba por procesar un vehiculo que salía y no habia al menos 1, se desbloquea y puede procesar su pago
        */ 
    }

    public synchronized void pagar(){
        while(puestos_ocupados == 0){
            try{
                System.out.println(Thread.currentThread().getName() + ". No hay vehículos estacionados todavía") ;
                wait() ;
            } catch(InterruptedException e) {
                Thread.currentThread().interrupt() ;
                return ;
            }
        }

        puestos_ocupados -= 1 ;
        System.out.println(Thread.currentThread().getName() + ". Estacionamiento con: " + puestos_ocupados) ;
        notify() ;
        /*
        si la taquilla que daba tickets estaba bloqueada por estar el estacinamiento lleno, 
        ahora se desbloquea al haber al menos 1 puesto libre 
         */
    }
}

class TaquillaTicket implements Runnable {
    private ParkingLot estacionamiento ;

    public TaquillaTicket(ParkingLot e) {
        estacionamiento = e ;
    }

    public void run() {
        for (int i = 0; i < 10 ; i++){
            estacionamiento.estacionar() ;

            try{
                Thread.currentThread().sleep( (int) (Math.random() * 3000)) ;
            } catch(InterruptedException e ){
                Thread.currentThread().interrupt();
            }
        }
    }
}

class TaquillaPago implements Runnable {
    private ParkingLot estacionamiento ;

    public TaquillaPago(ParkingLot e) {
        estacionamiento = e ;
    }

    public void run() {
        for (int i = 0; i < 10 ; i++){
            estacionamiento.pagar() ;

            try{
                Thread.currentThread().sleep( (int) (Math.random() * 3000)) ;
            } catch(InterruptedException e ){
                Thread.currentThread().interrupt();
            }
        }
    }
}

class Estacionamiento {
    public static void main(String args[]){
        ParkingLot pl = new ParkingLot(10) ;
        TaquillaTicket tt = new TaquillaTicket(pl) ;
        TaquillaPago tp = new TaquillaPago(pl) ;
        Thread taquilla_1 = new Thread(tt) ;
        Thread taquilla_2 = new Thread(tp) ;

        taquilla_1.start() ;
        taquilla_2.start() ;
    }
}