package com.example.PhoneBookSample;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UsersRepository extends JpaRepository<User, Long> {
    List<User> findAllByOrderByIdAsc();
    List<User> findByNameContains(String name);
    User findFirstById(Long id);

    @Query(value="select distinct u.* from test.users u, test.phones p where p.user_id=u.id and p.phone like '%' || :phone || '%'", nativeQuery = true)
    List<User> getUsersByPhone(@Param("phone") String phone);
}
