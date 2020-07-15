package com.massa.alpha.service;

import com.massa.alpha.common.ResultMassage;
import com.massa.alpha.data.Admin;
import com.massa.alpha.data.paging.Page;
import com.massa.alpha.data.paging.PagingRequest;
import com.massa.alpha.repository.AdminRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AdminService {

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    @Autowired
    AdminRepository adminRepository;

    public long count(String search) {
        try {
            //return adminRepository.count("");
            return adminRepository.count();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return 0;
    }

    public List<Admin> searchAll(){
        List<Admin> list =  adminRepository.findAll(new Sort(Sort.Direction.DESC, "regDate"));
        return list;
    }

    public List<Map<String, Object>> search(){

        return this.search("",null, 0, 0);
    }

    public List<Map<String, Object>> search(String search, Map<String, String> sortValue, int start, int length){

        List<Map<String, Object>> mapArrayList = new ArrayList<>();
        try {

            //List<Admin> list = adminRepository.list(search, sortValue, start, length);
            List<Admin> list = adminRepository.findAll();

            for (Admin admin : list) {
                Map<String, Object> map = new HashMap<>();

                //map.put("adminSeq", admin.getAdminSeq()+"");
                map.put("adminId", admin.getAdminId());
                map.put("adminName", admin.getAdminName());
                map.put("adminGroupSeq", admin.getAdminGroup().getName());
                map.put("regDate", admin.getRegDate().toLocaleString());

                mapArrayList.add(map);
            }
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        return mapArrayList;

    }

    public List<Map<String, Object>> search3(int start, int length, Sort sort, long offset){

        List<Map<String, Object>> mapArrayList = new ArrayList<>();

        try {
            PageRequest pr = new PageRequest(start, length, sort);
            org.springframework.data.domain.Page<Admin> list = adminRepository.findAll(pr);

            for (Admin admin : list) {
                Map<String, Object> map = new HashMap<>();

                //map.put("adminSeq", admin.getAdminSeq()+"");
                map.put("adminId", admin.getAdminId());
                map.put("adminName", admin.getAdminName());
                map.put("adminGroupSeq", admin.getAdminGroup().getName());
                map.put("regDate", admin.getRegDate().toLocaleString());

                mapArrayList.add(map);
            }
        } catch(Exception e) {
            logger.error(e.getMessage(), e);
        }
        return mapArrayList;

    }

    //public Admin findByAdminSeq(Integer adminSeq){
    //    Admin admin =  adminRepository.nativeFindByAdminSeq(adminSeq);
    //    return admin;
    //}

    public Admin findByAdminId(String adminId){
        Optional<Admin> admin =  adminRepository.findById(adminId);

        if(!admin.isPresent()) {
            //logger.error("query not found userId [userId({})]", userId);
            //throw new NullPointerException();
            return null;
        }

        return admin.get();
    }

    public ResultMassage create(Admin admin){

        //int result = ResultCodeKeys.SUCCESS;
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        try {
            String encPassword = passwordEncoder.encode(admin.getPassword());
            admin.setPassword(encPassword); //비밀번호 암호화 저장
            admin.setPasswordUpdateDate(Calendar.getInstance().getTime());

            adminRepository.save(admin);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultMassage.FAIL;
        }

        return ResultMassage.MAIL_CREATE;
    }

    public ResultMassage pwdmodify(String adminId, String updatePassword){

        logger.debug( "updatePassword : " + updatePassword);

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        try {

            Admin adminTemp = adminRepository.findById(adminId).get();

            if(StringUtils.equals(adminTemp.getPassword(), updatePassword)){
                logger.debug("현재 비밀번호와 변경 비밀번호가 동일");
                return ResultMassage.FAIL_PASSWORD_CURRENT_SAME;
            }


            String encPassword = passwordEncoder.encode(updatePassword);

            logger.debug( "encPassword : " + encPassword);

            //rawPassword: 암호화 되지않은 값
            //encodedPassword : 암호화된 값
            if(passwordEncoder.matches(updatePassword, encPassword)){
                System.out.println("matches ok");
            }

            String prePassword = adminTemp.getPassword(); //현재 비밀번호 ( 암호화 된 )
            String newPassword = encPassword;  //변경 할 비밀번호 ( 암호 화 된)

            if(updatePassword.equals("") || updatePassword == null){ //비밀번호 변경 없음
                adminTemp.setPassword(adminTemp.getPassword());
            }else if(!StringUtils.equals(prePassword, newPassword)){

                adminTemp.setPassword(encPassword); //비밀번호 암호화 저장
                adminTemp.setPasswordUpdateDate(Calendar.getInstance().getTime());
            }

            //adminTemp.setPassword(newPassword);
            //adminTemp.setPasswordUpdateDate(Calendar.getInstance().getTime());

            Admin newAdmin = adminRepository.save(adminTemp);
            System.out.println("admin: "+newAdmin);


        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultMassage.FAIL;
        }
        return ResultMassage.SUCCESS;
    }

    public ResultMassage modify(Admin admin){

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        try {
            //adminRepository.save(admin);

            //Admin newAdmin = adminRepository.save(admin);
            //System.out.println("admin: "+newAdmin);

            Optional<Admin> adminTemp = adminRepository.findById(admin.getAdminId());

            adminTemp.ifPresent(selectAdmin ->{

                selectAdmin.setAdminId(admin.getAdminId());
                selectAdmin.setAdminGroupSeq(admin.getAdminGroupSeq());
                selectAdmin.setAdminName(admin.getAdminName());

                selectAdmin.setModDate(new Date());
                Admin newAdmin = adminRepository.save(selectAdmin);
                System.out.println("admin: "+newAdmin);

            });

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultMassage.FAIL;
        }


        return ResultMassage.SUCCESS;
    }

    public ResultMassage delete(Admin admin){

        try {
            adminRepository.delete(admin);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResultMassage.FAIL;
        }

        return ResultMassage.SUCCESS;

    }

    public Page<Admin> getAdmins(PagingRequest pagingRequest) {

        List<Admin> list = adminRepository.findAll();

        return getPage(list, pagingRequest);
    }

    private Page<Admin> getPage(List<Admin> admin, PagingRequest pagingRequest) {
        List<Admin> filtered = admin.stream()
                //.sorted(sortEmployees(pagingRequest))
                //.filter(filterEmployees(pagingRequest))
                //.skip(pagingRequest.getStart())
                //.limit(pagingRequest.getLength())
                .collect(Collectors.toList());

        Page<Admin> page = new Page<>(filtered);
        page.setRecordsFiltered(admin.size());
        page.setRecordsTotal(admin.size());
        page.setDraw(pagingRequest.getDraw());

        return page;
    }

    public Admin isuser_id(String admin_id){
        Optional<Admin> a = adminRepository.findById(admin_id);
        return a.get();
    }

    public long isuser_pnemail(String phone, String name, String email){
        return adminRepository.countByPhoneAndAdminNameAndEmail(phone,name,email);
    }

    public List<Admin> findid(String phone, String name, String email){
        return adminRepository.findByPhoneAndAdminNameAndEmail(phone, name,email);
    }

    public void temp_pwd(String admin_id, String pwd){
        try {
            Optional<Admin> adminTemp = adminRepository.findById(admin_id);
            adminTemp.ifPresent(selectAdmin -> {
                selectAdmin.setPassword(pwd);
                adminRepository.save(selectAdmin);
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void succ_email(String mcode){
        try {
            Admin adminTemp = adminRepository.findByMcode(mcode);
            adminTemp.setMcode("1");
            adminRepository.save(adminTemp);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}