package byurens.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import byurens.dto.TableCafeRequest;
import byurens.dto.TableCafeResponse;
import byurens.service.TableCafeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/tables")
@RequiredArgsConstructor 
public class TableCafeController {
    private final TableCafeService tableCafeService;

    @PostMapping 
    public ResponseEntity<TableCafeResponse> createTableCafe(@Valid @RequestBody TableCafeRequest request) {
        TableCafeResponse response = tableCafeService.createTableCafe(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TableCafeResponse> updateTableCafe(@PathVariable UUID id, @Valid @RequestBody TableCafeRequest request) {
        TableCafeResponse response = tableCafeService.updateTableCafe(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping 
    public ResponseEntity<List<TableCafeResponse>> getTableCafes() {
        List<TableCafeResponse> responses = tableCafeService.getTableCafes();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TableCafeResponse> getTableCafeById(@PathVariable UUID id) {
        TableCafeResponse response = tableCafeService.getTableCafeById(id);
        return ResponseEntity.ok(response);
    }
}
