package com.waracle.cakemgr;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.*;


import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.List;


public class CakeServletTest {

    @Mock
    HttpServletResponse response;

    @Mock
    HttpServletRequest request;

    @Mock
    ServletContext servletContextMock;

    CakeServlet cakeServlet = new CakeServletMock();

    PersistenceService persistenceService = new PersistenceService();

    @Before
    public void setup() throws ServletException {
        MockitoAnnotations.initMocks(this);

        cakeServlet.changePersistenceService(persistenceService);
        cakeServlet.init();
    }

    @Test
    public void shouldForwardToJSPWithList() throws IOException, ServletException {
        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        when(request.getHeader("Accept")).thenReturn("");
        RequestDispatcher requestDispatcherMock = Mockito.mock(RequestDispatcher.class);
        when(servletContextMock.getRequestDispatcher("/index.jsp")).thenReturn(requestDispatcherMock);

        cakeServlet.doGet(request, response);

        Mockito.verify(servletContextMock).getRequestDispatcher("/index.jsp");
        Mockito.verify(requestDispatcherMock).forward(request, response);

        ArgumentCaptor<List> arg = ArgumentCaptor.forClass(List.class);
        Mockito.verify(request).setAttribute(Mockito.eq("cakes"), arg.capture());
        assertThat(arg.getValue()).hasSize(20);
    }

    @Test
    public void shouldGetJsonWithDefaultValues() throws IOException, ServletException {
        StringWriter writer = new StringWriter();
        when(response.getWriter()).thenReturn(new PrintWriter(writer));
        when(request.getHeader("Accept")).thenReturn("application/json");

        cakeServlet.doGet(request, response);

        String expected = IOUtils.toString(getClass().getClassLoader().getResourceAsStream("defaultCakes.json"));
        assertThat(writer.toString()).isEqualToIgnoringWhitespace(expected);
    }

    @Test
    public void shouldCreateNewCakeAndRedirectToGet() throws IOException {
        List<CakeEntity> list = persistenceService.getAllCakes();
        assertThat(list).hasSize(20);

        when(request.getParameter("title")).thenReturn("new Title for cake");
        when(request.getParameter("description")).thenReturn("new Description for cake");
        when(request.getParameter("image")).thenReturn("new image url for cake");

        cakeServlet.doPost(request, response);

        List<CakeEntity> listAfter = persistenceService.getAllCakes();
        assertThat(listAfter).hasSize(21);
        CakeEntity createdCake = listAfter.get(20);
        assertThat(createdCake.getTitle()).isEqualTo("new Title for cake");
        assertThat(createdCake.getDescription()).isEqualTo("new Description for cake");
        assertThat(createdCake.getImage()).isEqualTo("new image url for cake");

        Mockito.verify(response).sendRedirect("");
    }

    @After
    public void after() {
        persistenceService.shutdown();
    }

    private class CakeServletMock extends CakeServlet {
        public ServletContext getServletContext() {
            return servletContextMock;
        }
    }
}
