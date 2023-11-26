package ToBeSubmetted;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

class Device extends Thread{
    private String deviceName, deviceType;
    public Device(String name, String type){
        this.deviceName = name;
        this.deviceType = type;
    }

    @Override
    public void run() {
        try {
            Random random = new Random();
            int randomNum = random.nextInt(4001) + 1000;

            // connecting
            Network.connect(this);
            Thread.sleep(randomNum);
            int connectionNumber = Network.router.getConnectionNumber(this) + 1;

            // logged in
            Network.outputFile.write("- Connection " + connectionNumber + ": " + this.deviceName + " login\n");
            Thread.sleep(randomNum);

            // doing activity
            preformOnlineActivity(connectionNumber);
            Thread.sleep(randomNum);

            // logging out
            Network.outputFile.write("- Connection " + connectionNumber + ": " + this.deviceName + " Logged out\n");
            Network.logOut(this);
        }
        catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void preformOnlineActivity(int connectionNumber) throws IOException {
        Network.outputFile.write("- Connection " + connectionNumber + ": " +this.deviceName + " Performs Online Activity\n");
    }

    public String getDeviceName(){
        return this.deviceName;
    }

    public String getDeviceType(){
        return this.deviceType;
    }
}

class Router {
    private int capacity;
    private Device[] connectedDevices;

    public Router(int capacity){
        this.capacity = capacity;
        connectedDevices = new Device[capacity];
    }

    public void connectDevice(Device device) throws IOException {
        for(int i = 0; i < capacity; i++){
            if(connectedDevices[i] == null) {
                connectedDevices[i] = device;
                Network.outputFile.write("- Connection " + (i + 1) + ": " + device.getDeviceName() + " Occupied\n");
                break;
            }
        }
    }

    public void releaseDevice(Device device){
        int connectionNumber = this.getConnectionNumber(device);
        if(connectionNumber == -1)
            return;

        connectedDevices[this.getConnectionNumber(device)] = null;
    }

    public int getConnectionNumber(Device device){
        for(int i = 0; i < capacity; i++){
            if(connectedDevices[i] == device)
                return i;
        }
        return -1;
    }
}


class Semaphore {
    protected int value = 0;
    protected Semaphore() {value = 0;}
    protected Semaphore(int initial){value = initial;}
    public synchronized void P() throws InterruptedException, IOException {
        value--;
        if (value < 0) {
            Network.arrivingMassage += " and waiting";
            Network.outputFile.write(Network.arrivingMassage + "\n");
            wait();
        }
        else {
            Network.outputFile.write(Network.arrivingMassage + "\n");
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

public class Network {
    private static Semaphore remainingSpaces;
    public static Router router;
    public static ArrayList<Device> devices= new ArrayList<>();
    public static FileWriter outputFile;

    public static String arrivingMassage = "";
    public static void connect(Device device) throws IOException, InterruptedException {
        arrivingMassage = "- (" + device.getDeviceName() + ")" + " (" + device.getDeviceType() + ") arrived";

        remainingSpaces.P();
        router.connectDevice(device);
    }

    public static void logOut(Device device){
        router.releaseDevice(device);
        remainingSpaces.V();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        // IO, initializing section
        int maxNumOfConnections, totalNumOfDevices;
        try {
            outputFile = new FileWriter("log.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("What is the number of WI-FI Connections?");
        maxNumOfConnections = Integer.parseInt(scanner.nextLine());
        remainingSpaces = new Semaphore(maxNumOfConnections);
        router = new Router(maxNumOfConnections);

        System.out.println("What is the number of devices Clients want to connect?");
        totalNumOfDevices = Integer.parseInt(scanner.nextLine());

        for(int i = 0; i < totalNumOfDevices; i++){
            String line;
            line = scanner.nextLine();
            String[] arguments = line.split(" ");
            devices.add(new Device(arguments[0], arguments[1]));
        }
        System.out.println("running...");

        // starting threads section
        List<Thread> threads = new ArrayList<>();
        for(Device device : devices) {
            device.start();
            threads.add(device);
        }
        for(Thread t : threads){
            t.join();
        }
        outputFile.close();
    }
}
