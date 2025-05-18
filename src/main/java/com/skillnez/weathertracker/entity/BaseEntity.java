package com.skillnez.weathertracker.entity;

import jakarta.persistence.MappedSuperclass;

import java.io.Serializable;

@MappedSuperclass
public interface BaseEntity <T extends Serializable>{
    T getId();
}
