package ynu.edu.smart_energy.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResp {
    private String token;
    private String role;
}
