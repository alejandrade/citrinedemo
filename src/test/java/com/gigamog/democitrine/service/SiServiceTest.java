package com.gigamog.democitrine.service;

import com.gigamog.democitrine.domain.ConversionRepository;
import com.gigamog.democitrine.domain.model.Si;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * normally i would create more test cases but there isn't really any business logic in the other classes
 * so I figured it was a waste of time for this exercise
 */
public class SiServiceTest {
    SiService siService;

    @Before
    public void setup(){
        siService = new SiService(new ConversionRepository());
    }


    @Test
    public void getSi() throws Exception {
        Si si = siService.getSi("degree/minute");
        Assert.assertEquals("rad/s", si.getUnitName());
        Assert.assertEquals("0.00029088820867",si.getMultiplicationFactor()
                .setScale(14, BigDecimal.ROUND_UP).toPlainString());
    }

    @Test
    public void getSi_degree() throws Exception {
        Si si = siService.getSi("degree");
        Assert.assertEquals("rad", si.getUnitName());
        Assert.assertEquals("0.00029088820867",si.getMultiplicationFactor()
                .setScale(14, BigDecimal.ROUND_UP).toPlainString());
    }

    @Test
    public void getSi_degreeSymbol() throws Exception {
        Si si = siService.getSi("°");
        Assert.assertEquals("rad", si.getUnitName());
        Assert.assertEquals("0.00029088820867",si.getMultiplicationFactor()
                .setScale(14, BigDecimal.ROUND_UP).toPlainString());
    }

    @Test
    public void getSi_degreeMinuteSymbol() throws Exception {
        Si si = siService.getSi("°/min");
        Assert.assertEquals("rad/s", si.getUnitName());
        Assert.assertEquals("0.00029088820867",si.getMultiplicationFactor()
                .setScale(14, BigDecimal.ROUND_UP).toPlainString());
    }


    @Test
    public void getSi_DegreeMinuteHectare(){
        Si si = siService.getSi("degree/(minute*hectare)");
        Assert.assertEquals("rad/(s*m^2)", si.getUnitName());
        Assert.assertEquals("0.00000002908882", si.getMultiplicationFactor()
                .setScale(14, BigDecimal.ROUND_UP).toPlainString());
    }

    @Test
    public void getSi_wronginput(){
        Si si = siService.getSi("dsdfasd");
        Assert.assertEquals("", si.getUnitName());
        Assert.assertEquals(null, si.getMultiplicationFactor());
    }

}