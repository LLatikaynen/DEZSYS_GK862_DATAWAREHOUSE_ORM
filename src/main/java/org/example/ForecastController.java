package org.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
public class ForecastController {

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final PurchaseRepository purchaseRepo;
    private final DataWarehouseRepository warehouseRepo;
    private final ProductRepository productRepo;

    public ForecastController(PurchaseRepository purchaseRepo,
                            DataWarehouseRepository warehouseRepo,
                            ProductRepository productRepo) {
        this.purchaseRepo = purchaseRepo;
        this.warehouseRepo = warehouseRepo;
        this.productRepo = productRepo;
    }

    @GetMapping("/api/forecast")
    public String getSalesForecast() {
        // 1. Daten aus der DB holen
        List<Purchase> allPurchases = (List<Purchase>) purchaseRepo.findAll();

        // 2. Daten für das LLM aufbereiten (Aggregieren als Text)
        StringBuilder dataContext = new StringBuilder();
        dataContext.append("Hier sind die historischen Verkaufsdaten aus unserem Data Warehouse:\n\n");

        // Sende die ersten 50 Datenpunkte als Beispiel
        for(int i = 0; i < Math.min(allPurchases.size(), 50); i++) {
            Purchase p = allPurchases.get(i);
            dataContext.append(String.format("- Produkt: %s, Menge: %d, Datum: %s, Lager: %s\n",
                p.getProduct().getProductName(),
                p.getAmount(),
                p.getPurchaseDate().toString().substring(0, 10),
                p.getDataWarehouse().getWarehouseName()));
        }

        // 3. Den Prompt bauen
        String prompt = "Du bist ein KI-Analyst für ein Data Warehouse in Linz, Österreich. " +
                "Analysiere diese Verkaufsdaten und gib eine fundierte Prognose für die nächsten 3 Monate.\n\n" +
                dataContext.toString() +
                "\n\nFrage: Basierend auf diesen " + allPurchases.size() + " Verkaufsdatensätzen, " +
                "welche Produkte werden am beliebtesten sein? Gib eine detaillierte Analyse mit konkreten Zahlen und Trends. " +
                "Erkläre deine Prognose logisch und nenne saisonale Einflüsse.";

        try {
            // 4. Ollama API direkt aufrufen
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "llama3.2");
            requestBody.put("prompt", prompt);
            requestBody.put("stream", false);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                "http://localhost:11434/api/generate",
                entity,
                String.class
            );

            // Parse die Antwort
            JsonNode jsonResponse = objectMapper.readTree(response.getBody());
            String aiResponse = jsonResponse.get("response").asText();

            return "Prognose von Ollama/llama3.2:\n\n" + aiResponse;

        } catch (RestClientException e) {
            return "❌ Fehler bei der Ollama-Verbindung: 1" + e.getMessage() + "\n\nFallback zur Dummy-Antwort:\n\n" +
                   "Basierend auf " + allPurchases.size() + " Verkaufsdatensätzen:\n" +
                   "Die beliebtesten Produkte werden voraussichtlich Orangensaft, Kaffee und Wasser sein.";
        } catch (Exception e) {
            return "❌ Unerwarteter Fehler: " + e.getMessage() + "\n\nFallback zur Dummy-Antwort:\n\n" +
                    "Basierend auf " + allPurchases.size() + " Verkaufsdatensätzen:\n" +
                    "Die beliebtesten Produkte werden voraussichtlich Orangensaft, Kaffee und Wasser sein.";
        }
    }

    // ======== GRUNDLAGEN (GK) ========
    @GetMapping("/api/warehouses")
    public List<DataWarehouse> getAllWarehouses() {
        return (List<DataWarehouse>) warehouseRepo.findAll();
    }

    @GetMapping("/api/warehouses/{id}")
    public Map<String, Object> getWarehouse(@PathVariable String id) {
        var warehouse = warehouseRepo.findByWarehouseID(id);
        Map<String, Object> response = new HashMap<>();
        if (warehouse.isPresent()) {
            response.put("warehouse", warehouse.get());
            response.put("productCount", warehouse.get().getProducts().size());
        } else {
            response.put("error", "Warehouse nicht gefunden");
        }
        return response;
    }

    @GetMapping("/api/warehouses/{id}/products")
    public List<Product> getWarehouseProducts(@PathVariable String id) {
        var warehouse = warehouseRepo.findByWarehouseID(id);
        return warehouse.map(DataWarehouse::getProducts).orElse(List.of());
    }

    // ======== ERWEITERTE GRUNDLAGEN (EK) ========
    @GetMapping("/api/warehouse/{id}")
    public Map<String, Object> getWarehouseDetails(@PathVariable String id) {
        var warehouse = warehouseRepo.findByWarehouseID(id);
        Map<String, Object> response = new HashMap<>();
        if (warehouse.isPresent()) {
            DataWarehouse w = warehouse.get();
            response.put("id", w.getId());
            response.put("warehouseID", w.getWarehouseID());
            response.put("warehouseName", w.getWarehouseName());
            response.put("warehouseCity", w.getWarehouseCity());
            response.put("produktCount", w.getProducts().size());
        }
        return response;
    }

    @GetMapping("/api/warehouse/{id}/purchases")
    public List<Purchase> getWarehousePurchases(@PathVariable String id) {
        var warehouse = warehouseRepo.findByWarehouseID(id);
        if (warehouse.isPresent()) {
            return ((List<Purchase>) purchaseRepo.findAll()).stream()
                    .filter(p -> p.getDataWarehouse().getWarehouseID().equals(id))
                    .collect(Collectors.toList());
        }
        return List.of();
    }

    @GetMapping("/api/purchases/total")
    public Map<String, Object> getTotalPurchases() {
        Map<String, Object> response = new HashMap<>();
        response.put("totalPurchases", purchaseRepo.count());
        response.put("message", "Insgesamt " + purchaseRepo.count() + " Verkaufdatensätze in der Datenbank");
        return response;
    }

    @GetMapping("/api/purchases/count")
    public Map<String, Long> getPurchasesByWarehouse() {
        Map<String, Long> counts = new HashMap<>();
        List<DataWarehouse> warehouses = (List<DataWarehouse>) warehouseRepo.findAll();
        for (DataWarehouse w : warehouses) {
            long count = ((List<Purchase>) purchaseRepo.findAll()).stream()
                    .filter(p -> p.getDataWarehouse().getId().equals(w.getId()))
                    .count();
            counts.put(w.getWarehouseName() + " (" + w.getWarehouseID() + ")", count);
        }
        return counts;
    }

    // ======== STATISTIKEN / VERTIEFUNG ========
    @GetMapping("/api/statistics/top-products")
    public List<Map<String, Object>> getTopProducts() {
        List<Purchase> allPurchases = (List<Purchase>) purchaseRepo.findAll();

        return allPurchases.stream()
                .collect(Collectors.groupingBy(
                    p -> p.getProduct().getProductName(),
                    Collectors.summingInt(Purchase::getAmount)))
                .entrySet().stream()
                .sorted((a, b) -> b.getValue().compareTo(a.getValue()))
                .limit(5)
                .map(entry -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("product", entry.getKey());
                    map.put("totalSold", entry.getValue());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/api/statistics/by-location")
    public Map<String, Integer> getSalesByLocation() {
        List<Purchase> allPurchases = (List<Purchase>) purchaseRepo.findAll();
        return allPurchases.stream()
                .collect(Collectors.groupingBy(
                    Purchase::getLocation,
                    Collectors.summingInt(Purchase::getAmount)));
    }

    @GetMapping("/api/statistics/sales")
    public Map<String, Object> getSalesStatistics() {
        List<Purchase> allPurchases = (List<Purchase>) purchaseRepo.findAll();
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPurchases", allPurchases.size());
        stats.put("totalUnits", allPurchases.stream().mapToInt(Purchase::getAmount).sum());
        stats.put("averageUnitsPerPurchase",
            allPurchases.size() > 0 ?
            allPurchases.stream().mapToInt(Purchase::getAmount).average().orElse(0) : 0);
        stats.put("warehouses", ((List<DataWarehouse>) warehouseRepo.findAll()).size());
        stats.put("products", ((List<Product>) productRepo.findAll()).size());
        return stats;
    }
}
