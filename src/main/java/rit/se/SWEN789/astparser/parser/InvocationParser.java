package rit.se.SWEN789.astparser.parser;

import rit.se.SWEN789.astparser.vo.MethodBean;

public class InvocationParser {

    public static MethodBean parse(String pInvocationName) {
        MethodBean methodBean = new MethodBean();
        methodBean.setName(pInvocationName);
        return methodBean;
    }

}
