package com.example.PhoneBookSample;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PhoneBookSampleRestController {

    final PhoneBookService srv;

    public PhoneBookSampleRestController(PhoneBookService srv) {
        this.srv = srv;
    }

    @GetMapping("/list")
    public List<PhoneBookEntry> getList() {
        return srv.allUsers();
    }

    @GetMapping("/find_by_name")
    public List<PhoneBookEntry> findUserByName(@RequestParam(value = "value") String value) {
        return srv.getUserByName(value);
    }

    @GetMapping("/find_by_phone") // stream variant
    public List<PhoneBookEntry> findUserByPhone(@RequestParam(value = "value") String value) {
        return srv.getUserByPhone(value, false);
    }

    @GetMapping("/find_by_phone2")  // query variant
    public List<PhoneBookEntry> findUserByPhone2(@RequestParam(value = "value") String value) {
        return srv.getUserByPhone(value, true);
    }

    @PostMapping("/add_user")
    public String addUser(@RequestBody PhoneBookEntry entry) {
        Long resId = srv.save(entry);
        return resId > 0 ? "Added a new user with id=" + resId : "Failed to add a new user";
    }
}
