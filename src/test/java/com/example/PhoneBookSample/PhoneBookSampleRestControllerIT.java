package com.example.PhoneBookSample;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(printOnlyOnFailure = false)
class PhoneBookSampleRestControllerIT {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    PhoneBookService service;


    @Autowired
    ObjectMapper json;

    @Test
    @DisplayName("GET /find_by_name returns HTTP-response with status=OK and PhoneBookEntry list produced by getUserByName")
    void findUserByName_ReturnsValidPhoneBookEntryList() throws Exception {
        //given
        var entry = new PhoneBookEntry();
        String tester = "Tester";
        entry.setName(tester);
        entry.setPhones(new String[]{"111", "222", "333"});
        this.service.deleteByName(tester);
        this.service.save(entry);

        var requestBuilder = MockMvcRequestBuilders.get("http://localhost:8080/find_by_name?value=" + tester);

        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                                        [
                                                        {
                                                        "name":"Tester",
                                                        "phones":["111", "222", "333"]
                                                        }
                                                        ]
                                """)
                );
    }

}
