package com.dct.base.constants;

import com.dct.base.dto.response.BaseResponseDTO;

/**
 * Message in api response with internationalization (I18n) here <p>
 * Use when you want to create a detailed response message for the client in {@link BaseResponseDTO} <p>
 * The constant content corresponds to the message key in the resources bundle files of the resources/i18n directory
 * @author thoaidc
 */
public interface ResultConstants {

    // Get data success
    String GET_DATA_SUCCESS = "result.data.success";

    // Account information processing result messages
    String REGISTER_SUCCESS = "result.account.register.success";

    // Authenticate account result messages
    String LOGIN_SUCCESS = "result.auth.login.success";
}
