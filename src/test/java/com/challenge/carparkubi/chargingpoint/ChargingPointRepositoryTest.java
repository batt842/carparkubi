package com.challenge.carparkubi.chargingpoint;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * ChargingPointRepositoryTest
 * <p>
 * Let's focus on unit-testing of ChargingPointRepository
 * because it's the most Spring-independent module (except models).
 */
public class ChargingPointRepositoryTest {
    ChargingPointRepository repository;

    @Before
    public void setup() {
        repository = new ChargingPointRepository();
    }

    /**
     * Test a simple functionality
     */
    @Test
    public void exists() {
        assertTrue(repository.exists("CP1"));
        assertTrue(repository.exists("CP10"));

        assertFalse(repository.exists("CP0"));
        assertFalse(repository.exists("CP11"));
    }

    /**
     * Test a simple functionality
     */
    @Test
    public void charge() {
        assertFalse(repository.isCharging("CP1"));
        assertNull(repository.getChargingType("CP1"));

        repository.charge("CP1", ChargingType.Fast);
        assertTrue(repository.isCharging("CP1"));
        assertEquals(ChargingType.Fast, repository.getChargingType("CP1"));

        repository.charge("CP2", ChargingType.Slow);
        assertTrue(repository.isCharging("CP2"));
        assertEquals(ChargingType.Slow, repository.getChargingType("CP2"));
    }

    /**
     * Test a simple functionality
     */
    @Test
    public void unplug() {
        repository.charge("CP1", ChargingType.Fast);
        repository.unplug("CP1");
        assertFalse(repository.isCharging("CP1"));
        assertNull(repository.getChargingType("CP1"));
    }

    /**
     * Test a simple functionality
     */
    @Test
    public void switchOneFastToSlow() {
        assertFalse(repository.switchOneFastToSlow());

        repository.charge("CP1", ChargingType.Fast);
        repository.charge("CP2", ChargingType.Fast);
        repository.charge("CP3", ChargingType.Fast);

        assertTrue(repository.switchOneFastToSlow());

        assertEquals(ChargingType.Slow, repository.getChargingType("CP1"));
        assertEquals(ChargingType.Fast, repository.getChargingType("CP2"));
        assertEquals(ChargingType.Fast, repository.getChargingType("CP3"));
    }

    /**
     * Test a simple functionality and an edge case
     */
    @Test
    public void switchOneSlowToFast() {
        assertFalse(repository.switchOneSlowToFast());

        // use 90A
        repository.charge("CP1", ChargingType.Fast);
        repository.charge("CP2", ChargingType.Fast);
        repository.charge("CP3", ChargingType.Fast);
        repository.charge("CP4", ChargingType.Slow);
        repository.charge("CP5", ChargingType.Slow);
        repository.charge("CP6", ChargingType.Slow);

        assertTrue(repository.switchOneSlowToFast());
        assertFalse(repository.switchOneSlowToFast()); // no more, it's full
        assertEquals(ChargingPointRepository.CURRENT_CAPACITY, repository.getCurrentCurrent());

        assertEquals(ChargingType.Fast, repository.getChargingType("CP1"));
        assertEquals(ChargingType.Fast, repository.getChargingType("CP2"));
        assertEquals(ChargingType.Fast, repository.getChargingType("CP3"));
        assertEquals(ChargingType.Fast, repository.getChargingType("CP4"));
        assertEquals(ChargingType.Slow, repository.getChargingType("CP5"));
        assertEquals(ChargingType.Slow, repository.getChargingType("CP6"));
    }

    /**
     * Test a simple functionality and an edge case
     */
    @Test
    public void isChargingAvailable() {
        // use 90A
        repository.charge("CP1", ChargingType.Fast);
        repository.charge("CP2", ChargingType.Fast);
        repository.charge("CP3", ChargingType.Fast);
        repository.charge("CP4", ChargingType.Slow);
        repository.charge("CP5", ChargingType.Slow);
        repository.charge("CP6", ChargingType.Slow);

        assertTrue(repository.isSlowChargingAvailable());
        assertFalse(repository.isFastChargingAvailable());

        assertTrue(repository.switchOneFastToSlow());

        assertTrue(repository.isSlowChargingAvailable());
        assertTrue(repository.isFastChargingAvailable());
    }

    /**
     * Test a simple functionality
     */
    @Test
    public void getAllStatus() {
        repository.charge("CP1", ChargingType.Fast);
        repository.charge("CP2", ChargingType.Fast);
        repository.charge("CP3", ChargingType.Fast);
        repository.charge("CP4", ChargingType.Slow);
        repository.charge("CP5", ChargingType.Slow);
        repository.charge("CP6", ChargingType.Slow);

        assertEquals(
                "{CP1=OCCUPIED 20A, CP2=OCCUPIED 20A, CP3=OCCUPIED 20A," +
                        " CP4=OCCUPIED 10A, CP5=OCCUPIED 10A, CP6=OCCUPIED 10A," +
                        " CP7=AVAILABLE, CP8=AVAILABLE, CP9=AVAILABLE, CP10=AVAILABLE}",
                repository.getAllStatus().toString());
    }
}