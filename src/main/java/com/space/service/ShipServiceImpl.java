package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShipServiceImpl implements ShipService {

    private ShipRepository shipRepository;

    @Autowired
    public void setShipRepository(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    @Override
    public List<Ship> getAllShips(Specification<Ship> shipSpecification) {
        return shipRepository.findAll(shipSpecification);
    }

    @Override
    public Page<Ship> getAllShips(Specification<Ship> shipSpecification, Pageable pageable) {
        return shipRepository.findAll(shipSpecification, pageable);
    }
}
