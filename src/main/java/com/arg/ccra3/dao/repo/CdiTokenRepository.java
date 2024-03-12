/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.ccra3.dao.repo;


import com.arg.ccra3.models.CdiToken;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface CdiTokenRepository extends JpaRepository<CdiToken, Long>{
    public List<CdiToken>findByCdiToken(String cdiToken);
    
    public List<CdiToken> findByuIdAndCdiToken(Long uId,String cdiToken);
}
