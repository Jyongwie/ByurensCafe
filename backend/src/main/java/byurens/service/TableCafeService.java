package byurens.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import byurens.dto.TableCafeRequest;
import byurens.dto.TableCafeResponse;
import byurens.entities.TableCafe;
import byurens.enums.TableStatus;
import byurens.exception.ByurensCafeException;
import byurens.repository.TableCafeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TableCafeService {
    private final TableCafeRepository tableCafeRepository;

    @Transactional
    public TableCafeResponse createTableCafe(TableCafeRequest request) {
        if (tableCafeRepository.existsByTableIdentifier(request.tableIdentifier())) {
            throw new ByurensCafeException("Table identifier already exists");
        }
        TableCafe tableCafe = TableCafe.builder()
            .tableIdentifier(request.tableIdentifier())
            .capacity(request.capacity())
            .status(TableStatus.AVAILABLE)
            .build();

        TableCafe savedTable = tableCafeRepository.save(tableCafe);
        return mapToResponse(savedTable);
    }

    @Transactional
    public TableCafeResponse updateTableCafe(UUID id, TableCafeRequest request) {
        TableCafe existingTable = tableCafeRepository.findById(id)
            .orElseThrow(() -> new ByurensCafeException("Table not found"));

        if (!existingTable.getTableIdentifier().equals(request.tableIdentifier()) && tableCafeRepository.existsByTableIdentifier((request.tableIdentifier()))) {
            throw new ByurensCafeException("Table identifier already exists");
        }

        existingTable.setTableIdentifier(request.tableIdentifier());
        existingTable.setCapacity(request.capacity());
        if (request.status() != null) {
            existingTable.setStatus(request.status());
        }
        TableCafe updatedTable = tableCafeRepository.save(existingTable);
        return mapToResponse(updatedTable);
    }

    public List<TableCafeResponse> getTableCafes() {
        return tableCafeRepository.findAll().stream()
            .map(this::mapToResponse)
            .toList();
    }

    public TableCafeResponse getTableCafeById(UUID id) {
        TableCafe tableCafe = tableCafeRepository.findById(id)
            .orElseThrow(() -> new ByurensCafeException("Table not found"));
        return mapToResponse(tableCafe);
    }

    private TableCafeResponse mapToResponse(TableCafe tableCafe) {
        return new TableCafeResponse(
            tableCafe.getId(),
            tableCafe.getTableIdentifier(),
            tableCafe.getCapacity(),
            tableCafe.getStatus()
        );
    }
}