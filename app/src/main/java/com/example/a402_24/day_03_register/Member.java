package com.example.a402_24.day_03_register;

import java.util.Date;

public class Member {

    private String member_id;
    private String member_password;
    private String member_gender;
    private String member_name;
    private String member_birthday;

    public String getMember_profile_pic() {
        return member_profile_pic;
    }

    public void setMember_profile_pic(String member_profile_pic) {
        this.member_profile_pic = member_profile_pic;
    }

    private String member_profile_pic;

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getMember_password() {
        return member_password;
    }

    public void setMember_password(String member_password) {
        this.member_password = member_password;
    }

    public String getMember_gender() {
        return member_gender;
    }

    public void setMember_gender(String member_gender) {
        this.member_gender = member_gender;
    }

    public String getMember_name() {
        return member_name;
    }

    public void setMember_name(String member_name) {
        this.member_name = member_name;
    }

    public String getMember_birthday() {
        return member_birthday;
    }

    public void setMember_birthday(String member_birthday) {
        this.member_birthday = member_birthday;
    }
}
