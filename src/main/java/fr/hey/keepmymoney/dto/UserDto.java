package fr.hey.keepmymoney.dto;

import fr.hey.keepmymoney.validation.PasswordMatches;
import jakarta.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@PasswordMatches(message = "Les mots de passe ne correspondent pas")
public class UserDto
{
    private Integer id;
    @NotBlank(message = "Ne peut pas être vide")
    private String login;
    @NotBlank(message = "Ne peut pas être vide")
    private String password;
    @NotBlank(message = "Ne peut pas être vide")
    private String matchingPassword;
}
