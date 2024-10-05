package Project.PriceWise.services;

import Project.PriceWise.entities.User;
import Project.PriceWise.repositories.UserRepository;
import com.google.firebase.database.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final DatabaseReference databaseReference;

    @Autowired
    private UserRepository userRepository;

    public UserService() {
        this.databaseReference = FirebaseDatabase.getInstance().getReference("users");
    }

    @Scheduled(fixedRate = 600000)
    public void scheduledFetchUsers() {
        fetchUsers();
    }

    // Firebase Gerçek Zamanlı Veritabanından kullanıcıları getirir
    public void fetchUsers() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> users = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    // Map each child (user data) to a User object
                    User user = snapshot.getValue(User.class);
                    if (user != null) {
                        // Optionally, save to MySQL database using UserRepository
                        User existingUser = userRepository.findByFirebaseUid(user.getFirebaseUid());
                        if (existingUser != null) {
                            // Update existing user
                            existingUser.setName(user.getName());
                            existingUser.setEmail(user.getEmail());
                            userRepository.save(existingUser);
                        } else {
                            // Insert new user
                            userRepository.save(user);
                        }
                        users.add(user);
                    }
                }
                System.out.println("Fetched Users: " + users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.err.println("Error fetching users: " + databaseError.getMessage());
            }
        });
    }
}
