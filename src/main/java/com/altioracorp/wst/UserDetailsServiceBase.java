package com.altioracorp.wst;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserDetailsServiceBase<UD extends UserDetailsBase> extends UserDetailsService {

}
