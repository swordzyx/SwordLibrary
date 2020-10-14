package com.zero.kotlin

import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@WebServlet(name="Hello", value=["/Hello"])
class Main: HttpServlet(){
    override fun doGet(req: HttpServletRequest, resp: HttpServletResponse) {
        resp.writer.println("hello kotlin")
    }
}