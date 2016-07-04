package com.mylab.spring.coredemo.entity;

import java.util.List;

public class Booking implements Entity {

    private Long id;
    // ManyToOne
    private User user;
    // ManyToOne
    private Event event;
    // OneToMany
    private List<Ticket> tickets;

    public Booking() {}

    public Booking(List<Ticket> tickets) {
        this.user = tickets.get(0).getUser();
        this.event = tickets.get(0).getEvent();
        this.tickets = tickets;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Booking booking = (Booking) o;

        if (id != null ? !id.equals(booking.id) : booking.id != null) return false;
        if (!user.equals(booking.user)) return false;
        if (!event.equals(booking.event)) return false;
        return tickets.equals(booking.tickets);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + user.hashCode();
        result = 31 * result + event.hashCode();
        result = 31 * result + tickets.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Booking{");
        sb.append("id=").append(id);
        sb.append(", tickets=").append(tickets.size());
        sb.append(", user=").append(user.getName());
        sb.append(", event=").append(event.getName());
        sb.append('}');
        return sb.toString();
    }
}
