package com.repository.config.secuity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import javax.sql.DataSource;

import static com.repository.common.Constants.*;
/**
 * Created by Finderlo on 2016/11/8.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConifg extends WebSecurityConfigurerAdapter {
    @Autowired
    DataSource dataSource;

    @Autowired
    SecUsersDetailService usersDetailService;
    @Autowired
    SecAuthenticationProvider provider;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .formLogin()
                .loginPage(URL_SIGNIN)
                .successForwardUrl(URL_SIGNIN_SUCCESS)
                .failureUrl(URL_SIGNIN_FAIL);
        http
                .authorizeRequests()
                .antMatchers("/resources/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/fonts/**").permitAll()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/picture/**").permitAll()

                .antMatchers(URL_SIGNIN_FAIL).permitAll()
                .antMatchers(URL_SIGNIN).permitAll()
                .antMatchers("/generalError").permitAll()
                .antMatchers(URL_STORAGE,URL_STORAGE+"/**").hasRole("ADMIN")
                .antMatchers(URL_LOG,URL_LOG+"/**").hasRole("ADMIN")
                .antMatchers(URL_MANAGE,URL_MANAGE+"/**").hasRole("ADMIN")
                .antMatchers("/**").authenticated()
                .and()
                .httpBasic()
                .and().csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.inMemoryAuthentication()
                .withUser("user").password("user").roles("USER")
                .and()
                .withUser("admin").password("admin").roles("ADMIN");
    }

    //    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
////        auth
////                .jdbcAuthentication()
////                .dataSource(dataSource)
////                .usersByUsernameQuery(
////                        "select users_ID,users_password,true " +
////                                "from users where users_ID=?"
////                )
////                .authoritiesByUsernameQuery(
////                        "select users_ID,'ROLE_USER' from users where users_ID=?"
////                );
//
//    }


    //
    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        //auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ROLE_ADMIN");
    }

    //????????????
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //??????????????????????????????????????????
        auth.authenticationProvider(provider);
    }

}
