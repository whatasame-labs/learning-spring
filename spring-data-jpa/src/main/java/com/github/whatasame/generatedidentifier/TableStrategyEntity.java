package com.github.whatasame.generatedidentifier;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class TableStrategyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long id;
}
