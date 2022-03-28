package academy.kata.platform.spring.spring_boot_rest.service.details;

import academy.kata.platform.spring.spring_boot_rest.dao.UserRepository;
import academy.kata.platform.spring.spring_boot_rest.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;


    /*Для того, чтобы авторизироваться с использованием Spring Security и UserDetailsService,
    * нужно реализовать интерфейс UserDetailsService.
    * UserDetailsService - нужен, чтобы создать UserDetails, когда передано имя пользователя в виде String.
    * */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) {

        User user = userRepository.findUserByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }

        /*GrantedAuthority - отражает разрешения выданные доверителю в масштабе всего приложения
        * Получаем все роли, которые есть у юзера с переданным username и записываем их в grantedAuthorities
        * */
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        user.getRoles().stream().forEach(role -> grantedAuthorities.add(new SimpleGrantedAuthority(role.getRole())));

        //Возвращаем UserDetails (Spring Security) с логином, паролем и списоком ролей
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);

    }

}
