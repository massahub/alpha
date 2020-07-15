package com.massa.alpha.repository;

import com.massa.alpha.data.MenuFunc;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuFuncRepository extends JpaRepository<MenuFunc, Integer> {
    public List<MenuFunc> findByMenuSeq(Integer menuSeq, Sort sort);
    public MenuFunc findByUrl(String url);
}
