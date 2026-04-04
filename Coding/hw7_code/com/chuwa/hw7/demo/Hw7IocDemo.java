package com.chuwa.hw7.demo;

import com.chuwa.hw7.ChuwaBeanFactory;

/**
 * Runnable proof: singleton identity, prototype identity, and working field injection.
 */
public final class Hw7IocDemo {

    public static void main(String[] args) {
        ChuwaBeanFactory factory = new ChuwaBeanFactory();
        factory.register(UserRepository.class, ReportService.class, RequestContext.class);

        UserRepository r1 = factory.getBean(UserRepository.class);
        UserRepository r2 = factory.getBean(UserRepository.class);
        System.out.println("SINGLETON: UserRepository same instance? " + (r1 == r2));
        System.out.println("           identityHashCode r1=" + System.identityHashCode(r1)
                + " r2=" + System.identityHashCode(r2));

        ReportService s1 = factory.getBean(ReportService.class);
        ReportService s2 = factory.getBean(ReportService.class);
        System.out.println("SINGLETON: ReportService same instance? " + (s1 == s2));
        System.out.println("DI: ReportService -> UserRepository non-null? " + (s1.getUserRepository() != null));
        System.out.println("DI: greeting = " + s1.greetingForUser(1));

        RequestContext p1 = factory.getBean(RequestContext.class);
        RequestContext p2 = factory.getBean(RequestContext.class);
        System.out.println("PROTOTYPE: RequestContext same instance? " + (p1 == p2));
        System.out.println("           identityHashCode p1=" + System.identityHashCode(p1)
                + " p2=" + System.identityHashCode(p2));
        System.out.println("PROTOTYPE shares singleton ReportService? "
                + (p1.getReportService() == p2.getReportService()));
        System.out.println("PROTOTYPE sees injected service: " + p1.getReportService().greetingForUser(1));
    }

    private Hw7IocDemo() {
    }
}
