import java.util.ArrayList;

public class Router {
    private int capacity;
    private Device[] connectedDevices;

    public Router(int capacity){
        this.capacity = capacity;
        connectedDevices = new Device[capacity];
    }

    public void connectDevice(Device device){
        for(int i = 0; i < capacity; i++){
            if(connectedDevices[i] == null) {
                connectedDevices[i] = device;
                System.out.println("- Connection " + (i + 1) + ": " + device.getDeviceName() + " Occupied");
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
