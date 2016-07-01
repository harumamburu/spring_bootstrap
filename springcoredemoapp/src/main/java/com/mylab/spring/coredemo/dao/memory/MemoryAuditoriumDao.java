package com.mylab.spring.coredemo.dao.memory;

import com.mylab.spring.coredemo.dao.AuditoriumDao;
import com.mylab.spring.coredemo.dao.exception.EntityNotFoundException;
import com.mylab.spring.coredemo.entity.Auditorium;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class MemoryAuditoriumDao extends AbstractNamingMemoryDao<Auditorium> implements AuditoriumDao {

    private static final AtomicLong COUNTER = new AtomicLong();
    private final Map<Long, Auditorium> AUDITORIUMS = new ConcurrentHashMap<>(4, 0.9f, 1);

    @Override
    protected Map<Long, Auditorium> getStorage() {
        return AUDITORIUMS;
    }

    @Override
    protected AtomicLong getCounter() {
        return COUNTER;
    }

    @Override
    public Integer getNumberOfSeats(Long id) throws EntityNotFoundException {
        return getOrThrowNotExists(id).getNumberOfSeats();
    }

    @Override
    public List<Integer> getVipSeats(Long id) throws EntityNotFoundException {
        return getOrThrowNotExists(id).getVipSeats();
    }

    private Auditorium getOrThrowNotExists(Long id) throws EntityNotFoundException {
        return Optional.ofNullable(AUDITORIUMS.get(id)).
                orElseThrow(() -> new EntityNotFoundException(String.format("No auditorium with id %d", id)));
    }

    @Override
    public List<Auditorium> getAllEntities() {
        return AUDITORIUMS.values().parallelStream()
                .sorted(Comparator.comparing(Auditorium::getId))
                .collect(Collectors.toList());
    }

    @Override
    protected boolean isSavedAlready(Auditorium entity) {
        return AUDITORIUMS.values().parallelStream()
                .anyMatch(auditorium -> auditorium.getName().equals(entity.getName()));
    }
}
