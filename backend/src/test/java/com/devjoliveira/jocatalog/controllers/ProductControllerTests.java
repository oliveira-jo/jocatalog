package com.devjoliveira.jocatalog.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.devjoliveira.jocatalog.dtos.ProductDTO;
import com.devjoliveira.jocatalog.services.ProductService;
import com.devjoliveira.jocatalog.services.exceptions.DatabaseException;
import com.devjoliveira.jocatalog.services.exceptions.ResourceNotFoundException;
import com.devjoliveira.jocatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(value = ProductController.class, excludeAutoConfiguration = { SecurityAutoConfiguration.class })
public class ProductControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ProductService productServiceMock;

  @Autowired
  private ObjectMapper objectMapper;

  private Long existingId;
  private Long nonExistingId;
  private Long dependentId;
  private ProductDTO productDTO;
  private PageImpl<ProductDTO> page;

  @BeforeEach
  void setUp() throws Exception {

    existingId = 1L;
    nonExistingId = 1000L;
    dependentId = 2L;

    productDTO = Factory.createProductDTO();
    page = new PageImpl<>(List.of(productDTO));

    when(productServiceMock.findAllPaged(ArgumentMatchers.anyString(), ArgumentMatchers.anyString(),
        ArgumentMatchers.any())).thenReturn(page);

    when(productServiceMock.findById(existingId)).thenReturn(productDTO);
    when(productServiceMock.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

    when(productServiceMock.save(any())).thenReturn(productDTO);

    when(productServiceMock.update(eq(existingId), any())).thenReturn(productDTO);
    when(productServiceMock.update(eq(nonExistingId), any())).thenThrow(ResourceNotFoundException.class);

    doNothing().when(productServiceMock).delete(existingId);
    doThrow(ResourceNotFoundException.class).when(productServiceMock).delete(nonExistingId);
    doThrow(DatabaseException.class).when(productServiceMock).delete(dependentId);

  }

  @Test
  public void findAllShouldReturnPage() throws Exception {

    ResultActions result = mockMvc.perform(get("/products")
        .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk());

  }

  @Test
  public void findByIdShouldReturnProductWhenIdExistis() throws Exception {

    ResultActions result = mockMvc.perform(get("/products/{id}", existingId)
        .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk());
    result.andExpect(jsonPath("$.id").exists());

  }

  @Test
  public void findByIdShouldReturnNotFoundWhenIdDoesNotExisti() throws Exception {

    ResultActions result = mockMvc.perform(get("/products/{id}", nonExistingId)
        .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isNotFound());

  }

  @Test
  public void saveShouldReturnProductDTOWhenIdExists() throws Exception {

    String jsonBody = objectMapper.writeValueAsString(productDTO);

    ResultActions result = mockMvc.perform(post("/products")
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isCreated());
    result.andExpect(jsonPath("$.id").exists());
  }

  @Test
  public void updateShouldReturnProductDTOWhenIdExistis() throws Exception {

    String jsonBody = objectMapper.writeValueAsString(productDTO);

    ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isOk());
    result.andExpect(jsonPath("$.id").exists());

  }

  @Test
  public void updateShouldReturnNotFoundWhenIdDoesNotExisti() throws Exception {

    String jsonBody = objectMapper.writeValueAsString(productDTO);

    ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
        .content(jsonBody)
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON));

    result.andExpect(status().isNotFound());

  }

  @Test
  public void deleteShouldReturnNoContentWhenIdExists() throws Exception {

    ResultActions result = mockMvc.perform(delete("/products/{id}", existingId));
    result.andExpect(status().isNoContent());
  }

  @Test
  public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

    ResultActions result = mockMvc.perform(delete("/products/{id}", nonExistingId));
    result.andExpect(status().isNotFound());

  }

  @Test
  public void deleteShouldReturnNoContentWhenIdDoesNotExist() throws Exception {

    ResultActions result = mockMvc.perform(delete("/products/{id}", dependentId));
    result.andExpect(status().isBadRequest());

  }

}
