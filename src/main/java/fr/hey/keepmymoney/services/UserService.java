package fr.hey.keepmymoney.services;

import fr.hey.keepmymoney.dto.UserDto;
import fr.hey.keepmymoney.entities.User;

import java.util.List;

public interface UserService {
    void saveUser(UserDto userDto);

    User findByLogin(String login);

    List<UserDto> findAllUsers();

    /**
     * Méthode permettant de créer des utilisateurs à la volée, accessible uniquement avec le profile "dev"
     *
     * @param userInfo information utilisateur
     * @param roles    roles de l'utilisateur
     */
    void mockCreateUserIfNotExists(String userInfo, List<String> roles);

}
