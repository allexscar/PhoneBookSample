package com.example.PhoneBookSample;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PhoneBookSampleRestController {
    private final UsersRepository usersRepo;
    private final PhonesRepository phonesRepo;
    private final ObjectMapper json;

    public PhoneBookSampleRestController(UsersRepository usersRepo, PhonesRepository phonesRepo, ObjectMapper json) {
        this.usersRepo = usersRepo;
        this.phonesRepo = phonesRepo;
        this.json = json;
    }

    @GetMapping("/list")
    public List<User> getList() {
        //json processing sample (not used)
        /*
        try {
            System.out.println(json.writeValueAsString(repo.findAllByOrderByIdAsc()));
        } catch (Throwable th) {
            th.printStackTrace();
        }
        */
        return usersRepo.findAllByOrderByIdAsc();
    }

    @GetMapping("/find_by_name")
    public List<User> findUserByName(@RequestParam(value = "value") String value) {
        return usersRepo.findByNameContains(value);
    }

    @GetMapping("/find_by_phone") // stream variant
    public List<User> findUserByPhone(@RequestParam(value = "value") String value) {
        return phonesRepo.findAllByPhoneContains(value)
                .stream()
                .map(Phone::getUserId)
                .distinct()
                .map(usersRepo::findFirstById)
                .collect(Collectors.toList());
    }

    @GetMapping("/find_by_phone2")  // query variant
    public List<User> findUserByPhone2(@RequestParam(value = "value") String value) {
        return usersRepo.getUsersByPhone(value);
    }

    @PostMapping("/add_user")
    public String addUser(@RequestBody User user) {
        Long userId = saveWithDetails(user);
        return userId > 0 ? "Added a new user with id="+userId : "Failed to add a new user";
    }

    @Transactional
    Long saveWithDetails(User user) {
        List<Phone> phones = user.getPhones();
        user.setPhones(null);
        User tmpUser = usersRepo.save(user);
        for (Phone phone : phones) {
            phone.setUserId(tmpUser.getId());
            try {
                phonesRepo.save(phone);
            } catch (Throwable th) {
                usersRepo.deleteById(tmpUser.getId());
                return 0L;
            }
        }
        return tmpUser.getId();
    }
}
