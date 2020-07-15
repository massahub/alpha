package com.massa.alpha.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter @Setter
@ToString
@Table(name = "TBL_MENU_FUNC")
public class MenuFunc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="FUNC_SEQ")
    private Integer funcSeq;

    @Column(name="MENU_SEQ")
    private Integer menuSeq;

    @Column(name="NAME")
    //@NotNull    // null 불가능
    //@NotEmpty   // null, 빈 문자열(스페이스 포함X) 불가
    //@NotBlank   // null, 빈 문자열, 스페이스만 포함한 문자열 불가
    private String name;

    @Column(name="URL")
    //@NotNull    // null 불가능
    //@NotEmpty   // null, 빈 문자열(스페이스 포함X) 불가
    //@NotBlank   // null, 빈 문자열, 스페이스만 포함한 문자열 불가
    private String url;

    @Column(name="AUTH")
    private String auth;

    @Column(name="DESCRIPTION")
    //@NotNull    // null 불가능
    //@NotEmpty   // null, 빈 문자열(스페이스 포함X) 불가
    //@NotBlank   // null, 빈 문자열, 스페이스만 포함한 문자열 불가
    private String description;

    @Column(name="REG_DATE", updatable = false)
    private Date regDate;

    @Column(name="MOD_DATE")
    private Date modDate;

    @ManyToOne(targetEntity = Menu.class)
    @JoinColumn(name = "MENU_SEQ", insertable = false, updatable = false)
    private Menu menu;
}
