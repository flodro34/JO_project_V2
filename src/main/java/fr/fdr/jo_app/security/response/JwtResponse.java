package fr.fdr.jo_app.security.response;

import lombok.Data;

import java.util.List;

@Data
public class JwtResponse {

    private String accessToken;
    private String type = "Bearer";
    private Long id;
    private String username;
    private List<String> roles;
    private String tokenUser;

    public JwtResponse(String token, Long id, String username, List<String> roles, String tokenUser) {
        this.accessToken = token;
        this.id = id;
        this.username = username;
        this.roles = roles;
        this.tokenUser = tokenUser;
    }

}
