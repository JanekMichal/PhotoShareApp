package com.janek.photoShareApp.security;

import com.janek.photoShareApp.security.jwt.AuthTokenFilter;
import com.janek.photoShareApp.security.services.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        // securedEnabled = true,
        // jsr250Enabled = true,
        prePostEnabled = true)
@AllArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    UserDetailsServiceImpl userDetailsService;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors()
                .and()
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/comment/**").hasAnyAuthority("USER", "MODERATOR", "ADMIN")
                .antMatchers("/follow/**").hasAnyAuthority("USER", "MODERATOR", "ADMIN")
                .antMatchers("/image/get_feed_images/**",
                        "/image/change_description/**",
                        "/image/upload_image/**",
                        "/image/get/**",
                        "/image/get/all_images/**",
                        "/image/upload_profile_image/**",
                        "/image/get_profile_image/**"
                        , "/image/delete_own_image/**").hasAnyAuthority("USER", "MODERATOR", "ADMIN")
                .antMatchers("/image/delete_someone_image/**").hasAnyAuthority("ADMIN", "MODERATOR")
                .antMatchers("/like/**").hasAnyAuthority("USER", "MODERATOR", "ADMIN")
                .antMatchers("/user/page_users/**",
                        "/user/search/**",
                        "/user/profile/**",
                        "/user/update_user_data/**",
                        "/user/change_user_password/**",
                        "/user/change_user_password/**",
                        "/user/**",
                        "/user/**").hasAnyAuthority("USER", "MODERATOR", "ADMIN")
                .antMatchers("/user/delete_own_account/**").hasAnyAuthority("USER", "MODERATOR")
                .antMatchers("/user/delete_someone_else_account/**").hasAnyAuthority("MODERATOR", "ADMIN")
                .antMatchers("/user/give_moderator_role/**",
                        "/user/give_user_role/**").hasAnyAuthority("ADMIN")
                .anyRequest().authenticated();

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
