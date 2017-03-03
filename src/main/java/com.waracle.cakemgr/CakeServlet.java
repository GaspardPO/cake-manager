package com.waracle.cakemgr;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.util.List;

@WebServlet({"/cakes", ""})
public class CakeServlet extends HttpServlet {

    private PersistenceService persistenceService = new PersistenceService();
    // TODO: 02/03/2017 : Inject ?

    @Override
    public void init() throws ServletException {
        super.init();

        System.out.println("init started");

        try {
            initDefaultValues();
        } catch (Exception ex) {
            throw new ServletException(ex);
        }

        System.out.println("init finished");
    }

    private void initDefaultValues() throws IOException {
        System.out.println("downloading cake json");

        try (InputStream inputStream = new URL("https://gist.githubusercontent.com/hart88/198f29ec5114a3ec3460/raw/8dd19a88f9b8d24c23d9960f3300d0c917a4f07c/defaultCakes.json").openStream()) {
            List<CakeEntity> cakesEntities = new ObjectMapper().readValue(inputStream, new TypeReference<List<CakeEntity>>() {});
            // TODO: 02/03/2017  : should the duplicates values be removed ?
            persistenceService.persist(cakesEntities);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<CakeEntity> list = persistenceService.getAllCakes();

        if (request.getHeader("Accept").contains("json")) {
            new ObjectMapper().writerWithDefaultPrettyPrinter().writeValue(response.getWriter(), list);
        } else {
            request.setAttribute("cakes", list);
            this.getServletContext()
                    .getRequestDispatcher("/index.jsp")
                    .forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CakeEntity newCake = new CakeEntity();
        newCake.setTitle(request.getParameter("title"));
        newCake.setDescription(request.getParameter("description"));
        newCake.setImage(request.getParameter("image"));

        persistenceService.persist(newCake);
        response.sendRedirect("");
    }

    public void changePersistenceService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }
}
