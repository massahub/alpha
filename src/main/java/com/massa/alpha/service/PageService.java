package com.massa.alpha.service;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class PageService {

    private static final Logger logger = LoggerFactory.getLogger(AdminService.class);

    public boolean parsingDataTableParameter(Map<String, Map<String, String>> columnValue,
                                             Map<String, String> orderValue,
                                             Map<String, String> extraValue,
                                             HttpServletRequest request) {
        String columnHeader = "columns[";

        // column 카운트를 구한다.
        int columnCount = 0;
        Map<String, Object> columnParams = WebUtils.getParametersStartingWith(request, columnHeader);

        int idx;
        String strNum;
        Set<String> keys = columnParams.keySet();

        for(String key : keys) {
            idx = StringUtils.indexOf(key, "]");
            if(idx < 0) continue;

            strNum = StringUtils.trim(StringUtils.substring(key, 0, idx));
            if(strNum.equals("")) continue;
            if(NumberUtils.isNumber(strNum)) {
                if((idx=Integer.parseInt(strNum, 10)) > columnCount) columnCount = idx;
            }
        }

        if(columnCount <= 0) return false;

        // order 를 위해서 column name 을 순서 대로 저장 해야 한다.
        List<String> columnName = new ArrayList<>();

        // columns 의 attribute 를 구한다.
        String name;
        for(int i=0 ; i<=columnCount ; i++) {
            name = columnParams.get(i+"][name]").toString();
            columnName.add(name);

            Map<String, String> attr = new HashMap<>();
            attr.put("name", name);
            attr.put("data", columnParams.get(i + "][data]").toString());
            attr.put("searchable", columnParams.get(i + "][searchable]").toString());
            attr.put("orderable", columnParams.get(i + "][orderable]").toString());
            attr.put("search_value", columnParams.get(i + "][search][value]").toString());
            attr.put("search_regex", columnParams.get(i + "][search][regex]").toString());

            columnValue.put(name, attr);
        }

        // ordering 을 구한다.
        String orderHeader = "order[";

        // order 카운트를 구한다.
        int orderCount = 0;
        Map<String, Object> orderParams = WebUtils.getParametersStartingWith(request, orderHeader);

        keys = orderParams.keySet();

        for(String key : keys) {
            idx = StringUtils.indexOf(key, "]");
            if(idx < 0) continue;

            strNum = StringUtils.trim(StringUtils.substring(key, 0, idx));
            if(strNum.equals("")) continue;
            if(NumberUtils.isNumber(strNum)) {
                if((idx=Integer.parseInt(strNum, 10)) > orderCount) orderCount = idx;
            }
        }

        String unitColumn;
        int iUnitColumn = 0;

        for(int i=0 ; i<=orderCount ; i++) {
            unitColumn = StringUtils.trim(orderParams.get(i+"][column]").toString());
            if(StringUtils.equals(unitColumn, "")) continue;
            if(StringUtils.isNumeric(unitColumn))   iUnitColumn = Integer.parseInt(unitColumn, 10);
            else                                    continue;

            String dir = StringUtils.trim(orderParams.get(i+"][dir]").toString());

            // direction 오류 검증 하지 않는다. 실제 사용 하는 곳에서 사용하기 직전에 오류 검증 한다.
            //if(!StringUtils.equalsIgnoreCase(dir, "desc") && !StringUtils.equalsIgnoreCase(dir, "asc")) {
            //    LOG.warn("invalid column ordering. column [" + columnName.get(iUnitColumn) +
            //            "], direction [" + dir + "]");
            //    continue;
            //}
            orderValue.put(columnName.get(iUnitColumn), dir);
        }

        // 나머지 parameter 를 구한다.
        extraValue.put("draw", request.getParameter("draw"));
        extraValue.put("start", request.getParameter("start"));
        extraValue.put("length", request.getParameter("length"));
        extraValue.put("search_value", request.getParameter("search[value]"));
        extraValue.put("search_regex", request.getParameter("search[regex]"));

        return true;
    }

    public Map<String, String> getSearchFilter(Map<String, Map<String, String>> columnValue,
                                               Map<String, String> extraValue) {
        String oneSearchValue = extraValue.get("search_value");
        if(oneSearchValue != null) oneSearchValue = StringUtils.trim(oneSearchValue);

        Map<String, String> searchFilter = new HashMap<>();

        Set<String> keys = columnValue.keySet();
        if(keys.size() > 0) {
            for(String key : keys) {
                Map<String, String> column = columnValue.get(key);
                if(!column.containsKey("searchable") || !column.containsKey("orderable") ||
                        !column.containsKey("name") || !column.containsKey("search_value")) {
                    logger.warn("not found jquery data-table ajax request value. searchable exist[" +
                            column.containsKey("searchable") + "], orderable exist[" +
                            column.containsKey("orderable") + "], name exist["+
                            column.containsKey("name") + "], search_value exist[" +
                            column.containsKey("search_value") + "]");
                    continue;
                }

                // 검색하는 칼럼 정보를 넣는다.
                if(column.get("searchable").equals("true")) {
                    String svalue = column.get("search_value");
                    if(svalue != null) svalue = StringUtils.trim(svalue);

                    if(StringUtils.equals(StringUtils.trim(svalue), "") && !StringUtils.equals(oneSearchValue, ""))
                        searchFilter.put(column.get("name"), oneSearchValue);
                    else
                        searchFilter.put(column.get("name"), svalue);
                }
            }
        }

        return searchFilter;
    }

    public Sort convertSort(Map<String, String> sortValue) {

        Set<Map.Entry<String, String>> entrySet = sortValue.entrySet();
        Iterator<Map.Entry<String, String>> it = entrySet.iterator();

        List<Sort.Order> orderList = new ArrayList<>();

        while(it.hasNext()) {
            Map.Entry<String, String> entry = it.next();
            String key = entry.getKey();
            String value = entry.getValue();

            if (StringUtils.equals(value, "asc")) {
                orderList.add(Sort.Order.asc(key));
            } else if (StringUtils.equals(value, "desc")) {
                orderList.add(Sort.Order.desc(key));
            }
        }
        return Sort.by(orderList);
    }

    public void checkParameterValues(HttpServletRequest request) {

        try {
            Enumeration<String> paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                String[] values = request.getParameterValues(paramName);

                StringBuffer stringBuffer = new StringBuffer();

                for (String value : values) {
                    stringBuffer.append(value);
                }

                logger.debug("{}: {}", paramName, stringBuffer.toString());
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    public String getSearchValue(Map<String, Map<String, String>> columnValue, String name) {

        Map<String, String> values = columnValue.get(name);
        if (values == null) return "";

        String searchValue = values.get("search_value");
        if (searchValue == null) return "";

        return searchValue;
    }
}
