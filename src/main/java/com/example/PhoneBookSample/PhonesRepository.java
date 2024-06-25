package com.example.PhoneBookSample;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhonesRepository extends JpaRepository<Phone, Long> {
    List<Phone> findAllByPhoneContains(String phone);
}
