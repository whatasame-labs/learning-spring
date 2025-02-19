package com.whatasame.generatedidentifier;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class SequenceStrategyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "super_duper_seq")
    private Long id;
}
