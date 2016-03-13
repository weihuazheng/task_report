package com.iqiyi.uaa.task_report.service;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/help")
public class Help extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public Help() {
        super();
    }

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
        return ;
    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        final String print_help = "\nUsage : curl SERVICE_URL/task_report/[COMMAND]\n\n"
            + "COMMAND\n"
            + "  --help : Print the help manu\n"
            + "  --register?param=id,name,email\n"
            + "  --insert/data?param=id,task_name,var_name,var_value\n"
            + "  --select/data?param=id,dt [dt:%F]\n"
            + "  --send/msg?param=to_list,cc_list,subject,msg_content [email address sep by ';']\n"
            + "  --send/report?param=id\n"
            + "\n"
            + "TIP :\n"
            + "  1.How to use curl, please man curl\n"
            + "  2.If the INPUT contains SPACE, please use curl -d \"param=[input]\" SERVICE_URL/task_report/[cmd]\n"
            + "\n"
            + "AUTHOR\n\tWritten by Weehua Zheng\n";
        response.getWriter().println(print_help);
        return ;
    }
}
