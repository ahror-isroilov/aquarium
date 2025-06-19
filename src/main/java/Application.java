import model.Aquarium;
import common.Utils;

public class Application {
    public static void main(String[] args) {
        Utils.clearLogs();
        Aquarium aquarium = new Aquarium(10,15,10, 50);
        aquarium.startSimulation();
    }
}
