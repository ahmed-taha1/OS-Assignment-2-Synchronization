import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Network {
    private static Semaphore remainingSpaces;
    public static Router router;
    public static ArrayList<Device> devices= new ArrayList<>();

    public static void connect(Device device){
        String arrivingMassage = "- (" + device.getDeviceName() + ")" + " (" + device.getDeviceType() + ") arrived";

        remainingSpaces.P(arrivingMassage);
        router.connectDevice(device);
    }

    public static void logOut(Device device){
        router.releaseDevice(device);
        remainingSpaces.V();
    }

    public static void main(String[] args) throws InterruptedException {
        // IO, initializing section
         int maxNumOfConnections, totalNumOfDevices;

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

        // starting threads section
        for(Device device : devices) {
            device.start();
        }
    }
}
