package com.renault.web;

import com.renault.dto.VehicleDTO;
import com.renault.service.VehicleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/vehicle")
@Slf4j
public class VehicleController {
    private VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/save")
    public ResponseEntity<VehicleDTO> saveVehicle(@RequestBody VehicleDTO vehicleDTO) {
        log.info("Saving new vehicle : {}", vehicleDTO);
        var savedVehicle = this.vehicleService.save(vehicleDTO);
        return ResponseEntity.ok(savedVehicle);
    }

    @PutMapping("/update")
    public ResponseEntity<VehicleDTO> updateVehicle(@RequestBody VehicleDTO vehicleDTO) {
        log.info("Updating vehicle : {}", vehicleDTO);
        var updatedVehicle = this.vehicleService.update(vehicleDTO);
        return ResponseEntity.ok(updatedVehicle);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteVehicle(@PathVariable Long id) {
        log.info("Deleting vehicle : {}", id);
        var deleteStatus = this.vehicleService.delete(id);
        return ResponseEntity.ok(deleteStatus);
    }

    @PutMapping("/{vehicleId}/garage/{garageId}")
    public ResponseEntity<Boolean> addVehicleToGarage(@PathVariable Long vehicleId, @PathVariable Long garageId) {
        log.info("Adding vehicle {} to garage : {}", vehicleId, garageId);
        var addStatus = this.vehicleService.addVehicleToGarage(vehicleId, garageId);
        return ResponseEntity.ok(addStatus);
    }

    @DeleteMapping("/{vehicleId}/garage/{garageId}")
    public ResponseEntity<Boolean> removeVehicleFromGarage(@PathVariable Long vehicleId, @PathVariable Long garageId) {
        log.info("Removing vehicle {} to garage : {}", vehicleId, garageId);
        var removeStatus = this.vehicleService.removeVehicleFromGarage(vehicleId, garageId);
        return ResponseEntity.ok(removeStatus);
    }

    @GetMapping("/garage/{garageId}")
    public ResponseEntity<?> getVehiclesRelatedToGarage(@PathVariable Long garageId){
        log.info("Getting vehicles related to garage : {}", garageId);
        return ResponseEntity.ok(this.vehicleService.getVehiclesRelatedToAGarage(garageId));
    }

    @GetMapping("/brand/{brand}")
    public ResponseEntity<?> getVehiclesAssociatedToGaragesByBrand(@PathVariable String brand){
        log.info("Getting vehicles associated to garages by brand : {}", brand);
        return ResponseEntity.ok(this.vehicleService.getVehiclesByBrandAssociatedToGarages(brand));
    }

}
