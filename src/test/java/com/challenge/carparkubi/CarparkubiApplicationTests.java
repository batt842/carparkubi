package com.challenge.carparkubi;

import com.challenge.carparkubi.chargingpoint.ChargingPointRepository;
import com.challenge.carparkubi.chargingpoint.ChargingType;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * CarparkubiApplicationTests
 * <p>
 * Do a few API call tests because most of edge cases are covered by unit tests.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CarparkubiApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ChargingPointRepository repository;

    @After
    public void teardown() {
        repository.cleanUp();
    }

    @Test
    public void plug_unplug_report() throws Exception {
        for (int i = 1; i <= 6; i++)
            mockMvc.perform(post("/v1/cps/{id}", "CP" + i)
                    .contentType("application/json"))
                    .andExpect(status().isOk());

        assertEquals(ChargingType.Slow, repository.getChargingType("CP1"));
        assertEquals(ChargingType.Slow, repository.getChargingType("CP2"));
        assertEquals(ChargingType.Fast, repository.getChargingType("CP3"));
        assertEquals(ChargingType.Fast, repository.getChargingType("CP6"));

        mockMvc.perform(delete("/v1/cps/{id}", "CP3")
                .contentType("application/json"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/v1/cps/report").contentType("application/json"))
                .andDo(print())
                .andExpect(content().string(
                        "{\"CP1\":\"OCCUPIED 20A\",\"CP2\":\"OCCUPIED 20A\"," +
                                "\"CP3\":\"AVAILABLE\"," +
                                "\"CP4\":\"OCCUPIED 20A\",\"CP5\":\"OCCUPIED 20A\",\"CP6\":\"OCCUPIED 20A\"," +
                                "\"CP7\":\"AVAILABLE\",\"CP8\":\"AVAILABLE\",\"CP9\":\"AVAILABLE\",\"CP10\":\"AVAILABLE\"}"));

        assertEquals(ChargingType.Fast, repository.getChargingType("CP1"));
        assertEquals(ChargingType.Fast, repository.getChargingType("CP2"));
        assertFalse(repository.isCharging("CP3"));
        assertNull(repository.getChargingType("CP3"));
        assertEquals(ChargingType.Fast, repository.getChargingType("CP6"));
    }

    @Test
    public void plug_exception() throws Exception {
        mockMvc.perform(post("/v1/cps/{id}", "CP100")
                .contentType("application/json"))
                .andExpect(content().string("{\"message\":\"Resource not found\"}"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void report_after_clear() throws Exception {
        mockMvc.perform(get("/v1/cps/report").contentType("application/json"))
                .andDo(print());
    }
}
