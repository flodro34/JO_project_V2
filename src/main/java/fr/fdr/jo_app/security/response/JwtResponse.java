package fr.fdr.jo_app.security.response;

import java.util.List;

public class JwtResponse {

    private String accessToken;
    private String type = "Bearer";
    private Long id;
    private String username;
    private List<String> roles;

    public JwtResponse(String token, Long id, String username, List<String> roles) {
        this.accessToken = token;
        this.id = id;
        this.username = username;
        this.roles = roles;
    }
}
