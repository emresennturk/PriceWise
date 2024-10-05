package Project.PriceWise.controller;

import Project.PriceWise.entities.Product;
import Project.PriceWise.entities.User;
import Project.PriceWise.repositories.ProductRepository;
import Project.PriceWise.repositories.UserRepository;
import Project.PriceWise.services.FirebaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private FirebaseService firebaseService;

    @GetMapping("/me")
    public ResponseEntity<User> getMe(@RequestHeader("Authorization") String idToken) throws Exception {
        String firebaseUid = firebaseService.getUserIdFromToken(idToken);
        User user = userRepository.findByFirebaseUid(firebaseUid);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(user);
    }

    @PostMapping("/me/favorites")
    public ResponseEntity<String> addFavorites(@RequestHeader("Authorization") String idToken, @RequestParam Long productId) throws Exception {
        String firebaseUid = firebaseService.getUserIdFromToken(idToken);
        User user = userRepository.findByFirebaseUid(firebaseUid);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        if (!user.getFavouriteProductIds().contains(productId)) {
            user.getFavouriteProductIds().add(productId);
            userRepository.save(user);
        }
        return ResponseEntity.ok("Ürün favorilere eklendi");
    }

    @GetMapping("/me/favorites")
    public List<Product> getFavorites(@RequestHeader("Authorization") String idToken) throws Exception {
        String firebaseUid = firebaseService.getUserIdFromToken(idToken);
        User user = userRepository.findByFirebaseUid(firebaseUid);
        if (user == null) {
            return Collections.emptyList();
        }
        return productRepository.findAllById(user.getFavouriteProductIds());
    }
}
