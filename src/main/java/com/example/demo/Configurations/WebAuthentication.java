package com.example.demo.Configurations;


import com.example.demo.Models.Client;

import com.example.demo.Repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class WebAuthentication extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    ClientRepository clientRepository;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception{


        auth.userDetailsService(inputName -> {

            Optional<Client> client = clientRepository.findByEmailIgnoreCase(inputName);

            if (client.isPresent()) {

                if(client.get().getEmail().equals("admin@admin.com")) {//&& client.get().getPassword().equals("1234")
                    return new User(client.get().getEmail(), client.get().getPassword(),
                            AuthorityUtils.createAuthorityList("ADMIN"));
                }
                else {
                    return new User(client.get().getEmail(), client.get().getPassword(),
                            AuthorityUtils.createAuthorityList("CLIENT"));
                }
            } else {

                throw new UsernameNotFoundException("Unknown user: " + inputName);

            }


    });

}
}
