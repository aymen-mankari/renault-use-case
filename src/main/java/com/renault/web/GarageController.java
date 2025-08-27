package com.renault.web;

import com.renault.dto.GarageDTO;
import com.renault.service.GarageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/garage")
@Slf4j
public class GarageController {

    private GarageService garageService;

    public GarageController(GarageService garageService) {
        this.garageService = garageService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GarageDTO> getGarageById(@PathVariable Long id) {
        return ResponseEntity.ok(this.garageService.findById(id));
    }

    @PostMapping("/save")
    public ResponseEntity<GarageDTO> createGarage(@RequestBody GarageDTO garageDTO) {
        log.info("Creating new garage : {}", garageDTO);
        var savedGarage = this.garageService.save(garageDTO);
        return ResponseEntity.ok(savedGarage);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateGarage(@RequestBody GarageDTO garageDTO) {
        log.info("Updating garage : {}", garageDTO);
        var savedGarage = this.garageService.update(garageDTO);
        return ResponseEntity.ok(savedGarage);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteGarage(@PathVariable Long id) {
        log.info("Deleting garage with id : {}", id);
        var deleteStatus = this.garageService.delete(id);
        return ResponseEntity.ok(deleteStatus);
    }

}
