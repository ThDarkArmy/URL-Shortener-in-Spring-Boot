package tda.darkarmy.url.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;


@NoArgsConstructor
@Data
public class UserDto {

    private Long id;
    @Column(unique = true, nullable = false)
    @Size(max = 30, min = 5, message = "Name length must be in range 5 to 30 characters")
    private String name;
    @NotBlank
    @Email(message = "Enter valid email")
    private String email;

    @Size(min = 8, message = "Password length must be more than or equal to 8 characters")
    private String password;

    private List<String> roles;

    public UserDto(@Size(max = 30, min = 5, message = "Name length must be in range 5 to 30 characters") String name, List<String> roles) {
        this.name = name;
        this.roles = roles;
    }

    public UserDto(@Size(max = 30, min = 5, message = "Name length must be in range 5 to 30 characters") String name, @NotBlank @Email(message = "Enter valid email") String email, @Size(min = 8, message = "Password length must be more than or equal to 8 characters") String password, List<String> roles) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }


}
