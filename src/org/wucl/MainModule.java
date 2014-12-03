package org.wucl;

import org.nutz.mvc.annotation.Encoding;
import org.nutz.mvc.annotation.Fail;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.ioc.provider.ComboIocProvider;

/**
 * Nutz框架入口
 * 
 * @author wucl(lailaiwcl@gmail.com)
 * 
 */
@Modules(scanPackage = true)
@Ok("json")
@Fail("forward:/msg.jsp")
@Encoding(input = "UTF-8", output = "UTF-8")
@IocBy(type = ComboIocProvider.class, args = {
		"*org.nutz.ioc.loader.json.JsonLoader", "ioc.js",
		"*org.nutz.ioc.loader.annotation.AnnotationIocLoader", "org.wucl" })
public class MainModule {

}
