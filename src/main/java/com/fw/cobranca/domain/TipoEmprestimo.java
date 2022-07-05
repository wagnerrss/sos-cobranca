package com.fw.cobranca.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "tipo_emprestimo")
@Data
public class TipoEmprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String descricao;
    private int parcelas;
    private double juros;
    private double jurosAtraso;
    private int tipo;
}
