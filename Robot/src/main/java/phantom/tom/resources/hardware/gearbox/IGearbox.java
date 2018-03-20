package phantom.tom.resources.hardware.gearbox;

public interface IGearbox {
    void shift(int gear);
    double conversion();
    int gear();
    void gear(int gear);
}
