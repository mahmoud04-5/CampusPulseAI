package com.example.campuspulseai.common.util;


import com.example.campuspulseai.southbound.entity.User;

import java.nio.file.AccessDeniedException;

public interface IAuthUtils {
    User getAuthenticatedUser() throws AccessDeniedException;
}
