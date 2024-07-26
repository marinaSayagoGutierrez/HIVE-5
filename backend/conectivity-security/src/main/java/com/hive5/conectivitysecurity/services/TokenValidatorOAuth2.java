package com.hive5.conectivitysecurity.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import com.hive5.conectivitysecurity.entities.boards.Board;
import com.hive5.conectivitysecurity.entities.users.User;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

@Service
public class TokenValidatorOAuth2 {

  @Autowired
  private TokenStore tokenStore;

  @Autowired
  private DefaultTokenServices tokenServices;

  @Autowired
  private UserService userService;

  public boolean validateToken(String token) {
    OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
    if (accessToken instanceof DefaultOAuth2AccessToken) {
      DefaultOAuth2AccessToken defaultAccessToken = (DefaultOAuth2AccessToken) accessToken;
      OAuth2Authentication authentication = tokenServices.loadAuthentication(defaultAccessToken.getValue());
      if (authentication != null) {
        return true; // Token es válido
      }
    }
    return false; // Token no es válido
  }

  public String extractUserIdFromToken(String token) {
    OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
    if (accessToken instanceof DefaultOAuth2AccessToken) {
      DefaultOAuth2AccessToken defaultAccessToken = (DefaultOAuth2AccessToken) accessToken;
      OAuth2Authentication authentication = tokenServices.loadAuthentication(defaultAccessToken.getValue());
      if (authentication != null) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        if (user != null) {
          JSONObject jsonObject = new JSONObject();
          jsonObject.put("userId", user.getId());
          jsonObject.put("firstName", user.getFirstName());
          jsonObject.put("lastName", user.getLastName());
          jsonObject.put("email", user.getEmail());
          jsonObject.put("roleId", user.getRole().getId());
          jsonObject.put("roleName", user.getRole().getName());
          JSONArray boardsArray = new JSONArray();
          for (Board board : user.getBoards()) {
              JSONObject boardObject = new JSONObject();
              boardObject.put("id", board.getId());
              boardObject.put("name", board.getName());
              boardsArray.add(boardObject);
          }
          jsonObject.put("boards", boardsArray);
          return jsonObject.toJSONString();
          // return user.getRole().toString(); // Devuelve el ID del usuario
        }
      }
    }
    return null; // Token no es válido o no se puede extraer el ID del usuario
  }
}
