package com.mylab.spring.coredemo.entity;

import java.util.ArrayList;
import java.util.List;

public class Auditorium implements Entity, Named {

    private Long id;
    private String name;
    private Integer numberOfSeats;
    // OneToMany
    private List<Integer> vipSeats;
    // OneToMany
    private List<Event> events = new ArrayList<>();

    public Auditorium() {}

    public Auditorium(String name, Integer numberOfSeats) {
        this.name = name;
        this.numberOfSeats = numberOfSeats;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public List<Integer> getVipSeats() {
        return vipSeats;
    }

    public void setVipSeats(List<Integer> vipSeats) {
        this.vipSeats = vipSeats;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Auditorium that = (Auditorium) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (!name.equals(that.name)) return false;
        if (!numberOfSeats.equals(that.numberOfSeats)) return false;
        if (!vipSeats.equals(that.vipSeats)) return false;
        return events.equals(that.events);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + numberOfSeats.hashCode();
        result = 31 * result + vipSeats.hashCode();
        result = 31 * result + events.hashCode();
        return result;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Auditorium{");
        sb.append("id=").append(id);
        sb.append(", name='").append(name).append('\'');
        sb.append(", numberOfSeats=").append(numberOfSeats);
        sb.append(", vipSeats=").append(vipSeats);
        sb.append(", events=").append(events);
        sb.append('}');
        return sb.toString();
    }
}
