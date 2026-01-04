package ynu.edu.smart_energy.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ynu.edu.smart_energy.common.ApiResponse;
import ynu.edu.smart_energy.dao.ISysUserDao;
import ynu.edu.smart_energy.dto.auth.LoginReq;
import ynu.edu.smart_energy.dto.auth.LoginResp;
import ynu.edu.smart_energy.entity.SysUser;
import ynu.edu.smart_energy.security.jwt.JwtUtil;

@Tag(name = "认证模块")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final ISysUserDao userDao;

    @PostMapping("/login")
    public ApiResponse<LoginResp> login(@RequestBody LoginReq req) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword())
        );

        SysUser user = userDao.findByUsername(req.getUsername()).orElseThrow();
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole());

        return ApiResponse.ok(new LoginResp(token, user.getRole()));
    }
}
