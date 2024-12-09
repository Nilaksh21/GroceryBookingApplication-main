package com.gb.store.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gb.store.model.request.GroceryItemRequest;
import com.gb.store.model.request.GroceryItemUpdateRequest;
import com.gb.store.model.request.GroceryOrderRequest;
import com.gb.store.model.response.GroceryItemResponse;
import com.gb.store.service.GroceryService;
import com.gb.store.service.OrderService;
import com.gb.um.repo.UserInfoRepository;
import com.gb.um.repo.entity.Role;
import com.gb.um.repo.entity.UserInfo;
import com.gb.um.service.JwtService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class GroceryManagementRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private JwtService jwtService;

    @MockBean
    private GroceryService groceryService;

    private String adminJwtToken;

    @BeforeEach
    void beforeEach() {
        UserInfo adminInfo = UserInfo.builder()
                .email("admin@gmail.com")
                .password("12345")
                .role(Role.ADMIN)
                .build();

        userInfoRepository.save(adminInfo);
        adminJwtToken = jwtService.generateToken(adminInfo);
    }

    @AfterEach
    void afterEach() {
        Optional<UserInfo> userInfo = userInfoRepository.findByEmail("admin@gmail.com");
        userInfoRepository.deleteById(userInfo.get().getId());
    }

    @Test
    void getAllGroceries_withUserResultsForbidden() throws Exception {

        //normal user
        UserInfo userInfo = UserInfo.builder()
                .email("user@gmail.com")
                .password("12345")
                .role(Role.USER)
                .build();

        userInfoRepository.save(userInfo);
        String userJwtToken = jwtService.generateToken(userInfo);

        //api call
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/admin/groceries")
                        .header("Authorization", "Bearer " + userJwtToken)
                        .content(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.FORBIDDEN.value()));

        //cleanup
        Optional<UserInfo> dbUserInfo = userInfoRepository.findByEmail("user@gmail.com");
        userInfoRepository.deleteById(dbUserInfo.get().getId());
    }

    @Test
    void getAllGroceries_withAdmin() throws Exception {

        //request
        int pageNo = 0;
        int pageSize = 1;
        String sortBy = "name";
        String sortDir = "asc";

        //response object
        GroceryItemResponse groceryItemResponse =
                GroceryItemResponse.builder()
                        .id("12345")
                        .quantity(100)
                        .name("Soap")
                        .build();

        List<GroceryItemResponse> groceryItemResponseList = List.of(groceryItemResponse);

        Sort sort = Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(pageNo,pageSize,sort);

        //mocking
        Mockito.when(groceryService.getAllGroceryItems(pageNo,pageSize,sortBy,sortDir)).thenReturn(new PageImpl<>(groceryItemResponseList,pageable,1));

        //api call
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/admin/groceries")
                        .header("Authorization", "Bearer " + adminJwtToken)
                        .content(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value("12345"));
    }


    @Test
    void addGroceryItems() throws Exception {
        //request
        GroceryItemRequest groceryItemRequest = GroceryItemRequest.builder()
                .name("Soap")
                .quantity(100)
                .price(100.50)
                .build();

        List<GroceryItemRequest> groceryOrderRequestList = List.of(groceryItemRequest);

        //request json
        String groceryItemRequestListJson = objectMapper.writeValueAsString(groceryOrderRequestList);

        //mocking
        Mockito.doNothing().when(groceryService).addGrocery(groceryItemRequest);

        //rest api call
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/admin/groceries")
                        .content(groceryItemRequestListJson)
                        .header("Authorization", "Bearer " + adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()));
    }

    @Test
    void updateGroceryItem() throws Exception {
        //request
        GroceryItemUpdateRequest groceryItemUpdateRequest = GroceryItemUpdateRequest.builder()
                .id("12345")
                .name("Soap")
                .quantity(50)
                .price(100.50)
                .build();

        //request json
        String groceryItemUpdateRequestJson = objectMapper.writeValueAsString(groceryItemUpdateRequest);

        //response
        GroceryItemResponse groceryItemResponse = GroceryItemResponse.builder()
                .price(groceryItemUpdateRequest.getPrice())
                .quantity(groceryItemUpdateRequest.getQuantity())
                .name(groceryItemUpdateRequest.getName())
                .id(groceryItemUpdateRequest.getId())
                .build();

        //mocking
        Mockito.when(groceryService.updateGrocery(groceryItemUpdateRequest)).thenReturn(groceryItemResponse);

        //rest api call
        mockMvc.perform(MockMvcRequestBuilders
                        .patch("/api/v1/admin/groceries")
                        .content(groceryItemUpdateRequestJson)
                        .header("Authorization", "Bearer " + adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("12345"));

    }

    @Test
    void deleteGrocery() throws Exception {

        //mocking
        Mockito.doNothing().when(groceryService).deleteGrocery("12345");

        //rest api call
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/v1/admin/groceries/{grocery_item_id}", "12345")
                        .header("Authorization", "Bearer " + adminJwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()));
    }
}