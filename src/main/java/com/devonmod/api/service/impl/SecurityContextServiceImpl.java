package com.devonmod.api.service.impl;

import com.devonmod.api.security.user.AppUserDetails;
import com.devonmod.api.service.SecurityContextService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityContextServiceImpl implements SecurityContextService {

  @Override
  public AppUserDetails getUserDetails() {
    return (AppUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
  }
}
