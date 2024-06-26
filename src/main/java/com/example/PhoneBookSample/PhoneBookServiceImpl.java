package com.example.PhoneBookSample;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PhoneBookServiceImpl implements PhoneBookService {
    final
    UsersRepository usersRepo;

    final
    PhonesRepository phonesRepo;

    public PhoneBookServiceImpl(UsersRepository usersRepo, PhonesRepository phonesRepo) {
        this.usersRepo = usersRepo;
        this.phonesRepo = phonesRepo;
    }

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
    public PhoneBookEntry save(PhoneBookEntry entry) {
        return new PhoneBookEntry(usersRepo.saveAndFlush(entry.toUser()));
    }
}
