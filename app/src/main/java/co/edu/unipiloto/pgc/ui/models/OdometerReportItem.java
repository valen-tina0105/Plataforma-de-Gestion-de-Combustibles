package co.edu.unipiloto.pgc.ui.models;

public class OdometerReportItem {
    private final int deliveryId;
    private final double distance;

    public OdometerReportItem(int deliveryId, double distance) {
        this.deliveryId = deliveryId;
        this.distance = distance;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public double getDistance() {
        return distance;
    }
}
