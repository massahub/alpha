package com.massa.alpha.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Getter @Setter
@ToString
@Table(name="TBL_ADMIN")
public class Admin extends BaseSerializable {


    private static final long serialVersionUID = -2382480158604649420L;

    @Id
    @Column(name="ADMIN_ID")
    private String adminId;

    @Column(name="ADMIN_NAME")
    private String adminName;

    @Size(max = 128)
    @Column(name="PASSWORD")
    private String password;

    @Column(name="PASSWORD_UPDATE_DATE")
    private Date passwordUpdateDate;

    @Column(name="ADMIN_GROUP_SEQ")
    private int adminGroupSeq;

    @Column(name="PHONE")
    private String phone;

    @Column(name="MCODE")
    private String mcode;

    @Column(name="EMAIL")
    private String email;

    //@MapsId(value = "adminGroupSeq")
    @ManyToOne(targetEntity = AdminGroup.class)
    @JoinColumn(name="ADMIN_GROUP_SEQ", insertable = false, updatable = false)
    private AdminGroup adminGroup;

    @Transient
    private String roleName;




}

