package com.fw.cobranca.domain;

import com.fw.cobranca.util.Util;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.Map;

@Entity
@Table(name = "versao")
@Data
@NoArgsConstructor
public class Versao implements Util {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String versao;
    private String status;

}
