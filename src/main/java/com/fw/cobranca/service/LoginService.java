package com.fw.cobranca.service;


import com.fw.cobranca.domain.Usuario;
import com.fw.cobranca.repository.LoginRepository;
import com.fw.cobranca.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class LoginService implements Util {
    @Autowired
    private LoginRepository loginRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private VersaoService versaoService;

    public Usuario findUser(Map login) {
        Usuario usuario = loginRepository.findByUsernamePassword(
                toStr(login.get("usuario")).toUpperCase().trim(),
                toStr(login.get("senha")));

        if ((usuario.getStatus() == null) || (usuario.getStatus().isEmpty())) {
            usuario.setStatus(versaoService.getVersao().getStatus());
        }

        return usuario;
    }

}
