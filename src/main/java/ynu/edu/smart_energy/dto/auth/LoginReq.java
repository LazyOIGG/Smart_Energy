package ynu.edu.smart_energy.dto.auth;

import lombok.Data;

@Data
public class LoginReq {
    private String username;
    private String password;
}
