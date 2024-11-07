package org.dis.front;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import org.dis.back.BRException;
import org.dis.back.EmpleadoBR;
import org.dis.back.TipoEmpleado;
import org.w3c.dom.Text;

import java.util.Arrays;

/**
 * This UI is the application entry point. A UI may either represent a browser window 
 * (or tab) or some part of an HTML page where a Vaadin application is embedded.
 * <p>
 * The UI is initialized using {@link #init(VaadinRequest)}. This method is intended to be 
 * overridden to add component to the user interface and initialize non-component functionality.
 */
@Theme("mytheme")
public class MyUI extends UI {
    private TextField creaLabel(String texto){
        TextField etiqueta = new TextField(texto);
        etiqueta.setCaption(texto);
        return etiqueta;
    }
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        final HorizontalLayout salarioBruto = new HorizontalLayout();
        final VerticalLayout salarioNeto = new VerticalLayout();
        final VerticalLayout layout = new VerticalLayout();
        final VerticalLayout salarioBrutoContenedor = new VerticalLayout();
        final VerticalLayout salarioNetoContenedor = new VerticalLayout();



        //TextField tipo = creaLabel("Tipo de empleado");
        ComboBox <String> tipo = new ComboBox<>("Tipo de empleado", Arrays.asList(TipoEmpleado.ENCARGADO, TipoEmpleado.VENDEDOR));
        TextField ventaMes = creaLabel("Venta del mes:");
        TextField horasExtra = creaLabel("Horas extra: ");
        TextField inputSalarioBruto = creaLabel("Salario Bruto: ");

        salarioBruto.addComponents(tipo, ventaMes, horasExtra);
        salarioNeto.addComponents(inputSalarioBruto);


        Button botonSalarioBruto = new Button("Calcular Salario Bruto");
        botonSalarioBruto.addClickListener(e -> {
            String tipoEmpleadoIn = tipo.getValue();
            double ventaMesIn = Double.parseDouble( ventaMes.getValue());
            double horasExtraIn = Double.parseDouble( horasExtra.getValue());

            EmpleadoBR empleado = new EmpleadoBR();
            try {
                double resultado = empleado.calculaSalarioBruto(tipoEmpleadoIn, ventaMesIn, horasExtraIn);
                Label labelSalarioBruto = new Label("Salario Bruto: " + resultado + "€");
            } catch (BRException ex) {
                Label labelSalarioBruto = new Label(ex.getMessage());
                salarioBrutoContenedor.addComponent(labelSalarioBruto);
            }
        });
        Button botonSalarioNeto = new Button("Calcular Salario Neto");
        botonSalarioNeto.addClickListener(e -> {
            double SalarioBrutoIn = Double.parseDouble( inputSalarioBruto.getValue());

            EmpleadoBR empleado = new EmpleadoBR();
            try {
                double resultado = empleado.calculaSalarioNeto(SalarioBrutoIn);
                Label labelSalarioNeto = new Label("Salario neto: " + resultado + "€");
                salarioNetoContenedor.addComponent(labelSalarioNeto);
            } catch (BRException ex) {
                Label labelSalarioNeto = new Label(ex.getMessage());
                salarioNetoContenedor.addComponent(labelSalarioNeto);
            }
        });

        salarioBrutoContenedor.addComponents(salarioBruto, botonSalarioBruto);
        salarioNetoContenedor.addComponents(salarioNeto, botonSalarioNeto);

        TabSheet tabs = new TabSheet();
        tabs.addTab(salarioBrutoContenedor).setCaption("Calcular Salario Bruto");
        tabs.addTab(salarioNetoContenedor).setCaption("Calcular Salario Neto");

        layout.addComponents(tabs);
        setContent(layout);
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
