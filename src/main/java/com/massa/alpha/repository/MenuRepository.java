package com.massa.alpha.repository;

import com.massa.alpha.data.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Integer> {
    public List<Menu> findByParentSeqNot(Integer parentSeq, Sort sort);
}
