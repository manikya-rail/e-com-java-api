package com.ecommerce.techversantInfotech.Authservice.config;

import com.ecommerce.techversantInfotech.Authservice.entity.User;
import com.ecommerce.techversantInfotech.Authservice.enumDetails.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

public class CustomUserDetails implements UserDetails {

    private String name;
    private String location;
    private String mobileNumber;
    private String description;
    private boolean active;
    private boolean delete;
    private Date modifiedOn;
    private Date createdOn;

    private String username;
    private int id;
    private String password;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public UserRole getRole() {
        return role;
    }

    private UserRole role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public Date getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(Date modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public CustomUserDetails(User user) {
        this.username = user.getUsername();
        this.email=user.getEmail();
        this.id = user.getId();
        this.password = user.getPassword();
        this.role = user.getRole();
        this.image= user.getImage();
        this.name=user.getName();
        this.mobileNumber= user.getMobileNumber();
        this.description=user.getDescription();
        this.active=user.isActive();
        this.delete=user.isDelete();
        this.modifiedOn=user.getModifiedOn();
        this.createdOn=user.getCreateOn();
        this.location=user.getLocation();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
