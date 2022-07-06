package com.fw.cobranca.service;


import com.fw.cobranca.domain.Usuario;
import com.fw.cobranca.repository.LoginRepository;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Locale;
import java.util.Map;

@Service
public class LoginService implements Util {
    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    public Usuario findUser(Map login) {
        return loginRepository.findByUsernamePassword(
                toStr(login.get("usuario")).toUpperCase().trim(),
                toStr(login.get("senha")));
    }

}
