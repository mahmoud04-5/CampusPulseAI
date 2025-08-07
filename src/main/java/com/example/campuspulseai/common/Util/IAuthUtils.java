package com.example.campuspulseai.common.Util;



import com.example.campuspulseai.southBound.entity.User;

import java.nio.file.AccessDeniedException;

public interface IAuthUtils {
    User getAuthenticatedUser() throws AccessDeniedException;
}
