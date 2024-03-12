
package com.arg.ccra3.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.arg.ccra3.online.form.IDSearchForm;
import com.arg.ccra3.online.form.NameSearchForm;
import com.arg.ccra3.online.service.SearchService;


@RestController
@RequestMapping(path = "api/search")
public class SearchCtrl {
    @Autowired
    private SearchService search;

    @PostMapping(value = "/name-search")
    public Object searchName(@RequestBody NameSearchForm form){
        return null;
    }
    
    @PostMapping(value = "/id-search")
    public Object idSearch(@RequestBody IDSearchForm form){
        return null;
    }
}
