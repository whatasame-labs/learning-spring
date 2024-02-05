package com.github.whatasame.generatedidentifier;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.UUID;
import lombok.Getter;

@Entity
@Getter
public class AutoStrategyUuidEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
}
