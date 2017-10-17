package com.jfinal.plugin.spring;
/*
 * Copyright 2017 Luda Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * Created by Him on 2017/8/15.
 */
import java.lang.reflect.Field;
import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import org.springframework.context.ApplicationContext;

public class IocInterceptor implements Interceptor {
    static ApplicationContext ctx;
    @Override
    public void intercept(Invocation inv) {
        Controller controller = inv.getController();
        Field[] fields = inv.getMethod().getDeclaringClass().getDeclaredFields();
        for (Field field : fields) {
            Object bean = null;
            if (field.isAnnotationPresent(Inject.BY_NAME.class)) {
                bean = ctx.getBean(field.getName());
            }
            else if (field.isAnnotationPresent(Inject.BY_TYPE.class)) {
                bean = ctx.getBean(field.getType());
            }
            else {
                continue;
            }

            try {
                if (bean != null) {
                    field.setAccessible(true);
                    field.set(inv.getTarget(), bean);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        inv.invoke();

    }
}
