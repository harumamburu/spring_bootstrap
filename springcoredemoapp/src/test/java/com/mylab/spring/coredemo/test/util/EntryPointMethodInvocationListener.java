package com.mylab.spring.coredemo.test.util;

import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

import java.util.Optional;

public class EntryPointMethodInvocationListener implements IInvokedMethodListener {

    @Override
    public void beforeInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        Optional.ofNullable(getConfigurableEntryPointTestInstance(iInvokedMethod)).
                ifPresent(EntryPointConfiguration::entryPointHeadingConfiguration);
    }

    @Override
    public void afterInvocation(IInvokedMethod iInvokedMethod, ITestResult iTestResult) {
        Optional.ofNullable(getConfigurableEntryPointTestInstance(iInvokedMethod)).
                ifPresent(EntryPointConfiguration::entryPointTailingConfiguration);
    }

    private EntryPointConfiguration getConfigurableEntryPointTestInstance(IInvokedMethod iInvokedMethod) {
        ITestNGMethod testMethod = iInvokedMethod.getTestMethod();
        if (testMethod.getConstructorOrMethod().getMethod().getAnnotation(EntryPoint.class) != null) {
            if (EntryPointConfiguration.class.isAssignableFrom(testMethod.getRealClass())) {
                return (EntryPointConfiguration) testMethod.getInstance();
            }
        }
        return null;
    }
}
