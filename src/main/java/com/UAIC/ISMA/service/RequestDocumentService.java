package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dao.RequestDocument;
import com.UAIC.ISMA.repository.RequestDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequestDocumentService {

    private final RequestDocumentRepository repo;

    @Autowired
    public RequestDocumentService(RequestDocumentRepository repo) {
        this.repo = repo;
    }

    public List<RequestDocument> getAll() {
        try {
            return repo.findAll();
        } catch (Exception ex) {
            throw new RuntimeException("Error fetching all request documents", ex);
        }
    }

    public RequestDocument getById(Long id) {
        try {
            return repo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("RequestDocument not found with id " + id));
        } catch (ResourceNotFoundException rnfe) {
            throw rnfe;
        } catch (Exception ex) {
            throw new RuntimeException("Error fetching request document with id " + id, ex);
        }
    }

    public RequestDocument create(RequestDocument doc) {
        try {
            validateDocument(doc);
            return repo.save(doc);
        } catch (IllegalArgumentException iae) {
            throw iae;
        } catch (Exception ex) {
            throw new RuntimeException("Error creating request document", ex);
        }
    }

    public RequestDocument update(Long id, RequestDocument details) {
        try {
            validateDocument(details);
            RequestDocument existing = repo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("RequestDocument not found with id " + id));
            existing.setFilePath(details.getFilePath());
            existing.setDescription(details.getDescription());
            return repo.save(existing);
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Exception ex) {
            throw new RuntimeException("Error updating request document with id " + id, ex);
        }
    }

    public void delete(Long id) {
        try {
            RequestDocument existing = repo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("RequestDocument not found with id " + id));
            repo.delete(existing);
        } catch (ResourceNotFoundException rnfe) {
            throw rnfe;
        } catch (Exception ex) {
            throw new RuntimeException("Error deleting request document with id " + id, ex);
        }
    }

    private void validateDocument(RequestDocument doc) {
        if (doc.getTitle() == null || doc.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Document name must not be null or empty");
        }
    }
}
