import java.util.Random;

public class Device extends Thread{
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
            System.out.println("- Connection " + connectionNumber + ": " + this.deviceName + " login");

            // doing activity
            preformOnlineActivity(connectionNumber);
            Thread.sleep(randomNum);

            // logging out
            System.out.println("- Connection " + connectionNumber + ": " + this.deviceName + " Logged out");
            Network.logOut(this);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void preformOnlineActivity(int connectionNumber){
        System.out.println("- Connection " + connectionNumber + ": " +this.deviceName + " Performs Online Activity");
    }

    public String getDeviceName(){
        return this.deviceName;
    }

    public String getDeviceType(){
        return this.deviceType;
    }
}
