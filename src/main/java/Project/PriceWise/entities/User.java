package Project.PriceWise.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String firebaseUid;
    private String email;


    @ElementCollection
    private List<Long> favouriteProductIds = new ArrayList<>();
}
