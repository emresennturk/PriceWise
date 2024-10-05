package Project.PriceWise.services;

import Project.PriceWise.entities.Product;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class ScraperService {

    private static final String SCRAPER_URL = "http://scraper:5000/scrape";  // Scraper container URL'i
    private final RestTemplate restTemplate;

    public ScraperService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;  // Dependency injection
    }

    public Product scrapeProduct(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // URL'i scrapera gönder
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("url", url);

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        // Ürün ayrıntılarını içeren kazıyıcıdan gelen yanıt
        ResponseEntity<Map<String, Object>> response;

        try {
            //Ürün ayrıntılarını içeren kazıyıcıdan gelen yanıt
            response = restTemplate.exchange(SCRAPER_URL, HttpMethod.POST, requestEntity, new ParameterizedTypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            // Hata durumunda uygun bir mesaj veya istisna fırlar
            throw new RuntimeException("Ürün verileri kazıma hizmetinden kazınamadı.", e);
        }

        Map<String, Object> productData = response.getBody();

        if (productData != null) {
            Product product = new Product();
            product.setName((String) productData.get("name"));

            // Fiyat kontrolü
            Object priceObj = productData.get("price");
            if (priceObj != null) {
                product.setPrice(Double.parseDouble(priceObj.toString()));
            }

            product.setImageUrl((String) productData.get("image_url"));
            product.setUrl(url);
            return product;
        } else {
            throw new RuntimeException("Kazıyıcıdan ürün verisi döndürülmedi.");
        }
    }
}
