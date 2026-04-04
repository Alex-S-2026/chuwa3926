package com.chuwa.hw7.demo;

import com.chuwa.hw7.annotation.Autowire;
import com.chuwa.hw7.annotation.Component;
import com.chuwa.hw7.annotation.Scope;
import com.chuwa.hw7.annotation.ScopeType;

/**
 * Prototype-scoped bean: a new instance for every {@link com.chuwa.hw7.ChuwaBeanFactory#getBean} call.
 */
@Component
@Scope(ScopeType.PROTOTYPE)
public class RequestContext {

    @Autowire
    private ReportService reportService;

    public ReportService getReportService() {
        return reportService;
    }
}
