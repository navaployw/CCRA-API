/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.arg.ccra3.dao.repo;


import com.arg.ccra3.models.MessageError;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface MessageErrorRepository extends JpaRepository<MessageError, Long>{
    public List<MessageError> findByerrorCode(String errorCode);
}
