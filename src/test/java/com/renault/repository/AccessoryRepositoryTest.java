package com.renault.repository;

import com.renault.model.Accessory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class AccessoryRepositoryTest {
    @Autowired
    private AccessoryRepository accessoryRepository;
    private Accessory accessory;

    @BeforeEach
    void initializeData() {
        //Initialize common mock
        accessory = new Accessory();
        accessory.setType("type accessory");
        accessory.setPrice(new BigDecimal(120.98));
        accessory.setDescription("description accessory");
        accessory.setName("name accessory");
    }

    @Test
    void saveTest() {
        //Invoke DB statement
        var result = accessoryRepository.save(accessory);
        //Verify results
        assertNotNull(result);
        assertNotNull(result.getId());
    }

    @Test
    void saveUpdate() {
        //Save an accessory to modify it later
        accessoryRepository.save(accessory);
        //Get & modify accessory
        var accessoryToUpdate = accessoryRepository.findById(accessory.getId()).get();
        accessoryToUpdate.setDescription("new description");
        accessoryToUpdate.setPrice(new BigDecimal(200.99));
        //Invoke DB statement
        var result = accessoryRepository.save(accessoryToUpdate);
        //Verify results
        assertEquals(new BigDecimal(200.99), result.getPrice());
        assertEquals("new description", result.getDescription());

    }
}
