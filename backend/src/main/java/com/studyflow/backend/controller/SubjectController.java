package com.studyflow.backend.controller;

import com.studyflow.backend.controller.docs.SubjectControllerDocs;
import com.studyflow.backend.data.dto.SubjectDTO;
import com.studyflow.backend.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
@RequiredArgsConstructor
public class SubjectController implements SubjectControllerDocs {

    private final SubjectService subjectService;

    @PostMapping
    public ResponseEntity<SubjectDTO> create(@RequestBody @Valid SubjectDTO dto) {
        SubjectDTO subject = subjectService.create(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(subject);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectDTO> update(@PathVariable Long id, @RequestBody @Valid SubjectDTO dto) {
        SubjectDTO updatedSubject = subjectService.update(id, dto);
        return ResponseEntity.ok(updatedSubject);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subjectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SubjectDTO>> listAll() {
        List<SubjectDTO> subjects = subjectService.listAll();
        return ResponseEntity.ok(subjects);
    }
}
