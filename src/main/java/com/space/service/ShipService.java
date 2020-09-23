package com.space.service;

import com.space.model.Ship;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface ShipService {
    List<Ship> getAllShips(Specification<Ship> shipSpecification);

    Page<Ship> getAllShips(Specification<Ship> shipSpecification, Pageable pageable);

    Ship createShip(Ship ship);
}
