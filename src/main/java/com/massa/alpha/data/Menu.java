package com.massa.alpha.data;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter @Setter
@ToString(exclude = {"menuFuncList", "adminGroupAuthList"})
@Table(name = "TBL_MENU")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="MENU_SEQ")
    private Integer menuSeq;

    @Column(name="PARENT_SEQ")
    private Integer parentSeq;

    @Column(name="TITLE")
    private String title;

    @Column(name="TYPE")
    private String type;

    @Column(name="SORT")
    private Integer sort;

    @Column(name="URL")
    private String url;

    @Column(name="DESCRIPTION")
    private String description;

    @Column(name="REG_DATE", updatable=false)
    private Date regDate;

    @Column(name="MOD_DATE")
    private Date modDate;

    @Column(name="MENU_ICON")
    private String menuIcon;

    @OneToMany(mappedBy = "menu", /*fetch = FetchType.EAGER,*/ cascade = CascadeType.REMOVE)
    private List<MenuFunc> menuFuncList;

    @OneToMany(mappedBy = "menu", /*fetch = FetchType.EAGER,*/ cascade = CascadeType.REMOVE)
    private List<AdminGroupAuth> adminGroupAuthList;
}
