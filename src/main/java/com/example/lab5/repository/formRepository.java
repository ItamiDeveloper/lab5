package com.example.lab5.repository;

import com.example.lab5.model.form;  // Manteniendo 'form' en minúsculas
import org.springframework.data.jpa.repository.JpaRepository;

public interface formRepository extends JpaRepository<form, Long> {  // Manteniendo 'formRepository' en minúsculas
}
