package com.openclassrooms.poseidon.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

  @Value("${api.ver}")
  private String apiVersion;

  @Autowired
  private DataSource dataSource;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2B);
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    auth.inMemoryAuthentication()
      .withUser("springadmin").password("$2b$10$kv4VoEktZZhs4ukksvozuOVUgXawst4Sg9pkQQUX8Ful0r2WT9tdO").roles("ADMIN", "USER")
      .and()
      .withUser("springuser").password("$2b$10$shK1zb9/88ZBYn5oQnSdHucz8TuLfkbalS2Z.yNAnOQMH819VkGAy").roles("USER");

    auth.jdbcAuthentication()
      .dataSource(dataSource)
      .usersByUsernameQuery(
        "select username, password, enabled from users where username = ?;"
      )
      .authoritiesByUsernameQuery(
        "select username, role as name "
          + "from users u join authorities a on (u.Id=a.userid) " +
          "where username = ?;"
      );
  }

  @Override
  public void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
      .authorizeRequests()
      .antMatchers("/", "/error/**", "/css/**", "/login").permitAll()
      .antMatchers("/" + apiVersion + "/**").hasRole("ADMIN")
      .antMatchers("/user/**").hasRole("USER")
      .antMatchers("/**").hasRole("ADMIN")
      .anyRequest().authenticated()

      .and()
      .formLogin()
      .loginProcessingUrl("/login_perform")
      .defaultSuccessUrl("/home", false)

      .and()
      .oauth2Login()

      .and()
      // 31 536 000 seconds which corresponds to one year of validity token.
      .rememberMe().key("uniqueAndSecret").tokenValiditySeconds(31536000)

      .and()
      .logout()
      .logoutUrl("/app-logout")
      .invalidateHttpSession(true)
      .logoutSuccessUrl("/")
      .deleteCookies("JSESSIONID")
      .deleteCookies("remember-me")

      //Disabled for the API
      .and()
      .csrf()
      .ignoringAntMatchers("/" + apiVersion + "/**")

      .and()
      .httpBasic()
    ;

  }

}
