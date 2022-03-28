package academy.kata.platform.spring.spring_boot_rest.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/*WebSecurityConfig - настройка секьюрности по определенным URL, а также настройка UserDetails и GrantedAuthority*/
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final SuccessUserHandler successUserHandler;

    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    public WebSecurityConfig(SuccessUserHandler successUserHandler) {
        this.successUserHandler = successUserHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/", "/index").permitAll()
                .antMatchers("/admin", "/admin/**").hasRole("ADMIN")
                .antMatchers("/user", "/user/**").hasRole("USER")
                .antMatchers(HttpMethod.GET, "admin", "/admin/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "admin", "/admin/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.PATCH, "admin", "/admin/**").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "admin", "/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .formLogin().successHandler(successUserHandler)
                .permitAll()
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/login")
                .permitAll();
    }




    /*Материалы которые помогли:
    https://highload.today/spring-security/
    https://www.youtube.com/watch?v=7uxROJ1nduk
    https://medium.com/@ashifm4/protection-from-cross-site-request-forgery-csrf-9cf4f542e268
    https://ru.wikibooks.org/wiki/Spring_Security/%D0%A2%D0%B5%D1%85%D0%BD%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%B8%D0%B9_%D0%BE%D0%B1%D0%B7%D0%BE%D1%80_Spring_Security#:~:text=GrantedAuthority%20%D0%BE%D1%82%D1%80%D0%B0%D0%B6%D0%B0%D0%B5%D1%82%20%D1%80%D0%B0%D0%B7%D1%80%D0%B5%D1%88%D0%B5%D0%BD%D0%B8%D1%8F%20%D0%B2%D1%8B%D0%B4%D0%B0%D0%BD%D0%BD%D1%8B%D0%B5%20%D0%B4%D0%BE%D0%B2%D0%B5%D1%80%D0%B8%D1%82%D0%B5%D0%BB%D1%8E,%D0%B4%D1%80%D1%83%D0%B3%D0%B8%D1%85%20%D0%B8%D1%81%D1%82%D0%BE%D1%87%D0%BD%D0%B8%D0%BA%D0%B0%20%D0%B4%D0%B0%D0%BD%D0%BD%D1%8B%D1%85%20%D1%81%D0%B8%D1%81%D1%82%D0%B5%D0%BC%D1%8B%20%D0%B1%D0%B5%D0%B7%D0%BE%D0%BF%D0%B0%D1%81%D0%BD%D0%BE%D1%81%D1%82%D0%B8.
    https://habr.com/ru/post/203318/
    https://habr.com/ru/post/482552/
    https://www.youtube.com/watch?v=iivY8B5A0Tk
    https://bcrypt-generator.com/
     */

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return authenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                //.passwordEncoder(NoOpPasswordEncoder.getInstance());
                .passwordEncoder(bCryptPasswordEncoder());
    }


    /*Материалы которые помогли:
    https://www.youtube.com/watch?v=WDlifgLS8iQ
    https://m.youtube.com/watch?v=IjLqJKJxNzg
    https://zametkinapolyah.ru/zametki-o-mysql/chast-12-14-obedinenie-tablic-v-sql-i-bazax-dannyx-sqlite-join-i-select.html
    */

    /*@Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(NoOpPasswordEncoder.getInstance())
                .usersByUsernameQuery("SELECT username, password, enabled FROM users WHERE username = ?")
                //Из таблицы users и присоединенной к ней таблице users_roles, соединенные через поля id и user_id, выбираем поля username и roles
                .authoritiesByUsernameQuery("SELECT a.username, b.role FROM users a INNER JOIN roles b INNER JOIN users_roles c ON a.id = c.user_id AND b.id = c.roles_id WHERE a.username = ?");
    }*/

}
