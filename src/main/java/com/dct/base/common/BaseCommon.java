package com.dct.base.common;

import com.dct.base.constants.BaseDatetimeConstants;
import com.dct.base.dto.response.BaseAuditingEntityDTO;
import com.dct.base.entity.BaseAbstractAuditingEntity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class BaseCommon {

    private static final Logger log = LoggerFactory.getLogger(BaseCommon.class);
    private static final String ENTITY_NAME = "BaseCommon";

    public static void setAuditingInfo(BaseAbstractAuditingEntity entity, BaseAuditingEntityDTO auditingDTO) {
        auditingDTO.setCreatedByStr(entity.getCreatedBy());
        auditingDTO.setLastModifiedByStr(entity.getLastModifiedBy());

        try {
            String createdDate = DateUtils.ofInstant(entity.getCreatedDate())
                .toString(BaseDatetimeConstants.Formatter.DD_MM_YYYY_HH_MM_SS_DASH);

            String lastModifiedDate = DateUtils.ofInstant(entity.getLastModifiedDate())
                .toString(BaseDatetimeConstants.Formatter.DD_MM_YYYY_HH_MM_SS_DASH);

            auditingDTO.setCreatedDateStr(createdDate);
            auditingDTO.setLastModifiedDateStr(lastModifiedDate);
        } catch (Exception e) {
            log.error("[{}] - Could not set entity auditing info. {}", ENTITY_NAME, e.getMessage());
        }
    }
}
