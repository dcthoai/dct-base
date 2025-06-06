package com.dct.base.dto.request;

import com.dct.base.common.DateUtils;
import com.dct.base.constants.BaseDatetimeConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * Contains common information for all requests
 * @author thoaidc
 */
@SuppressWarnings("unused")
public class BaseRequestDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(BaseRequestDTO.class);
    private static final String ENTITY_NAME = "BaseRequestDTO";
    private Integer page = 0;
    private Integer size = 20;
    private String sort;
    private String fromDate;
    private String toDate;
    private String status;
    private String keyword;

    public Pageable getPageable() {
        if (page == null || size == null || page < 0 || size <= 0) {
            return PageRequest.of(0, 20);
        }

        if (StringUtils.hasText(sort)) {
            String[] sortArray = sort.split(",");
            Sort.Direction sortDirection = Sort.Direction.ASC;

            if (sortArray.length > 1) {
                String sortField = sortArray[0].trim();
                String direction = sortArray[1].trim();

                if (Objects.equals(direction, "desc")) {
                    sortDirection = Sort.Direction.DESC;
                }

                if (StringUtils.hasText(sortField)) {
                    return PageRequest.of(page, size, Sort.by(sortDirection, sortField));
                }
            }
        }

        return PageRequest.of(page, size);
    }

    public String getFromDateSearch() {
        if (StringUtils.hasText(fromDate)) {
            try {
                return DateUtils.ofLocalDateTime(
                    fromDate,
                    BaseDatetimeConstants.Formatter.DEFAULT,
                    BaseDatetimeConstants.ZoneID.ASIA_HO_CHI_MINH
                ).toString();
            } catch (DateTimeParseException e) {
                log.error("[{}] - Could not parse fromDate, skip filter by fromDate. {}", ENTITY_NAME, e.getMessage());
            }
        }

        return null;
    }

    public String getToDateSearch() {
        if (StringUtils.hasText(toDate)) {
            try {
                return DateUtils.ofLocalDateTime(
                    toDate,
                    BaseDatetimeConstants.Formatter.DEFAULT,
                    BaseDatetimeConstants.ZoneID.ASIA_HO_CHI_MINH
                ).toString();
            } catch (DateTimeParseException e) {
                log.error("[{}] - Could not parse toDate, skip filter by toDate. {}", ENTITY_NAME, e.getMessage());
            }
        }

        return null;
    }

    public String getStatusSearch(String regex) {
        if (Objects.nonNull(status) && !status.matches(regex)) {
            return null;
        }

        return status;
    }

    public String getKeywordSearch() {
        if (StringUtils.hasText(keyword)) {
            return "%" + keyword + "%";
        }

        return null;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
