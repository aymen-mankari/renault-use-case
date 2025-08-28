package com.renault.repository;

import com.renault.model.Accessory;
import com.renault.model.Garage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AccessoryRepository extends JpaRepository<Accessory, Long> {

}
