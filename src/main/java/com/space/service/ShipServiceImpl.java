package com.space.service;

import com.space.model.Ship;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
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

    @Override
    public Ship createShip(Ship ship) {
        if (ship.getName() == null
                || ship.getPlanet() == null
                || ship.getShipType() == null
                || ship.getProdDate() == null
                || ship.getSpeed() == null
                || ship.getCrewSize() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "There is a null parameter");

        checkShipParams(ship);

        if (ship.getUsed() == null)
            ship.setUsed(false);

        Double rating = calculateRating(ship);
        ship.setRating(rating);

        return shipRepository.saveAndFlush(ship);
    }

    private void checkShipParams(Ship ship) {
        if (ship.getName() != null && (ship.getName().length() < 1 || ship.getName().length() > 50))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid name");

        if (ship.getPlanet() != null && (ship.getPlanet().length() < 1 || ship.getPlanet().length() > 50))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid planet");

        if (ship.getCrewSize() != null && (ship.getCrewSize() < 1 || ship.getCrewSize() > 9999))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid crewSize");

        if (ship.getSpeed() != null && (ship.getSpeed() < 0.01D || ship.getSpeed() > 0.99D))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid speed");

        if (ship.getProdDate() != null) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(ship.getProdDate());
            if (cal.get(Calendar.YEAR) < 2800 || cal.get(Calendar.YEAR) > 3019)
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date");
        }
    }

    private Double calculateRating(Ship ship) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(ship.getProdDate());
        int year = cal.get(Calendar.YEAR);

        BigDecimal raiting = new BigDecimal((80 * ship.getSpeed() * (ship.getUsed() ? 0.5 : 1)) / (3019 - year + 1));
        raiting = raiting.setScale(2, RoundingMode.HALF_UP);
        return raiting.doubleValue();
    }
}
