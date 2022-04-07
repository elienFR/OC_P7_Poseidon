package com.openclassrooms.poseidon.configuration;

import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  private DataSource dataSource;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2B);
  }

  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth)
    throws Exception {
    auth.jdbcAuthentication()
      .dataSource(dataSource)
      .authoritiesByUsernameQuery(
        "select username, role as name from users u where username = ?;"
      );
  }

  @Override
  public void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity.authorizeRequests()
      .antMatchers("/css/**").permitAll()
      .antMatchers("/").permitAll()
      .antMatchers("/login").permitAll()
      .antMatchers("/**").hasRole("ADMIN")
//      .antMatchers("/admin**").hasRole("ADMIN")
//      .antMatchers("/bidList/list").hasRole("ADMIN")
//      .antMatchers("/user**").hasRole("ADMIN")

      .anyRequest().authenticated()

      .and()
      .formLogin()
      .loginProcessingUrl("/login_perform")
      .defaultSuccessUrl("/admin/home", false)

//       TODO : Do we add OAuth2.0 authentication ?
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
    ;

  }

}
