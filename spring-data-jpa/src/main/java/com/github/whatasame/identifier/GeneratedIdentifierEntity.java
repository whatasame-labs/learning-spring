package com.github.whatasame.identifier;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class GeneratedIdentifierEntity {

    @Id
    @GeneratedValue
    private Long id;
}
