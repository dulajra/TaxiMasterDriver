package com.innocept.taximasterdriver.model.foundation;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dulaj on 5/24/16.
 */
public class Order implements Serializable{

    private int customerId;
    private int id;
    private String origin;
    private String destination;
    private Location originCoordinates;
    private Location destinationCoordinates;
    private Date time;
    private Date endTime;
    private String note;
    private String contact;
    private double distance;
    private int fare;

    public int getFare() {
        return fare;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }

    private Order.OrderState orderState;

    public Order() {
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public Location getOriginCoordinates() {
        return originCoordinates;
    }

    public void setOriginCoordinates(Location originCoordinates) {
        this.originCoordinates = originCoordinates;
    }

    public Location getDestinationCoordinates() {
        return destinationCoordinates;
    }

    public void setDestinationCoordinates(Location destinationCoordinates) {
        this.destinationCoordinates = destinationCoordinates;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getContact() {
        return contact;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public enum OrderState {

        ACCEPTED(1),
        PENDING(2),
        FINISHED(3),
        REJECTED(4),
        NOW(5);

        int value;

        OrderState(int v) {
            value = v;
        }

        public int getValue() {
            return value;
        }

    }
}
