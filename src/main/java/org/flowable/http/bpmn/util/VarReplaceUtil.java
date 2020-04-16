//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.flowable.http.bpmn.util;

import java.util.Iterator;
import java.util.Map;

public class VarReplaceUtil {
    public VarReplaceUtil() {
    }

    public static String replace_var(String str, Map<String, Object> param) {
        Iterator var2 = param.keySet().iterator();

        while(var2.hasNext()) {
            String key = (String)var2.next();

            try {
                str = str.replace("${" + key + "}", (String)param.get(key));
            } catch (Exception var5) {
            }
        }

        return str;
    }
}
