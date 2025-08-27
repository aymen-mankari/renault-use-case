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
    private Accessory mockAccessory;

    @BeforeEach
    void initializeData() {
        mockAccessory = new Accessory();
        mockAccessory.setType("type accessory");
        mockAccessory.setPrice(new BigDecimal(120.98));
        mockAccessory.setDescription("description accessory");
        mockAccessory.setName("name accessory");
    }

    @Test
    void saveTest() {
        accessoryRepository.save(mockAccessory);
        assertNotNull(mockAccessory);
        assertNotNull(mockAccessory.getId());
    }

    @Test
    void saveUpdate() {
        accessoryRepository.save(mockAccessory);
        var accessoireToUpdate = accessoryRepository.findById(mockAccessory.getId()).get();
        accessoireToUpdate.setDescription("new description");
        accessoireToUpdate.setPrice(new BigDecimal(200.99));
        var result = accessoryRepository.save(accessoireToUpdate);
        assertEquals(new BigDecimal(200.99), accessoireToUpdate.getPrice());
        assertEquals("new description", accessoireToUpdate.getDescription());

    }
}
