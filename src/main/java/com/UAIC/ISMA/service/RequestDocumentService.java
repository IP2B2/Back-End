package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dao.RequestDocument;
import com.UAIC.ISMA.dto.RequestDocumentDTO;
import com.UAIC.ISMA.exception.RequestDocumentNotFoundException;
import com.UAIC.ISMA.mapper.RequestDocumentMapper;
import com.UAIC.ISMA.repository.AccessRequestRepository;
import com.UAIC.ISMA.repository.RequestDocumentRepository;
import com.UAIC.ISMA.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestDocumentService {

    private final RequestDocumentRepository requestDocumentRepository;
    private final AccessRequestRepository accessRequestRepository;
    private final UserRepository userRepository;

    public RequestDocumentService(RequestDocumentRepository requestDocumentRepository,
                                  AccessRequestRepository accessRequestRepository,
                                  UserRepository userRepository) {
        this.requestDocumentRepository = requestDocumentRepository;
        this.accessRequestRepository = accessRequestRepository;
        this.userRepository = userRepository;
    }

    public List<RequestDocumentDTO> findAll(Long userId, Long accessRequestId) {
        return requestDocumentRepository.findAll()
                .stream()
                .map(RequestDocumentMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RequestDocumentDTO findById(Long id) {
        RequestDocument document = requestDocumentRepository.findById(id)
                .orElseThrow(() -> new RequestDocumentNotFoundException(id));
        return RequestDocumentMapper.toDTO(document);
    }

    public RequestDocumentDTO create(RequestDocumentDTO dto) {
        RequestDocument entity = RequestDocumentMapper.toEntity(dto);


        RequestDocument saved = requestDocumentRepository.save(entity);
        return RequestDocumentMapper.toDTO(saved);
    }

    public RequestDocumentDTO update(Long id, RequestDocumentDTO dto) {
        RequestDocument existing = requestDocumentRepository.findById(id)
                .orElseThrow(() -> new RequestDocumentNotFoundException(id));

        existing.setTitle(dto.getTitle());
        existing.setDescription(dto.getDescription());
        existing.setFilePath(dto.getFilePath());



        RequestDocument updated = requestDocumentRepository.save(existing);
        return RequestDocumentMapper.toDTO(updated);
    }

    public void delete(Long id) {
        RequestDocument entity = requestDocumentRepository.findById(id)
                .orElseThrow(() -> new RequestDocumentNotFoundException(id));
        requestDocumentRepository.delete(entity);
    }
}
