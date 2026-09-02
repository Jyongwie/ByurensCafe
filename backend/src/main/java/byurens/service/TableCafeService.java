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
    public TableCafe updateTableCafe(UUID id, TableCafe tableCafe) {
        TableCafe existingTable = tableCafeRepository.findById(id)
            .orElseThrow(() -> new ByurensCafeException("Table not found"));

        existingTable.setTableIdentifier(tableCafe.getTableIdentifier());
        existingTable.setCapacity(tableCafe.getCapacity());
        existingTable.setStatus(tableCafe.getStatus());

        return tableCafeRepository.save(existingTable);
    }

    public List<TableCafe> getTableCafes() {
        return tableCafeRepository.findAll();
    }

    public TableCafe getTableCafeById(UUID id) {
        return tableCafeRepository.findById(id)
            .orElseThrow(() -> new ByurensCafeException("Table not found"));
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