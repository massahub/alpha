package com.massa.alpha.repository;

import com.massa.alpha.data.AdminGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminGroupRepository extends JpaRepository<AdminGroup, Integer> {

    @Query(value="SELECT NAME FROM TBL_ADMIN_GROUP WHERE ADMIN_GROUP_SEQ = :admin_group_seq LIMIT 1", nativeQuery=true)
    public AdminGroup nativeFindByAdminGroupSeq(@Param("admin_group_seq") Integer admin_group_seq);
}
