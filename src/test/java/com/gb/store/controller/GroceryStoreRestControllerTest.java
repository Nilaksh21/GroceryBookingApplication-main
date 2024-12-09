package com.gb.store.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gb.store.constant.Constant;
import com.gb.store.model.request.GroceryOrderRequest;
import com.gb.store.model.response.GroceryItemResponse;
import com.gb.store.model.response.GroceryOrderResponse;
import com.gb.store.service.GroceryService;
import com.gb.store.service.OrderService;
import com.gb.um.model.request.LoginRequest;
import com.gb.um.model.request.UserRegistrationRequest;
import com.gb.um.repo.UserInfoRepository;
import com.gb.um.repo.entity.Role;
import com.gb.um.repo.entity.UserInfo;
import com.gb.um.service.AuthenticationService;
import com.gb.um.service.JwtService;
import com.gb.um.service.UserInfoService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
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
class GroceryStoreRestControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserInfoRepository userInfoRepository;
    @Autowired
    private JwtService jwtService;

    @MockBean
    private OrderService orderService;
    @MockBean
    private GroceryService groceryService;

    private String userJwtToken;

    @BeforeEach
    void beforeEach() {
        UserInfo userInfo = UserInfo.builder()
                .email("user@gmail.com")
                .password("12345")
                .role(Role.USER)
                .build();

        userInfoRepository.save(userInfo);
        userJwtToken = jwtService.generateToken(userInfo);
    }

    @AfterEach
    void afterEach() {
        Optional<UserInfo> userInfo = userInfoRepository.findByEmail("user@gmail.com");
        userInfoRepository.deleteById(userInfo.get().getId());
    }


    @Test
    void getAllGroceries_withUser() throws Exception {

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

        //expected response
        String expectedResponseGroceryItems = objectMapper.writeValueAsString(groceryItemResponseList);

        //mocking
        Mockito.when(groceryService.getAllGroceryItems(pageNo,pageSize,sortBy,sortDir)).thenReturn(new PageImpl<>(groceryItemResponseList,pageable,1));

        //api call
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/v1/groceries")
                        .header("Authorization", "Bearer " + userJwtToken)
                        .content(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.OK.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id").value("12345"));
    }

    @Test
    void placeOrder_withUser() throws Exception {

        //request object
        GroceryOrderRequest groceryOrderRequest =
                GroceryOrderRequest.builder()
                        .groceryItemId("12345678")
                        .quantity(2)
                        .build();
        List<GroceryOrderRequest> groceryOrderRequestList = List.of(groceryOrderRequest);

        String groceryOrderRequestListJson = objectMapper.writeValueAsString(groceryOrderRequestList);

        //response object
        GroceryOrderResponse orderResponse = GroceryOrderResponse.builder()
                .status(Constant.SUCCESS)
                .build();

        //mocking
        Mockito.doNothing().when(orderService).placeOrder(groceryOrderRequestList);

        //call grocery order endpoint
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/v1/groceries/order")
                        .header("Authorization", "Bearer " + userJwtToken)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(groceryOrderRequestListJson)
                )
                .andExpect(MockMvcResultMatchers.status().is(HttpStatus.CREATED.value()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(Constant.SUCCESS));
    }
}