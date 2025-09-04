package com.devjoliveira.jocatalog.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.devjoliveira.jocatalog.dtos.ProductDTO;
import com.devjoliveira.jocatalog.tests.Factory;
import com.devjoliveira.jocatalog.util.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIT {

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @Autowired
  private TokenUtil tokenUtil;

  private Long existingId;
  private Long nonExistingId;
  private Long totalProducts;

  private String username, password, bearerToken;

  @BeforeEach
  void setUp() throws Exception {

    existingId = 1L;
    nonExistingId = 1000L;
    totalProducts = 25L;

    username = "maria@gmail.com";
    password = "123456";

    bearerToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

  }

  @Test
  public void findAllPagedShouldReturnSortedPageWhenSortedByName() throws Exception {

    ResultActions result = mockMvc.perform(get("/products?page=0&size=12&sort=name,asc")
        .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk());
    result.andExpect(jsonPath("$.totalElements").value(totalProducts));
    result.andExpect(jsonPath("$.content").exists());
    result.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"));
    result.andExpect(jsonPath("$.content[1].name").value("PC Gamer"));
    result.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"));

  }

  @Test
  public void updateShouldReturnProductDTOWhenIdExistis() throws Exception {

    ProductDTO productDTO = Factory.createProductDTO();
    String jsonBody = objectMapper.writeValueAsString(productDTO);
    String expectedName = productDTO.name();

    ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
        .header("Authorization", "Bearer " + bearerToken)
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk());
    result.andExpect(jsonPath("$.id").value(existingId));
    result.andExpect(jsonPath("$.name").value(expectedName));

  }

  @Test
  public void updateShouldReturnNotFoundWhenIdDoesNotExisti() throws Exception {

    ProductDTO productDTO = Factory.createProductDTO();
    String jsonBody = objectMapper.writeValueAsString(productDTO);

    ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
        .header("Authorization", "Bearer " + bearerToken)
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isNotFound());
  }

}
