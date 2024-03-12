/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.ccra3.dao.repo;



import com.arg.ccra3.models.CtrlKey;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
@Repository
public interface CtrlKeyRepository extends JpaRepository<CtrlKey, Long> {
    @Query(value = """
                   SELECT TOP 1 * FROM API_CTRL_KEY
                   WHERE CTRLTYPE = 'CDIKEY' AND CTRL_FLAG = 1
                   AND CTRL_START <= GETDATE() AND (CTRL_END IS NULL OR CTRL_END >= GETDATE() )
                   ORDER BY CTRL_START""",nativeQuery = true)
    List<CtrlKey> getKey();
}
