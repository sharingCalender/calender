package sharingcalender.calender.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(length = 100)
    private String name;

    private String email;

    private LocalDateTime createdAt;

    private String mobile;

    private String username;

    private String password;

    private String provider;

    private User(String name, String email, LocalDateTime createdAt, String mobile, String username,
        String password, String provider) {
        this.name = name;
        this.email = email;
        this.createdAt = createdAt;
        this.mobile = mobile;
        this.username = username;
        this.password = password;
        this.provider = provider;
    }

    public static User create(String name, String email, LocalDateTime createdAt, String mobile,
        String username,
        String password, String provider) {

        return new User(name, email, createdAt, mobile, username, password, provider);
    }
}
