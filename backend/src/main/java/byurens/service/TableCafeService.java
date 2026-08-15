package byurens.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import byurens.entities.TableCafe;
import byurens.exception.ByurensCafeException;
import byurens.repository.TableCafeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TableCafeService {
    private final TableCafeRepository tableCafeRepository;

    public TableCafe createTableCafe(TableCafe tableCafe) {
        return tableCafeRepository.save(tableCafe);
    }

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
}