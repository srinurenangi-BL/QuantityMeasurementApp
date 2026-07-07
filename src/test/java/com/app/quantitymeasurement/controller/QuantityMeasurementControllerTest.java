package com.app.quantitymeasurement.controller;

import com.app.quantitymeasurement.config.SecurityConfig;
import com.app.quantitymeasurement.exception.RestExceptionHandler;
import com.app.quantitymeasurement.model.QuantityDTO;
import com.app.quantitymeasurement.model.QuantityMeasurementRequest;
import com.app.quantitymeasurement.services.IQuantityMeasurementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(QuantityMeasurementController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import({SecurityConfig.class, RestExceptionHandler.class})
class QuantityMeasurementControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IQuantityMeasurementService service;

    @Test
    void shouldCompareQuantitiesThroughRestEndpoint() throws Exception {
        when(service.compare(any(QuantityDTO.class), any(QuantityDTO.class)))
                .thenReturn(new QuantityDTO(1.0, "FEET", "length", "compare", "true", true, null));
        QuantityMeasurementRequest request = new QuantityMeasurementRequest(
                new QuantityDTO(1.0, "FEET", "length", null, null, true, null),
                new QuantityDTO(12.0, "INCHES", "length", null, null, true, null),
                null
        );

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.result").value("true"));
    }

    @Test
    void shouldConvertQuantityThroughRestEndpoint() throws Exception {
        when(service.convert(any(QuantityDTO.class), eq("INCHES")))
                .thenReturn(new QuantityDTO(12.0, "INCHES", "length", "convert", "12.0", true, null));

        mockMvc.perform(post("/api/v1/quantities/convert")
                        .param("targetUnit", "INCHES")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new QuantityDTO(1.0, "FEET", "length", null, null, true, null))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.unit").value("INCHES"))
                .andExpect(jsonPath("$.result").value("12.0"));
    }

    @Test
    void shouldReturnBadRequestForInvalidQuantityRequest() throws Exception {
        QuantityMeasurementRequest request = new QuantityMeasurementRequest(
                new QuantityDTO(1.0, "", "length", null, null, true, null),
                new QuantityDTO(12.0, "INCHES", "length", null, null, true, null),
                null
        );

        mockMvc.perform(post("/api/v1/quantities/compare")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}
