package com.springboot.moa.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
public class ViewController {
    @GetMapping("/main")
    public String main(){
        return "main";
    }

    @GetMapping("/")
    public void redirection(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://seolmunzip.shop:9000/main.html");
    }

}
