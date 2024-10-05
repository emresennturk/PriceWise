package Project.PriceWise.services;

import Project.PriceWise.entities.Product;
import Project.PriceWise.entities.User;
import Project.PriceWise.repositories.ProductRepository;
import Project.PriceWise.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceDropNotificationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ScraperService scraperService;

    // Favori ürünlerdeki fiyat düşüşlerini kontrol etmek için zamanlanmış methot
    @Scheduled(fixedRate = 21600000) //Her 6 saatte bir
    public void checkForPriceDrops() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            for (Long productId : user.getFavouriteProductIds()) {
                Product product = productRepository.findById(productId).orElse(null);
                if (product != null) {
                    double currentPrice = product.getPrice();
                    double scrapedPrice = scrapeProductPrice(product.getUrl());

                    if (scrapedPrice < currentPrice) {
                        product.setPrice(scrapedPrice);
                        productRepository.save(product);

                        //// Kullanıcıya bildirim gönder
                        emailService.sendPriceDropNotification(user.getEmail(), product);
                    }
                }
            }
        }
    }

    // Ürün URL'sini al güncellenmiş fiyatı döndür
    private double scrapeProductPrice(String productUrl) {

        //Güncel fiyatı almak için scraping servisi çağır
        Product scrapedProduct = scraperService.scrapeProduct(productUrl);
        return scrapedProduct.getPrice();
    }
}
