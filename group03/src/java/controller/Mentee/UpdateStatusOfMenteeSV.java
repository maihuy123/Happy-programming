/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.Mentee;

import DAO.MenteeDAO;
import DAO.RequestDAO;
import DAO.WalletDAO;
import Model.Mentee;
import Model.Request;
import Model.User;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author asus
 */
@WebServlet(name = "UpdateStatusOfMenteeSV", urlPatterns = {"/updatestatusofmentee"})
public class UpdateStatusOfMenteeSV extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet UpdateStatusOfMenteeSV</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateStatusOfMenteeSV at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User curUser = (User) session.getAttribute("acc");
        RequestDAO requestDAO = new RequestDAO();
        MenteeDAO menteeDAO = new MenteeDAO();
        WalletDAO walletDAO = new WalletDAO();

        if (curUser == null) {
            response.sendRedirect("signin");
            return;
        }

        int roleID = curUser.getRoleId();
        int requestId = Integer.parseInt(request.getParameter("requestId"));
        Request requests = requestDAO.getRequestByID(requestId);
        Mentee menteeName = menteeDAO.getMenteeByID(requests.getMenteeId());

        try {
            if (roleID == 2) {
                String action = request.getParameter("action");

                switch (action) {
                    case "update":
                        response.sendRedirect("updaterequestofmentee?requestId=" + requestId);
                        break;
                    case "complete":
                        requestDAO.updateRequestStatus(requestId, "Completed");
                        break;
                    case "cancel":
                        //Buoc 1: Huy course
                        requestDAO.updateRequestStatus(requestId, "Canceled");
                        //Buoc 2: Update Hold
                        walletDAO.updateHoldByUsername(menteeName.getUsername(), walletDAO.getWalletByUsername(menteeName.getUsername()).getHold() - requests.getPrice());
                        break;
                    default:
                        break;
                }
                response.sendRedirect("listrequestbymentee");
            } else if (roleID == 1) {
                response.sendRedirect("home");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
