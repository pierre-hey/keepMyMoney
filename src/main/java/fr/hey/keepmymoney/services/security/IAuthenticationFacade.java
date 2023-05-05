package fr.hey.keepmymoney.services.security;

import fr.hey.keepmymoney.entities.User;
import org.springframework.security.core.Authentication;

public interface IAuthenticationFacade {
    Authentication getAuthentication();

    User getUserAuth();
}
