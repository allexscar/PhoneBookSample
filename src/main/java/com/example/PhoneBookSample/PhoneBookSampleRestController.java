package com.example.PhoneBookSample;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class PhoneBookSampleRestController {

    final PhoneBookService srv;

    public PhoneBookSampleRestController(PhoneBookService srv) {
        this.srv = srv;
    }

    @GetMapping("/list")
    public ResponseEntity<List<PhoneBookEntry>> getList() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(srv.allUsers());
    }

    @GetMapping("/find_by_name")
    public ResponseEntity<List<PhoneBookEntry>> findUserByName(@RequestParam(value = "value") String value) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(srv.getUserByName(value));
    }

    @GetMapping("/find_by_phone") // stream variant
    public ResponseEntity<List<PhoneBookEntry>> findUserByPhone(@RequestParam(value = "value") String value) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(srv.getUserByPhone(value, false));
    }

    @GetMapping("/find_by_phone2")  // query variant
    public ResponseEntity<List<PhoneBookEntry>> findUserByPhone2(@RequestParam(value = "value") String value) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(srv.getUserByPhone(value, true));
    }

    @PostMapping("/add")
    public ResponseEntity<?> addUser(@RequestBody PhoneBookEntry entry) {
        if (entry.getName() == null || "".equals(entry.getName())) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("Bad field 'name' of PhoneBookEntry");
        } else {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(srv.save(entry));
        }
    }

    @DeleteMapping("/delete")
    public void deleteUserByName(@RequestParam(value = "value") String value) {
        srv.deleteByName(value);
    }
}
