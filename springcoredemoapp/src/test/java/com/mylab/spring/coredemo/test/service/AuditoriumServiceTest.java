package com.mylab.spring.coredemo.test.service;

import com.mylab.spring.coredemo.dao.AuditoriumDao;
import com.mylab.spring.coredemo.dao.exception.DaoException;
import com.mylab.spring.coredemo.entity.Auditorium;
import com.mylab.spring.coredemo.service.AuditoriumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AuditoriumServiceTest extends AbstractServiceTest<AuditoriumService> {

    @Autowired
    private AuditoriumDao auditoriumDao;
    private List<Auditorium> auditoriums;

    @DataProvider(name = "auditoriumsPopulator")
    private Iterator<Object[]> populateAuditoriums() {
        return new Iterator<Object[]>() {
            Iterator<Auditorium> internal = auditoriums.iterator();
            @Override
            public boolean hasNext() {
                return internal.hasNext();
            }
            @Override
            public Object[] next() {
                return new Object[]{ internal.next() };
            }
        };
    }

    @Override
    @Autowired
    protected void setService(AuditoriumService service) {
        this.service = service;
    }

    @BeforeClass
    private void setAuditoriums() {
        List<Auditorium> list = auditoriumDao.getAllEntities();
        if (list.isEmpty()) {
            throw new RuntimeException("Auditorium dao has return an empty list");
        }
        auditoriums = new ArrayList<>(list);
    }


    @Test
    public void getAllAuditoriums() {
        Assert.assertEquals(service.getAllAuditoriums(), auditoriums, "Not all auditoriums match");
    }

    @Test(dataProvider = "auditoriumsPopulator")
    public void getSeatNumbers(Auditorium auditorium) throws DaoException {
        Assert.assertEquals(service.getSeatsNumber(auditorium.getId()), auditorium.getNumberOfSeats(),
                "Seats numbers don't match");
    }

    @Test(dataProvider = "auditoriumsPopulator")
    public void getVipSeats(Auditorium auditorium) throws DaoException {
        Assert.assertEquals(service.getVipSeats(auditorium.getId()), auditorium.getVipSeats(),
                "Vip seats lists don't match");
    }
}
