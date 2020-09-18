package com.example.consigliaviaggi19.Models;

public class Utenti {
    private String nome;
    private String cognome;
    private String email;
    private String password;
    private String nickname;

    public String getNome(){
        return nome;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getCognome(){
        return cognome;
    }

    public void setCognome(String cognome){
        this.cognome = cognome;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getPassword(){
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getUsername(){
        return nickname;
    }

    public void setUsername(String nickname){
        this.nickname = nickname;
    }

    public Utenti(){}
}
