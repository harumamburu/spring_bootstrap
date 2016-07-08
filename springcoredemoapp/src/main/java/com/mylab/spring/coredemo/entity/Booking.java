package com.mylab.spring.coredemo.entity;

public class Booking implements Entity {

    private Long id;
    private User user;
    private Ticket ticket;

    public Booking() {}

    public Booking(Ticket ticket, User user) {
        this.user = user;
        this.ticket = ticket;
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

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Booking booking = (Booking) o;

        if (id != null ? !id.equals(booking.id) : booking.id != null) return false;
        if (!user.equals(booking.user)) return false;
        return ticket.equals(booking.ticket);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + user.hashCode();
        result = 31 * result + ticket.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Booking{");
        sb.append("id=").append(id);
        sb.append(", ticket=").append(ticket.getSeat());
        sb.append(", user=").append(user.getName());
        sb.append(", event=").append(ticket.getEvent().getName());
        sb.append('}');
        return sb.toString();
    }
}
