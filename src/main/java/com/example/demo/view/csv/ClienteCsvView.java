package com.example.demo.view.csv;

import com.example.demo.modelos.entidad.Cliente;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.AbstractView;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@Component("listar.csv")
public class ClienteCsvView extends AbstractView {

    public ClienteCsvView() {
        setContentType("text/csv");
    }

    @Override
    protected boolean generatesDownloadContent() {
        return true;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("Content-Disposition", "attachhment; filename: \"clientes.csv\"");
        response.setContentType(getContentType());

        List<Cliente> clientes = (List<Cliente>) model.get("clientes");

        ICsvBeanWriter beanWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);

        String[] header = {"id", "nombre", "apellido", "email", "createAt"};
        beanWriter.writeHeader(header);

        for (Cliente cliente: clientes) {
            beanWriter.write(cliente, header);
        }

        beanWriter.close();
    }
}
