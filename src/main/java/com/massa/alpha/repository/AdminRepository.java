package com.massa.alpha.repository;

import com.massa.alpha.data.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {

    public List<Admin> findByAdminName(String adminName);

    public Admin findByAdminId(String adminId);

    public Admin findByMcode(String mcode);

    public long countByPhoneAndAdminNameAndEmail(String phone, String name, String email);

    public List<Admin> findByPhoneAndAdminNameAndEmail(String phone, String name, String email);
}