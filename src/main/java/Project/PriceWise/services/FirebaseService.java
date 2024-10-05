package Project.PriceWise.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

@Service
public class FirebaseService {
    // Frontend'ten gönderilen ID tokeni doğrular (Firebase Auth)
    public FirebaseToken verifyToken(String idToken) throws Exception {
        return FirebaseAuth.getInstance().verifyIdToken(idToken);
    }

    // Token'dan kullanıcı bilgilerini çıkarır
    public String getUserIdFromToken(String idToken) throws Exception {
        FirebaseToken decodedToken = verifyToken(idToken);
        return decodedToken.getUid();
    }
}
