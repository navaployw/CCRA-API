
package com.arg.ccra3.online.rest;

import com.arg.ccra3.online.form.IDSearchForm;
import com.arg.ccra3.online.form.NameSearchForm;
import com.arg.ccra3.online.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/search")
public class SearchCtrl {
    @Autowired
    private SearchService search;

    /*@GetMapping
    public int getCount(){
        return search.count();
    }*/

    @PostMapping(value = "/name-search")
    public Object searchName(@RequestBody NameSearchForm form){
        /*List<SearchByNameData> results = search.searchName(form);
        HashMap<String, Object> res = new HashMap<>();
        res.put("results", results);
        res.put("status", 200);
        return res;*/
        return null;
    }
    
    @PostMapping(value = "/id-search")
    public Object idSearch(@RequestBody IDSearchForm form){
        /*List<SearchByNameData> results = search.searchName(form);
        HashMap<String, Object> res = new HashMap<>();
        res.put("results", results);
        res.put("status", 200);
        return res;*/
        return null;
    }

    /*@PostMapping
    public int saveResults(@RequestBody Object input){
        System.out.println(input);
        search.saveResults(input);
        return 200;
    }*/

    /*@PostMapping
    public Results searchResults(@RequestBody Results input){
        System.out.println(input);
        return search.searchResults(input);
    }*/
}
