package com.example.PhoneBookSample;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PhoneBookEntry {
    private Long id;
    private String name;
    private String[] phones = null;

    public PhoneBookEntry() {
    }

    public PhoneBookEntry(User user) {
        id = user.getId();
        name = user.getName();
        phones = user.getPhones()
                .stream()
                .map(Phone::getPhone)
                .toList()
                .toArray(new String[0]);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getPhones() {
        return phones;
    }

    public void setPhones(String[] phones) {
        this.phones = phones;
    }

    public User toUser() {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setPhones(Arrays.stream(phones)
                .map(phone -> new Phone(phone, user))
                .toList());
        return user;
    }
}
