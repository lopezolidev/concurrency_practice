class Cuenta {
    private int saldo ;


    public Cuenta (int s) {
        saldo = s ;
    }

    public synchronized void consultar() {
        System.out.println("Consulta - Saldo actual: " + saldo + "$");
    }

    public synchronized void retirar(int monto) {
        while((saldo - monto) < 0) {
            try {
                System.out.println(Thread.currentThread().getName() + " esperando: Fondos insuficientes (" + monto + "$)");
                wait() ;
            } catch(InterruptedException e) { 
                Thread.currentThread().interrupt(); // Restaurar estado de interrupción
                return;
                }
        }

        saldo -= monto ;
        System.out.println(Thread.currentThread().getName() + " retiró: " + monto + "$. Nuevo saldo: " + saldo + "$");
    } 

    public synchronized void depositar(int monto) {
        saldo += monto ;
        System.out.println(Thread.currentThread().getName() + " depositó: " + monto + "$. Nuevo saldo: " + saldo + "$");
        notifyAll() ;
    }
}

class Cajero_automatico_1 implements Runnable {
    private Cuenta cuenta ;


    public Cajero_automatico_1 (Cuenta c) {
        cuenta = c ;
    } 

    public void run() {

        cuenta.retirar(4000) ;
        try {
            Thread.currentThread().sleep( (int) (Math.random() * 1000)) ;
        } catch(InterruptedException e) {;} 

        cuenta.retirar(8000) ;
        try {
            Thread.currentThread().sleep( (int) (Math.random() * 1000)) ;
        } catch(InterruptedException e) {;} 

        cuenta.depositar(3000) ;
        try {
            Thread.currentThread().sleep( (int) (Math.random() * 1000)) ;
        } catch(InterruptedException e) {;} 

        cuenta.retirar(9000) ;
        try {
            Thread.currentThread().sleep( (int) (Math.random() * 1000)) ;
        } catch(InterruptedException e) {;} 

        cuenta.retirar(1000) ;
        try {
            Thread.currentThread().sleep( (int) (Math.random() * 1000)) ;
        } catch(InterruptedException e) {;} 

        cuenta.depositar(2000) ;
    }
}

class Cajero_automatico_2 implements Runnable {
    private Cuenta cuenta ;


    public Cajero_automatico_2 (Cuenta c) {
        cuenta = c ;
    } 

    public void run() {

        cuenta.depositar(4000) ;
        try {
            Thread.currentThread().sleep( (int) (Math.random() * 1000)) ;
        } catch(InterruptedException e) {;} 

        cuenta.retirar(2000) ;
        try {
            Thread.currentThread().sleep( (int) (Math.random() * 1000)) ;
        } catch(InterruptedException e) {;} 

        cuenta.depositar(3000) ;
        try {
            Thread.currentThread().sleep( (int) (Math.random() * 1000)) ;
        } catch(InterruptedException e) {;} 

        cuenta.retirar(1000) ;
        try {
            Thread.currentThread().sleep( (int) (Math.random() * 1000)) ;
        } catch(InterruptedException e) {;} 

        cuenta.depositar(5500) ;
        try {
            Thread.currentThread().sleep( (int) (Math.random() * 1000)) ;
        } catch(InterruptedException e) {;} 

        cuenta.retirar(2000) ;
        try {
            Thread.currentThread().sleep( (int) (Math.random() * 1000)) ;
        } catch(InterruptedException e) {;} 
    }
}

// mejora sobre cajeros
class Cajero implements Runnable {
    private final Cuenta cuenta;
    private final int[] transacciones;

    public Cajero(Cuenta c, int[] transacciones) {
        this.cuenta = c;
        this.transacciones = transacciones;
    }

    @Override
    public void run() {
        for (int monto : transacciones) {
            if (monto > 0) cuenta.depositar(monto);
            else cuenta.retirar(Math.abs(monto));

            try {
                Thread.sleep((int) (Math.random() * 500));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

class ATM {
    public static void main (String args[]) {
        Cuenta cuenta = new Cuenta(10000) ;
        int[] transacciones1 = {-4000, -8000, 3000, -9000, -1000, 2000};
        int[] transacciones2 = {4000, -2000, 3000, -1000, 5500, -2000};

        Cajero ca1 = new Cajero(cuenta, transacciones1) ;
        Cajero ca2 = new Cajero(cuenta, transacciones2) ;
        Thread atm1 = new Thread(ca1) ;
        Thread atm2 = new Thread(ca2) ;

        atm1.start() ;
        atm2.start() ;
    }


}