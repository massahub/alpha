package com.massa.alpha.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter
@ToString(exclude = {"adminList", "adminGroupAuthList"})
@Table(name="TBL_ADMIN_GROUP")
public class AdminGroup {

    private static final long serialVersionUID = -2382480158604649420L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ADMIN_GROUP_SEQ")
    private Integer adminGroupSeq;

    @Column(name="NAME")
    private String name;

    @Column(name="DESCRIPTION")
    private String description;

    @Column(name="REG_DATE", columnDefinition = "DATETIME", updatable = false)
    private Date regDate;

    @Column(name="MOD_DATE", columnDefinition = "DATETIME")
    private Date modDate;

    @OneToMany(mappedBy = "adminGroup", /*fetch = FetchType.EAGER,*/ cascade = CascadeType.REMOVE)
    private List<Admin> adminList;

    @OneToMany(mappedBy = "adminGroup", /*fetch = FetchType.EAGER,*/ cascade = CascadeType.REMOVE)
    private List<AdminGroupAuth> adminGroupAuthList;
}
