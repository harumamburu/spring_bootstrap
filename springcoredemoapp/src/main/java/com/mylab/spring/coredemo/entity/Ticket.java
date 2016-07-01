package com.mylab.spring.coredemo.entity;

public class Ticket implements Entity {

    private Long id;
    private Event event;
    private Double price;
    private Integer seat;
    private User user;

    public Ticket() {}

    public Ticket(Event event, User user, Integer seat) {
        this.event = event;
        this.user = user;
        this.price = event.getBasePrice();
        this.seat = seat;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getSeat() {
        return seat;
    }

    public void setSeat(Integer seat) {
        this.seat = seat;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Ticket ticket = (Ticket) o;

        if (id != null ? !id.equals(ticket.id) : ticket.id != null) return false;
        if (!event.equals(ticket.event)) return false;
        if (!price.equals(ticket.price)) return false;
        if (!seat.equals(ticket.seat)) return false;
        return user.equals(ticket.user);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + event.hashCode();
        result = 31 * result + price.hashCode();
        result = 31 * result + seat.hashCode();
        result = 31 * result + user.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", event=" + event.getName() +
                ", price=" + price +
                ", seat=" + seat +
                ", user=" + user.getName() +
                '}';
    }
}
