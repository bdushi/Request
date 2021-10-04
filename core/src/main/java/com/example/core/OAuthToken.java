package com.example.core;

import java.io.Serializable;

import javax.crypto.spec.SecretKeySpec;

public abstract class OAuthToken implements Serializable {

    private static final long serialVersionUID = -7841506492508140600L;
    private final String token;
    private final String tokenSecret;

    private transient SecretKeySpec secretKeySpec;
    private String[] responseStr = null;

    public OAuthToken(String token, String tokenSecret) {
        if(token == null)
            throw new IllegalArgumentException("Token can't be null");
        if(tokenSecret == null)
            throw new IllegalArgumentException("TokenSecret can't be null");
        this.token = token;
        this.tokenSecret = tokenSecret;
    }

    OAuthToken(String string) {
        responseStr = string.split("&");
        tokenSecret = getParameter("oauth_token_secret");
        token = getParameter("oauth_token");
    }

    public String getToken() {
        return token;
    }

    public String getTokenSecret() {
        return tokenSecret;
    }

    void setSecretKeySpec(SecretKeySpec secretKeySpec) {
        this.secretKeySpec = secretKeySpec;
    }

    SecretKeySpec getSecretKeySpec() {
        return secretKeySpec;
    }

    public String getParameter(String parameter) {
        String value = null;
        for (String str : responseStr) {
            if (str.startsWith(parameter + '=')) {
                value = str.split("=")[1].trim();
                break;
            }
        }
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OAuthToken)) return false;

        OAuthToken that = (OAuthToken) o;

        if (!token.equals(that.token)) return false;
        return tokenSecret.equals(that.tokenSecret);
    }

    @Override
    public int hashCode() {
        int result = token.hashCode();
        result = 31 * result + tokenSecret.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "OAuthToken{" +
                "token='" + token + '\'' +
                ", tokenSecret='" + tokenSecret + '\'' +
                ", secretKeySpec=" + secretKeySpec +
                '}';
    }
}