package application.uw.cse.mag.appliancereader.views;

import application.uw.cse.mag.appliancereader.util.FXMLPaneLoader;
import application.uw.cse.mag.appliancereader.views.input.params.features.ParametersView;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import uw.cse.mag.appliancereader.lib.cv.params.core.FeatureDetectionParams;

import java.net.URL;
import java.util.*;

/**
 * View that contains all feature detection paramaters.
 *
 * @author Michael Hotan, michael.hotan@gmail.com
 */
public class FeatureDetectorsView extends BorderPane implements ChangeListener<Tab> {

    @FXML
    protected ResourceBundle resources;

    @FXML
    protected URL location;

    @FXML
    private TabPane tabPane;

    /**
     * Listener for Feature detection.
     */
    private final OnFeatureDetectorChangedListener listener;

    /**
     *
     */
    private final Map<Tab, ParametersView> mapping;

    /**
     * Creates a view that allows users to alternate between different feature detctectors
     *
     * @param views All the different Parameter Views to show.
     * @param listener Callback that handles channge between detectors
     */
    public FeatureDetectorsView(List<ParametersView> views, OnFeatureDetectorChangedListener listener) {
        if (listener == null)
            throw new NullPointerException(getClass().getSimpleName() + "() Null listener.");
        if (views == null || views.isEmpty()) {
            throw new IllegalArgumentException("Illegal list of views " + views);
        }
        FXMLPaneLoader.load(this, "FeatureDetectorsView.fxml");

        this.listener = listener;
        mapping = new HashMap<Tab, ParametersView>();
        for (ParametersView view : views) {
            Tab t = new Tab();
            t.setText(view.getTitle());
            t.setContent(view);
            mapping.put(t, view);
            tabPane.getTabs().add(t);
        }

        tabPane.getSelectionModel().selectedItemProperty().addListener(this);
        tabPane.getSelectionModel().select(new ArrayList<Tab>(mapping.keySet()).get(0));
    }

    public ParametersView getCurrentView() {
        Tab current = tabPane.getSelectionModel().getSelectedItem();
        return mapping.get(current);
    }

    @Override
    public void changed(ObservableValue<? extends Tab> ov, Tab oldVal, Tab newVal) {
        if (listener == null) return;
        FeatureDetectionParams oldParams = mapping.containsKey(oldVal) ? mapping.get(oldVal).getParameters() : null;
        FeatureDetectionParams newParams = mapping.containsKey(newVal) ? mapping.get(newVal).getParameters() : null;
        listener.onFeatureParameterChanged(newParams, oldParams);
    }

    @FXML
    void initialize() {
        assert tabPane != null : "fx:id=\"tabPane\" was not injected: check your FXML file 'FeatureDetectorsView.fxml'.";
    }


    /**
     * Interface for notifyi
     */
    public interface OnFeatureDetectorChangedListener {

        /**
         * The feature detector changed.
         *
         * @param newVal The Newly selected feature detector parameter
         * @param oldVal The old feature detector parameter
         */
        public void onFeatureParameterChanged(FeatureDetectionParams newVal,
                                              FeatureDetectionParams oldVal);

    }

}
