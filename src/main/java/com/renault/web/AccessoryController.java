package com.renault.web;

import com.renault.dto.AccessoryDTO;
import com.renault.service.AccessoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accessory")
@Slf4j
public class AccessoryController {
    private AccessoryService accessoryService;

    public AccessoryController(AccessoryService accessoryService) {
        this.accessoryService = accessoryService;
    }

    @PostMapping("/save")
    public ResponseEntity<?> createAccessory(@RequestBody AccessoryDTO accessoryDTO) {
        log.info("Creating new accessory : {}", accessoryDTO);
        var savedAccessory = this.accessoryService.save(accessoryDTO);
        return ResponseEntity.ok(savedAccessory);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateAccessory(@RequestBody AccessoryDTO accessoryDTO) {
        log.info("Updating accessory : {}", accessoryDTO);
        var updatedAccessory = this.accessoryService.update(accessoryDTO);
        return ResponseEntity.ok(updatedAccessory);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAccessory(@PathVariable Long id) {
        log.info("Deleting accessory with id : {}", id);
        var deleteStatus = this.accessoryService.delete(id);
        return ResponseEntity.ok(deleteStatus);
    }

    @PutMapping("/{accessoryId}/vehicle/{vehicleId}")
    public ResponseEntity<Boolean> addAccessoryToVehicle(@PathVariable Long vehicleId, @PathVariable Long accessoryId) {
        log.info("Adding accessory {} to vehicle : {}", accessoryId, vehicleId);
        var addStatus = this.accessoryService.addAccessoryToVehicle(vehicleId, accessoryId);
        return ResponseEntity.ok(addStatus);
    }

    @DeleteMapping("/{accessoryId}/vehicle/{vehicleId}")
    public ResponseEntity<Boolean> removeAccessoryFromVehicle(@PathVariable Long vehicleId, @PathVariable Long accessoryId) {
        log.info("Removing accessory {} from vehicle : {}", accessoryId, vehicleId);
        var removeStatus = this.accessoryService.removeAccessoryFromVehicle(vehicleId, accessoryId);
        return ResponseEntity.ok(removeStatus);
    }

    @GetMapping("/vehicle/{vehicleId}")
    public ResponseEntity<?> getAccessoriesForVehicle(@PathVariable Long vehicleId) {
        log.info("Getting accessories for vehicle : {}", vehicleId);
        var listAccessories = this.accessoryService.getListAccessoriesForAVehicle(vehicleId);
        return ResponseEntity.ok(listAccessories);
    }


}
