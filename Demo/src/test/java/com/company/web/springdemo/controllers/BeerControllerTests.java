package com.company.web.springdemo.controllers;

import com.company.web.springdemo.services.BeerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilderSupport;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static com.company.web.springdemo.Helpers.createMockBeer;

@SpringBootTest
@AutoConfigureMockMvc
public class BeerControllerTests {

    @MockBean
    BeerService mockService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void getById_Should_Return_StatusOK_When_BeerExists() throws Exception{
        // Arrange
        var mockBeer = createMockBeer();
        Mockito.when(mockService.get(mockBeer.getId()))
                .thenReturn(mockBeer);

        //Act, Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/beers/{id}", mockBeer.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(mockBeer.getName()));
    }


}
