package sg.edu.nus.iss.JWT.demo.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    // private String firstname;
    // private String lastname;
    private String username;

    private String email;
    private String password;

    private Set<String> roles;
}
