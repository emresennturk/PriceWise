package Project.PriceWise.controller;

import Project.PriceWise.entities.Product;
import Project.PriceWise.repositories.ProductRepository;
import Project.PriceWise.services.ScraperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ScraperService scraperService;

    //Bütün ürünleri getirir
    @GetMapping
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // id ile tek ürün getirme
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        return productRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    // Bir ürünün verilerini kazıyın (scraper tetikleme)
    @PostMapping("/scrape")
    public ResponseEntity<Product> scrapeProduct(@RequestParam String url) {
        // Ürün ayrıntılarını URL'den çıkarmak için kazıyıcı hizmetini çağırma
        Product scrapedProduct = scraperService.scrapeProduct(url);
        Optional<Product> existingProduct = productRepository.findByUrl(url);

        // Ürün mevcutsa, fiyatı ve diğer ayrıntıları günceller, aksi takdirde yeni bir ürün kayder
        if (existingProduct.isPresent()) {
            Product product = existingProduct.get();
            product.setPrice(scrapedProduct.getPrice());
            product.setName(scrapedProduct.getName());
            product.setImageUrl(scrapedProduct.getImageUrl());
            return ResponseEntity.ok(productRepository.save(product));
        } else {
            return ResponseEntity.ok(productRepository.save(scrapedProduct));
        }
    }
}
