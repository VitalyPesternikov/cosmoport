package com.space.specification;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class ShipSpecification {
    public static Specification<Ship> getSpecification(
            String name,
            String planet,
            ShipType shipType,
            Long after,
            Long before,
            Boolean isUsed,
            Double minSpeed,
            Double maxSpeed,
            Integer minCrewSize,
            Integer maxCrewSize,
            Double minRating,
            Double maxRating
    ) {
        return Specification.where(getLikeStringSpecification(name, "name")
                .and(getLikeStringSpecification(planet, "planet"))
                .and(getShipTypeSpecification(shipType))
                .and(getDateSpecification(after, before))
                .and(getUsageSpecification(isUsed)).and(getBetweenDoubleSpecification(minSpeed, maxSpeed, "speed"))
                .and(getBetweenIntegerSpecification(minCrewSize, maxCrewSize, "crewSize"))
                .and(getBetweenDoubleSpecification(minRating, maxRating, "rating")));
    }

    private static Specification<Ship> getLikeStringSpecification(String string, String attribute) {
        return (Specification<Ship>) (root, query, criteriaBuilder) -> string == null ?
                null : criteriaBuilder.like(root.get(attribute), "%" + string + "%");
    }

    private static Specification<Ship> getShipTypeSpecification(ShipType shipType) {
        return (Specification<Ship>) (root, query, criteriaBuilder) -> shipType == null ?
                null : criteriaBuilder.equal(root.get("shipType"), shipType);
    }

    private static Specification<Ship> getDateSpecification(Long after, Long before) {
        return (Specification<Ship>) (root, query, criteriaBuilder) -> {
            Date afterDate = after == null ? null : new Date(after);
            Date beforeDate = before == null ? null : new Date(before);
            if (afterDate == null && before == null) {
                return null;
            }
            if (afterDate == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("prodDate"), beforeDate);
            }
            if (beforeDate == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"), afterDate);
            }
            return criteriaBuilder.between(root.get("prodDate"), afterDate, beforeDate);
        };
    }

    private static Specification<Ship> getUsageSpecification(Boolean isUsed) {
        return (Specification<Ship>) (root, query, criteriaBuilder) -> isUsed == null ? null :
                isUsed ? criteriaBuilder.isTrue(root.get("isUsed")) : criteriaBuilder.isFalse(root.get("isUsed"));
    }

    private static Specification<Ship> getBetweenDoubleSpecification(Double minNumber, Double maxNumber, String attribute) {
        return (Specification<Ship>) (root, query, criteriaBuilder) -> {
            if (minNumber == null && maxNumber == null) {
                return null;
            }
            if (minNumber == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get(attribute), maxNumber);
            }
            if (maxNumber == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get(attribute), minNumber);
            }
            return criteriaBuilder.between(root.get(attribute), minNumber, maxNumber);
        };
    }

    private static Specification<Ship> getBetweenIntegerSpecification(Integer minNumber, Integer maxNumber, String attribute) {
        Double min = minNumber == null ? null : Double.valueOf(minNumber);
        Double max = maxNumber == null ? null : Double.valueOf(maxNumber);
        return getBetweenDoubleSpecification(min, max, attribute);
    }
}
