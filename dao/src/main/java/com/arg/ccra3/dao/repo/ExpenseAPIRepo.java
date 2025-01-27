package com.arg.ccra3.dao.repo;

import com.arg.ccra3.model.api.ExpenseAPI;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseAPIRepo extends JpaRepository<ExpenseAPI, Long> {
    public List<ExpenseAPI> findByexpenseid(Long ID);
}
