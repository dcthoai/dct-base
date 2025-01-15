package com.dct.base.service;

import com.dct.base.dto.request.AuthRequestDTO;
import com.dct.base.dto.response.BaseResponseDTO;

public interface AuthService {

    BaseResponseDTO authenticate(AuthRequestDTO authRequestDTO);
}
