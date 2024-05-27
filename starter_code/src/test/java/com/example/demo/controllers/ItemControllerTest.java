package com.example.demo.controllers;

import com.example.demo.model.persistence.Item;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

import static com.example.demo.utils.Constants.RESPONSE_200;
import static com.example.demo.utils.Constants.RESPONSE_404;

@Transactional
@SpringBootTest
@RunWith(SpringRunner.class)
public class ItemControllerTest {

    @Autowired
    private ItemController itemController;

    @Test
    public void getItemsResponse200() {
        ResponseEntity<List<Item>> responseEntity = itemController.getItems();
        Assertions.assertEquals(RESPONSE_200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void getItemByIdResponse200() {
        ResponseEntity<Item> itemResponseEntity = itemController.getItemById(1L);
        Assertions.assertEquals(RESPONSE_200, itemResponseEntity.getStatusCodeValue());
    }

    @Test
    public void getItemByIdResponse404() {
        ResponseEntity<Item> itemResponseEntity = itemController.getItemById(-1L);
        Assertions.assertEquals(RESPONSE_404, itemResponseEntity.getStatusCodeValue());
    }

    @Test
    public void getItemsByNameResponse200() {
        ResponseEntity<List<Item>> itemResponseEntity = itemController.getItemsByName("Round Widget");
        Assertions.assertEquals(RESPONSE_200, itemResponseEntity.getStatusCodeValue());
    }

    @Test
    public void getItemsByNameResponse404() {
        ResponseEntity<List<Item>> itemResponseEntity = itemController.getItemsByName("Item name");
        Assertions.assertEquals(RESPONSE_404, itemResponseEntity.getStatusCodeValue());
    }
}
