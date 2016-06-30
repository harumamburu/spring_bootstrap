package com.mylab.spring.coredemo.entity;

import com.mylab.spring.coredemo.entity.enumeration.Rating;

import java.util.Date;

public class Event implements Entity, Named {

    private Long id;
    private String name;
    private Date date;
    private Double basePrice;
    private Rating rating;

    public Event() {}

    public Event(String name, Date date, Double basePrice) {
        this.name = name;
        this.date = date;
        this.basePrice = basePrice;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Event event = (Event) o;

        if (id != null ? !id.equals(event.id) : event.id != null) return false;
        if (!name.equals(event.name)) return false;
        if (!date.equals(event.date)) return false;
        if (!basePrice.equals(event.basePrice)) return false;
        return rating == event.rating;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + name.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + basePrice.hashCode();
        result = 31 * result + (rating != null ? rating.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", basePrice=" + basePrice +
                ", rating=" + rating +
                '}';
    }
}
