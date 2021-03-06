package club.imemory.app.db;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * @Author: 张杭
 * @Date: 2017/3/28 13:08
 */

public class User extends DataSupport implements java.io.Serializable {

    private Integer id;
    private String name;
    private String password;
    private String phone;
    private String head;
    private String sex;
    private Date birthday;
    private String email;
    private String address;
    private String personality;
    private Date logintime;
    private Date createtime;
    private Date updatetime;

    public User() {
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", password=" + password
                + ", phone=" + phone + ", head=" + head + ", sex=" + sex
                + ", birthday=" + birthday + ", email=" + email + ", address="
                + address + ", personality=" + personality + ", logintime="
                + logintime + ", createtime=" + createtime + ", updatetime="
                + updatetime + "]";
    }

    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getHead() {
        return this.head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPersonality() {
        return this.personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public Date getLogintime() {
        return this.logintime;
    }

    public void setLogintime(Date logintime) {
        this.logintime = logintime;
    }

    public Date getCreatetime() {
        return this.createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Date getUpdatetime() {
        return this.updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

}