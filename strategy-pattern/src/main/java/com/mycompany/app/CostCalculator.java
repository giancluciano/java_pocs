package com.mycompany.app;

public record CostCalculator (Transport transport, int distance) {

    public void execute() {
        Strategy strategy;
        if (this.transport == Transport.Bike) {
            strategy = new BikeStrategy();
        } else if (this.transport == Transport.Motorcicle){
            strategy = new MotorcicleStrategy();
        } else if (this.transport == Transport.Car) {
            strategy = new CarStrategy();
        } else {
            return;
        }
        strategy.execute(this.distance);
    }
}
