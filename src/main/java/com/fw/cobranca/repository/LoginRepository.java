package com.fw.cobranca.repository;

import com.fw.cobranca.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface LoginRepository extends JpaRepository<Usuario, Integer> {

    @Query(value = "SELECT * FROM USUARIO WHERE (DOCUMENTO = :USUARIO) AND (SENHA = :SENHA)", nativeQuery = true)
    public Usuario findByUsernamePassword(
            @Param("USUARIO") String USUARIO,
            @Param("SENHA") String SENHA);

}
