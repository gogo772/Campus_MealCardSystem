package com.example.campus_mealcardsystem.security;

import com.example.campus_mealcardsystem.entity.SysUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

public class SysUserDetails implements UserDetails {
    private final SysUser sysUser;

    public SysUserDetails(SysUser sysUser) {
        this.sysUser = sysUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 角色必须拼接ROLE_，匹配hasRole校验规则
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + sysUser.getRole()));
    }

    @Override
    public String getPassword() {
        return sysUser.getPassword();
    }

    @Override
    public String getUsername() {
        return sysUser.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() {
        return sysUser.getEnabled() == 1;
    }

    // 扩展方法：获取当前登录用户的真实姓名
    public String getRealName() {
        return sysUser.getRealName();
    }

    // 扩展方法：获取当前登录用户绑定的饭卡号
    public String getCardNumber() {
        return sysUser.getCardNumber();
    }

    // 扩展方法：获取当前登录用户的角色
    public String getRole() {
        return sysUser.getRole();
    }
}
