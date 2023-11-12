public class Semaphore {
    protected int value = 0;
    protected Semaphore() {value = 0;}
    protected Semaphore(int initial){value = initial;}
    public synchronized void P(String massage){
        value--;
        if (value < 0) {
            try {
                massage += " and waiting";
                System.out.println(massage);
                wait();
            } catch (InterruptedException ignored) {
            }
        }
        else {
            System.out.println(massage);
        }
    }

    public synchronized  void V(){
        value++;
        if(value <= 0) {
            notify();
        }
    }

    public int getValue(){
        return value;
    }
}
