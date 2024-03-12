package com.arg.ccra3.dao.repo;

import com.arg.ccra3.models.api.UserAPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserAPIRepo extends JpaRepository<UserAPI, Long> {
    
}
