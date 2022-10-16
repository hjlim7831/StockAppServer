package com.main;

import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyObject;
import org.python.util.PythonInterpreter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainCrawling {
	private static PythonInterpreter intPre;

    @GetMapping("test")
    @ResponseBody
    public String getTest() {
    	System.setProperty("python.import.site", "false");
        intPre = new PythonInterpreter();
        intPre.execfile("src/main/resources/crawling/RealTimeStock.py");
        intPre.exec("print(a())");

//        PyFunction pyFunction = (PyFunction) intPre.get("a");
//      int a = 10, b = 20;
//      PyObject pyobj = pyFuntion.__call__(new PyInteger(a), new PyInteger(b));
//        PyObject pyobj = pyFunction.__call__();
//        System.out.println(pyobj.toString());
        return "a";
//        return pyobj.toString();
    }
}