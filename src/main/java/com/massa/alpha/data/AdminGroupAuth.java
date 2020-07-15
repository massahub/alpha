package com.massa.alpha.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter @Setter
@ToString
@Table(name="TBL_ADMIN_GROUPAUTH")
@IdClass(AdminGroupAuthPK.class)
public class AdminGroupAuth {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SEQ", updatable=false)
    private Integer seq;

    @Id
    @Column(name = "MENU_SEQ")
    private Integer menuSeq;

    @Id
    @Column(name = "ADMIN_GROUP_SEQ")
    private Integer adminGroupSeq;

    @Column(name = "AUTH")
    private String auth;

    @Column(name = "REG_DATE", updatable=false)
    private Date regDate;

    @Column(name = "MOD_DATE")
    private Date modDate;

    @ManyToOne(targetEntity = Menu.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "MENU_SEQ", insertable = false, updatable = false)
    private Menu menu;

    @ManyToOne(targetEntity = AdminGroup.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "ADMIN_GROUP_SEQ", insertable = false, updatable = false)
    private AdminGroup adminGroup;
}
