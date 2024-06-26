package com.example.PhoneBookSample;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class PhoneBookServiceImpl implements PhoneBookService {
    @Autowired
    UsersRepository usersRepo;

    @Autowired
    PhonesRepository phonesRepo;

    @Override
    public List<PhoneBookEntry> allUsers() {
        return usersRepo.findAllByOrderByIdAsc()
                .stream()
                .map(PhoneBookEntry::new)
                .toList();
    }

    @Override
    public List<PhoneBookEntry> getUserByName(String name) {
        return usersRepo.findByNameContains(name)
                .stream()
                .map(PhoneBookEntry::new)
                .toList();
    }

    @Override
    public List<PhoneBookEntry> getUserByPhone(String phone, boolean useQuery) {
        if (useQuery) {
            return usersRepo.getUsersByPhone(phone)
                    .stream()
                    .map(PhoneBookEntry::new)
                    .toList();
        } else {
            return phonesRepo.findAllByPhoneContains(phone)
                    .stream()
                    .map(ph -> ph.getUser().getId())
                    .distinct()
                    .map(usersRepo::findFirstById)
                    .map(PhoneBookEntry::new)
                    .toList();
        }
    }

    @Override
    public Long save(PhoneBookEntry entry) {
        User user = new User(entry.getName(),
                Arrays.stream(entry.getPhones())
                        .map(Phone::new)
                        .toList());
        return usersRepo.save(user).getId();
    }
}
