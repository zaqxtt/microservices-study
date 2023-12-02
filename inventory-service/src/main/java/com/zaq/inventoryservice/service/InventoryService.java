package com.zaq.inventoryservice.service;

import com.zaq.inventoryservice.dto.InventoryResponse;
import com.zaq.inventoryservice.model.Inventory;
import com.zaq.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCodeList) {
        List<InventoryResponse> inventoryResponses = inventoryRepository.findBySkuCodeIn(skuCodeList)
                .stream()
                .map(inventory -> InventoryResponse.builder()
                        .skuCode(inventory.getSkuCode())
                        .isInStock(inventory.getQuantity() > 0)
                        .build())
                .toList();
        return inventoryResponses;
    }
}
