package com.arg.ccra3.dao.repo;

import com.arg.ccra3.model.api.MalProductDeliverAPI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MalProductDeliverAPIRepo extends JpaRepository<MalProductDeliverAPI, Long> {

}
