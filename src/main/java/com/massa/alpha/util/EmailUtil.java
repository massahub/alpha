package com.massa.alpha.util;

import com.massa.alpha.common.ResultMassage;
import com.massa.alpha.data.Admin;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Properties;

public class EmailUtil {

    public static ResultMassage sendEmail(Admin admin, int mode){
        if(mode==1 || mode==2) {
            String pwd = admin.getPassword();

            String host = "smtp.gmail.com";
            String username = "mobileparkmailsender";//보내는 사람의 메일
            String passwd = "mobile2020*";// 보내는 사람의 비밀번호
            int port = 465;

            String ip = "localhost:";//도메인
            String localport = "8090";//도메인 포트

            String recipient = admin.getEmail();

            String subject="";
            String content="";

            try {

                if (mode == 1) {
                    subject = "[모바일파크] 비밀번호 찾기";
                    content = "아이디 : " + admin.getAdminId() + "<br>비밀번호 : " + pwd + "<br>주소 : <a href='http://" + ip + localport + "/admin/login'>로그인 페이지 이동</a>";
                } else if(mode==2) {
                    subject = "[모바일파크] 이메일 인증";
                    content = "아이디 : " + admin.getAdminId() + "<br>회원 가입을 완료하려면 버튼을 눌러주세요. <br>본인이 아니라면 클릭하지마세요." +
                            "<form action='http://" + ip + localport + "/common/checkemail' method='post' id=form>" +
                            "<br><input type='hidden' id='admin_id' name='admin_id' value='" + admin.getAdminId()+ "'>"+
                            "<br><input type='hidden' id='mcode' name='mcode' value='" + admin.getMcode() + "'>" +
                            "<input type='submit' value='가입완료하기'>"+
                            "</form>";
                }

                Properties props = System.getProperties();
                props.put("mail.smtp.host", host);
                props.put("mail.smtp.port", port);
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.ssl.enable", "true");
                props.put("mail.smtp.ssl.trust", host);

                Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                    String un = username;
                    String pw = passwd;

                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new javax.mail.PasswordAuthentication(un, pw);
                    }
                });

                Message mimeMessage = new MimeMessage(session);
                mimeMessage.setFrom(new InternetAddress(recipient));
                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));


                subject = MimeUtility.encodeText(subject, "UTF-8", "B");
                mimeMessage.setSubject(subject);

                mimeMessage.setContent(content, "text/html; charset=UTF-8");
                Transport.send(mimeMessage);

                return ResultMassage.MAIL_PWD;
            } catch (Exception e) {
                return ResultMassage.FAIL_MAIL_SEND;
            }
        }else{
            return ResultMassage.FAIL_MAIL_MODE;
        }
    }
}