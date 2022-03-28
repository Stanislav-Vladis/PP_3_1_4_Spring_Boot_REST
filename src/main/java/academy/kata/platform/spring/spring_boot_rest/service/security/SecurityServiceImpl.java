package academy.kata.platform.spring.spring_boot_rest.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

//Сервис безопасности нужен для предоставления текущего авторизованного пользователя и автоматического входа в систему после регистрации.
@Service
public class SecurityServiceImpl implements SecurityService {

    /*Токен передается экземпляру AuthenticationManager для проверки.
    Spring получает данные из нашего класса UserDetailsServiceImpl и создает на их основе токен.
    AuthenticationManager возвращает полностью заполненный экземпляр Authentication в случае успешной аутентификации.
    */
    @Autowired
    private AuthenticationManager authenticationManager;

    /*UserDetailsService - нужен, чтобы создать UserDetails, когда передано имя пользователя в виде String.
    У нас этот интерфейс реализован в классе UserDetailsServiceImpl*/
    @Autowired
    private UserDetailsService userDetailsService;


    /*Поиск пользователя, который пытается войти в систему
    С этого момента пользователь считается подлинным, если он будет найден.
    */
    @Override
    public String findLoggedInUsername() {

        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getDetails();
        if (userDetails instanceof UserDetails) {
            return ((UserDetails)userDetails).getUsername();
        } else {
            return null;
        }

    }

    @Override
    public void autoLogin(String username, String password) {

        //Получаем логин, который ввели для авторизации
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        /*Создаем токен
        getAuthorities() - этот метод предоставляет массив объектов GrantedAuthority.
        GrantedAuthority это полномочия (роли), которые предоставляются пользователю.
        */
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
        //Производим аунтентификацию
        authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        if (usernamePasswordAuthenticationToken.isAuthenticated()) {
            //Устанавливается контекст безопасности, куда передается экземпляр Authentication.
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        } else {
            throw new BadCredentialsException("Bad Credentials");
        }

    }

}
