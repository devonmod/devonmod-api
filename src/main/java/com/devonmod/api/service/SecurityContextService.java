package com.devonmod.api.service;

import com.devonmod.api.security.user.AppUserDetails;

public interface SecurityContextService {

  /**
   * Retrieves the details of the authenticated user from the security context.
   *
   * @return An instance of {@link AppUserDetails} representing the authenticated user's details.
   */
  AppUserDetails getUserDetails();
}
