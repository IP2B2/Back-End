package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dao.RequestDocument;
import com.UAIC.ISMA.service.RequestDocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/request-document")
public class RequestDocumentController {

    private final RequestDocumentService service;

    @Autowired
    public RequestDocumentController(RequestDocumentService service) {
        this.service = service;
    }

    @GetMapping
    public List<RequestDocument> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public RequestDocument getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public RequestDocument create(@RequestBody RequestDocument doc) {
        return service.create(doc);
    }

    @PutMapping("/{id}")
    public RequestDocument update(@PathVariable Long id, @RequestBody RequestDocument doc) {
        return service.update(id, doc);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
