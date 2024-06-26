package com.example.PhoneBookSample;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhoneBookSampleRestControllerTest {
    @Mock
    PhoneBookService service;

    @InjectMocks
    PhoneBookSampleRestController controller;

    @Test
    @DisplayName("GET /list returns HTTP-response with status=OK and PhoneBookEntry list")
    void getList_ReturnsValidPhoneBookEntryList() {
        //given
        var entry1 = new PhoneBookEntry();
        entry1.setName("First entry");
        entry1.setPhones(new String[0]);

        var entry2 = new PhoneBookEntry();
        entry2.setName("Second entry");
        entry2.setPhones(new String[] {"111", "222", "333"});

        var entries = List.of(entry1, entry2);

        doReturn(entries).when(this.service).allUsers();

        //when
        var responseEntries = this.controller.getList();

        //then
        assertNotNull(responseEntries);
        assertEquals(HttpStatus.OK, responseEntries.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntries.getHeaders().getContentType());
        assertEquals(entries, responseEntries.getBody());
    }

    @Test
    @DisplayName("POST /add returns HTTP-response with status=OK and save new PhoneBookEntry to repository")
    void addUser_PhoneBookEntryIsValid_ReturnsValidPhoneBookEntry() {
        //given
        var entry = new PhoneBookEntry();
        entry.setName("New entry");
        entry.setPhones(new String[] {"111", "222", "333"});

        doReturn(entry).when(this.service).save(entry);

        //when
        var responseEntry = this.controller.addUser(entry);

        //then
        assertNotNull(responseEntry);
        assertEquals(HttpStatus.OK, responseEntry.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntry.getHeaders().getContentType());
        if (responseEntry.getBody() instanceof PhoneBookEntry savedEntry) {
            assertNotNull(savedEntry.getName());
            assertNotNull(savedEntry.getPhones());
            assertEquals(savedEntry, responseEntry.getBody());

            verify(this.service).save(savedEntry);
        } else {
            assertInstanceOf(PhoneBookEntry.class, responseEntry.getBody());
        }

        verifyNoMoreInteractions(this.service);
    }

    @Test
    @DisplayName("POST /add returns bad request and not save new PhoneBookEntry to repository")
    void addUser_PhoneBookEntryIsInvalid_ReturnsValidPhoneBookEntry() {
        //given
        var entry = new PhoneBookEntry();
        entry.setPhones(new String[] {"111", "222", "333"});

        //when
        var responseEntry = this.controller.addUser(entry);

        //then
        assertNotNull(responseEntry);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntry.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, responseEntry.getHeaders().getContentType());
        assertEquals("Bad field 'name' of PhoneBookEntry", responseEntry.getBody());

        verifyNoInteractions(this.service);
    }

}