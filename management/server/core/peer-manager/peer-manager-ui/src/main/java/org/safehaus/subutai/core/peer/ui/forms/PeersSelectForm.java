package org.safehaus.subutai.pet.ui.forms;

import com.vaadin.annotations.AutoGenerated;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.TwinColSelect;

public class PeersSelectForm extends CustomComponent {

	/*- VaadinEditorProperties={"grid":"RegularGrid,20","showGrid":true,"snapToGrid":true,"snapToObject":true,"movingGuides":false,"snappingDistance":10} */

    @AutoGenerated
    private AbsoluteLayout mainLayout;
    @AutoGenerated
    private AbsoluteLayout absoluteLayout_3;
    @AutoGenerated
    private Label peersLabel;
    @AutoGenerated
    private TwinColSelect peersColSelect;

    /**
     * The constructor should first build the main layout, set the
     * composition root and then do any custom initialization.
     * <p/>
     * The constructor will not be automatically regenerated by the
     * visual editor.
     */
    public PeersSelectForm() {
        buildMainLayout();
        setCompositionRoot(mainLayout);

        // TODO add user code here
    }

    @AutoGenerated
    private AbsoluteLayout buildMainLayout() {
        // common part: create layout
        mainLayout = new AbsoluteLayout();
        mainLayout.setImmediate(false);
        mainLayout.setWidth("100%");
        mainLayout.setHeight("100%");

        // top-level component properties
        setWidth("100.0%");
        setHeight("100.0%");

        // absoluteLayout_3
        absoluteLayout_3 = buildAbsoluteLayout_3();
        mainLayout.addComponent(absoluteLayout_3, "top:0.0px;left:0.0px;");

        return mainLayout;
    }

    @AutoGenerated
    private AbsoluteLayout buildAbsoluteLayout_3() {
        // common part: create layout
        absoluteLayout_3 = new AbsoluteLayout();
        absoluteLayout_3.setImmediate(false);
        absoluteLayout_3.setWidth("100.0%");
        absoluteLayout_3.setHeight("100.0%");

        // peersColSelect
        peersColSelect = new TwinColSelect();
        peersColSelect.setCaption("Peers");
        peersColSelect.setImmediate(false);
        peersColSelect.setWidth("100.0%");
        peersColSelect.setHeight("100.0%");
        absoluteLayout_3.addComponent(peersColSelect,
                "top:64.0px;right:300.0px;bottom:197.0px;left:40.0px;");

        // peersLabel
        peersLabel = new Label();
        peersLabel.setImmediate(false);
        peersLabel.setWidth("-1px");
        peersLabel.setHeight("-1px");
        peersLabel.setValue("Select peers");
        absoluteLayout_3.addComponent(peersLabel, "top:20.0px;left:40.0px;");

        return absoluteLayout_3;
    }

}
