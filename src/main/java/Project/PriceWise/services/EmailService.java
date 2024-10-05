package Project.PriceWise.services;

import Project.PriceWise.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailsender;


    public void sendPriceDropNotification(String userEmail, Product product) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject("Fiyat düştü uyarısı" + product.getName());
        message.setText("Şu ürürnün" + product.getName() + "fiyatı düştü! Yeni fiyat: " + product.getPrice() + "TL" + "Gözatmak için: " + product.getUrl());

        mailsender.send(message);
    }
}
