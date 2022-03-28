package academy.kata.platform.spring.spring_boot_rest.service.security;

public interface SecurityService {

    String findLoggedInUsername();

    void autoLogin(String username, String password);

}
