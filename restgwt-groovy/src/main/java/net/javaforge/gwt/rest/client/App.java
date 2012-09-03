package net.javaforge.gwt.rest.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.RootPanel;
import net.javaforge.gwt.rest.client.remote.rest.PersonResourceAsync;
import net.javaforge.gwt.rest.shared.domain.Person;
import org.fusesource.restygwt.client.Defaults;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;

import java.util.List;

public class App implements EntryPoint {

    private FlexTable personsTable;

    static {
        // if you don't do this, on JSON response you'll get something like
        // this:
        // "Could not parse response: org.fusesource.restygwt.client.ResponseFormatException: Response was NOT a valid JSON document"
        Defaults.setDateFormat(null);
    }

    @Override
    public void onModuleLoad() {

        Button button = new Button("load persons");
        button.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                loadPersons();
            }
        });

        personsTable = new FlexTable();
        personsTable.setCellPadding(1);
        personsTable.setCellSpacing(0);
        personsTable.setWidth("600px");

        RootPanel.get().add(button);
        RootPanel.get().add(personsTable);

    }

    private void loadPersons() {

        PersonResourceAsync.Util.get().getPersons(
                new MethodCallback<List<Person>>() {

                    @Override
                    public void onSuccess(Method method, List<Person> persons) {
                        if (personsTable.getRowCount() == 0) { // add header
                            personsTable.setText(0, 0, "ID");
                            personsTable.setText(0, 1, "NAME");
                            personsTable.setText(0, 2, "DATE OF BIRTH");
                            personsTable.getRowFormatter().addStyleName(0,
                                    "tableHeader");
                        }

                        for (Person p : persons) {
                            int rowNum = personsTable.getRowCount();
                            personsTable.setText(rowNum, 0,
                                    String.valueOf(p.getId()));
                            personsTable.setText(rowNum, 1, p.getName());
                            personsTable.setText(rowNum, 2,
                                    String.valueOf(p.getDateOfBirth()));
                        }
                    }

                    @Override
                    public void onFailure(Method method, Throwable exception) {
                        Window.alert("Error while loading persons! Cause: "
                                + exception.getMessage());
                    }
                });

    }

}
