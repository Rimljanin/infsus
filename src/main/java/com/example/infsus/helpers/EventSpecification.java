package com.example.infsus.helpers;

import com.example.infsus.model.Event;
import com.example.infsus.model.Location;
import com.example.infsus.model.Sport;
import com.example.infsus.model.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventSpecification {
    public static Specification<Event> getEventsByCriteria(String name, Location location, Sport sport, LocalDateTime startTime, Boolean myGames, User currentUser) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (name != null) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + name + "%"));
            }
            if (location != null) {
                predicates.add(criteriaBuilder.equal(root.get("location"), location));
            }
            if (sport != null) {
                predicates.add(criteriaBuilder.equal(root.get("sport"), sport));
            }
            if (startTime != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), startTime));
            }
            if (myGames != null) {
                if (myGames) {
                    predicates.add(criteriaBuilder.equal(root.get("eventOwner"), currentUser));
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), LocalDateTime.now()));
                } else {
                    predicates.add(criteriaBuilder.notEqual(root.get("eventOwner"), currentUser));
                    predicates.add(criteriaBuilder.lessThan(root.get("currentPeople"), root.get("maxPeople")));
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("startTime"), LocalDateTime.now()));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
