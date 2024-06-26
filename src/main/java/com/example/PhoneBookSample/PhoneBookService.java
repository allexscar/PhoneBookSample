package com.example.PhoneBookSample;

import java.util.List;

public interface PhoneBookService {
    List<PhoneBookEntry> allUsers();
    List<PhoneBookEntry> getUserByName(String name);
    List<PhoneBookEntry> getUserByPhone(String phone, boolean useQuery);
    PhoneBookEntry save(PhoneBookEntry entry);
    Integer deleteByName(String name);
}
