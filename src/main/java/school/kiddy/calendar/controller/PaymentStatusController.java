package school.kiddy.calendar.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Base64;

@RestController
public class PaymentStatusController {

    private final String publicId = "pk_93b3c9742cd14c041341d5d7591ca";
    private final String apiSecret = "312018dbea356e5abd01b568494df3a7";

    @GetMapping("/checkPaymentStatus")
    public ResponseEntity<String> checkPaymentStatus(@RequestParam String invoiceId) {
        String url = "https://api.cloudpayments.ru/payments/find";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String auth = publicId + ":" + apiSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);

        // Подготовка данных запроса
        String requestBody = "{\"InvoiceId\": \"" + invoiceId + "\"}";

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            // Обработка JSON-ответа
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            if (root.path("Success").asBoolean()) {
                JsonNode model = root.path("Model");
                double amount = model.path("Amount").asDouble();
                String status = model.path("Status").asText();

                return ResponseEntity.ok("Сумма платежа: " + amount + ", Статус платежа: " + status);
            } else {
                return ResponseEntity.badRequest().body("Ошибка: " + root.path("Message").asText());
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка сервера: " + e.getMessage());
        }
    }


    @GetMapping("/checkInvoiceStatus")
    public ResponseEntity<String> checkInvoiceStatus(@RequestParam String invoiceId) {
        String url = "https://api.cloudpayments.ru/orders/find";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String auth = publicId + ":" + apiSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);

        // Подготовка данных запроса
        String requestBody = "{\"InvoiceId\": \"" + invoiceId + "\"}";

        HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            // Обработка JSON-ответа
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            if (root.path("Success").asBoolean()) {
                JsonNode model = root.path("Model");
                double amount = model.path("Amount").asDouble();
                String status = model.path("Status").asText();

                return ResponseEntity.ok("Сумма инвойса: " + amount + ", Статус инвойса: " + status);
            } else {
                return ResponseEntity.badRequest().body("Ошибка: " + root.path("Message").asText());
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка сервера: " + e.getMessage());
        }
    }


    @GetMapping("/listInvoices")
    public ResponseEntity<String> listInvoices() {
        String url = "https://api.cloudpayments.ru/invoices/list";
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String auth = publicId + ":" + apiSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
        headers.set("Authorization", "Basic " + encodedAuth);

        // Подготовка запроса без тела, так как /invoices/list может не требовать параметров для получения всех заказов
        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

            // Обработка JSON-ответа
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            if (root.path("Success").asBoolean()) {
                JsonNode invoices = root.path("Model");
                return ResponseEntity.ok("Список инвойсов: " + invoices.toString());
            } else {
                return ResponseEntity.badRequest().body("Ошибка: " + root.path("Message").asText());
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка сервера: " + e.getMessage());
        }
    }



}